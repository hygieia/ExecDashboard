package com.capitalone.dashboard.exec.model.vz;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "configuration_metrics")
public class ConfigurationMetrics extends BaseModel {

	private String vastId;
	private String vastAcronym;
	private String vastAppName;
	private String vastBunit;
	private boolean enabled;
	private int modules;
	private boolean featureEnabled;
	private int featureComponents;
	private boolean codeRepoEnabled;
	private int repoComponents;
	private boolean buildEnabled;
	private int buildComponents;
	private boolean prodDeployEnabled;
	private int prodDeployComponents;
	private boolean uatDeployEnabled;
	private int uatDeployComponents;
	private boolean sitDeployEnabled;
	private int sitDeploycomponents;
	private boolean ditDeployEnabled;
	private int ditDeployComponents;
	private boolean seleniumEnabled;
	private int seleniumComponents;
	private boolean cucumberEnabled;
	private int cucumberComponents;
	private boolean soapuiEnabled;
	private int soapuiComponents;
	private boolean tapsiumEnabled;
	private int tapsiumComponents;
	private boolean jmeterEnabled;
	private int jmeterComponents;
	private boolean loadrunnerEnabled;
	private int loadrunnerComponents;
	private boolean codeAnalysisEnabled;
	private int codeAnalysisComponents;
	private boolean fortifyEnabled;
	private int fortifyComponents;
	private long timeStamp;
	
	
	public boolean isCucumberEnabled() {
		return cucumberEnabled;
	}

	public void setCucumberEnabled(boolean cucumberEnabled) {
		this.cucumberEnabled = cucumberEnabled;
	}

	public int getCucumberComponents() {
		return cucumberComponents;
	}

	public void setCucumberComponents(int cucumberComponents) {
		this.cucumberComponents = cucumberComponents;
	}

	public boolean isSoapuiEnabled() {
		return soapuiEnabled;
	}

	public void setSoapuiEnabled(boolean soapuiEnabled) {
		this.soapuiEnabled = soapuiEnabled;
	}

	public int getSoapuiComponents() {
		return soapuiComponents;
	}

	public void setSoapuiComponents(int soapuiComponents) {
		this.soapuiComponents = soapuiComponents;
	}

	public boolean isTapsiumEnabled() {
		return tapsiumEnabled;
	}

	public void setTapsiumEnabled(boolean tapsiumEnabled) {
		this.tapsiumEnabled = tapsiumEnabled;
	}

	public int getTapsiumComponents() {
		return tapsiumComponents;
	}

	public void setTapsiumComponents(int tapsiumComponents) {
		this.tapsiumComponents = tapsiumComponents;
	}

	public boolean isJmeterEnabled() {
		return jmeterEnabled;
	}

	public void setJmeterEnabled(boolean jmeterEnabled) {
		this.jmeterEnabled = jmeterEnabled;
	}

	public int getJmeterComponents() {
		return jmeterComponents;
	}

	public void setJmeterComponents(int jmeterComponents) {
		this.jmeterComponents = jmeterComponents;
	}

	public boolean isLoadrunnerEnabled() {
		return loadrunnerEnabled;
	}

	public void setLoadrunnerEnabled(boolean loadrunnerEnabled) {
		this.loadrunnerEnabled = loadrunnerEnabled;
	}

	public int getLoadrunnerComponents() {
		return loadrunnerComponents;
	}

	public void setLoadrunnerComponents(int loadrunnerComponents) {
		this.loadrunnerComponents = loadrunnerComponents;
	}

	public String getVastId() {
		return vastId;
	}

	public void setVastId(String vastId) {
		this.vastId = vastId;
	}

	public String getVastAcronym() {
		return vastAcronym;
	}

	public void setVastAcronym(String vastAcronym) {
		this.vastAcronym = vastAcronym;
	}

	public String getVastAppName() {
		return vastAppName;
	}

	public void setVastAppName(String vastAppName) {
		this.vastAppName = vastAppName;
	}

	public String getVastBunit() {
		return vastBunit;
	}

	public void setVastBunit(String vastBunit) {
		this.vastBunit = vastBunit;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getModules() {
		return modules;
	}

	public void setModules(int modules) {
		this.modules = modules;
	}

	public boolean isFeatureEnabled() {
		return featureEnabled;
	}

	public void setFeatureEnabled(boolean featureEnabled) {
		this.featureEnabled = featureEnabled;
	}

	public int getFeatureComponents() {
		return featureComponents;
	}

	public void setFeatureComponents(int featureComponents) {
		this.featureComponents = featureComponents;
	}

	public boolean isCodeRepoEnabled() {
		return codeRepoEnabled;
	}

	public void setCodeRepoEnabled(boolean codeRepoEnabled) {
		this.codeRepoEnabled = codeRepoEnabled;
	}

	public int getRepoComponents() {
		return repoComponents;
	}

	public void setRepoComponents(int repoComponents) {
		this.repoComponents = repoComponents;
	}

	public boolean isBuildEnabled() {
		return buildEnabled;
	}

	public void setBuildEnabled(boolean buildEnabled) {
		this.buildEnabled = buildEnabled;
	}

	public int getBuildComponents() {
		return buildComponents;
	}

	public void setBuildComponents(int buildComponents) {
		this.buildComponents = buildComponents;
	}

	public boolean isProdDeployEnabled() {
		return prodDeployEnabled;
	}

	public void setProdDeployEnabled(boolean prodDeployEnabled) {
		this.prodDeployEnabled = prodDeployEnabled;
	}

	public int getProdDeployComponents() {
		return prodDeployComponents;
	}

	public void setProdDeployComponents(int prodDeployComponents) {
		this.prodDeployComponents = prodDeployComponents;
	}

	public boolean isUatDeployEnabled() {
		return uatDeployEnabled;
	}

	public void setUatDeployEnabled(boolean uatDeployEnabled) {
		this.uatDeployEnabled = uatDeployEnabled;
	}

	public int getUatDeployComponents() {
		return uatDeployComponents;
	}

	public void setUatDeployComponents(int uatDeployComponents) {
		this.uatDeployComponents = uatDeployComponents;
	}

	public boolean isSitDeployEnabled() {
		return sitDeployEnabled;
	}

	public void setSitDeployEnabled(boolean sitDeployEnabled) {
		this.sitDeployEnabled = sitDeployEnabled;
	}

	public int getSitDeploycomponents() {
		return sitDeploycomponents;
	}

	public void setSitDeploycomponents(int sitDeploycomponents) {
		this.sitDeploycomponents = sitDeploycomponents;
	}

	public boolean isDitDeployEnabled() {
		return ditDeployEnabled;
	}

	public void setDitDeployEnabled(boolean ditDeployEnabled) {
		this.ditDeployEnabled = ditDeployEnabled;
	}

	public int getDitDeployComponents() {
		return ditDeployComponents;
	}

	public void setDitDeployComponents(int ditDeployComponents) {
		this.ditDeployComponents = ditDeployComponents;
	}

	public boolean isSeleniumEnabled() {
		return seleniumEnabled;
	}

	public void setSeleniumEnabled(boolean seleniumEnabled) {
		this.seleniumEnabled = seleniumEnabled;
	}

	public int getSeleniumComponents() {
		return seleniumComponents;
	}

	public void setSeleniumComponents(int seleniumComponents) {
		this.seleniumComponents = seleniumComponents;
	}

	public boolean isCodeAnalysisEnabled() {
		return codeAnalysisEnabled;
	}

	public void setCodeAnalysisEnabled(boolean codeAnalysisEnabled) {
		this.codeAnalysisEnabled = codeAnalysisEnabled;
	}

	public int getCodeAnalysisComponents() {
		return codeAnalysisComponents;
	}

	public void setCodeAnalysisComponents(int codeAnalysisComponents) {
		this.codeAnalysisComponents = codeAnalysisComponents;
	}

	public boolean isFortifyEnabled() {
		return fortifyEnabled;
	}

	public void setFortifyEnabled(boolean fortifyEnabled) {
		this.fortifyEnabled = fortifyEnabled;
	}

	public int getFortifyComponents() {
		return fortifyComponents;
	}

	public void setFortifyComponents(int fortifyComponents) {
		this.fortifyComponents = fortifyComponents;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}


}
