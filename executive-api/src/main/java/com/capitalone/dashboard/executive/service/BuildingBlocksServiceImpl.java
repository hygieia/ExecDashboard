package com.capitalone.dashboard.executive.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.exec.model.MetricLevel;
import com.capitalone.dashboard.exec.model.MetricType;
import com.capitalone.dashboard.exec.model.vz.BuildingBlocks;
import com.capitalone.dashboard.exec.model.vz.ExecutiveHierarchy;
import com.capitalone.dashboard.exec.model.vz.ExecutiveSummaryList;
import com.capitalone.dashboard.exec.model.vz.PortfolioResponse;
import com.capitalone.dashboard.exec.repository.vz.BuildingBlocksRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveHierarchyRepository;
import com.capitalone.dashboard.exec.repository.vz.ExecutiveSummaryListRepository;
import com.capitalone.dashboard.exec.repository.vz.PortfolioResponseRepository;

@Service
@SuppressWarnings("PMD")
public class BuildingBlocksServiceImpl implements BuildingBlocksService {

	private final BuildingBlocksRepository buildingBlocksRepository;
	private final PortfolioResponseRepository portfolioResponseRepository;
	private final ExecutiveSummaryListRepository executiveSummaryListRepository;
	private final ExecutiveHierarchyRepository executiveHierarchyRepository;

	@Autowired
	public BuildingBlocksServiceImpl(BuildingBlocksRepository buildingBlocksRepository,
			PortfolioResponseRepository portfolioResponseRepository,
			ExecutiveSummaryListRepository executiveSummaryListRepository,
			ExecutiveHierarchyRepository executiveHierarchyRepository) {
		this.buildingBlocksRepository = buildingBlocksRepository;
		this.portfolioResponseRepository = portfolioResponseRepository;
		this.executiveSummaryListRepository = executiveSummaryListRepository;
		this.executiveHierarchyRepository = executiveHierarchyRepository;
	}

	@Override
	public List<BuildingBlocks> getBuildingBlocks(MetricLevel metricLevel, MetricType metricType, String portfolioId) {
		ObjectId id = new ObjectId(portfolioId);
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(id);
		if (portfolioResponse != null) {
			ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository
					.findByEid(portfolioResponse.getEid());
			if (executiveSummaryList != null) {
				return buildingBlocksRepository.findByMetricLevelIdAndMetricLevelForProduct(executiveSummaryList.getAppId(),
						metricLevel, metricType.getName());
			}
		}
		return null;
	}

	@Override
	public List<BuildingBlocks> getBuildingBlocksProducts(String id) {
		ObjectId portfolioId = new ObjectId(id);
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(portfolioId);
		if (portfolioResponse != null) {
			ExecutiveSummaryList executiveSummaryList = executiveSummaryListRepository
					.findByEid(portfolioResponse.getEid());
			if (executiveSummaryList != null) {
				return buildingBlocksRepository.findByMetricLevelIdAndMetricLevel(executiveSummaryList.getAppId(),
						MetricLevel.PRODUCT);
			}
		}
		return null;
	}

	@Override
	public List<BuildingBlocks> getPortfolioProductsFavs(String id) {
		List<BuildingBlocks> executives = new ArrayList<>();
		ExecutiveSummaryList executiveSummaryListList = executiveSummaryListRepository.findByEid(id);
		if (executiveSummaryListList != null) {
			List<String> favExecs = executiveSummaryListList.getFavourite();
			if (favExecs != null) {
				favExecs = favExecs.stream().distinct().collect(Collectors.toList());
				executives = buildingBlocksRepository.findByMetricLevelIdAndMetricLevel(favExecs,
						MetricLevel.PORTFOLIO);
				if (executives != null && !executives.isEmpty()) {
					Collections.sort(executives, new Comparator<BuildingBlocks>() {
						@Override
						public int compare(BuildingBlocks b1, BuildingBlocks b2) {
							return b1.getName().compareTo(b2.getName());
						}
					});
				}
			}
		}
		return executives;
	}

	@Override
	public List<BuildingBlocks> getBuildingBlocksPortfolios(String id) {
		List<BuildingBlocks> executives = new ArrayList<>();
		ObjectId portfolioId = new ObjectId(id);
		PortfolioResponse portfolioResponse = portfolioResponseRepository.findOne(portfolioId);
		if (portfolioResponse != null) {
			ExecutiveHierarchy executiveHierarchy = executiveHierarchyRepository.findByEid(portfolioResponse.getEid());
			if (executiveHierarchy != null && executiveHierarchy.getReportees() != null) {
				List<String> reportings = executiveHierarchy.getDirectReportees();
				if (reportings != null && !reportings.isEmpty()) {
					reportings = reportings.stream().distinct().collect(Collectors.toList());
					executives = buildingBlocksRepository.findByMetricLevelIdAndMetricLevel(reportings,
							MetricLevel.PORTFOLIO);
					if (executives != null && !executives.isEmpty()) {
						Collections.sort(executives, new Comparator<BuildingBlocks>() {
							@Override
							public int compare(BuildingBlocks d1, BuildingBlocks d2) {
								return d1.getName().compareTo(d2.getName());
							}
						});
					}
				}
			}
		}
		return executives;
	}

	@Override
	public List<BuildingBlocks> getAllPortfolioProducts(String[] idList) {
		List<BuildingBlocks> executives = buildingBlocksRepository
				.findByMetricLevelIdAndMetricLevel(Arrays.asList(idList), MetricLevel.PORTFOLIO);
		if (executives != null && !executives.isEmpty()) {
			Collections.sort(executives, new Comparator<BuildingBlocks>() {
				@Override
				public int compare(BuildingBlocks d1, BuildingBlocks d2) {
					return d1.getName().compareTo(d2.getName());
				}
			});
		}
		return executives;
	}

	@Override
	public BuildingBlocks getBuildingBlocksProducts(String portfolioId, String productId) {
		ObjectId id = new ObjectId(productId);
		BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository.findOne(id);
		if (buildingBlockMetricSummary != null)
			return buildingBlocksRepository.findByMetricLevelIdAndMetricLevel(
					buildingBlockMetricSummary.getMetricLevelId(), MetricLevel.PRODUCT);
		return buildingBlockMetricSummary;
	}

	@Override
	public List<BuildingBlocks> getBuildingBlocksComponents(String productId) {
		List<BuildingBlocks> result = new ArrayList<>();
		ObjectId id = new ObjectId(productId);
		BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository.findOne(id);
		if (buildingBlockMetricSummary != null)
			return buildingBlocksRepository.getAllByMetricLevelIdAndMetricLevel(
					buildingBlockMetricSummary.getMetricLevelId(), MetricLevel.COMPONENT);
		return result;
	}

	@Override
	public List<BuildingBlocks> getBuildingBlocksComponentsForMetric(String productId, MetricType metricType) {
		List<BuildingBlocks> result = new ArrayList<>();
		ObjectId id = new ObjectId(productId);
		BuildingBlocks buildingBlockMetricSummary = buildingBlocksRepository.findOne(id);
		if (buildingBlockMetricSummary != null)
			return buildingBlocksRepository.findByMetricLevelIdAndMetricLevelAndMetricType(
					buildingBlockMetricSummary.getMetricLevelId(), MetricLevel.COMPONENT, metricType);
		return result;
	}

}
