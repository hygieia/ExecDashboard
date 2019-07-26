package com.capitalone.dashboard.exec.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A collection of widgets, collectors and application components that represent
 * a software project under development and/or in production use.
 *
 */
@Document(collection = "dashboards")
public class Dashboard extends BaseModel {

	private String template;
	private String title;
	private Application application;
	private String businessUnit;
	private List<Widget> widgets = new ArrayList<>();
	private String owner;
	private Long timeStamp;
	private BunitCredentials dbCredentials;
	private String instance;

	@Indexed(unique = true)
	private String appId;

	public Dashboard(String template, String title, Application application, String owner, String appId,
			String businessUnit) {
		this.template = template;
		this.title = title;
		this.application = application;
		this.owner = owner;
		this.appId = appId;
		this.businessUnit = businessUnit;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public List<Widget> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<Widget> widgets) {
		this.widgets = widgets;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public BunitCredentials getDbCredentials() {
		return dbCredentials;
	}

	public void setDbCredentials(BunitCredentials dbCredentials) {
		this.dbCredentials = dbCredentials;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

}