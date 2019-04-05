package com.capitalone.dashboard.executive.model;

import com.capitalone.dashboard.exec.model.PeopleRoleRelation;
import com.capitalone.dashboard.exec.model.Portfolio;
import com.capitalone.dashboard.exec.model.RoleRelationShipType;

import java.util.Objects;

public class ExecutiveResponse {
    private String firstName;
    private String lastName;
    private String role;

    public ExecutiveResponse(String firstName, String lastName) {
        this(firstName, lastName, "");
    }

    public ExecutiveResponse(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public static ExecutiveResponse getExecutive (Portfolio portfolio) {
        if (portfolio == null) { return null; }

        PeopleRoleRelation relation
                = portfolio.getOwners().stream()
                .filter(o -> Objects.equals(RoleRelationShipType.BusinessOwner, o.getRelation()))
                .findFirst().orElse(null);

        return  (relation != null) ?
            new ExecutiveResponse(relation.getRelatedTo().getFirstName(),
                    relation.getRelatedTo().getLastName()) : null;
    }
}
