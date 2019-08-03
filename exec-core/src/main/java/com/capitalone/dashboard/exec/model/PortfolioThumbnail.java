package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="thumbnail")
public final class PortfolioThumbnail extends BaseConfigItem {

    public PortfolioThumbnail() {}

    public PortfolioThumbnail(String name, String lob) {
        super(name, lob);
    }
}
