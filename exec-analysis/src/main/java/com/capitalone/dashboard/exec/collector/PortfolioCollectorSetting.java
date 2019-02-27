package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.Filter;
import com.capitalone.dashboard.exec.model.MetricType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "portfolio")
public class PortfolioCollectorSetting {
    private String cron;

    public String readUriUserName;
    public String readUriPassword;
    public String readUriDatabase;
    public String readUriPrefix;

    public String readUri;
    public String writeUri;
    public String readDatabase;
    public String writeDatabase;

    @Value("${feature.incidentsCollectorFlag:true}")
    private boolean incidentsCollectorFlag;
    @Value("${feature.scmCollectorFlag:true}")
    private boolean scmCollectorFlag;
    @Value("${feature.libraryPolicyCollectorFlag:true}")
    private boolean libraryPolicyCollectorFlag;
    @Value("${feature.staticCodeAnalysisCollectorFlag:true}")
    private boolean staticCodeAnalysisCollectorFlag;
    @Value("${feature.unitTestCoverageCollectorFlag:true}")
    private boolean unitTestCoverageCollectorFlag;
    @Value("${feature.auditResultCollectorFlag:true}")
    private boolean auditResultCollectorFlag;
    @Value("${feature.securityCollectorFlag:true}")
    private boolean securityCollectorFlag;

    private Map<MetricType, List<Filter>> filters;

    public String getReadUri() { return readUri; }
    public void setReadUri(String readUri) { this.readUri = readUri; }

    public String getReadUriUserName() { return readUriUserName; }
    public void setReadUriUserName(String readUriUserName) { this.readUriUserName = readUriUserName; }

    public String getReadUriPassword() { return readUriPassword; }
    public void setReadUriPassword(String readUriPassword) { this.readUriPassword = readUriPassword; }

    public String getReadUriDatabase() { return readUriDatabase; }
    public void setReadUriDatabase(String readUriDatabase) { this.readUriDatabase = readUriDatabase; }

    public String getReadUriPrefix() { return readUriPrefix; }
    public void setReadUriPrefix(String readUriPrefix) { this.readUriPrefix = readUriPrefix; }

    public Map<MetricType, List<Filter>> getFilters() { return filters; }
    public void setFilters(Map<MetricType, List<Filter>> filters) { this.filters = filters; }

    public boolean isScmCollectorFlag() {
        return scmCollectorFlag;
    }

    public void setScmCollectorFlag(boolean scmCollectorFlag) {
        this.scmCollectorFlag = scmCollectorFlag;
    }

    public boolean isIncidentsCollectorFlag() { return incidentsCollectorFlag; }

    public void setIncidentsCollectorFlag(boolean incidentsCollectorFlag) {
        this.incidentsCollectorFlag = incidentsCollectorFlag;
    }

    public String getWriteDatabase() { return writeDatabase; }
    public void setWriteDatabase(String writeDatabase) { this.writeDatabase = writeDatabase; }

    public String getWriteUri() { return writeUri; }
    public void setWriteUri(String writeUri) { this.writeUri = writeUri; }

    public String getReadDatabase() { return readDatabase; }
    public void setReadDatabase(String readDatabase) { this.readDatabase = readDatabase; }

    public String getCron() { return cron; }
    public void setCron(String cron) { this.cron = cron; }

    public boolean isLibraryPolicyCollectorFlag() {
        return libraryPolicyCollectorFlag;
    }

    public void setLibraryPolicyCollectorFlag(boolean libraryPolicyCollectorFlag) {
        this.libraryPolicyCollectorFlag = libraryPolicyCollectorFlag;
    }

    public boolean isStaticCodeAnalysisCollectorFlag() {
        return staticCodeAnalysisCollectorFlag;
    }

    public void setStaticCodeAnalysisCollectorFlag(boolean staticCodeAnalysisCollectorFlag) {
        this.staticCodeAnalysisCollectorFlag = staticCodeAnalysisCollectorFlag;
    }

    public boolean isUnitTestCoverageCollectorFlag() {
        return unitTestCoverageCollectorFlag;
    }

    public void setUnitTestCoverageCollectorFlag(boolean unitTestCoverageCollectorFlag) {
        this.unitTestCoverageCollectorFlag = unitTestCoverageCollectorFlag;
    }

    public boolean isAuditResultCollectorFlag() {
        return auditResultCollectorFlag;
    }

    public void setAuditResultCollectorFlag(boolean auditResultCollectorFlag) {
        this.auditResultCollectorFlag = auditResultCollectorFlag;
    }

    public boolean isSecurityCollectorFlag() {
        return securityCollectorFlag;
    }

    public void setSecurityCollectorFlag(boolean securityCollectorFlag) {
        this.securityCollectorFlag = securityCollectorFlag;
    }

    @PostConstruct
    private void buildReadUri() {
        if (!StringUtils.isEmpty(this.readUriUserName) && !StringUtils.isEmpty(this.readUriPassword)) {
            this.readUri = this.readUriPrefix + "://" + this.readUriUserName + ":" + this.readUriPassword + "@" + this.readUriDatabase;
        } else {
            this.readUri = this.readUriPrefix + "://"  + this.readUriDatabase;
        }
    }
}