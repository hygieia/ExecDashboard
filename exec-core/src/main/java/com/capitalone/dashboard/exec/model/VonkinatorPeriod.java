package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author V143611
 *
 */
@Document(collection = "vonkinator_date")
public class VonkinatorPeriod extends BaseModel {

	private Long startDate;
	private Long endDate;
	private String roundStartDate;
	private String roundEndDate;
	private String period;
	private int order;
	@Indexed(name = "active")
	private Boolean active;

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public String getRoundStartDate() {
		return roundStartDate;
	}

	public void setRoundStartDate(String roundStartDate) {
		this.roundStartDate = roundStartDate;
	}

	public String getRoundEndDate() {
		return roundEndDate;
	}

	public void setRoundEndDate(String roundEndDate) {
		this.roundEndDate = roundEndDate;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
