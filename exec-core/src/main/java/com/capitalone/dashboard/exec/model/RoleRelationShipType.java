package com.capitalone.dashboard.exec.model;

public enum RoleRelationShipType {
    BusinessOwner,
    DevelopmentOwner,
    AppServiceOwner,
    SupportOwner,
    Developer,
    Tester;

    public static RoleRelationShipType fromString(String value) {
        for (RoleRelationShipType roleRelationShipType : values()) {
            if (roleRelationShipType.toString().equalsIgnoreCase(value)) {
                return roleRelationShipType;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid roleRelationShipType.");
    }

}
