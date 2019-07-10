package com.capitalone.dashboard.exec.model.vz;

public class ModuleMetricsStatus {
	
	private String moduleName;
	private boolean featureEnabled;
	private boolean featureDataAvailable;
	private boolean codeRepoEnabled;
	private boolean codeRepoDataAvailable;
	private boolean buildEnabled;
	private boolean buildDataAvailable;
	private boolean prodDeployEnabled;
	private boolean prodDeployDataAvailable;
	private boolean uatDeployEnabled;
	private boolean uatDeployDataAvailable;
	private boolean sitDeployEnabled;
	private boolean sitDeployDataAvailable;
	private boolean ditDeployEnabled;
	private boolean ditDeployDataAvailable;
	private boolean seleniumEnabled;
	private boolean seleniumDataAvailable;
	private boolean dastEnabled;
	private boolean dastDataAvailable;
	private boolean cloudEnabled;
	private boolean cloudDataAvailable;
	private boolean ravenEnabled;
	private boolean ravenDataAvailable;
	private boolean codeAnalysisEnabled;
	private boolean codeAnalysisDataAvailable;
	private boolean fortifyEnabled;
	private boolean fortifyDataAvailable;
	
	public boolean isDastEnabled() {
		return dastEnabled;
	}
	public void setDastEnabled(boolean dastEnabled) {
		this.dastEnabled = dastEnabled;
	}
	public boolean isDastDataAvailable() {
		return dastDataAvailable;
	}
	public void setDastDataAvailable(boolean dastDataAvailable) {
		this.dastDataAvailable = dastDataAvailable;
	}
	public boolean isCloudEnabled() {
		return cloudEnabled;
	}
	public void setCloudEnabled(boolean cloudEnabled) {
		this.cloudEnabled = cloudEnabled;
	}
	public boolean isCloudDataAvailable() {
		return cloudDataAvailable;
	}
	public void setCloudDataAvailable(boolean cloudDataAvailable) {
		this.cloudDataAvailable = cloudDataAvailable;
	}
	public boolean isRavenEnabled() {
		return ravenEnabled;
	}
	public void setRavenEnabled(boolean ravenEnabled) {
		this.ravenEnabled = ravenEnabled;
	}
	public boolean isRavenDataAvailable() {
		return ravenDataAvailable;
	}
	public void setRavenDataAvailable(boolean ravenDataAvailable) {
		this.ravenDataAvailable = ravenDataAvailable;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public boolean isFeatureEnabled() {
		return featureEnabled;
	}
	public void setFeatureEnabled(boolean featureEnabled) {
		this.featureEnabled = featureEnabled;
	}
	public boolean isFeatureDataAvailable() {
		return featureDataAvailable;
	}
	public void setFeatureDataAvailable(boolean featureDataAvailable) {
		this.featureDataAvailable = featureDataAvailable;
	}
	public boolean isCodeRepoEnabled() {
		return codeRepoEnabled;
	}
	public void setCodeRepoEnabled(boolean codeRepoEnabled) {
		this.codeRepoEnabled = codeRepoEnabled;
	}
	public boolean isCodeRepoDataAvailable() {
		return codeRepoDataAvailable;
	}
	public void setCodeRepoDataAvailable(boolean codeRepoDataAvailable) {
		this.codeRepoDataAvailable = codeRepoDataAvailable;
	}
	public boolean isBuildEnabled() {
		return buildEnabled;
	}
	public void setBuildEnabled(boolean buildEnabled) {
		this.buildEnabled = buildEnabled;
	}
	public boolean isBuildDataAvailable() {
		return buildDataAvailable;
	}
	public void setBuildDataAvailable(boolean buildDataAvailable) {
		this.buildDataAvailable = buildDataAvailable;
	}
	public boolean isProdDeployEnabled() {
		return prodDeployEnabled;
	}
	public void setProdDeployEnabled(boolean prodDeployEnabled) {
		this.prodDeployEnabled = prodDeployEnabled;
	}
	public boolean isProdDeployDataAvailable() {
		return prodDeployDataAvailable;
	}
	public void setProdDeployDataAvailable(boolean prodDeployDataAvailable) {
		this.prodDeployDataAvailable = prodDeployDataAvailable;
	}
	public boolean isUatDeployEnabled() {
		return uatDeployEnabled;
	}
	public void setUatDeployEnabled(boolean uatDeployEnabled) {
		this.uatDeployEnabled = uatDeployEnabled;
	}
	public boolean isUatDeployDataAvailable() {
		return uatDeployDataAvailable;
	}
	public void setUatDeployDataAvailable(boolean uatDeployDataAvailable) {
		this.uatDeployDataAvailable = uatDeployDataAvailable;
	}
	public boolean isSitDeployEnabled() {
		return sitDeployEnabled;
	}
	public void setSitDeployEnabled(boolean sitDeployEnabled) {
		this.sitDeployEnabled = sitDeployEnabled;
	}
	public boolean isSitDeployDataAvailable() {
		return sitDeployDataAvailable;
	}
	public void setSitDeployDataAvailable(boolean sitDeployDataAvailable) {
		this.sitDeployDataAvailable = sitDeployDataAvailable;
	}
	public boolean isDitDeployEnabled() {
		return ditDeployEnabled;
	}
	public void setDitDeployEnabled(boolean ditDeployEnabled) {
		this.ditDeployEnabled = ditDeployEnabled;
	}
	public boolean isDitDeployDataAvailable() {
		return ditDeployDataAvailable;
	}
	public void setDitDeployDataAvailable(boolean ditDeployDataAvailable) {
		this.ditDeployDataAvailable = ditDeployDataAvailable;
	}
	public boolean isSeleniumEnabled() {
		return seleniumEnabled;
	}
	public void setSeleniumEnabled(boolean seleniumEnabled) {
		this.seleniumEnabled = seleniumEnabled;
	}
	public boolean isSeleniumDataAvailable() {
		return seleniumDataAvailable;
	}
	public void setSeleniumDataAvailable(boolean seleniumDataAvailable) {
		this.seleniumDataAvailable = seleniumDataAvailable;
	}
	public boolean isCodeAnalysisEnabled() {
		return codeAnalysisEnabled;
	}
	public void setCodeAnalysisEnabled(boolean codeAnalysisEnabled) {
		this.codeAnalysisEnabled = codeAnalysisEnabled;
	}
	public boolean isCodeAnalysisDataAvailable() {
		return codeAnalysisDataAvailable;
	}
	public void setCodeAnalysisDataAvailable(boolean codeAnalysisDataAvailable) {
		this.codeAnalysisDataAvailable = codeAnalysisDataAvailable;
	}
	public boolean isFortifyEnabled() {
		return fortifyEnabled;
	}
	public void setFortifyEnabled(boolean fortifyEnabled) {
		this.fortifyEnabled = fortifyEnabled;
	}
	public boolean isFortifyDataAvailable() {
		return fortifyDataAvailable;
	}
	public void setFortifyDataAvailable(boolean fortifyDataAvailable) {
		this.fortifyDataAvailable = fortifyDataAvailable;
	}

}
