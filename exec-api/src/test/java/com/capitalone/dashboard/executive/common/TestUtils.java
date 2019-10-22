package com.capitalone.dashboard.executive.common;

import com.capitalone.dashboard.exec.model.BuildingBlockMetricSummary;
import com.capitalone.dashboard.exec.model.ComponentMetricDetail;
import com.capitalone.dashboard.exec.model.Environment;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricSummary;
import com.capitalone.dashboard.exec.model.Owner;
import com.capitalone.dashboard.exec.model.PeopleRoleRelation;
import com.capitalone.dashboard.exec.model.Portfolio;
import com.capitalone.dashboard.exec.model.PortfolioMetricDetail;
import com.capitalone.dashboard.exec.model.Product;
import com.capitalone.dashboard.exec.model.ProductComponent;
import com.capitalone.dashboard.exec.model.ProductMetricDetail;
import com.capitalone.dashboard.exec.model.RoleRelationShipType;
import com.capitalone.dashboard.executive.mapper.CustomObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class TestUtils {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static Portfolio makePortfolio(MetricLevel metricLevel,RoleRelationShipType roleRelationShipType){
        Portfolio portfolio = new Portfolio();
        ObjectId id = ObjectId.get();
        portfolio.setId(id);
        portfolio.setName("portfolio1");
        portfolio.setCommonName("PF");
        portfolio.setLob("LOB");
        portfolio.setThumbnail("thumbnailPhoto");
        portfolio.setDashboardDisplayName("PF dashboard");
        portfolio.setMetricLevel(metricLevel);
        portfolio.setMetricsId(ObjectId.get());
        portfolio.setOwners(makeOwners(roleRelationShipType));
        portfolio.setProducts(Stream.of(makeProduct()).collect(Collectors.toList()));
        return portfolio;
    }


    public static List<PeopleRoleRelation> makeOwners( RoleRelationShipType roleRelationShipType){
        Owner owner = new Owner("firstName", "lastName" , "userId", "jobTitle" ,  "role");
        PeopleRoleRelation peopleRoleRelation = new PeopleRoleRelation(owner, roleRelationShipType);
        return Stream.of(peopleRoleRelation).collect(Collectors.toList());
    }

    public static Product makeProduct(){
        Product product = new Product(Stream.of(makeEnvironment()).collect(Collectors.toList()),"Product1","LOB");
        product.setReporting(true);
        product.setProductDashboardId(ObjectId.get());
        product.setProductComponentList(Stream.of(makeProductComponent()).collect(Collectors.toList()));
        return product;
    }

    public static Environment makeEnvironment(){

        Environment environment = new Environment(true,"DEV","LOB");
        environment.setUrl("http://testable-env.com");
        environment.setEnvironmentDashboardId(ObjectId.get());
        return environment;
    }

    public static ProductComponent makeProductComponent(){
        ProductComponent productComponent = new ProductComponent(true,"product1","LOB");
        productComponent.setUrl("http://product-comp.com");
        productComponent.setProductComponentDashboardId(ObjectId.get());
        return productComponent;
    }

    public static PortfolioMetricDetail makePortfolioMetricDetail(){
        PortfolioMetricDetail portfolioMetricDetail = new PortfolioMetricDetail();
        portfolioMetricDetail.setName("PortfolioMetricDetail");
        portfolioMetricDetail.setProductMetricDetailList(Stream.of(makeProductMetricDetail()).collect(Collectors.toList()));
        return portfolioMetricDetail;
    }


    public static ProductMetricDetail makeProductMetricDetail(){
        ProductMetricDetail productMetricDetail = new ProductMetricDetail();
        productMetricDetail.setComponentMetricDetailList(Stream.of(makeComponentMetricDetail()).collect(Collectors.toList()));
        productMetricDetail.setSummary(makeMetricSummary());
        productMetricDetail.setName("Product1");
        productMetricDetail.setLob("LOB");
        return  productMetricDetail;
    }

    public static ComponentMetricDetail makeComponentMetricDetail(){
        ComponentMetricDetail componentMetricDetail = new ComponentMetricDetail();
        componentMetricDetail.setItem(makeProductComponent());
        componentMetricDetail.setName("Product1");
        return componentMetricDetail;
    }

    public static MetricSummary makeMetricSummary(){
        MetricSummary metricSummary = new MetricSummary();
        metricSummary.setCounts(Stream.of(makeMetricCount()).collect(Collectors.toList()));
        metricSummary.setName("metricSummary");
        metricSummary.setTrendSlope(10000);
        return  metricSummary;
    }

    public static MetricCount makeMetricCount(){
        Map<String,String> map = new HashMap<>();
        map.put("label","value");
        MetricCount metricCount = new MetricCount(map,666);
        return metricCount;
    }

    public static BuildingBlockMetricSummary makeBuildingBlockMetricSummary(){
        return BuildingBlockMetricSummary.getInstance(makeProductMetricDetail());

    }



    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new CustomObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
