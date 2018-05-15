package com.capitalone.dashboard.exec.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="portfolio")
public final class Portfolio extends BaseConfigItem{

    private List<Product> products = new ArrayList();
    private ObjectId metricsId;

    public Portfolio() {}

    public Portfolio(List<Product> products, String name, String lob, List<PeopleRoleRelation> owners) {
        super(name, lob, owners);
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct (Product product) {
        if (products == null) {
            products = new ArrayList<>();
        }
        if (!products.contains(product)) {
            products.add(product);
        }
    }

    public ObjectId getMetricsId() {
        return metricsId;
    }

    public void setMetricsId(ObjectId metricsId) {
        this.metricsId = metricsId;
    }
}
