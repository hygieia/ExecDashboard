package com.capitalone.dashboard.exec.model.vz;

/**
 * AwsNonProdScoreRequest for Cloud Excellence
 * 
 * @author RHE94MG
 *
 */
public class AwsNonProdScoreRequest {

	private Integer automatedDevScore;
	private Integer automatedUATScore;
	private Integer nonProdMigrationScore;
	private Integer costOptimizationScore;
	private Integer onPremiseNonProdDecomScore;
	private Integer totalScore;

	public Integer getAutomatedDevScore() {
		return automatedDevScore;
	}

	public void setAutomatedDevScore(Integer automatedDevScore) {
		this.automatedDevScore = automatedDevScore;
	}

	public Integer getAutomatedUATScore() {
		return automatedUATScore;
	}

	public void setAutomatedUATScore(Integer automatedUATScore) {
		this.automatedUATScore = automatedUATScore;
	}

	public Integer getNonProdMigrationScore() {
		return nonProdMigrationScore;
	}

	public void setNonProdMigrationScore(Integer nonProdMigrationScore) {
		this.nonProdMigrationScore = nonProdMigrationScore;
	}

	public Integer getCostOptimizationScore() {
		return costOptimizationScore;
	}

	public void setCostOptimizationScore(Integer costOptimizationScore) {
		this.costOptimizationScore = costOptimizationScore;
	}

	public Integer getOnPremiseNonProdDecomScore() {
		return onPremiseNonProdDecomScore;
	}

	public void setOnPremiseNonProdDecomScore(Integer onPremiseNonProdDecomScore) {
		this.onPremiseNonProdDecomScore = onPremiseNonProdDecomScore;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

}
