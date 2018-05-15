package com.capitalone.dashboard.exec.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseConfigItem {
    @Id
    private ObjectId id;
    private String name;
    private String commonName;
    private String lob;
    private String dashboardDisplayName;
    private List<PeopleRoleRelation> owners;
    private MetricLevel metricLevel;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getDashboardDisplayName() { return dashboardDisplayName; }
    public void setDashboardDisplayName(String dashboardDisplayName) {
        this.dashboardDisplayName = dashboardDisplayName;
    }

    public String getCommonName() { return commonName; }

    public void setCommonName(String commonName) { this.commonName = commonName; }

    public MetricLevel getMetricLevel() {
        return metricLevel;
    }

    public void setMetricLevel(MetricLevel metricLevel) {
        this.metricLevel = metricLevel;
    }

    public List<PeopleRoleRelation> getOwners() {
        return owners;
    }

    public void setOwners(List<PeopleRoleRelation> owners) {
        this.owners = owners;
    }

    public BaseConfigItem() {}

    public BaseConfigItem(String name, String lob) {
        this.name = name;
        this.lob = lob;
    }

    public BaseConfigItem(String name, String lob, List<PeopleRoleRelation> owners) {
        this.name = name;
        this.lob = lob;
        this.owners = owners;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLob() { return lob; }
    public void setLob(String lob) { this.lob = lob; }

    public void addOwner(PeopleRoleRelation owner) {
        if (owners == null) {
            owners = new ArrayList<>();
        }
        if (!owners.contains(owner)) {
            owners.add(owner);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseConfigItem)) return false;
        BaseConfigItem that = (BaseConfigItem) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(lob, that.lob);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, lob);
    }
}
