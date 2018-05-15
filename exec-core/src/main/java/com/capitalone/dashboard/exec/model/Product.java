package com.capitalone.dashboard.exec.model;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Product extends BaseConfigItem {
    private boolean reporting;
    private ObjectId productDashboardId;
    private List<Environment> environmentList = new ArrayList<>();
    private List<ProductComponent> productComponentList = new ArrayList<>();

    public Product() {}

    public Product(List<Environment> environmentList, String name, String lob) {
        super(name, lob);
        this.environmentList = environmentList;
    }

    public boolean isReporting() { return reporting; }
    public void setReporting(boolean reporting) { this.reporting = reporting; }

    public List<Environment> getEnvironments() { return environmentList; }
    public void setEnvironments(List<Environment> environments) { this.environmentList = environments; }

    public List<ProductComponent> getProductComponentList() { return productComponentList; }
    public void setProductComponentList(List<ProductComponent> productComponentList) {
        this.productComponentList = productComponentList;
    }

    public ObjectId getProductDashboardId() {
        return productDashboardId;
    }
    public void setProductDashboardId(ObjectId productDashboardId) {
        this.productDashboardId = productDashboardId;
    }

    public void addEnvironment (Environment environment) {
        if (environmentList == null) {
            environmentList = new ArrayList<>();
        }
        if (!environmentList.contains(environment)) {
            environmentList.add(environment);
        }
    }

    public void addProductComponent (ProductComponent productComponent) {
        if (productComponentList == null) {
            productComponentList = new ArrayList<>();
        }
        if (!productComponentList.contains(productComponent)) {
            productComponentList.add(productComponent);
        }
    }
}
