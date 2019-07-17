package com.capitalone.dashboard.ops;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.dao.CloudDAO;
import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import com.capitalone.dashboard.exec.model.CloudCost;
import com.capitalone.dashboard.exec.model.ExecutiveMetricsSeries;
import com.capitalone.dashboard.exec.model.ExecutiveModuleMetrics;
import com.capitalone.dashboard.exec.model.SeriesCount;
import com.capitalone.dashboard.exec.repository.CloudCostRepository;
import com.capitalone.dashboard.utils.GenericMethods;
import com.mongodb.MongoClient;

/**
 * @author RAISH4S
 */
@Component
@SuppressWarnings("PMD")
public class CloudOps {
	private final CloudDAO cloudDao;
	private final GenericMethods genericMethods;
	private final CloudCostRepository cloudCostRepository;
	private static final String TYPE = "type";
	private static final String PROD = "PROD";
	private static final String NONPROD = "NONPROD";
	private static final String STAGING = "STAGING";
	private static final String COST = "cost";
	private static final String ENCRYPTEDEBS = "encryptedEBS";
	private static final String UNENCRYPTEDEBS = "unencryptedEBS";
	private static final String ENCRYPTEDS3 = "encryptedS3";
	private static final String UNENCRYPTEDS3 = "unencryptedS3";
	private static final String MIGRATIONENABLED = "migrationEnabled";
	private static final String COSTOPTIMIZED = "costOptimized";
	private static final String AMI = "Total AMI Count";
	private static final String ELB = "Total ELB Count";
	private static final String RDS = "Total RDS Count";
	private static final String UNUSEDELB = "Total unusedElb";
	private static final String UNUSEDENI = "Total unusedEni";
	private static final String UNUSEDEBS = "Total unusedEbs";
	private static final String PRODENCRYPTEDEBS = "PROD encryptedEBS";
	private static final String PRODUNENCRYPTEDEBS = "PROD unencryptedEBS";
	private static final String PRODENCRYPTEDS3 = "PROD encryptedS3";
	private static final String PRODUNENCRYPTEDS3 = "PROD unencryptedS3";
	private static final String PRODMIGRATIONENABLED = "PROD migrationEnabled";
	private static final String PRODCOSTOPTIMIZED = "PROD costOptimized";
	private static final String PRODAMI = "PROD AMI Count";
	private static final String PRODELB = "PROD ELB Count";
	private static final String PRODRDS = "PROD RDS Count";
	private static final String PRODUNUSEDELB = "PROD unusedElb";
	private static final String PRODUNUSEDENI = "PROD unusedEni";
	private static final String PRODUNUSEDEBS = "PROD unusedEbs";
	private static final String NONPRODENCRYPTEDEBS = "NONPROD encryptedEBS";
	private static final String NONPRODUNENCRYPTEDEBS = "NONPROD unencryptedEBS";
	private static final String NONPRODENCRYPTEDS3 = "NONPROD encryptedS3";
	private static final String NONPRODUNENCRYPTEDS3 = "NONPROD unencryptedS3";
	private static final String NONPRODMIGRATIONENABLED = "NONPROD migrationEnabled";
	private static final String NONPRODCOSTOPTIMIZED = "NONPROD costOptimized";
	private static final String NONPRODAMI = "NONPROD AMI Count";
	private static final String NONPRODELB = "NONPROD ELB Count";
	private static final String NONPRODRDS = "NONPROD RDS Count";
	private static final String NONPRODUNUSEDELB = "NONPROD unusedElb";
	private static final String NONPRODUNUSEDENI = "NONPROD unusedEni";
	private static final String NONPRODUNUSEDEBS = "NONPROD unusedEbs";
	private static final String STAGINGENCRYPTEDEBS = "STAGING encryptedEBS";
	private static final String STAGINGUNENCRYPTEDEBS = "STAGING unencryptedEBS";
	private static final String STAGINGENCRYPTEDS3 = "STAGING encryptedS3";
	private static final String STAGINGUNENCRYPTEDS3 = "STAGING unencryptedS3";
	private static final String STAGINGMIGRATIONENABLED = "STAGING migrationEnabled";
	private static final String STAGINGCOSTOPTIMIZED = "STAGING costOptimized";
	private static final String STAGINGAMI = "STAGING AMI Count";
	private static final String STAGINGELB = "STAGING ELB Count";
	private static final String STAGINGRDS = "STAGING RDS Count";
	private static final String STAGINGUNUSEDELB = "STAGING unusedElb";
	private static final String STAGINGUNUSEDENI = "STAGING unusedEni";
	private static final String STAGINGUNUSEDEBS = "STAGING unusedEbs";
	Map<String, String> costLabel = new HashMap<>();
	Map<String, String> encryptedEBSLabel = new HashMap<>();
	Map<String, String> unencryptedEBSLabel = new HashMap<>();
	Map<String, String> encryptedS3Label = new HashMap<>();
	Map<String, String> unencryptedS3Label = new HashMap<>();
	Map<String, String> migrationEnabledLabel = new HashMap<>();
	Map<String, String> costOptimizedLabel = new HashMap<>();
	Map<String, String> amiLabel = new HashMap<>();
	Map<String, String> elbLabel = new HashMap<>();
	Map<String, String> rdsLabel = new HashMap<>();
	Map<String, String> unusedElbLabel = new HashMap<>();
	Map<String, String> unusedEniLabel = new HashMap<>();
	Map<String, String> unusedEbsLabel = new HashMap<>();
	Map<String, String> prodencryptedEBSLabel = new HashMap<>();
	Map<String, String> produnencryptedEBSLabel = new HashMap<>();
	Map<String, String> prodencryptedS3Label = new HashMap<>();
	Map<String, String> produnencryptedS3Label = new HashMap<>();
	Map<String, String> prodmigrationEnabledLabel = new HashMap<>();
	Map<String, String> prodcostOptimizedLabel = new HashMap<>();
	Map<String, String> prodamiLabel = new HashMap<>();
	Map<String, String> prodelbLabel = new HashMap<>();
	Map<String, String> prodrdsLabel = new HashMap<>();
	Map<String, String> produnusedElbLabel = new HashMap<>();
	Map<String, String> produnusedEniLabel = new HashMap<>();
	Map<String, String> produnusedEbsLabel = new HashMap<>();
	Map<String, String> nonprodencryptedEBSLabel = new HashMap<>();
	Map<String, String> nonprodunencryptedEBSLabel = new HashMap<>();
	Map<String, String> nonprodencryptedS3Label = new HashMap<>();
	Map<String, String> nonprodunencryptedS3Label = new HashMap<>();
	Map<String, String> nonprodmigrationEnabledLabel = new HashMap<>();
	Map<String, String> nonprodcostOptimizedLabel = new HashMap<>();
	Map<String, String> nonprodamiLabel = new HashMap<>();
	Map<String, String> nonprodelbLabel = new HashMap<>();
	Map<String, String> nonprodrdsLabel = new HashMap<>();
	Map<String, String> nonprodunusedElbLabel = new HashMap<>();
	Map<String, String> nonprodunusedEniLabel = new HashMap<>();
	Map<String, String> nonprodunusedEbsLabel = new HashMap<>();
	Map<String, String> stagingencryptedEBSLabel = new HashMap<>();
	Map<String, String> stagingunencryptedEBSLabel = new HashMap<>();
	Map<String, String> stagingencryptedS3Label = new HashMap<>();
	Map<String, String> stagingunencryptedS3Label = new HashMap<>();
	Map<String, String> stagingmigrationEnabledLabel = new HashMap<>();
	Map<String, String> stagingcostOptimizedLabel = new HashMap<>();
	Map<String, String> stagingamiLabel = new HashMap<>();
	Map<String, String> stagingelbLabel = new HashMap<>();
	Map<String, String> stagingrdsLabel = new HashMap<>();
	Map<String, String> stagingunusedElbLabel = new HashMap<>();
	Map<String, String> stagingunusedEniLabel = new HashMap<>();
	Map<String, String> stagingunusedEbsLabel = new HashMap<>();

	/**
	 * @param cloudDao
	 * @param genericMethods
	 * @param cloudCostRepository
	 */
	@Autowired
	public CloudOps(CloudDAO cloudDao, GenericMethods genericMethods, CloudCostRepository cloudCostRepository) {
		this.cloudDao = cloudDao;
		this.genericMethods = genericMethods;
		this.cloudCostRepository = cloudCostRepository;
	}

	/**
	 * @param appId
	 * @param client
	 * @param dateValue
	 * @return
	 */
	public List<SeriesCount> processSeriesCount(String appId, MongoClient client, String dateValue) {
		List<SeriesCount> seriesCountList = new ArrayList<>();
		SeriesCount costCount = new SeriesCount();
		SeriesCount encryptedEBSCount = new SeriesCount();
		SeriesCount unencryptedEBSCount = new SeriesCount();
		SeriesCount encryptedS3Count = new SeriesCount();
		SeriesCount unencryptedS3Count = new SeriesCount();
		SeriesCount migrationEnabledCount = new SeriesCount();
		SeriesCount costOptimizedCount = new SeriesCount();
		SeriesCount amiCount = new SeriesCount();
		SeriesCount elbCount = new SeriesCount();
		SeriesCount rdsCount = new SeriesCount();
		SeriesCount unusedElbCount = new SeriesCount();
		SeriesCount unusedEniCount = new SeriesCount();
		SeriesCount unusedEbsCount = new SeriesCount();
		SeriesCount prodEncryptedEBSCount = new SeriesCount();
		SeriesCount prodUnencryptedEBSCount = new SeriesCount();
		SeriesCount prodEncryptedS3Count = new SeriesCount();
		SeriesCount prodUnencryptedS3Count = new SeriesCount();
		SeriesCount prodMigrationEnabledCount = new SeriesCount();
		SeriesCount prodCostOptimizedCount = new SeriesCount();
		SeriesCount prodAmiCount = new SeriesCount();
		SeriesCount prodElbCount = new SeriesCount();
		SeriesCount prodRdsCount = new SeriesCount();
		SeriesCount prodUnusedElbCount = new SeriesCount();
		SeriesCount prodUnusedEniCount = new SeriesCount();
		SeriesCount prodUnusedEbsCount = new SeriesCount();
		SeriesCount nonprodEncryptedEBSCount = new SeriesCount();
		SeriesCount nonprodUnencryptedEBSCount = new SeriesCount();
		SeriesCount nonprodEncryptedS3Count = new SeriesCount();
		SeriesCount nonprodUnencryptedS3Count = new SeriesCount();
		SeriesCount nonprodMigrationEnabledCount = new SeriesCount();
		SeriesCount nonprodCostOptimizedCount = new SeriesCount();
		SeriesCount nonprodAmiCount = new SeriesCount();
		SeriesCount nonprodElbCount = new SeriesCount();
		SeriesCount nonprodRdsCount = new SeriesCount();
		SeriesCount nonprodUnusedElbCount = new SeriesCount();
		SeriesCount nonprodUnusedEniCount = new SeriesCount();
		SeriesCount nonprodUnusedEbsCount = new SeriesCount();
		SeriesCount stagingEncryptedEBSCount = new SeriesCount();
		SeriesCount stagingUnencryptedEBSCount = new SeriesCount();
		SeriesCount stagingEncryptedS3Count = new SeriesCount();
		SeriesCount stagingUnencryptedS3Count = new SeriesCount();
		SeriesCount stagingMigrationEnabledCount = new SeriesCount();
		SeriesCount stagingCostOptimizedCount = new SeriesCount();
		SeriesCount stagingAmiCount = new SeriesCount();
		SeriesCount stagingElbCount = new SeriesCount();
		SeriesCount stagingRdsCount = new SeriesCount();
		SeriesCount stagingUnusedElbCount = new SeriesCount();
		SeriesCount stagingUnusedEniCount = new SeriesCount();
		SeriesCount stagingUnusedEbsCount = new SeriesCount();
		costLabel.put(TYPE, COST);
		encryptedEBSLabel.put(TYPE, ENCRYPTEDEBS);
		unencryptedEBSLabel.put(TYPE, UNENCRYPTEDEBS);
		encryptedS3Label.put(TYPE, ENCRYPTEDS3);
		unencryptedS3Label.put(TYPE, UNENCRYPTEDS3);
		migrationEnabledLabel.put(TYPE, MIGRATIONENABLED);
		costOptimizedLabel.put(TYPE, COSTOPTIMIZED);
		amiLabel.put(TYPE, AMI);
		elbLabel.put(TYPE, ELB);
		rdsLabel.put(TYPE, RDS);
		unusedElbLabel.put(TYPE, UNUSEDELB);
		unusedEniLabel.put(TYPE, UNUSEDENI);
		unusedEbsLabel.put(TYPE, UNUSEDEBS);

		long totalEBS = cloudDao.getEBSByAppId(appId, client);
		long totalEncryptedEBS = cloudDao.getEncryptedEBSByAppId(appId, client);
		long totalS3 = cloudDao.getS3BucketsByAppId(appId, client);
		long totalEncryptedS3 = cloudDao.getEncryptedS3BucketsByAppId(appId, client);

		String dateFromDateWise = dateValue.substring(3);
		String date = "30-" + dateFromDateWise;
		CloudCost cost = cloudCostRepository.findByAppIdAndTime(appId, date);
		if (cost != null && cost.getCost() != null)
			costCount.setCount(Math.round(cost.getCost()));
		else
			costCount.setCount((long) 0);
		costCount.setLabel(costLabel);
		encryptedEBSCount.setCount(totalEncryptedEBS);
		encryptedEBSCount.setLabel(encryptedEBSLabel);
		unencryptedEBSCount.setCount(Math.abs(totalEBS - totalEncryptedEBS));
		unencryptedEBSCount.setLabel(unencryptedEBSLabel);
		encryptedS3Count.setCount(totalEncryptedS3);
		encryptedS3Count.setLabel(encryptedS3Label);
		unencryptedS3Count.setCount(totalS3 - totalEncryptedS3);
		unencryptedS3Count.setLabel(unencryptedS3Label);
		migrationEnabledCount.setCount(cloudDao.getInstancesByAppIdAndEnv(appId, client, PROD));
		migrationEnabledCount.setLabel(migrationEnabledLabel);
		costOptimizedCount.setCount((long) 3);
		costOptimizedCount.setLabel(costOptimizedLabel);
		amiCount.setCount(cloudDao.getInstancesByAppId(appId, client));
		amiCount.setLabel(amiLabel);
		elbCount.setCount(cloudDao.getELBsByAppId(appId, client));
		elbCount.setLabel(elbLabel);
		rdsCount.setCount(cloudDao.getRDSByAppId(appId, client));
		rdsCount.setLabel(rdsLabel);
		unusedElbCount.setCount(cloudDao.getUnusedELBsByAppId(appId, client));
		unusedElbCount.setLabel(unusedElbLabel);
		unusedEniCount.setCount(cloudDao.getUnusedENIsByAppId(appId, client));
		unusedEniCount.setLabel(unusedEniLabel);
		unusedEbsCount.setCount(cloudDao.getUnusedEBsByAppId(appId, client));
		unusedEbsCount.setLabel(unusedEbsLabel);

		seriesCountList.add(costCount);
		seriesCountList.add(encryptedEBSCount);
		seriesCountList.add(unencryptedEBSCount);
		seriesCountList.add(encryptedS3Count);
		seriesCountList.add(unencryptedS3Count);
		seriesCountList.add(migrationEnabledCount);
		seriesCountList.add(costOptimizedCount);
		seriesCountList.add(amiCount);
		seriesCountList.add(elbCount);
		seriesCountList.add(rdsCount);
		seriesCountList.add(unusedElbCount);
		seriesCountList.add(unusedEniCount);
		seriesCountList.add(unusedEbsCount);

		// prod
		prodencryptedEBSLabel.put(TYPE, PRODENCRYPTEDEBS);
		produnencryptedEBSLabel.put(TYPE, PRODUNENCRYPTEDEBS);
		prodencryptedS3Label.put(TYPE, PRODENCRYPTEDS3);
		produnencryptedS3Label.put(TYPE, PRODUNENCRYPTEDS3);
		prodmigrationEnabledLabel.put(TYPE, PRODMIGRATIONENABLED);
		prodcostOptimizedLabel.put(TYPE, PRODCOSTOPTIMIZED);
		prodamiLabel.put(TYPE, PRODAMI);
		prodelbLabel.put(TYPE, PRODELB);
		prodrdsLabel.put(TYPE, PRODRDS);
		produnusedElbLabel.put(TYPE, PRODUNUSEDELB);
		produnusedEniLabel.put(TYPE, PRODUNUSEDENI);
		produnusedEbsLabel.put(TYPE, PRODUNUSEDEBS);

		long prodtotalEBS = cloudDao.getEBSByAppIdAndEnv(appId, client, PROD);
		long prodtotalEncryptedEBS = cloudDao.getEncryptedEBSByAppIdAndEnv(appId, client, PROD);
		long prodtotalS3 = cloudDao.getS3BucketsByAppIdAndEnv(appId, client, PROD);
		long prodtotalEncryptedS3 = cloudDao.getS3BucketsByAppIdAndEnv(appId, client, PROD);
		prodEncryptedEBSCount.setCount(prodtotalEncryptedEBS);
		prodEncryptedEBSCount.setLabel(prodencryptedEBSLabel);
		prodUnencryptedEBSCount.setCount(Math.abs(prodtotalEBS - prodtotalEncryptedEBS));
		prodUnencryptedEBSCount.setLabel(produnencryptedEBSLabel);
		prodEncryptedS3Count.setCount(prodtotalEncryptedS3);
		prodEncryptedS3Count.setLabel(prodencryptedS3Label);
		prodUnencryptedS3Count.setCount(prodtotalS3 - prodtotalEncryptedS3);
		prodUnencryptedS3Count.setLabel(produnencryptedS3Label);
		prodMigrationEnabledCount.setCount(cloudDao.getInstancesByAppIdAndEnv(appId, client, PROD));
		prodMigrationEnabledCount.setLabel(prodmigrationEnabledLabel);
		prodCostOptimizedCount.setCount((long) 4);
		prodCostOptimizedCount.setLabel(prodcostOptimizedLabel);
		prodAmiCount.setCount(cloudDao.getInstancesByAppIdAndEnv(appId, client, PROD));
		prodAmiCount.setLabel(prodamiLabel);
		prodElbCount.setCount(cloudDao.getELBsByAppIdAndEnv(appId, client, PROD));
		prodElbCount.setLabel(prodelbLabel);
		prodRdsCount.setCount(cloudDao.getRDSByAppIdAndEnv(appId, client, PROD));
		prodRdsCount.setLabel(prodrdsLabel);
		prodUnusedElbCount.setCount(cloudDao.getUnusedELBsByAppIdAndEnv(appId, client, PROD));
		prodUnusedElbCount.setLabel(produnusedElbLabel);
		prodUnusedEniCount.setCount(cloudDao.getUnusedENIsByAppIdAndEnv(appId, client, PROD));
		prodUnusedEniCount.setLabel(produnusedEniLabel);
		prodUnusedEbsCount.setCount(cloudDao.getUnusedEBsByAppIdAndEnv(appId, client, PROD));
		prodUnusedEbsCount.setLabel(produnusedEbsLabel);

		seriesCountList.add(prodEncryptedEBSCount);
		seriesCountList.add(prodUnencryptedEBSCount);
		seriesCountList.add(prodEncryptedS3Count);
		seriesCountList.add(prodUnencryptedS3Count);
		seriesCountList.add(prodMigrationEnabledCount);
		seriesCountList.add(prodCostOptimizedCount);
		seriesCountList.add(prodAmiCount);
		seriesCountList.add(prodElbCount);
		seriesCountList.add(prodRdsCount);
		seriesCountList.add(prodUnusedElbCount);
		seriesCountList.add(prodUnusedEniCount);
		seriesCountList.add(prodUnusedEbsCount);

		// nonprod
		nonprodencryptedEBSLabel.put(TYPE, NONPRODENCRYPTEDEBS);
		nonprodunencryptedEBSLabel.put(TYPE, NONPRODUNENCRYPTEDEBS);
		nonprodencryptedS3Label.put(TYPE, NONPRODENCRYPTEDS3);
		nonprodunencryptedS3Label.put(TYPE, NONPRODUNENCRYPTEDS3);
		nonprodmigrationEnabledLabel.put(TYPE, NONPRODMIGRATIONENABLED);
		nonprodcostOptimizedLabel.put(TYPE, NONPRODCOSTOPTIMIZED);
		nonprodamiLabel.put(TYPE, NONPRODAMI);
		nonprodelbLabel.put(TYPE, NONPRODELB);
		nonprodrdsLabel.put(TYPE, NONPRODRDS);
		nonprodunusedElbLabel.put(TYPE, NONPRODUNUSEDELB);
		nonprodunusedEniLabel.put(TYPE, NONPRODUNUSEDENI);
		nonprodunusedEbsLabel.put(TYPE, NONPRODUNUSEDEBS);

		long nonprodtotalEBS = cloudDao.getEBSByAppIdAndEnv(appId, client, NONPROD);
		long nonprodtotalEncryptedEBS = cloudDao.getEncryptedEBSByAppIdAndEnv(appId, client, NONPROD);
		long nonprodtotalS3 = cloudDao.getS3BucketsByAppIdAndEnv(appId, client, NONPROD);
		long nonprodtotalEncryptedS3 = cloudDao.getS3BucketsByAppIdAndEnv(appId, client, NONPROD);

		nonprodEncryptedEBSCount.setCount(nonprodtotalEncryptedEBS);
		nonprodEncryptedEBSCount.setLabel(nonprodencryptedEBSLabel);
		nonprodUnencryptedEBSCount.setCount(Math.abs(nonprodtotalEBS - nonprodtotalEncryptedEBS));
		nonprodUnencryptedEBSCount.setLabel(nonprodunencryptedEBSLabel);
		nonprodEncryptedS3Count.setCount(nonprodtotalEncryptedS3);
		nonprodEncryptedS3Count.setLabel(nonprodencryptedS3Label);
		nonprodUnencryptedS3Count.setCount(nonprodtotalS3 - nonprodtotalEncryptedS3);
		nonprodUnencryptedS3Count.setLabel(nonprodunencryptedS3Label);
		nonprodMigrationEnabledCount.setCount(cloudDao.getInstancesByAppIdAndEnv(appId, client, NONPROD));
		nonprodMigrationEnabledCount.setLabel(nonprodmigrationEnabledLabel);
		nonprodCostOptimizedCount.setCount((long) 4);
		nonprodCostOptimizedCount.setLabel(nonprodcostOptimizedLabel);
		nonprodAmiCount.setCount(cloudDao.getInstancesByAppIdAndEnv(appId, client, NONPROD));
		nonprodAmiCount.setLabel(nonprodamiLabel);
		nonprodElbCount.setCount(cloudDao.getELBsByAppIdAndEnv(appId, client, NONPROD));
		nonprodElbCount.setLabel(nonprodelbLabel);
		nonprodRdsCount.setCount(cloudDao.getS3BucketsByAppIdAndEnv(appId, client, NONPROD));
		nonprodRdsCount.setLabel(nonprodrdsLabel);
		nonprodUnusedElbCount.setCount(cloudDao.getUnusedELBsByAppIdAndEnv(appId, client, NONPROD));
		nonprodUnusedElbCount.setLabel(nonprodunusedElbLabel);
		nonprodUnusedEniCount.setCount(cloudDao.getUnusedENIsByAppIdAndEnv(appId, client, NONPROD));
		nonprodUnusedEniCount.setLabel(nonprodunusedEniLabel);
		nonprodUnusedEbsCount.setCount(cloudDao.getUnusedEBsByAppIdAndEnv(appId, client, NONPROD));
		nonprodUnusedEbsCount.setLabel(nonprodunusedEbsLabel);
		seriesCountList.add(nonprodEncryptedEBSCount);
		seriesCountList.add(nonprodUnencryptedEBSCount);
		seriesCountList.add(nonprodEncryptedS3Count);
		seriesCountList.add(nonprodUnencryptedS3Count);
		seriesCountList.add(nonprodMigrationEnabledCount);
		seriesCountList.add(nonprodCostOptimizedCount);
		seriesCountList.add(nonprodAmiCount);
		seriesCountList.add(nonprodElbCount);
		seriesCountList.add(nonprodRdsCount);
		seriesCountList.add(nonprodUnusedElbCount);
		seriesCountList.add(nonprodUnusedEniCount);
		seriesCountList.add(nonprodUnusedEbsCount);
		// staging
		stagingencryptedEBSLabel.put(TYPE, STAGINGENCRYPTEDEBS);
		stagingunencryptedEBSLabel.put(TYPE, STAGINGUNENCRYPTEDEBS);
		stagingencryptedS3Label.put(TYPE, STAGINGENCRYPTEDS3);
		stagingunencryptedS3Label.put(TYPE, STAGINGUNENCRYPTEDS3);
		stagingmigrationEnabledLabel.put(TYPE, STAGINGMIGRATIONENABLED);
		stagingcostOptimizedLabel.put(TYPE, STAGINGCOSTOPTIMIZED);
		stagingamiLabel.put(TYPE, STAGINGAMI);
		stagingelbLabel.put(TYPE, STAGINGELB);
		stagingrdsLabel.put(TYPE, STAGINGRDS);
		stagingunusedElbLabel.put(TYPE, STAGINGUNUSEDELB);
		stagingunusedEniLabel.put(TYPE, STAGINGUNUSEDENI);
		stagingunusedEbsLabel.put(TYPE, STAGINGUNUSEDEBS);

		long stagingtotalEBS = cloudDao.getEBSByAppIdAndEnv(appId, client, STAGING);
		long stagingtotalEncryptedEBS = cloudDao.getEncryptedEBSByAppIdAndEnv(appId, client, STAGING);
		long stagingtotalS3 = cloudDao.getS3BucketsByAppIdAndEnv(appId, client, STAGING);
		long stagingtotalEncryptedS3 = cloudDao.getS3BucketsByAppIdAndEnv(appId, client, STAGING);

		stagingEncryptedEBSCount.setCount(stagingtotalEncryptedEBS);
		stagingEncryptedEBSCount.setLabel(stagingencryptedEBSLabel);
		stagingUnencryptedEBSCount.setCount(Math.abs(stagingtotalEBS - stagingtotalEncryptedEBS));
		stagingUnencryptedEBSCount.setLabel(stagingunencryptedEBSLabel);
		stagingEncryptedS3Count.setCount(stagingtotalEncryptedS3);
		stagingEncryptedS3Count.setLabel(stagingencryptedS3Label);
		stagingUnencryptedS3Count.setCount(stagingtotalS3 - stagingtotalEncryptedS3);
		stagingUnencryptedS3Count.setLabel(stagingunencryptedS3Label);
		stagingMigrationEnabledCount.setCount(cloudDao.getInstancesByAppIdAndEnv(appId, client, STAGING));
		stagingMigrationEnabledCount.setLabel(stagingmigrationEnabledLabel);
		stagingCostOptimizedCount.setCount((long) 4);
		stagingCostOptimizedCount.setLabel(stagingcostOptimizedLabel);
		stagingAmiCount.setCount(cloudDao.getInstancesByAppIdAndEnv(appId, client, STAGING));
		stagingAmiCount.setLabel(stagingamiLabel);
		stagingElbCount.setCount(cloudDao.getELBsByAppIdAndEnv(appId, client, STAGING));
		stagingElbCount.setLabel(stagingelbLabel);
		stagingRdsCount.setCount(cloudDao.getS3BucketsByAppIdAndEnv(appId, client, STAGING));
		stagingRdsCount.setLabel(stagingrdsLabel);
		stagingUnusedElbCount.setCount(cloudDao.getUnusedELBsByAppIdAndEnv(appId, client, STAGING));
		stagingUnusedElbCount.setLabel(stagingunusedElbLabel);
		stagingUnusedEniCount.setCount(cloudDao.getUnusedENIsByAppIdAndEnv(appId, client, STAGING));
		stagingUnusedEniCount.setLabel(stagingunusedEniLabel);
		stagingUnusedEbsCount.setCount(cloudDao.getUnusedEBsByAppIdAndEnv(appId, client, STAGING));
		stagingUnusedEbsCount.setLabel(stagingunusedEbsLabel);

		seriesCountList.add(stagingEncryptedEBSCount);
		seriesCountList.add(stagingUnencryptedEBSCount);
		seriesCountList.add(stagingEncryptedS3Count);
		seriesCountList.add(stagingUnencryptedS3Count);
		seriesCountList.add(stagingMigrationEnabledCount);
		seriesCountList.add(stagingCostOptimizedCount);
		seriesCountList.add(stagingAmiCount);
		seriesCountList.add(stagingElbCount);
		seriesCountList.add(stagingRdsCount);
		seriesCountList.add(stagingUnusedElbCount);
		seriesCountList.add(stagingUnusedEniCount);
		seriesCountList.add(stagingUnusedEbsCount);
		return seriesCountList;
	}

	private int getDaysAgoValue(Long timeStamp) {
		Date dateFromDB = new Date(timeStamp);
		Date presentDate = new Date();
		long diff = presentDate.getTime() - dateFromDB.getTime();
		return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	/**
	 * @param modules
	 * @return
	 */
	public List<MetricTimeSeriesElement> processExecutiveMetricsSeries(List<ExecutiveModuleMetrics> modules) {
		List<MetricTimeSeriesElement> metricTimeSeriesElementResponseList = new ArrayList<>();
		Map<Integer, List<MetricCount>> seriesTimeList = new HashMap<>();
		for (ExecutiveModuleMetrics executiveModuleMetrics : modules) {
			List<ExecutiveMetricsSeries> executiveMetricsSeriesList = executiveModuleMetrics.getSeries();
			if (!executiveMetricsSeriesList.isEmpty()) {
				for (ExecutiveMetricsSeries executiveMetricsSeries : executiveMetricsSeriesList) {
					int days = getDaysAgoValue(executiveMetricsSeries.getTimeStamp());
					long costCount = 0;
					long encryptedEBSCount = 0;
					long unencryptedEBSCount = 0;
					long encryptedS3Count = 0;
					long unencryptedS3Count = 0;
					long migrationEnabledCount = 0;
					long costOptimizedCount = 0;
					long amiCount = 0;
					long elbCount = 0;
					long rdsCount = 0;
					long unusedElbCount = 0;
					long unusedEniCount = 0;
					long unusedEbsCount = 0;
					costLabel.put(TYPE, COST);
					encryptedEBSLabel.put(TYPE, ENCRYPTEDEBS);
					unencryptedEBSLabel.put(TYPE, UNENCRYPTEDEBS);
					encryptedS3Label.put(TYPE, ENCRYPTEDS3);
					unencryptedS3Label.put(TYPE, UNENCRYPTEDS3);
					migrationEnabledLabel.put(TYPE, MIGRATIONENABLED);
					costOptimizedLabel.put(TYPE, COSTOPTIMIZED);
					amiLabel.put(TYPE, AMI);
					elbLabel.put(TYPE, ELB);
					rdsLabel.put(TYPE, RDS);
					unusedElbLabel.put(TYPE, UNUSEDELB);
					unusedEniLabel.put(TYPE, UNUSEDENI);
					unusedEbsLabel.put(TYPE, UNUSEDEBS);
					// Prod
					long prodEncryptedEBSCount = 0;
					long prodUnencryptedEBSCount = 0;
					long prodEncryptedS3Count = 0;
					long prodUnencryptedS3Count = 0;
					long prodMigrationEnabledCount = 0;
					long prodCostOptimizedCount = 0;
					long prodAmiCount = 0;
					long prodElbCount = 0;
					long prodRdsCount = 0;
					long prodUnusedElbCount = 0;
					long prodUnusedEniCount = 0;
					long prodUnusedEbsCount = 0;
					prodencryptedEBSLabel.put(TYPE, PRODENCRYPTEDEBS);
					produnencryptedEBSLabel.put(TYPE, PRODUNENCRYPTEDEBS);
					prodencryptedS3Label.put(TYPE, PRODENCRYPTEDS3);
					produnencryptedS3Label.put(TYPE, PRODUNENCRYPTEDS3);
					prodmigrationEnabledLabel.put(TYPE, PRODMIGRATIONENABLED);
					prodcostOptimizedLabel.put(TYPE, PRODCOSTOPTIMIZED);
					prodamiLabel.put(TYPE, PRODAMI);
					prodelbLabel.put(TYPE, PRODELB);
					prodrdsLabel.put(TYPE, PRODRDS);
					produnusedElbLabel.put(TYPE, PRODUNUSEDELB);
					produnusedEniLabel.put(TYPE, PRODUNUSEDENI);
					produnusedEbsLabel.put(TYPE, PRODUNUSEDEBS);
					// nonprod
					long nonprodEncryptedEBSCount = 0;
					long nonprodUnencryptedEBSCount = 0;
					long nonprodEncryptedS3Count = 0;
					long nonprodUnencryptedS3Count = 0;
					long nonprodMigrationEnabledCount = 0;
					long nonprodCostOptimizedCount = 0;
					long nonprodAmiCount = 0;
					long nonprodElbCount = 0;
					long nonprodRdsCount = 0;
					long nonprodUnusedElbCount = 0;
					long nonprodUnusedEniCount = 0;
					long nonprodUnusedEbsCount = 0;
					nonprodencryptedEBSLabel.put(TYPE, NONPRODENCRYPTEDEBS);
					nonprodunencryptedEBSLabel.put(TYPE, NONPRODUNENCRYPTEDEBS);
					nonprodencryptedS3Label.put(TYPE, NONPRODENCRYPTEDS3);
					nonprodunencryptedS3Label.put(TYPE, NONPRODUNENCRYPTEDS3);
					nonprodmigrationEnabledLabel.put(TYPE, NONPRODMIGRATIONENABLED);
					nonprodcostOptimizedLabel.put(TYPE, NONPRODCOSTOPTIMIZED);
					nonprodamiLabel.put(TYPE, NONPRODAMI);
					nonprodelbLabel.put(TYPE, NONPRODELB);
					nonprodrdsLabel.put(TYPE, NONPRODRDS);
					nonprodunusedElbLabel.put(TYPE, NONPRODUNUSEDELB);
					nonprodunusedEniLabel.put(TYPE, NONPRODUNUSEDENI);
					nonprodunusedEbsLabel.put(TYPE, NONPRODUNUSEDEBS);
					// staging
					long stagingEncryptedEBSCount = 0;
					long stagingUnencryptedEBSCount = 0;
					long stagingEncryptedS3Count = 0;
					long stagingUnencryptedS3Count = 0;
					long stagingMigrationEnabledCount = 0;
					long stagingCostOptimizedCount = 0;
					long stagingAmiCount = 0;
					long stagingElbCount = 0;
					long stagingRdsCount = 0;
					long stagingUnusedElbCount = 0;
					long stagingUnusedEniCount = 0;
					long stagingUnusedEbsCount = 0; // staging
					stagingencryptedEBSLabel.put(TYPE, STAGINGENCRYPTEDEBS);
					stagingunencryptedEBSLabel.put(TYPE, STAGINGUNENCRYPTEDEBS);
					stagingencryptedS3Label.put(TYPE, STAGINGENCRYPTEDS3);
					stagingunencryptedS3Label.put(TYPE, STAGINGUNENCRYPTEDS3);
					stagingmigrationEnabledLabel.put(TYPE, STAGINGMIGRATIONENABLED);
					stagingcostOptimizedLabel.put(TYPE, STAGINGCOSTOPTIMIZED);
					stagingamiLabel.put(TYPE, STAGINGAMI);
					stagingelbLabel.put(TYPE, STAGINGELB);
					stagingrdsLabel.put(TYPE, STAGINGRDS);
					stagingunusedElbLabel.put(TYPE, STAGINGUNUSEDELB);
					stagingunusedEniLabel.put(TYPE, STAGINGUNUSEDENI);
					stagingunusedEbsLabel.put(TYPE, STAGINGUNUSEDEBS);

					if (!seriesTimeList.containsKey(days)) {
						List<MetricCount> metricCountResponseList = new ArrayList<>();
						List<SeriesCount> countList = executiveMetricsSeries.getCounts();
						Map<String, Long> seriesCounts = genericMethods.processSeriesCount(countList);
						MetricCount metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(costLabel);
						metricCountResponse.setValue(seriesCounts.get("costCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(encryptedEBSLabel);
						metricCountResponse.setValue(seriesCounts.get("encryptedEBSCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(unencryptedEBSLabel);
						metricCountResponse.setValue(seriesCounts.get("unencryptedEBSCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(encryptedS3Label);
						metricCountResponse.setValue(seriesCounts.get("encryptedS3Count"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(unencryptedS3Label);
						metricCountResponse.setValue(seriesCounts.get("unencryptedS3Count"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(migrationEnabledLabel);
						metricCountResponse.setValue(seriesCounts.get("migrationEnabledCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(costOptimizedLabel);
						metricCountResponse.setValue(seriesCounts.get("costOptimizedCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(amiLabel);
						metricCountResponse.setValue(seriesCounts.get("amiCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(elbLabel);
						metricCountResponse.setValue(seriesCounts.get("elbCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(rdsLabel);
						metricCountResponse.setValue(seriesCounts.get("rdsCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(unusedElbLabel);
						metricCountResponse.setValue(seriesCounts.get("unusedElbCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(unusedEniLabel);
						metricCountResponse.setValue(seriesCounts.get("unusedEniCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(unusedEbsLabel);
						metricCountResponse.setValue(seriesCounts.get("unusedEbsCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodencryptedEBSLabel);
						metricCountResponse.setValue(seriesCounts.get("prodEncryptedEBSCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(produnencryptedEBSLabel);
						metricCountResponse.setValue(seriesCounts.get("prodUnencryptedEBSCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodencryptedS3Label);
						metricCountResponse.setValue(seriesCounts.get("prodEncryptedS3Count"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(produnencryptedS3Label);
						metricCountResponse.setValue(seriesCounts.get("prodUnencryptedS3Count"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodmigrationEnabledLabel);
						metricCountResponse.setValue(seriesCounts.get("prodMigrationEnabledCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodcostOptimizedLabel);
						metricCountResponse.setValue(seriesCounts.get("prodCostOptimizedCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodamiLabel);
						metricCountResponse.setValue(seriesCounts.get("prodAmiCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodelbLabel);
						metricCountResponse.setValue(seriesCounts.get("prodElbCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodrdsLabel);
						metricCountResponse.setValue(seriesCounts.get("prodRdsCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(produnusedElbLabel);
						metricCountResponse.setValue(seriesCounts.get("prodUnusedElbCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(produnusedEniLabel);
						metricCountResponse.setValue(seriesCounts.get("prodUnusedEniCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(produnusedEbsLabel);
						metricCountResponse.setValue(seriesCounts.get("prodUnusedEbsCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodencryptedEBSLabel);
						metricCountResponse.setValue(seriesCounts.get("nonprodEncryptedEBSCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodunencryptedEBSLabel);
						metricCountResponse.setValue(seriesCounts.get("nonprodUnencryptedEBSCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodencryptedS3Label);
						metricCountResponse.setValue(seriesCounts.get("nonprodEncryptedS3Count"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodunencryptedS3Label);
						metricCountResponse.setValue(seriesCounts.get("nonprodUnencryptedS3Count"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodmigrationEnabledLabel);
						metricCountResponse.setValue(seriesCounts.get("nonprodMigrationEnabledCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodcostOptimizedLabel);
						metricCountResponse.setValue(seriesCounts.get("nonprodCostOptimizedCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodamiLabel);
						metricCountResponse.setValue(seriesCounts.get("nonprodAmiCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodelbLabel);
						metricCountResponse.setValue(seriesCounts.get("nonprodElbCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodrdsLabel);
						metricCountResponse.setValue(seriesCounts.get("nonprodRdsCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodunusedElbLabel);
						metricCountResponse.setValue(seriesCounts.get("nonprodUnusedElbCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodunusedEniLabel);
						metricCountResponse.setValue(seriesCounts.get("nonprodUnusedEniCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodunusedEbsLabel);
						metricCountResponse.setValue(seriesCounts.get("nonprodUnusedEbsCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingencryptedEBSLabel);
						metricCountResponse.setValue(seriesCounts.get("stagingEncryptedEBSCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingunencryptedEBSLabel);
						metricCountResponse.setValue(seriesCounts.get("stagingUnencryptedEBSCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingencryptedS3Label);
						metricCountResponse.setValue(seriesCounts.get("stagingEncryptedS3Count"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingunencryptedS3Label);
						metricCountResponse.setValue(seriesCounts.get("stagingUnencryptedS3Count"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingmigrationEnabledLabel);
						metricCountResponse.setValue(seriesCounts.get("stagingMigrationEnabledCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingcostOptimizedLabel);
						metricCountResponse.setValue(seriesCounts.get("stagingCostOptimizedCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingamiLabel);
						metricCountResponse.setValue(seriesCounts.get("stagingAmiCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingelbLabel);
						metricCountResponse.setValue(seriesCounts.get("stagingElbCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingrdsLabel);
						metricCountResponse.setValue(seriesCounts.get("stagingRdsCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingunusedElbLabel);
						metricCountResponse.setValue(seriesCounts.get("stagingUnusedElbCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingunusedEniLabel);
						metricCountResponse.setValue(seriesCounts.get("stagingUnusedEniCount"));
						metricCountResponseList.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingunusedEbsLabel);
						metricCountResponse.setValue(seriesCounts.get("stagingUnusedEbsCount"));
						metricCountResponseList.add(metricCountResponse);

						seriesTimeList.put(days, metricCountResponseList);

					} else {
						List<SeriesCount> countList = executiveMetricsSeries.getCounts();
						List<MetricCount> countsProcessed = new ArrayList<>();
						List<MetricCount> counts = seriesTimeList.get(days);

						for (MetricCount metricCountResponse : counts) {
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(COST)) {
								costCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(ENCRYPTEDEBS)) {
								encryptedEBSCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(UNENCRYPTEDEBS)) {
								unencryptedEBSCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(ENCRYPTEDS3)) {
								encryptedS3Count += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(UNENCRYPTEDS3)) {
								unencryptedS3Count += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(COSTOPTIMIZED)) {
								costOptimizedCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(AMI)) {
								amiCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(ELB)) {
								elbCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(RDS)) {
								rdsCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(UNUSEDELB)) {
								unusedElbCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(UNUSEDENI)) {
								unusedEniCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(UNUSEDEBS)) {
								unusedEbsCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODENCRYPTEDEBS)) {
								prodEncryptedEBSCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODUNENCRYPTEDEBS)) {
								prodUnencryptedEBSCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODENCRYPTEDS3)) {
								prodEncryptedS3Count += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODUNENCRYPTEDS3)) {
								prodUnencryptedS3Count += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODCOSTOPTIMIZED)) {
								prodCostOptimizedCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODAMI)) {
								prodAmiCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODELB)) {
								prodElbCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODRDS)) {
								prodRdsCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODUNUSEDELB)) {
								prodUnusedElbCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODUNUSEDENI)) {
								prodUnusedEniCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(PRODUNUSEDEBS)) {
								prodUnusedEbsCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODENCRYPTEDEBS)) {
								nonprodEncryptedEBSCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODUNENCRYPTEDEBS)) {
								nonprodUnencryptedEBSCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODENCRYPTEDS3)) {
								nonprodEncryptedS3Count += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODUNENCRYPTEDS3)) {
								nonprodUnencryptedS3Count += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODCOSTOPTIMIZED)) {
								nonprodCostOptimizedCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODAMI)) {
								nonprodAmiCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODELB)) {
								nonprodElbCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODRDS)) {
								nonprodRdsCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODUNUSEDELB)) {
								nonprodUnusedElbCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODUNUSEDENI)) {
								nonprodUnusedEniCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(NONPRODUNUSEDEBS)) {
								nonprodUnusedEbsCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGENCRYPTEDEBS)) {
								stagingEncryptedEBSCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGUNENCRYPTEDEBS)) {
								stagingUnencryptedEBSCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGENCRYPTEDS3)) {
								stagingEncryptedS3Count += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGUNENCRYPTEDS3)) {
								stagingUnencryptedS3Count += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGCOSTOPTIMIZED)) {
								stagingCostOptimizedCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGAMI)) {
								stagingAmiCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGELB)) {
								stagingElbCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGRDS)) {
								stagingRdsCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGUNUSEDELB)) {
								stagingUnusedElbCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGUNUSEDENI)) {
								stagingUnusedEniCount += metricCountResponse.getValue();
							}
							if (metricCountResponse.getLabel().get(TYPE).equalsIgnoreCase(STAGINGUNUSEDEBS)) {
								stagingUnusedEbsCount += metricCountResponse.getValue();
							}
						}

						for (SeriesCount count : countList) {
							if (count.getLabel().get(TYPE).equalsIgnoreCase(COST)) {
								costCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(ENCRYPTEDEBS)) {
								encryptedEBSCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(UNENCRYPTEDEBS)) {
								unencryptedEBSCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(ENCRYPTEDS3)) {
								encryptedS3Count += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(UNENCRYPTEDS3)) {
								unencryptedS3Count += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(MIGRATIONENABLED) && count.getCount() > 0) {
								migrationEnabledCount++;
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(COSTOPTIMIZED)) {
								costOptimizedCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(AMI)) {
								amiCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(ELB)) {
								elbCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(RDS)) {
								rdsCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(UNUSEDELB)) {
								unusedElbCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(UNUSEDENI)) {
								unusedEniCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(UNUSEDEBS)) {
								unusedEbsCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODENCRYPTEDEBS)) {
								prodEncryptedEBSCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODUNENCRYPTEDEBS)) {
								prodUnencryptedEBSCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODENCRYPTEDS3)) {
								prodEncryptedS3Count += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODUNENCRYPTEDS3)) {
								prodUnencryptedS3Count += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODMIGRATIONENABLED)
									&& count.getCount() > 0) {
								prodMigrationEnabledCount++;
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODCOSTOPTIMIZED)) {
								prodCostOptimizedCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODAMI)) {
								prodAmiCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODELB)) {
								prodElbCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODRDS)) {
								prodRdsCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODUNUSEDELB)) {
								prodUnusedElbCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODUNUSEDENI)) {
								prodUnusedEniCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(PRODUNUSEDEBS)) {
								prodUnusedEbsCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODENCRYPTEDEBS)) {
								nonprodEncryptedEBSCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODUNENCRYPTEDEBS)) {
								nonprodUnencryptedEBSCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODENCRYPTEDS3)) {
								nonprodEncryptedS3Count += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODUNENCRYPTEDS3)) {
								nonprodUnencryptedS3Count += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODMIGRATIONENABLED)
									&& count.getCount() > 0) {
								nonprodMigrationEnabledCount++;
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODCOSTOPTIMIZED)) {
								nonprodCostOptimizedCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODAMI)) {
								nonprodAmiCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODELB)) {
								nonprodElbCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODRDS)) {
								nonprodRdsCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODUNUSEDELB)) {
								nonprodUnusedElbCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODUNUSEDENI)) {
								nonprodUnusedEniCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(NONPRODUNUSEDEBS)) {
								nonprodUnusedEbsCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGENCRYPTEDEBS)) {
								stagingEncryptedEBSCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGUNENCRYPTEDEBS)) {
								stagingUnencryptedEBSCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGENCRYPTEDS3)) {
								stagingEncryptedS3Count += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGUNENCRYPTEDS3)) {
								stagingUnencryptedS3Count += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGMIGRATIONENABLED)
									&& count.getCount() > 0) {
								stagingMigrationEnabledCount++;
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGCOSTOPTIMIZED)) {
								stagingCostOptimizedCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGAMI)) {
								stagingAmiCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGELB)) {
								stagingElbCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGRDS)) {
								stagingRdsCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGUNUSEDELB)) {
								stagingUnusedElbCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGUNUSEDENI)) {
								stagingUnusedEniCount += count.getCount();
							}
							if (count.getLabel().get(TYPE).equalsIgnoreCase(STAGINGUNUSEDEBS)) {
								stagingUnusedEbsCount += count.getCount();
							}
						}
						MetricCount metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(costLabel);
						metricCountResponse.setValue(costCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(encryptedEBSLabel);
						metricCountResponse.setValue(encryptedEBSCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(unencryptedEBSLabel);
						metricCountResponse.setValue(unencryptedEBSCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(encryptedS3Label);
						metricCountResponse.setValue(encryptedS3Count);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(unencryptedS3Label);
						metricCountResponse.setValue(unencryptedS3Count);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(migrationEnabledLabel);
						metricCountResponse.setValue(migrationEnabledCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(costOptimizedLabel);
						metricCountResponse.setValue(costOptimizedCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(amiLabel);
						metricCountResponse.setValue(amiCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(elbLabel);
						metricCountResponse.setValue(elbCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(rdsLabel);
						metricCountResponse.setValue(rdsCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(unusedElbLabel);
						metricCountResponse.setValue(unusedElbCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(unusedEniLabel);
						metricCountResponse.setValue(unusedEniCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(unusedEbsLabel);
						metricCountResponse.setValue(unusedEbsCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodencryptedEBSLabel);
						metricCountResponse.setValue(prodEncryptedEBSCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(produnencryptedEBSLabel);
						metricCountResponse.setValue(prodUnencryptedEBSCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodencryptedS3Label);
						metricCountResponse.setValue(prodEncryptedS3Count);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(produnencryptedS3Label);
						metricCountResponse.setValue(prodUnencryptedS3Count);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodmigrationEnabledLabel);
						metricCountResponse.setValue(prodMigrationEnabledCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodcostOptimizedLabel);
						metricCountResponse.setValue(prodCostOptimizedCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodamiLabel);
						metricCountResponse.setValue(prodAmiCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodelbLabel);
						metricCountResponse.setValue(prodElbCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(prodrdsLabel);
						metricCountResponse.setValue(prodRdsCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(produnusedElbLabel);
						metricCountResponse.setValue(prodUnusedElbCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(produnusedEniLabel);
						metricCountResponse.setValue(prodUnusedEniCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(produnusedEbsLabel);
						metricCountResponse.setValue(prodUnusedEbsCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodencryptedEBSLabel);
						metricCountResponse.setValue(nonprodEncryptedEBSCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodunencryptedEBSLabel);
						metricCountResponse.setValue(nonprodUnencryptedEBSCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodencryptedS3Label);
						metricCountResponse.setValue(nonprodEncryptedS3Count);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodunencryptedS3Label);
						metricCountResponse.setValue(nonprodUnencryptedS3Count);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodmigrationEnabledLabel);
						metricCountResponse.setValue(nonprodMigrationEnabledCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodcostOptimizedLabel);
						metricCountResponse.setValue(nonprodCostOptimizedCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodamiLabel);
						metricCountResponse.setValue(nonprodAmiCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodelbLabel);
						metricCountResponse.setValue(nonprodElbCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodrdsLabel);
						metricCountResponse.setValue(nonprodRdsCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodunusedElbLabel);
						metricCountResponse.setValue(nonprodUnusedElbCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodunusedEniLabel);
						metricCountResponse.setValue(nonprodUnusedEniCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(nonprodunusedEbsLabel);
						metricCountResponse.setValue(nonprodUnusedEbsCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingencryptedEBSLabel);
						metricCountResponse.setValue(stagingEncryptedEBSCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingunencryptedEBSLabel);
						metricCountResponse.setValue(stagingUnencryptedEBSCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingencryptedS3Label);
						metricCountResponse.setValue(stagingEncryptedS3Count);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingunencryptedS3Label);
						metricCountResponse.setValue(stagingUnencryptedS3Count);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingmigrationEnabledLabel);
						metricCountResponse.setValue(stagingMigrationEnabledCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingcostOptimizedLabel);
						metricCountResponse.setValue(stagingCostOptimizedCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingamiLabel);
						metricCountResponse.setValue(stagingAmiCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingelbLabel);
						metricCountResponse.setValue(stagingElbCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingrdsLabel);
						metricCountResponse.setValue(stagingRdsCount);
						countsProcessed.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingunusedElbLabel);
						metricCountResponse.setValue(stagingUnusedElbCount);
						countsProcessed.add(metricCountResponse);

						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingunusedEniLabel);
						metricCountResponse.setValue(stagingUnusedEniCount);
						countsProcessed.add(metricCountResponse);
						metricCountResponse = new MetricCount();
						metricCountResponse.setLabel(stagingunusedEbsLabel);
						metricCountResponse.setValue(stagingUnusedEbsCount);
						countsProcessed.add(metricCountResponse);
						seriesTimeList.replace(days, countsProcessed);
					}
				}
			}
		}
		if (!seriesTimeList.isEmpty()) {
			for (Entry<Integer, List<MetricCount>> entry : seriesTimeList.entrySet()) {
				MetricTimeSeriesElement metricTimeSeriesElementResponse = new MetricTimeSeriesElement();
				metricTimeSeriesElementResponse.setDaysAgo(entry.getKey());
				metricTimeSeriesElementResponse.setCounts(entry.getValue());
				metricTimeSeriesElementResponseList.add(metricTimeSeriesElementResponse);
			}
		}
		return metricTimeSeriesElementResponseList;
	}
}
