package com.capitalone.dashboard.executive.model;

import com.capitalone.dashboard.exec.model.Portfolio;
import com.capitalone.dashboard.exec.model.Product;

import java.util.List;

public final class PortfolioResponse {
    private String id;
    private String lob;
    private String name;
    private ExecutiveResponse executive;
    List<Product> productList;

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLob() { return lob; }
    public void setLob(String lob) { this.lob = lob; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ExecutiveResponse getExecutive() { return executive; }
    public void setExecutive(ExecutiveResponse executive) { this.executive = executive; }

    public static PortfolioResponse getPortfolio(Portfolio portfolio) {
        if (portfolio == null) {return null;}

        PortfolioResponse portfolioResponse = new PortfolioResponse();
        portfolioResponse.setName(portfolio.getName()); // businessOwner
        portfolioResponse.setLob(portfolio.getLob());    // ownerDept
        List<Product> productList = portfolio.getProducts();
        portfolioResponse.setProductList(productList);
        ExecutiveResponse executiveResponse = ExecutiveResponse.getExecutive(portfolio);
        portfolioResponse.setExecutive(executiveResponse);
        return portfolioResponse;
    }

}
