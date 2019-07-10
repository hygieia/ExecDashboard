package com.capitalone.dashboard.exec.model.vz;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A self-contained, independently deployable piece of the larger application.
 * Each component of an application has a different source repo, build job,
 * deploy job, etc.
 *
 */
@Document(collection = "components")
public class ComponentClass extends BaseModel {
	private String name; // must be unique to the application
	private String owner;
	private List<Owner> owners = new ArrayList<>();
	private String title;
	private String type;
	private String template;
	private Map<CollectorType, List<CollectorItem>> collectorItems = new HashMap<>();

	public ComponentClass() {
	}

	public ComponentClass(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Map<CollectorType, List<CollectorItem>> getCollectorItems() {
		return collectorItems;
	}

	public List<CollectorItem> getCollectorItems(CollectorType type) {
		return collectorItems.get(type);
	}

	public void setCollectorItems(CollectorType collectorType, List<CollectorItem> newCollectorItems) {
		collectorItems.put(collectorType, newCollectorItems);
	}

	public void addCollectorItem(CollectorType collectorType, CollectorItem collectorItem) {
		// Currently only one collectorItem per collectorType is supported
		if (collectorItems.get(collectorType) == null) {
			collectorItems.put(collectorType, Arrays.asList(collectorItem));
		} else {
			List<CollectorItem> existing = new ArrayList<>(collectorItems.get(collectorType));
			if (isNewCollectorItem(existing, collectorItem)) {
				existing.add(collectorItem);
				collectorItems.put(collectorType, existing);
			}
		}
	}

	private boolean isNewCollectorItem(List<CollectorItem> existing, CollectorItem item) {
		for (CollectorItem ci : existing) {
			if (ci.getId().equals(item.getId()))
				return false;
		}
		return true;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public CollectorItem getFirstCollectorItemForType(CollectorType type) {

		if (getCollectorItems().get(type) == null) {
			return null;
		}
		List<CollectorItem> collectorItems = new ArrayList<>();
		collectorItems.addAll(getCollectorItems().get(type));
		return collectorItems.get(0);
	}

	public List<Owner> getOwners() {
		return owners;
	}

	public void setOwners(List<Owner> owners) {
		this.owners = owners;
	}

}
