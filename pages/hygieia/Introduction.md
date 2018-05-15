---
title: The Hygieia Executive Dashboard
tags:
type: first_page
homepage: true
toc: false
keywords:
summary:
sidebar: hygieia_sidebar
permalink: Introduction.html
---

**The Hygieia Executive Dashboard** enables senior leadership at your organization to understand the DevOps state of maturity and risk across a wide range of product portfolios. This macro view of product portfolios allows them leverage to set precise goals for improvements on DevOps maturity, risk, and maintaining consistency across the portfolio. It provides insights into areas such as:

- Key Performance Indicators (KPIs) for quality aggregated across team instances
- Enterprise-wide quality standards to measure SDLC maturity risk and compliance
- Incidence visibility for service at various severity levels in production
- Alerts for product standards across products in a portfolio
- Drives usage of standard DevOps toolsets

## Why an Executive Dashboard?

Hygieia spans across various tools and systems, collecting information related to the CI/CD pipeline. This information is then displayed in the following dashboards:

- **The Team Dashboard** - offers component-level metrics to illustrate that a team can deliver software quickly to achieve DevOps maturity
- **The Product Dashboard** - shows each component’s lifecycle progression for a product through the development stages

These dashboards are mainly used by the development teams for monitoring the CI/CD pipeline. **The Hygieia Executive Dashboard** leverages the component and product information from these dashboards, aggregates them into metrics, and gives a visual representation that are meaningful at an executive (portfolio) level.

For example, as a portfolio owner, you will be able to see the amount of test coverage there is for your portfolio, or the known security violations that exist in all your products.

Each portfolio is structured such that there is a relation between the person that owns the portfolio, the products under the portfolio, and the components under each product.

## Brief Overview of the Dashboard

**The Hygieia Executive Dashboard** is designed to showcase the portfolio-product-component relationship for the senior leadership in the organization. Each view on the dashboard establishes the breakdown of the metrics displayed at the portfolio level.

The Hygieia Executive Dashboard is designed to display information for a combination of a portfolio, product, and component, which are defined as follows:

- **Portfolio** - A portfolio on the dashboard is a relationship between the senior executive, the products that they manage, and the components under each product.
- **Products** - List of all the all the products or applications under a portfolio (managed by an executive).
- **Components** - All the components associated with a product. Each component is unit of software that is part of the larger product or application.

Hygieia Executive collects information from the configuration management database, which gives the following details:
- Organizational structure of your company
- List of products owned by an executive
- The components under each product
