package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.config.DataFrameLoader;
import com.capitalone.dashboard.exec.model.Environment;
import com.capitalone.dashboard.exec.model.HygieiaSparkQuery;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.Owner;
import com.capitalone.dashboard.exec.model.PeopleRoleRelation;
import com.capitalone.dashboard.exec.model.Portfolio;
import com.capitalone.dashboard.exec.model.Product;
import com.capitalone.dashboard.exec.model.ProductComponent;
import com.capitalone.dashboard.exec.model.RoleRelationShipType;
import com.capitalone.dashboard.exec.repository.PortfolioRepository;
import com.tupilabs.human_name_parser.HumanNameParserParser;
import com.tupilabs.human_name_parser.Name;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import scala.collection.JavaConversions;
import scala.collection.mutable.WrappedArray;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class PortfolioCollector implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioCollector.class);

    private final TaskScheduler taskScheduler;

    private final PortfolioRepository portfolioRepository;

    private final PortfolioCollectorSetting setting;

    private List<Row> productRowsList;

    private List<Row> environmentRowsList;

    private List<Row> componentRowsList;

    private List<Row> dashboardRowsList;

    private final SCMCollector scmCollector;

    private final LibraryPolicyCollector libraryPolicyCollector;

    private StaticCodeAnalysisCollector staticCodeAnalysisCollector;

    private final IncidentCollector incidentCollector;

    private final AuditResultCollector auditResultCollector;

    private UnitTestCoverageCollector unitTestCoverageCollector;

    private SecurityCollector securityCollector;

    @Autowired
    @SuppressWarnings("PMD.ExcessiveParameterList")
    public PortfolioCollector(TaskScheduler taskScheduler, PortfolioRepository portfolioRepository,
                              PortfolioCollectorSetting setting,
                              SCMCollector scmCollector,
                              LibraryPolicyCollector libraryPolicyCollector,
                              StaticCodeAnalysisCollector staticCodeAnalysisCollector,
                              IncidentCollector incidentCollector,
                              UnitTestCoverageCollector unitTestCoverageCollector,
                              AuditResultCollector auditResultCollector,
                              SecurityCollector securityCollector) {

        this.taskScheduler = taskScheduler;
        this.portfolioRepository = portfolioRepository;
        this.setting = setting;
        this.scmCollector = scmCollector;
        this.libraryPolicyCollector = libraryPolicyCollector;
        this.staticCodeAnalysisCollector = staticCodeAnalysisCollector;
        this.incidentCollector = incidentCollector;
        this.auditResultCollector = auditResultCollector;
        this.unitTestCoverageCollector = unitTestCoverageCollector;
        this.securityCollector = securityCollector;
    }

    /**
     * Main collection loop
     */
    @SuppressWarnings("PMD.NPathComplexity")
    public void collect() {
        HygieiaSparkConnection sparkConnection = new HygieiaSparkConnection(setting.getReadUri(), setting.getReadDatabase(),
                setting.getWriteUri(), setting.getWriteDatabase());
        SparkSession sparkSession = sparkConnection.getInstance();
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkSession.sparkContext());

        //Build portfolio structure: Portfolio -> Product (ASV) -> Environment -> Component (BAP)
        List<Portfolio> portfolioList = collectCMDB(sparkSession, javaSparkContext);

        if (CollectionUtils.isEmpty(portfolioList)) {
            LOGGER.info("##### Portfolio List is empty, cannot procedd further, returning ... #####");
            return;
        }


        if(setting.isScmCollectorFlag()) {
            LOGGER.info("##### Starting SCM Collector #####");
            scmCollector.collect(sparkSession, javaSparkContext, portfolioList);
        }
        if(setting.isLibraryPolicyCollectorFlag()) {
            LOGGER.info("##### Starting Library Policy Collector #####");
            libraryPolicyCollector.collect(sparkSession, javaSparkContext, portfolioList);
        }
        if(setting.isIncidentsCollectorFlag()){
            LOGGER.info("##### Starting Incident Collector #####");
            incidentCollector.collect(sparkSession, javaSparkContext, portfolioList);
        }
        if(setting.isStaticCodeAnalysisCollectorFlag()){
            LOGGER.info("##### Starting Static Code Collector #####");
            staticCodeAnalysisCollector.collect(sparkSession, javaSparkContext, portfolioList);
        }
        if(setting.isUnitTestCoverageCollectorFlag()){
            LOGGER.info("##### Starting Unit Test Collector #####");
            unitTestCoverageCollector.collect(sparkSession, javaSparkContext, portfolioList);
        }
        if(setting.isAuditResultCollectorFlag()){
            LOGGER.info("##### Starting Audit Results Collector #####");
            auditResultCollector.collect(sparkSession, javaSparkContext, portfolioList);
        }
        if(setting.isSecurityCollectorFlag()) {
            LOGGER.info("##### Starting Security Collector #####");
            securityCollector.collect(sparkSession, javaSparkContext, portfolioList);
        }
        sparkSession.close();
        javaSparkContext.close();
    }

    public List<Portfolio> collectCMDB(SparkSession sparkSession, JavaSparkContext javaSparkContext) {
        LOGGER.info("##### Begin: collectCMDB #####");
        if ((sparkSession == null) || (javaSparkContext == null)) {
            return null;
        }

        List<Portfolio> portfolioList = null;
        DataFrameLoader.loadDataFrame("cmdb", javaSparkContext);
        DataFrameLoader.loadDataFrame("dashboards", javaSparkContext);

        // Unique list of products per businessOwner
        Dataset<Row> productRows = sparkSession.sql(HygieiaSparkQuery.CMDB_PRODUCT_QUERY);
        productRows.groupBy("businessOwner", "productName");

        // Unique list of Environments
        Dataset<Row> environmentRows = sparkSession.sql(HygieiaSparkQuery.CMDB_ENVIRONMENT_QUERY);
        environmentRows.groupBy("configurationItem");

        // Unique list of components
        Dataset<Row> componentRows = sparkSession.sql(HygieiaSparkQuery.CMDB_COMPONENT_QUERY);
        componentRows.groupBy("configurationItem");

        // Unique list of dashboards, no groupBy needed here looks like ...
        Dataset<Row> dashboardRows = sparkSession.sql(HygieiaSparkQuery.DASHBOARD_QUERY_EXPLODE);

        productRowsList = productRows.collectAsList();
        environmentRowsList = environmentRows.collectAsList();
        componentRowsList = componentRows.collectAsList();
        dashboardRowsList = dashboardRows.collectAsList();

        portfolioList = createPortfolios();
        portfolioRepository.deleteAll();
        portfolioRepository.save(portfolioList);

        LOGGER.info("##### End: collectCMDB #####");

        return portfolioList;
    }

    private List<Portfolio> createPortfolios() {
        List<Portfolio> portfolioList = new ArrayList<>();
        if (CollectionUtils.isEmpty(productRowsList)) {
            return portfolioList;
        }

        for (Row productRow : productRowsList) {
            String pName = productRow.getAs("businessOwner");
            String lob = productRow.getAs("ownerDept");

            Portfolio portfolio =
                    portfolioList.stream()
                            .filter(p -> (p.getName().equalsIgnoreCase(pName))
                                    && (p.getLob().equalsIgnoreCase(lob)))
                            .findFirst().orElse(null);
            if (portfolio == null) {
                portfolio = new Portfolio();
                portfolio.setName(pName); // businessOwner
                portfolio.setLob(lob);
                portfolioList.add(portfolio);
            }

            LOGGER.debug("Portfolio Name = " + pName + " ; Owner Dept = " + lob);

            addProductToPortfolio(portfolio, productRow);

            portfolio.addOwner(new PeopleRoleRelation(getPeople(pName, "businessOwner"), RoleRelationShipType.BusinessOwner));
        }

        return portfolioList;
    }

    private void addProductToPortfolio(Portfolio portfolio, Row productRow) {
        String productName = productRow.getAs("productName");
        String productDept = productRow.getAs("ownerDept");
        String commonName = productRow.getAs("commonName");
        String productId = (String) ((GenericRowWithSchema) productRow.getAs("productId")).values()[0];

        LOGGER.debug("    Product Name = " + productName + " ; Owner Dept = " + productDept);

        // For a given portfolio, check if the current product already exists in the product list for the portfolio
        // If not, add it to the product list
        Product product =
                Optional.ofNullable(portfolio.getProducts())
                        .orElseGet(Collections::emptyList).stream()
                        .filter(p -> p.getName().equalsIgnoreCase(productName)
                                && p.getLob().equalsIgnoreCase(productDept))
                        .findFirst().orElse(null);
        if (product == null) {
            product = new Product();
            product.setId(new ObjectId(productId));
            product.setLob(productDept);
            product.setName(productName);
            product.setCommonName(commonName);
            product.setMetricLevel(MetricLevel.PRODUCT);
        }
        if (productRow.getAs("environments") != null) {
            Collection<Object> environmentNames = JavaConversions.asJavaCollection(((WrappedArray) productRow.getAs("environments")).toStream().toList());
            addEnvironmentsToProduct(product, environmentNames);
        }
        if (productRow.getAs("components") != null) {
            Collection<Object> componentNames = JavaConversions.asJavaCollection(((WrappedArray) productRow.getAs("components")).toStream().toList());
            addComponentsToProduct(product, componentNames);
        }
        product.addOwner(new PeopleRoleRelation(getPeople(productRow.getAs("appServiceOwner"), "appServiceOwner"), RoleRelationShipType.AppServiceOwner));
        product.addOwner(new PeopleRoleRelation(getPeople(productRow.getAs("supportOwner"), "supportOwner"), RoleRelationShipType.SupportOwner));
        product.addOwner(new PeopleRoleRelation(getPeople(productRow.getAs("developmentOwner"), "developmentOwner"), RoleRelationShipType.DevelopmentOwner));

        portfolio.addProduct(product);
    }

    private void addEnvironmentsToProduct(Product product, Collection<Object> environmentNames) {
        if (CollectionUtils.isEmpty(environmentNames)) {
            return;
        }

        environmentNames.forEach(obj -> {
            Row optErow =
                    Optional.ofNullable(environmentRowsList)
                            .orElseGet(Collections::emptyList).stream()
                            .filter(c -> Objects.equals(c.getAs("configurationItem"), obj))
                            .findFirst().orElse(null);
            if (optErow != null) {
                Environment environment = new Environment();
                environment.setName((String) obj);
                environment.setCommonName((String) optErow.getAs("componentName"));
                environment.setLob(product.getLob());
                environment.setMetricLevel(MetricLevel.ENVIRONMENT);
                String componentId = (String) ((GenericRowWithSchema) optErow.getAs("componentId")).values()[0];
                environment.setId(new ObjectId(componentId));

                LOGGER.debug("        Environment Name = " + obj);

                product.addEnvironment(environment);
            }
        });
    }

    private void addComponentsToProduct(Product product, Collection<Object> componentNames) {
        if (CollectionUtils.isEmpty(componentNames)) {
            return;
        }

        componentNames.forEach(obj -> {
            Row optCrow =
                    Optional.ofNullable(componentRowsList)
                            .orElseGet(Collections::emptyList).stream()
                            .filter(c -> Objects.equals(c.getAs("configurationItem"), obj))
                            .findFirst().orElse(null);
            if (optCrow != null) {
                ProductComponent productComponent = new ProductComponent();
                productComponent.setName((String) obj);
                productComponent.setLob(product.getLob());
                productComponent.setMetricLevel(MetricLevel.COMPONENT);
                productComponent.setCommonName((String) optCrow.getAs("componentName"));
                // Check if the productComponent (BAP: Business Application) is associated with a dashboard,i.e. if it is being reported.
                String componentId = (String) ((GenericRowWithSchema) optCrow.getAs("componentId")).values()[0];
                String configItem = optCrow.getAs("configurationItem");
                productComponent.setId(new ObjectId(componentId));

                Row matchingDashboard =
                        Optional.ofNullable(dashboardRowsList)
                                .orElseGet(Collections::emptyList).stream()
                                .filter(c -> (
                                        ((String) c.getAs("componentName")).equalsIgnoreCase(configItem))
                                        && ((String) c.getAs("productName")).equalsIgnoreCase(product.getName())
                                ).findFirst().orElse(null);

                if (matchingDashboard != null) {
                    productComponent.setReporting(true);
                    productComponent.setProductComponentDashboardId(new ObjectId((String) ((GenericRowWithSchema) matchingDashboard.getAs("dashboardId")).values()[0]));
                } else {
                    productComponent.setReporting(false);
                }
                product.addProductComponent(productComponent);

                //Debug Statement
                LOGGER.debug("            Component Name = " + productComponent.getName());
            }
        });
    }

    private Owner getPeople(String name, String role) {
        if (StringUtils.isEmpty(name)) {
            return new Owner(name, "", "", "", role);
        }

        try {
            HumanNameParserParser hnp = new HumanNameParserParser(new Name(name));
            return new Owner(hnp.getFirst(), hnp.getLast(), "", "", role);
        } catch (com.tupilabs.human_name_parser.ParseException pe) {
            // LOGGER.error("Error pasring name " + name, pe);
            return new Owner(name, "", "", "", role);
        }
    }

    @Override
    public void run() {
        LOGGER.info("Running Hygieia EXEC Collector");
        collect();
    }

    @PostConstruct
    public void onStartup() {
        taskScheduler.schedule(this, new CronTrigger(setting.getCron()));
    }
}