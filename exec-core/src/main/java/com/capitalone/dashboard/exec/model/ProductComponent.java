package com.capitalone.dashboard.exec.model;

import org.bson.types.ObjectId;

public class ProductComponent extends BaseConfigItem {
    private String url;
    private boolean reporting;
    private ObjectId productComponentDashboardId;

    public ProductComponent() {}

    public ProductComponent(boolean reporting, String name, String lob) {
        super(name, lob);
        this.reporting = reporting;
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public boolean isReporting() { return reporting; }
    public void setReporting(boolean reporting) { this.reporting = reporting; }

    public ObjectId getProductComponentDashboardId() {
        return productComponentDashboardId;
    }

    public void setProductComponentDashboardId(ObjectId productComponentDashboardId) {
        this.productComponentDashboardId = productComponentDashboardId;
    }
}