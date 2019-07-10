package com.capitalone.dashboard.exec.model.vz;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vast")
public class Vast extends BaseModel {

	private String vastID;
	private String vastApplID;
	private String vastAppType;
	private String vastAppName;
	private String vastAcronym;
	private String vastInvestmentStatus;
	private String vastAvailabilityStatus;
	private String vastBusinessUnit;
	private String vastRoadMapIndicator;
	private String vastStatus;
	private String vastCustodianContactName;
	private String vastCustodianContactEmail;
	private String vastCustodianContactEid;
	private String vastTierOneContactName;
	private String vastTierOneContactEmail;
	private String vastTierOneContactEid;
	private String vastTierOneContactTitle;
	private String vastTierTwoContactName;
	private String vastTierTwoContactEmail;
	private String vastTierTwoContactEid;
	private String vastTierTwoContactTitle;
	private String vastTierThreeContactName;
	private String vastTierThreeContactEmail;
	private String vastTierThreeContactEid;
	private String vastTierThreeContactTitle;
	private String vastTierFourContactName;
	private String vastTierFourContactEmail;
	private String vastTierFourContactEid;
	private String vastTierFourContactTitle;
	private String vastTierFiveContactName;
	private String vastTierFiveContactEmail;
	private String vastTierFiveContactEid;
	private String vastTierFiveContactTitle;
	private String vastTierSixContactName;
	private String vastTierSixContactEmail;
	private String vastTierSixContactEid;
	private String vastTierSixContactTitle;
	private String vastTierSevenContactName;
	private String vastTierSevenContactEmail;
	private String vastTierSevenContactEid;
	private String vastTierSevenContactTitle;
	private Long updatedTimeStamp;
	private boolean isIT;

	public String getVastID() {
		return vastID;
	}

	public void setVastID(String vastID) {
		this.vastID = vastID;
	}

	public String getVastApplID() {
		return vastApplID;
	}

	public void setVastApplID(String vastApplID) {
		this.vastApplID = vastApplID;
	}

	public String getVastAppType() {
		return vastAppType;
	}

	public void setVastAppType(String vastAppType) {
		this.vastAppType = vastAppType;
	}

	public String getVastAppName() {
		return vastAppName;
	}

	public void setVastAppName(String vastAppName) {
		this.vastAppName = vastAppName;
	}

	public String getVastInvestmentStatus() {
		return vastInvestmentStatus;
	}

	public void setVastInvestmentStatus(String vastInvestmentStatus) {
		this.vastInvestmentStatus = vastInvestmentStatus;
	}

	public String getVastCustodianContactEmail() {
		return vastCustodianContactEmail;
	}

	public void setVastCustodianContactEmail(String vastCustodianContactEmail) {
		this.vastCustodianContactEmail = vastCustodianContactEmail;
	}

	public String getVastAcronym() {
		return vastAcronym;
	}

	public void setVastAcronym(String vastAcronym) {
		this.vastAcronym = vastAcronym;
	}

	public String getVastAvailabilityStatus() {
		return vastAvailabilityStatus;
	}

	public void setVastAvailabilityStatus(String vastAvailabilityStatus) {
		this.vastAvailabilityStatus = vastAvailabilityStatus;
	}

	public String getVastBusinessUnit() {
		return vastBusinessUnit;
	}

	public void setVastBusinessUnit(String vastBusinessUnit) {
		this.vastBusinessUnit = vastBusinessUnit;
	}

	public String getVastRoadMapIndicator() {
		return vastRoadMapIndicator;
	}

	public void setVastRoadMapIndicator(String vastRoadMapIndicator) {
		this.vastRoadMapIndicator = vastRoadMapIndicator;
	}

	public String getVastStatus() {
		return vastStatus;
	}

	public void setVastStatus(String vastStatus) {
		this.vastStatus = vastStatus;
	}

	public String getVastCustodianContactName() {
		return vastCustodianContactName;
	}

	public void setVastCustodianContactName(String vastCustodianContactName) {
		this.vastCustodianContactName = vastCustodianContactName;
	}

	public String getVastCustodianContactEid() {
		return vastCustodianContactEid;
	}

	public void setVastCustodianContactEid(String vastCustodianContactEid) {
		this.vastCustodianContactEid = vastCustodianContactEid;
	}

	public String getVastTierThreeContactName() {
		return vastTierThreeContactName;
	}

	public void setVastTierThreeContactName(String vastTierThreeContactName) {
		this.vastTierThreeContactName = vastTierThreeContactName;
	}

	public String getVastTierThreeContactEmail() {
		return vastTierThreeContactEmail;
	}

	public void setVastTierThreeContactEmail(String vastTierThreeContactEmail) {
		this.vastTierThreeContactEmail = vastTierThreeContactEmail;
	}

	public String getVastTierThreeContactEid() {
		return vastTierThreeContactEid;
	}

	public void setVastTierThreeContactEid(String vastTierThreeContactEid) {
		this.vastTierThreeContactEid = vastTierThreeContactEid;
	}

	public String getVastTierFourContactName() {
		return vastTierFourContactName;
	}

	public void setVastTierFourContactName(String vastTierFourContactName) {
		this.vastTierFourContactName = vastTierFourContactName;
	}

	public String getVastTierFiveContactEid() {
		return vastTierFiveContactEid;
	}

	public void setVastTierFiveContactEid(String vastTierFiveContactEid) {
		this.vastTierFiveContactEid = vastTierFiveContactEid;
	}

	public String getVastTierFiveContactEmail() {
		return vastTierFiveContactEmail;
	}

	public void setVastTierFiveContactEmail(String vastTierFiveContactEmail) {
		this.vastTierFiveContactEmail = vastTierFiveContactEmail;
	}

	public String getVastTierFourContactEid() {
		return vastTierFourContactEid;
	}

	public void setVastTierFourContactEid(String vastTierFourContactEid) {
		this.vastTierFourContactEid = vastTierFourContactEid;
	}

	public String getVastTierFourContactEmail() {
		return vastTierFourContactEmail;
	}

	public void setVastTierFourContactEmail(String vastTierFourContactEmail) {
		this.vastTierFourContactEmail = vastTierFourContactEmail;
	}

	public String getVastTierFiveContactName() {
		return vastTierFiveContactName;
	}

	public void setVastTierFiveContactName(String vastTierFiveContactName) {
		this.vastTierFiveContactName = vastTierFiveContactName;
	}

	public String getVastTierSixContactName() {
		return vastTierSixContactName;
	}

	public void setVastTierSixContactName(String vastTierSixContactName) {
		this.vastTierSixContactName = vastTierSixContactName;
	}

	public String getVastTierSixContactEmail() {
		return vastTierSixContactEmail;
	}

	public void setVastTierSixContactEmail(String vastTierSixContactEmail) {
		this.vastTierSixContactEmail = vastTierSixContactEmail;
	}

	public String getVastTierSixContactEid() {
		return vastTierSixContactEid;
	}

	public void setVastTierSixContactEid(String vastTierSixContactEid) {
		this.vastTierSixContactEid = vastTierSixContactEid;
	}

	public String getVastTierSevenContactName() {
		return vastTierSevenContactName;
	}

	public void setVastTierSevenContactName(String vastTierSevenContactName) {
		this.vastTierSevenContactName = vastTierSevenContactName;
	}

	public String getVastTierSevenContactEmail() {
		return vastTierSevenContactEmail;
	}

	public void setVastTierSevenContactEmail(String vastTierSevenContactEmail) {
		this.vastTierSevenContactEmail = vastTierSevenContactEmail;
	}

	public String getVastTierSevenContactEid() {
		return vastTierSevenContactEid;
	}

	public void setVastTierSevenContactEid(String vastTierSevenContactEid) {
		this.vastTierSevenContactEid = vastTierSevenContactEid;
	}

	public String getVastTierOneContactName() {
		return vastTierOneContactName;
	}

	public void setVastTierOneContactName(String vastTierOneContactName) {
		this.vastTierOneContactName = vastTierOneContactName;
	}

	public String getVastTierOneContactEmail() {
		return vastTierOneContactEmail;
	}

	public void setVastTierOneContactEmail(String vastTierOneContactEmail) {
		this.vastTierOneContactEmail = vastTierOneContactEmail;
	}

	public String getVastTierOneContactEid() {
		return vastTierOneContactEid;
	}

	public void setVastTierOneContactEid(String vastTierOneContactEid) {
		this.vastTierOneContactEid = vastTierOneContactEid;
	}

	public String getVastTierOneContactTitle() {
		return vastTierOneContactTitle;
	}

	public void setVastTierOneContactTitle(String vastTierOneContactTitle) {
		this.vastTierOneContactTitle = vastTierOneContactTitle;
	}

	public String getVastTierTwoContactName() {
		return vastTierTwoContactName;
	}

	public void setVastTierTwoContactName(String vastTierTwoContactName) {
		this.vastTierTwoContactName = vastTierTwoContactName;
	}

	public String getVastTierTwoContactEmail() {
		return vastTierTwoContactEmail;
	}

	public void setVastTierTwoContactEmail(String vastTierTwoContactEmail) {
		this.vastTierTwoContactEmail = vastTierTwoContactEmail;
	}

	public String getVastTierTwoContactEid() {
		return vastTierTwoContactEid;
	}

	public void setVastTierTwoContactEid(String vastTierTwoContactEid) {
		this.vastTierTwoContactEid = vastTierTwoContactEid;
	}

	public String getVastTierTwoContactTitle() {
		return vastTierTwoContactTitle;
	}

	public void setVastTierTwoContactTitle(String vastTierTwoContactTitle) {
		this.vastTierTwoContactTitle = vastTierTwoContactTitle;
	}

	public String getVastTierThreeContactTitle() {
		return vastTierThreeContactTitle;
	}

	public void setVastTierThreeContactTitle(String vastTierThreeContactTitle) {
		this.vastTierThreeContactTitle = vastTierThreeContactTitle;
	}

	public String getVastTierFourContactTitle() {
		return vastTierFourContactTitle;
	}

	public void setVastTierFourContactTitle(String vastTierFourContactTitle) {
		this.vastTierFourContactTitle = vastTierFourContactTitle;
	}

	public String getVastTierFiveContactTitle() {
		return vastTierFiveContactTitle;
	}

	public void setVastTierFiveContactTitle(String vastTierFiveContactTitle) {
		this.vastTierFiveContactTitle = vastTierFiveContactTitle;
	}

	public String getVastTierSixContactTitle() {
		return vastTierSixContactTitle;
	}

	public void setVastTierSixContactTitle(String vastTierSixContactTitle) {
		this.vastTierSixContactTitle = vastTierSixContactTitle;
	}

	public String getVastTierSevenContactTitle() {
		return vastTierSevenContactTitle;
	}

	public void setVastTierSevenContactTitle(String vastTierSevenContactTitle) {
		this.vastTierSevenContactTitle = vastTierSevenContactTitle;
	}

	public Long getUpdatedTimeStamp() {
		return updatedTimeStamp;
	}

	public void setUpdatedTimeStamp(Long updatedTimeStamp) {
		this.updatedTimeStamp = updatedTimeStamp;
	}

	public boolean isIT() {
		return isIT;
	}

	public void setIT(boolean isIT) {
		this.isIT = isIT;
	}

}