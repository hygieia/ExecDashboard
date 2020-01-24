package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.config.DataFrameLoader;
import com.capitalone.dashboard.exec.config.LdapHandler;
import com.capitalone.dashboard.exec.model.Environment;
import com.capitalone.dashboard.exec.model.HygieiaSparkQuery;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.Owner;
import com.capitalone.dashboard.exec.model.PeopleRoleRelation;
import com.capitalone.dashboard.exec.model.Person;
import com.capitalone.dashboard.exec.model.Portfolio;
import com.capitalone.dashboard.exec.model.Lob;
import com.capitalone.dashboard.exec.model.PortfolioThumbnail;
import com.capitalone.dashboard.exec.model.Product;
import com.capitalone.dashboard.exec.model.ProductComponent;
import com.capitalone.dashboard.exec.model.RoleRelationShipType;
import com.capitalone.dashboard.exec.repository.LobRepository;
import com.capitalone.dashboard.exec.repository.PortfolioRepository;
import com.capitalone.dashboard.exec.repository.PortfolioRepositoryThumbnail;
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
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import scala.collection.JavaConversions;
import scala.collection.mutable.WrappedArray;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Component
public class PortfolioCollector implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioCollector.class);

    private final TaskScheduler taskScheduler;

    private final PortfolioRepository portfolioRepository;

    private final LobRepository lobRepository;

    private final PortfolioRepositoryThumbnail portfolioRepositoryThumbnail;

    private final PortfolioCollectorSetting setting;

    private List<Row> productRowsList;

    private List<Row> lobRowsList;

    private List<Row> environmentRowsList;

    private List<Row> componentRowsList;

    private List<Row> dashboardRowsList;

    private final SCMCollector scmCollector;

    private final LibraryPolicyCollector libraryPolicyCollector;

    private final StaticCodeAnalysisCollector staticCodeAnalysisCollector;

    private final IncidentCollector incidentCollector;

    private final TraceabilityCollector traceabilityCollector;

    private final UnitTestCoverageCollector unitTestCoverageCollector;

    private final SecurityCollector securityCollector;

    private final PerformanceCollector performanceCollector;
	
	private final PipelineCollector pipelineCollector;

    private final EngineeringMaturityCollector engineeringMaturityCollector;

    @Autowired
    private LdapHandler ldapHandler;

    private LdapTemplate ldapTemplate;

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    private final EngineeringMaturityCollector engineeringMaturityCollector;

    @Autowired
    @SuppressWarnings("PMD.ExcessiveParameterList")
    public PortfolioCollector(TaskScheduler taskScheduler, PortfolioRepository portfolioRepository,
                              LobRepository lobRepository,
                              PortfolioCollectorSetting setting,
                              SCMCollector scmCollector,
                              LibraryPolicyCollector libraryPolicyCollector,
                              StaticCodeAnalysisCollector staticCodeAnalysisCollector,
                              IncidentCollector incidentCollector,
                              UnitTestCoverageCollector unitTestCoverageCollector,
                              TraceabilityCollector traceabilityCollector,
                              SecurityCollector securityCollector,
                              PerformanceCollector performanceCollector,
                              EngineeringMaturityCollector engineeringMaturityCollector,
							  PipelineCollector pipelineCollector,
							  PortfolioRepositoryThumbnail portfolioRepositoryThumbnail) {
        this.taskScheduler = taskScheduler;
        this.portfolioRepository = portfolioRepository;
        this.lobRepository = lobRepository;
        this.setting = setting;
        this.scmCollector = scmCollector;
        this.libraryPolicyCollector = libraryPolicyCollector;
        this.staticCodeAnalysisCollector = staticCodeAnalysisCollector;
        this.incidentCollector = incidentCollector;
        this.traceabilityCollector = traceabilityCollector;
        this.unitTestCoverageCollector = unitTestCoverageCollector;
        this.securityCollector = securityCollector;
        this.performanceCollector = performanceCollector;
        this.engineeringMaturityCollector = engineeringMaturityCollector;
		this.portfolioRepositoryThumbnail = portfolioRepositoryThumbnail;
		this.pipelineCollector = pipelineCollector;
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
        collectCMDB(sparkSession, javaSparkContext);
        List<Portfolio> portfolioList = createPortfolios();
        ArrayList<Lob> lobList = (ArrayList<Lob>) createLobs();

        if (CollectionUtils.isEmpty(portfolioList)) {
            LOGGER.info("##### Portfolio List is empty, cannot procedd further, returning ... #####");
            return;
        }

        if(setting.isScmCollectorFlag()) {
            LOGGER.info("##### Starting SCM Collector #####");
            scmCollector.collect(sparkSession, javaSparkContext, portfolioList);
			LOGGER.info("##### Completed SCM Collector #####");
        }
        if(setting.isLibraryPolicyCollectorFlag()) {
            LOGGER.info("##### Starting Library Policy Collector #####");
            libraryPolicyCollector.collect(sparkSession, javaSparkContext, portfolioList);
			LOGGER.info("##### Completed Library Policy Collector #####");
        }
        if(setting.isIncidentsCollectorFlag()){
            LOGGER.info("##### Starting Incident Collector #####");
            incidentCollector.collect(sparkSession, javaSparkContext, portfolioList);
			LOGGER.info("##### Completed Incident Collector #####");
        }
        if(setting.isStaticCodeAnalysisCollectorFlag()){
            LOGGER.info("##### Starting Static Code Collector #####");
            staticCodeAnalysisCollector.collect(sparkSession, javaSparkContext, portfolioList);
			LOGGER.info("##### Completed Static Code Analysis Collector #####");
        }
        if(setting.isUnitTestCoverageCollectorFlag()){
            LOGGER.info("##### Starting Unit Test Collector #####");
            unitTestCoverageCollector.collect(sparkSession, javaSparkContext, portfolioList);
			LOGGER.info("##### Completed Unit Test Collector #####");
        }
		if(setting.isPipelineCollectorFlag()){
            LOGGER.info("##### Starting Pipeline Collector #####");
            pipelineCollector.collect(sparkSession, javaSparkContext, portfolioList);
            LOGGER.info("##### Completed Pipeline Collector #####");
        }
        if(setting.isTraceabilityCollectorFlag()){
            LOGGER.info("##### Starting Traceability Collector #####");
            traceabilityCollector.collect(sparkSession, javaSparkContext, portfolioList);
            LOGGER.info("##### Completed Traceability Collector #####");
        }
        if(setting.isSecurityCollectorFlag()) {
            LOGGER.info("##### Starting Security Collector #####");
            securityCollector.collect(sparkSession, javaSparkContext, portfolioList);
			LOGGER.info("##### Completed Security Collector #####");
        }
        if(setting.isPerformanceCollectorFlag()) {
            LOGGER.info("##### Starting Performance Collector #####");
            performanceCollector.collect(sparkSession, javaSparkContext, portfolioList);
			LOGGER.info("##### Completed Performance Collector #####");
        }
        if(setting.isEngineeringMaturityFlag()) {
            LOGGER.info("##### Starting Engineering Maturity Collector #####");
            engineeringMaturityCollector.collect(sparkSession, javaSparkContext, lobList);
        }

        sparkSession.close();
        javaSparkContext.close();
    }

    public void collectCMDB(SparkSession sparkSession, JavaSparkContext javaSparkContext) {
        LOGGER.info("##### Begin: collectCMDB #####");
        if ((sparkSession == null) || (javaSparkContext == null)) {
            return;
        }

        DataFrameLoader.loadDataFrame("cmdb", javaSparkContext);
        DataFrameLoader.loadDataFrame("dashboards", javaSparkContext);

        // Unique list of products per businessOwner
        Dataset<Row> productRows = sparkSession.sql(HygieiaSparkQuery.CMDB_PRODUCT_QUERY);
        productRows.groupBy("businessOwner", "productName");

        Dataset<Row> lobRows = sparkSession.sql(HygieiaSparkQuery.CMDB_PRODUCT_QUERY);
        lobRows.groupBy( "ownerDept");

        // Unique list of Environments
        Dataset<Row> environmentRows = sparkSession.sql(HygieiaSparkQuery.CMDB_ENVIRONMENT_QUERY);
        environmentRows.groupBy("configurationItem");

        // Unique list of components
        Dataset<Row> componentRows = sparkSession.sql(HygieiaSparkQuery.CMDB_COMPONENT_QUERY);
        componentRows.groupBy("configurationItem");

        // Unique list of dashboards, no groupBy needed here looks like ...
        Dataset<Row> dashboardRows = sparkSession.sql(HygieiaSparkQuery.DASHBOARD_QUERY_EXPLODE);

        productRowsList = productRows.collectAsList();
        lobRowsList = lobRows.collectAsList();
        environmentRowsList = environmentRows.collectAsList();
        componentRowsList = componentRows.collectAsList();
        dashboardRowsList = dashboardRows.collectAsList();
        List<Portfolio> portfolioList = createPortfolios();
        portfolioRepositoryThumbnail.deleteAll();
        getPortfolioWithThumbnails(portfolioList);
        portfolioRepository.deleteAll();
        portfolioRepository.save(portfolioList);
        LOGGER.info("##### End: collectCMDB #####");
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

        portfolioRepository.deleteAll();
        portfolioRepository.save(portfolioList);

        return portfolioList;
    }

    private Iterable<Lob> createLobs() {
        List<Lob> lobList = new ArrayList<>();

        if (CollectionUtils.isEmpty(lobRowsList)){
            return lobList;
        }
        for (Row productRow : lobRowsList) {
            String lobName = productRow.getAs("ownerDept");
            String pName = productRow.getAs("businessOwner");

            Lob lob =
                    lobList.stream()
                            .filter(l -> l.getName().equalsIgnoreCase(lobName))
                            .findFirst().orElse(null);
            if (lob == null) {
                lob = new Lob();
                lob.setName(lobName);
                lobList.add(lob);
            }
            LOGGER.debug("Lob Name = " + lobName);
            addProductToLob(lob, productRow);
            lob.addOwner(new PeopleRoleRelation(getPeople(pName, "businessOwner"), RoleRelationShipType.BusinessOwner));
        }
        lobRepository.deleteAll();
        return lobRepository.save(lobList);
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

    private void addProductToLob(Lob lob, Row productRow) {
        String productName = productRow.getAs("productName");
        String productDept = productRow.getAs("ownerDept");
        String commonName = productRow.getAs("commonName");
        String productId = (String) ((GenericRowWithSchema) productRow.getAs("productId")).values()[0];

        LOGGER.debug("    Product Name = " + productName + " ; Owner Dept = " + productDept);

        // For a given portfolio, check if the current product already exists in the product list for the portfolio
        // If not, add it to the product list
        Product product =
                Optional.ofNullable(lob.getProducts())
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

        lob.addProduct(product);
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

                LOGGER.debug("Environment Name = " + obj);

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
                LOGGER.debug("Component Name = " + productComponent.getName());
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

    public void getPortfolioWithThumbnails(List<Portfolio> portfolioList) {
        ldapHandler.initLdaptemplate();
        setLdapTemplate(LdapHandler.getLdaptemplate());
        PortfolioThumbnail portfolioThumbnail = null;
        List<PortfolioThumbnail> portfolioListThumbnail = new ArrayList<>();
        for(Portfolio p :  portfolioList) {
            portfolioThumbnail = new PortfolioThumbnail();
            String pName = p.getName();
            String lob = p.getLob();
            String lastName = pName.substring(pName.indexOf(" ") + 1);
            String firstName = pName.substring(0, pName.indexOf(" "));
            String fullName = lastName + ", " + firstName;
            String thumbNail = null;
            if (!fullName.isEmpty()) {
                List<Person> execInfo = findByName(fullName);
                thumbNail = getExecInfo(execInfo);
            } else {
                LOGGER.info("List size is empty");
            }
            portfolioThumbnail.setName(pName);
            portfolioThumbnail.setLob(lob);
            portfolioThumbnail.setThumbnail(thumbNail);
            portfolioListThumbnail.add(portfolioThumbnail);
        }
        portfolioRepositoryThumbnail.save(portfolioListThumbnail);
    }

    public List<Person> findByName(String name) {
        ldapTemplate.setIgnorePartialResultException(true);
        return ldapTemplate.find(query().where("displayName").is(name), Person.class);
    }

    public String getExecInfo(List<Person> execInfo){
        String photoString = null;
        if (execInfo != null && !execInfo.isEmpty()) {
            byte[] photo = (byte[]) execInfo.get(0).getThumbnailPhoto();
            if (photo == null) {
                photoString = "";
                LOGGER.debug("Executive doesn't have thumbnail");
            } else {
                photoString = Base64.getEncoder().encodeToString(photo);
                LOGGER.info("Byte code convert to string :" + photoString);
            }
            LOGGER.debug("Byte code convert to string :" + photoString);
        }
        return photoString;
    }
}
