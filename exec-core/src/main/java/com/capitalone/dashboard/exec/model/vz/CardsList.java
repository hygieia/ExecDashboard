package com.capitalone.dashboard.exec.model.vz;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author V143611
 *
 */
@Document(collection = "cards_list")
public class CardsList extends BaseModel {

	@Indexed(unique = true)
	private String cardName;
	private Boolean enabled;
	private String previewName;
	private Boolean defaultMetrics;

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getPreviewName() {
		return previewName;
	}

	public void setPreviewName(String previewName) {
		this.previewName = previewName;
	}

	public Boolean getDefaultMetrics() {
		return defaultMetrics;
	}

	public void setDefaultMetrics(Boolean defaultMetrics) {
		this.defaultMetrics = defaultMetrics;
	}

}
