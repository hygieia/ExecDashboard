package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.model.Filter;
import com.capitalone.dashboard.exec.model.MetricType;
import org.apache.commons.lang.StringUtils;
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
    private boolean incidentsCollectorFlag;
    private boolean githubCollectorFlag;
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

    public boolean isGithubCollectorFlag() { return githubCollectorFlag; }
    public void setGithubCollectorFlag(boolean githubCollectorFlag) { this.githubCollectorFlag = githubCollectorFlag; }

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

    @PostConstruct
    private void buildReadUri() {
        if (!StringUtils.isEmpty(this.readUriUserName) && !StringUtils.isEmpty(this.readUriPassword)) {
            this.readUri = this.readUriPrefix + "://" + this.readUriUserName + ":" + this.readUriPassword + "@" + this.readUriDatabase;
        } else {
            this.readUri = this.readUriPrefix + "://"  + this.readUriDatabase;
        }
    }
}