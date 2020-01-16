package com.capitalone.dashboard.exec.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="lob")
public final class Lob extends BaseConfigItem{

    private List<Product> products = new ArrayList();
    private ObjectId metricsId;

    public Lob() {}

    public Lob(String name, List<Product> products) {
        super(name);
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
