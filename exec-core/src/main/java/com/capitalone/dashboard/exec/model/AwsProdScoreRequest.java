package com.capitalone.dashboard.exec.model;

/**
 * AwsProdScoreRequest for cloud Excellence
 * 
 * @author RHE94MG
 *
 */
public class AwsProdScoreRequest {

	private Integer prodMigrationICPScore;
	private Integer prodMigration201718Score;
	private Integer costOptimizationScore;
	private Integer onPremiseDecom201718Score;
	private Integer drMigration201718Score;
	private Integer totalScore;

	public Integer getProdMigrationICPScore() {
		return prodMigrationICPScore;
	}

	public void setProdMigrationICPScore(Integer prodMigrationICPScore) {
		this.prodMigrationICPScore = prodMigrationICPScore;
	}

	public Integer getProdMigration201718Score() {
		return prodMigration201718Score;
	}

	public void setProdMigration201718Score(Integer prodMigration201718Score) {
		this.prodMigration201718Score = prodMigration201718Score;
	}

	public Integer getCostOptimizationScore() {
		return costOptimizationScore;
	}

	public void setCostOptimizationScore(Integer costOptimizationScore) {
		this.costOptimizationScore = costOptimizationScore;
	}

	public Integer getOnPremiseDecom201718Score() {
		return onPremiseDecom201718Score;
	}

	public void setOnPremiseDecom201718Score(Integer onPremiseDecom201718Score) {
		this.onPremiseDecom201718Score = onPremiseDecom201718Score;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getDrMigration201718Score() {
		return drMigration201718Score;
	}

	public void setDrMigration201718Score(Integer drMigration201718Score) {
		this.drMigration201718Score = drMigration201718Score;
	}

}
