package com.capitalone.dashboard.exec.model;

public enum CollectorType {
    SCM,
    CMDB,
    Incident,
    Build,
    Artifact,
    Deployment,
    AgileTool,
    /** @deprecated */
    @Deprecated
    Feature,
    /** @deprecated */
    @Deprecated
    ScopeOwner,
    /** @deprecated */
    @Deprecated
    Scope,
    CodeQuality,
    Test,
    StaticSecurityScan,
    LibraryPolicy,
    ChatOps,
    Cloud,
    Product,
    AppPerformance,
    InfraPerformance;

    private CollectorType() {
    }

    public static CollectorType fromString(String value) {
        CollectorType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CollectorType collectorType = var1[var3];
            if (collectorType.toString().equalsIgnoreCase(value)) {
                return collectorType;
            }
        }

        throw new IllegalArgumentException(value + " is not a CollectorType");
    }
}

