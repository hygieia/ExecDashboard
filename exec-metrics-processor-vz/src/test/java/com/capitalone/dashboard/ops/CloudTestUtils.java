package com.capitalone.dashboard.ops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.capitalone.dashboard.exec.model.MetricCount;
import com.capitalone.dashboard.exec.model.vz.SeriesCount;

/**
 * @author raish4s
 *
 */
@Component
public class CloudTestUtils {
	private static final String TYPE = "type";
	private static final String COST = "cost";
	private static final String ENCRYPTEDEBS = "encryptedEBS";
	private static final String UNENCRYPTEDEBS = "unencryptedEBS";
	private static final String ENCRYPTEDS3 = "encryptedS3";
	private static final String UNENCRYPTEDS3 = "unencryptedS3";
	private static final String MIGRATIONENABLED = "migrationEnabled";
	private static final String COSTOPTIMIZED = "costOptimized";
	private static final String AMI = "Total AMI Count";
	private static final String ELB = "Total ELB Count";
	private static final String S3 = "Total S3 Count";
	private static final String EBS = "Total EBS Count";
	private static final String UNUSEDELB = "Total unusedElb";
	private static final String UNUSEDENI = "Total unusedEni";
	private static final String UNUSEDEBS = "Total unusedEbs";
	private static final String PRODCOST = "PROD cost";
	private static final String PRODENCRYPTEDEBS = "PROD encryptedEBS";
	private static final String PRODUNENCRYPTEDEBS = "PROD unencryptedEBS";
	private static final String PRODENCRYPTEDS3 = "PROD encryptedS3";
	private static final String PRODUNENCRYPTEDS3 = "PROD unencryptedS3";
	private static final String PRODMIGRATIONENABLED = "PROD migrationEnabled";
	private static final String PRODCOSTOPTIMIZED = "PRODcostOptimized";
	private static final String PRODAMI = "PROD AMI Count";
	private static final String PRODELB = "PROD ELB Count";
	private static final String PRODS3 = "PROD S3 Count";
	private static final String PRODEBS = "PROD EBS Count";
	private static final String PRODUNUSEDELB = "PROD unusedElb";
	private static final String PRODUNUSEDENI = "PROD unusedEni";
	private static final String PRODUNUSEDEBS = "PROD unusedEbs";
	private static final String NONPRODCOST = "NON cost";
	private static final String NONPRODENCRYPTEDEBS = "NONPROD encryptedEBS";
	private static final String NONPRODUNENCRYPTEDEBS = "NONPROD unencryptedEBS";
	private static final String NONPRODENCRYPTEDS3 = "NONPROD encryptedS3";
	private static final String NONPRODUNENCRYPTEDS3 = "NONPROD unencryptedS3";
	private static final String NONPRODMIGRATIONENABLED = "NONPROD migrationEnabled";
	private static final String NONPRODCOSTOPTIMIZED = "NONPROD costOptimized";
	private static final String NONPRODAMI = "NONPROD AMI Count";
	private static final String NONPRODELB = "NONPROD ELB Count";
	private static final String NONPRODS3 = "NONPROD S3 Count";
	private static final String NONPRODEBS = "NONPROD EBS Count";
	private static final String NONPRODUNUSEDELB = "NONPROD unusedElb";
	private static final String NONPRODUNUSEDENI = "NONPROD unusedEni";
	private static final String NONPRODUNUSEDEBS = "NONPROD unusedEbs";
	private static final String STAGINGCOST = "STAGING cost";
	private static final String STAGINGENCRYPTEDEBS = "STAGING encryptedEBS";
	private static final String STAGINGUNENCRYPTEDEBS = "STAGING unencryptedEBS";
	private static final String STAGINGENCRYPTEDS3 = "STAGING encryptedS3";
	private static final String STAGINGUNENCRYPTEDS3 = "STAGING unencryptedS3";
	private static final String STAGINGMIGRATIONENABLED = "STAGING migrationEnabled";
	private static final String STAGINGCOSTOPTIMIZED = "STAGING costOptimized";
	private static final String STAGINGAMI = "STAGING AMI Count";
	private static final String STAGINGELB = "STAGING ELB Count";
	private static final String STAGINGS3 = "STAGING S3 Count";
	private static final String STAGINGEBS = "STAGING EBS Count";
	private static final String STAGINGUNUSEDELB = "STAGING unusedElb";
	private static final String STAGINGUNUSEDENI = "STAGING unusedEni";
	private static final String STAGINGUNUSEDEBS = "STAGING unusedEbs";

	/**
	 * @return
	 */
	public List<SeriesCount> getSeries() {
		List<SeriesCount> counts = new ArrayList<>();
		Map<String, String> prodcost = new HashMap<>();// prod
		Map<String, String> prodencryptedEBS = new HashMap<>();
		Map<String, String> produnencryptedEBS = new HashMap<>();
		Map<String, String> prodencryptedS3 = new HashMap<>();
		Map<String, String> produnencryptedS3 = new HashMap<>();
		Map<String, String> prodmigrationEnabled = new HashMap<>();
		Map<String, String> prodcostOptimized = new HashMap<>();
		Map<String, String> prodami = new HashMap<>();
		Map<String, String> prodelb = new HashMap<>();
		Map<String, String> prods3 = new HashMap<>();
		Map<String, String> prodebs = new HashMap<>();
		Map<String, String> produnusedElb = new HashMap<>();
		Map<String, String> produnusedEni = new HashMap<>();
		Map<String, String> produnusedEbs = new HashMap<>();
		Map<String, String> nonprodcost = new HashMap<>(); // nonprod
		Map<String, String> nonprodencryptedEBS = new HashMap<>();
		Map<String, String> nonprodunencryptedEBS = new HashMap<>();
		Map<String, String> nonprodencryptedS3 = new HashMap<>();
		Map<String, String> nonprodunencryptedS3 = new HashMap<>();
		Map<String, String> nonprodmigrationEnabled = new HashMap<>();
		Map<String, String> nonprodcostOptimized = new HashMap<>();
		Map<String, String> nonprodami = new HashMap<>();
		Map<String, String> nonprodelb = new HashMap<>();
		Map<String, String> nonprods3 = new HashMap<>();
		Map<String, String> nonprodebs = new HashMap<>();
		Map<String, String> nonprodunusedElb = new HashMap<>();
		Map<String, String> nonprodunusedEni = new HashMap<>();
		Map<String, String> nonprodunusedEbs = new HashMap<>();
		Map<String, String> stagingcost = new HashMap<>(); // staging
		Map<String, String> stagingencryptedEBS = new HashMap<>();
		Map<String, String> stagingunencryptedEBS = new HashMap<>();
		Map<String, String> stagingencryptedS3 = new HashMap<>();
		Map<String, String> stagingunencryptedS3 = new HashMap<>();
		Map<String, String> stagingmigrationEnabled = new HashMap<>();
		Map<String, String> stagingcostOptimized = new HashMap<>();
		Map<String, String> stagingami = new HashMap<>();
		Map<String, String> stagingelb = new HashMap<>();
		Map<String, String> stagings3 = new HashMap<>();
		Map<String, String> stagingebs = new HashMap<>();
		Map<String, String> stagingunusedElb = new HashMap<>();
		Map<String, String> stagingunusedEni = new HashMap<>();
		Map<String, String> stagingunusedEbs = new HashMap<>();
		Map<String, String> cost = new HashMap<>();
		Map<String, String> encryptedEBS = new HashMap<>();
		Map<String, String> unencryptedEBS = new HashMap<>();
		Map<String, String> encryptedS3 = new HashMap<>();
		Map<String, String> unencryptedS3 = new HashMap<>();
		Map<String, String> migrationEnabled = new HashMap<>();
		Map<String, String> costOptimized = new HashMap<>();
		Map<String, String> ami = new HashMap<>();
		Map<String, String> elb = new HashMap<>();
		Map<String, String> s3 = new HashMap<>();
		Map<String, String> ebs = new HashMap<>();
		Map<String, String> unusedElb = new HashMap<>();
		Map<String, String> unusedEni = new HashMap<>();
		Map<String, String> unusedEbs = new HashMap<>();
		SeriesCount costCount = new SeriesCount();
		SeriesCount encryptedEBSCount = new SeriesCount();
		SeriesCount unencryptedEBSCount = new SeriesCount();
		SeriesCount encryptedS3Count = new SeriesCount();
		SeriesCount unencryptedS3Count = new SeriesCount();
		SeriesCount migrationEnabledCount = new SeriesCount();
		SeriesCount costOptimizedCount = new SeriesCount();
		SeriesCount amiCount = new SeriesCount();
		SeriesCount elbCount = new SeriesCount();
		SeriesCount s3Count = new SeriesCount();
		SeriesCount ebsCount = new SeriesCount();
		SeriesCount unusedElbCount = new SeriesCount();
		SeriesCount unusedEniCount = new SeriesCount();
		SeriesCount unusedEbsCount = new SeriesCount();
		SeriesCount prodCostCount = new SeriesCount();// prod
		SeriesCount prodEncryptedEBSCount = new SeriesCount();
		SeriesCount prodUnencryptedEBSCount = new SeriesCount();
		SeriesCount prodEncryptedS3Count = new SeriesCount();
		SeriesCount prodUnencryptedS3Count = new SeriesCount();
		SeriesCount prodMigrationEnabledCount = new SeriesCount();
		SeriesCount prodCostOptimizedCount = new SeriesCount();
		SeriesCount prodAmiCount = new SeriesCount();
		SeriesCount prodElbCount = new SeriesCount();
		SeriesCount prodS3Count = new SeriesCount();
		SeriesCount prodEbsCount = new SeriesCount();
		SeriesCount prodUnusedElbCount = new SeriesCount();
		SeriesCount prodUnusedEniCount = new SeriesCount();
		SeriesCount prodUnusedEbsCount = new SeriesCount();
		SeriesCount nonprodCostCount = new SeriesCount();// nonprod
		SeriesCount nonprodEncryptedEBSCount = new SeriesCount();
		SeriesCount nonprodUnencryptedEBSCount = new SeriesCount();
		SeriesCount nonprodEncryptedS3Count = new SeriesCount();
		SeriesCount nonprodUnencryptedS3Count = new SeriesCount();
		SeriesCount nonprodMigrationEnabledCount = new SeriesCount();
		SeriesCount nonprodCostOptimizedCount = new SeriesCount();
		SeriesCount nonprodAmiCount = new SeriesCount();
		SeriesCount nonprodElbCount = new SeriesCount();
		SeriesCount nonprodS3Count = new SeriesCount();
		SeriesCount nonprodEbsCount = new SeriesCount();
		SeriesCount nonprodUnusedElbCount = new SeriesCount();
		SeriesCount nonprodUnusedEniCount = new SeriesCount();
		SeriesCount nonprodUnusedEbsCount = new SeriesCount();
		SeriesCount stagingCostCount = new SeriesCount(); // staging
		SeriesCount stagingEncryptedEBSCount = new SeriesCount();
		SeriesCount stagingUnencryptedEBSCount = new SeriesCount();
		SeriesCount stagingEncryptedS3Count = new SeriesCount();
		SeriesCount stagingUnencryptedS3Count = new SeriesCount();
		SeriesCount stagingMigrationEnabledCount = new SeriesCount();
		SeriesCount stagingCostOptimizedCount = new SeriesCount();
		SeriesCount stagingAmiCount = new SeriesCount();
		SeriesCount stagingElbCount = new SeriesCount();
		SeriesCount stagingS3Count = new SeriesCount();
		SeriesCount stagingEbsCount = new SeriesCount();
		SeriesCount stagingUnusedElbCount = new SeriesCount();
		SeriesCount stagingUnusedEniCount = new SeriesCount();
		SeriesCount stagingUnusedEbsCount = new SeriesCount();
		cost.put(TYPE, COST);
		encryptedEBS.put(TYPE, ENCRYPTEDEBS);
		unencryptedEBS.put(TYPE, UNENCRYPTEDEBS);
		encryptedS3.put(TYPE, ENCRYPTEDS3);
		unencryptedS3.put(TYPE, UNENCRYPTEDS3);
		migrationEnabled.put(TYPE, MIGRATIONENABLED);
		costOptimized.put(TYPE, COSTOPTIMIZED);
		ami.put(TYPE, AMI);
		elb.put(TYPE, ELB);
		s3.put(TYPE, S3);
		ebs.put(TYPE, EBS);
		unusedElb.put(TYPE, UNUSEDELB);
		unusedEni.put(TYPE, UNUSEDENI);
		unusedEbs.put(TYPE, UNUSEDEBS);
		costCount.setCount((long) 20);
		costCount.setLabel(cost);
		encryptedEBSCount.setCount((long) 23);
		encryptedEBSCount.setLabel(encryptedEBS);
		unencryptedEBSCount.setCount((long) 23);
		unencryptedEBSCount.setLabel(unencryptedEBS);
		encryptedS3Count.setCount((long) 23);
		encryptedS3Count.setLabel(encryptedS3);
		unencryptedS3Count.setCount((long) 23);
		unencryptedS3Count.setLabel(unencryptedS3);
		migrationEnabledCount.setCount((long) 23);
		migrationEnabledCount.setLabel(migrationEnabled);
		costOptimizedCount.setCount((long) 3);
		costOptimizedCount.setLabel(costOptimized);
		amiCount.setCount((long) 23);
		amiCount.setLabel(ami);
		elbCount.setCount((long) 23);
		elbCount.setLabel(elb);
		s3Count.setCount((long) 234);
		s3Count.setLabel(s3);
		ebsCount.setCount((long) 23);
		ebsCount.setLabel(ebs);
		unusedElbCount.setCount((long) 22);
		unusedElbCount.setLabel(unusedElb);
		unusedEniCount.setCount((long) 25);
		unusedEniCount.setLabel(unusedEni);
		unusedEbsCount.setCount((long) 26);
		unusedEbsCount.setLabel(unusedEbs);
		counts.add(costCount);
		counts.add(encryptedEBSCount);
		counts.add(unencryptedEBSCount);
		counts.add(encryptedS3Count);
		counts.add(unencryptedS3Count);
		counts.add(migrationEnabledCount);
		counts.add(costOptimizedCount);
		counts.add(amiCount);
		counts.add(elbCount);
		counts.add(s3Count);
		counts.add(ebsCount);
		counts.add(unusedElbCount);
		counts.add(unusedEniCount);
		counts.add(unusedEbsCount);
		prodcost.put(TYPE, PRODCOST);
		prodencryptedEBS.put(TYPE, PRODENCRYPTEDEBS);
		produnencryptedEBS.put(TYPE, PRODUNENCRYPTEDEBS);
		prodencryptedS3.put(TYPE, PRODENCRYPTEDS3);
		produnencryptedS3.put(TYPE, PRODUNENCRYPTEDS3);
		prodmigrationEnabled.put(TYPE, PRODMIGRATIONENABLED);
		prodcostOptimized.put(TYPE, PRODCOSTOPTIMIZED);
		prodami.put(TYPE, PRODAMI);
		prodelb.put(TYPE, PRODELB);
		prods3.put(TYPE, PRODS3);
		prodebs.put(TYPE, PRODEBS);
		produnusedElb.put(TYPE, PRODUNUSEDELB);
		produnusedEni.put(TYPE, PRODUNUSEDENI);
		produnusedEbs.put(TYPE, PRODUNUSEDEBS);
		prodCostCount.setCount((long) 10);
		prodCostCount.setLabel(prodcost);
		prodEncryptedEBSCount.setCount((long) 43);
		prodEncryptedEBSCount.setLabel(prodencryptedEBS);
		prodUnencryptedEBSCount.setCount((long) 43);
		prodUnencryptedEBSCount.setLabel(produnencryptedEBS);
		prodEncryptedS3Count.setCount((long) 43);
		prodEncryptedS3Count.setLabel(prodencryptedS3);
		prodUnencryptedS3Count.setCount((long) 43);
		prodUnencryptedS3Count.setLabel(produnencryptedS3);
		prodMigrationEnabledCount.setCount((long) 43);
		prodMigrationEnabledCount.setLabel(prodmigrationEnabled);
		prodCostOptimizedCount.setCount((long) 4);
		prodCostOptimizedCount.setLabel(prodcostOptimized);
		prodAmiCount.setCount((long) 43);
		prodAmiCount.setLabel(prodami);
		prodElbCount.setCount((long) 43);
		prodElbCount.setLabel(prodelb);
		prodS3Count.setCount((long) 43);
		prodS3Count.setLabel(prods3);
		prodEbsCount.setCount((long) 43);
		prodEbsCount.setLabel(prodebs);
		prodUnusedElbCount.setCount((long) 43);
		prodUnusedElbCount.setLabel(produnusedElb);
		prodUnusedEniCount.setCount((long) 43);
		prodUnusedEniCount.setLabel(produnusedEni);
		prodUnusedEbsCount.setCount((long) 43);
		prodUnusedEbsCount.setLabel(produnusedEbs);
		counts.add(prodCostCount);
		counts.add(prodEncryptedEBSCount);
		counts.add(prodUnencryptedEBSCount);
		counts.add(prodEncryptedS3Count);
		counts.add(prodUnencryptedS3Count);
		counts.add(prodMigrationEnabledCount);
		counts.add(prodCostOptimizedCount);
		counts.add(prodAmiCount);
		counts.add(prodElbCount);
		counts.add(prodS3Count);
		counts.add(prodEbsCount);
		counts.add(prodUnusedElbCount);
		counts.add(prodUnusedEniCount);
		counts.add(prodUnusedEbsCount);
		nonprodcost.put(TYPE, NONPRODCOST); // nonprod
		nonprodencryptedEBS.put(TYPE, NONPRODENCRYPTEDEBS);
		nonprodunencryptedEBS.put(TYPE, NONPRODUNENCRYPTEDEBS);
		nonprodencryptedS3.put(TYPE, NONPRODENCRYPTEDS3);
		nonprodunencryptedS3.put(TYPE, NONPRODUNENCRYPTEDS3);
		nonprodmigrationEnabled.put(TYPE, NONPRODMIGRATIONENABLED);
		nonprodcostOptimized.put(TYPE, NONPRODCOSTOPTIMIZED);
		nonprodami.put(TYPE, NONPRODAMI);
		nonprodelb.put(TYPE, NONPRODELB);
		nonprods3.put(TYPE, NONPRODS3);
		nonprodebs.put(TYPE, NONPRODEBS);
		nonprodunusedElb.put(TYPE, NONPRODUNUSEDELB);
		nonprodunusedEni.put(TYPE, NONPRODUNUSEDENI);
		nonprodunusedEbs.put(TYPE, NONPRODUNUSEDEBS);
		nonprodCostCount.setCount((long) 10);
		nonprodCostCount.setLabel(nonprodcost);
		nonprodEncryptedEBSCount.setCount((long) 43);
		nonprodEncryptedEBSCount.setLabel(nonprodencryptedEBS);
		nonprodUnencryptedEBSCount.setCount((long) 43);
		nonprodUnencryptedEBSCount.setLabel(nonprodunencryptedEBS);
		nonprodEncryptedS3Count.setCount((long) 43);
		nonprodEncryptedS3Count.setLabel(nonprodencryptedS3);
		nonprodUnencryptedS3Count.setCount((long) 43);
		nonprodUnencryptedS3Count.setLabel(nonprodunencryptedS3);
		nonprodMigrationEnabledCount.setCount((long) 43);
		nonprodMigrationEnabledCount.setLabel(nonprodmigrationEnabled);
		nonprodCostOptimizedCount.setCount((long) 4);
		nonprodCostOptimizedCount.setLabel(nonprodcostOptimized);
		nonprodAmiCount.setCount((long) 43);
		nonprodAmiCount.setLabel(nonprodami);
		nonprodElbCount.setCount((long) 43);
		nonprodElbCount.setLabel(nonprodelb);
		nonprodS3Count.setCount((long) 43);
		nonprodS3Count.setLabel(nonprods3);
		nonprodEbsCount.setCount((long) 43);
		nonprodEbsCount.setLabel(nonprodebs);
		nonprodUnusedElbCount.setCount((long) 43);
		nonprodUnusedElbCount.setLabel(nonprodunusedElb);
		nonprodUnusedEniCount.setCount((long) 43);
		nonprodUnusedEniCount.setLabel(nonprodunusedEni);
		nonprodUnusedEbsCount.setCount((long) 43);
		nonprodUnusedEbsCount.setLabel(nonprodunusedEbs);
		counts.add(nonprodCostCount);
		counts.add(nonprodEncryptedEBSCount);
		counts.add(nonprodUnencryptedEBSCount);
		counts.add(nonprodEncryptedS3Count);
		counts.add(nonprodUnencryptedS3Count);
		counts.add(nonprodMigrationEnabledCount);
		counts.add(nonprodCostOptimizedCount);
		counts.add(nonprodAmiCount);
		counts.add(nonprodElbCount);
		counts.add(nonprodS3Count);
		counts.add(nonprodEbsCount);
		counts.add(nonprodUnusedElbCount);
		counts.add(nonprodUnusedEniCount);
		counts.add(nonprodUnusedEbsCount);
		stagingcost.put(TYPE, STAGINGCOST); // staging
		stagingencryptedEBS.put(TYPE, STAGINGENCRYPTEDEBS);
		stagingunencryptedEBS.put(TYPE, STAGINGUNENCRYPTEDEBS);
		stagingencryptedS3.put(TYPE, STAGINGENCRYPTEDS3);
		stagingunencryptedS3.put(TYPE, STAGINGUNENCRYPTEDS3);
		stagingmigrationEnabled.put(TYPE, STAGINGMIGRATIONENABLED);
		stagingcostOptimized.put(TYPE, STAGINGCOSTOPTIMIZED);
		stagingami.put(TYPE, STAGINGAMI);
		stagingelb.put(TYPE, STAGINGELB);
		stagings3.put(TYPE, STAGINGS3);
		stagingebs.put(TYPE, STAGINGEBS);
		stagingunusedElb.put(TYPE, STAGINGUNUSEDELB);
		stagingunusedEni.put(TYPE, STAGINGUNUSEDENI);
		stagingunusedEbs.put(TYPE, STAGINGUNUSEDEBS);
		stagingCostCount.setCount((long) 10);
		stagingCostCount.setLabel(stagingcost);
		stagingEncryptedEBSCount.setCount((long) 43);
		stagingEncryptedEBSCount.setLabel(stagingencryptedEBS);
		stagingUnencryptedEBSCount.setCount(Math.abs((long) 43));
		stagingUnencryptedEBSCount.setLabel(stagingunencryptedEBS);
		stagingEncryptedS3Count.setCount((long) 43);
		stagingEncryptedS3Count.setLabel(stagingencryptedS3);
		stagingUnencryptedS3Count.setCount((long) 43);
		stagingUnencryptedS3Count.setLabel(stagingunencryptedS3);
		stagingMigrationEnabledCount.setCount((long) 43);
		stagingMigrationEnabledCount.setLabel(stagingmigrationEnabled);
		stagingCostOptimizedCount.setCount((long) 4);
		stagingCostOptimizedCount.setLabel(stagingcostOptimized);
		stagingAmiCount.setCount((long) 43);
		stagingAmiCount.setLabel(stagingami);
		stagingElbCount.setCount((long) 43);
		stagingElbCount.setLabel(stagingelb);
		stagingS3Count.setCount((long) 43);
		stagingS3Count.setLabel(stagings3);
		stagingEbsCount.setCount((long) 43);
		stagingEbsCount.setLabel(stagingebs);
		stagingUnusedElbCount.setCount((long) 43);
		stagingUnusedElbCount.setLabel(stagingunusedElb);
		stagingUnusedEniCount.setCount((long) 43);
		stagingUnusedEniCount.setLabel(stagingunusedEni);
		stagingUnusedEbsCount.setCount((long) 43);
		stagingUnusedEbsCount.setLabel(stagingunusedEbs);
		counts.add(stagingCostCount);
		counts.add(stagingEncryptedEBSCount);
		counts.add(stagingUnencryptedEBSCount);
		counts.add(stagingEncryptedS3Count);
		counts.add(stagingUnencryptedS3Count);
		counts.add(stagingMigrationEnabledCount);
		counts.add(stagingCostOptimizedCount);
		counts.add(stagingAmiCount);
		counts.add(stagingElbCount);
		counts.add(stagingS3Count);
		counts.add(stagingEbsCount);
		counts.add(stagingUnusedElbCount);
		counts.add(stagingUnusedEniCount);
		counts.add(stagingUnusedEbsCount);
		return counts;
	}

	/**
	 * @return
	 */
	public List<MetricCount> getCounts() {
		List<MetricCount> counts = new ArrayList<MetricCount>();
		MetricCount costCount = new MetricCount();
		MetricCount encryptedEBSCount = new MetricCount();
		MetricCount unencryptedEBSCount = new MetricCount();
		MetricCount encryptedS3Count = new MetricCount();
		MetricCount unencryptedS3Count = new MetricCount();
		MetricCount migrationEnabledCount = new MetricCount();
		MetricCount costOptimizedCount = new MetricCount();
		MetricCount amiCount = new MetricCount();
		MetricCount elbCount = new MetricCount();
		MetricCount s3Count = new MetricCount();
		MetricCount ebsCount = new MetricCount();
		MetricCount unusedElbCount = new MetricCount();
		MetricCount unusedEniCount = new MetricCount();
		MetricCount unusedEbsCount = new MetricCount();
		// Prod
		MetricCount prodCostCount = new MetricCount();
		MetricCount prodEncryptedEBSCount = new MetricCount();
		MetricCount prodUnencryptedEBSCount = new MetricCount();
		MetricCount prodEncryptedS3Count = new MetricCount();
		MetricCount prodUnencryptedS3Count = new MetricCount();
		MetricCount prodMigrationEnabledCount = new MetricCount();
		MetricCount prodCostOptimizedCount = new MetricCount();
		MetricCount prodAmiCount = new MetricCount();
		MetricCount prodElbCount = new MetricCount();
		MetricCount prodS3Count = new MetricCount();
		MetricCount prodEbsCount = new MetricCount();
		MetricCount prodUnusedElbCount = new MetricCount();
		MetricCount prodUnusedEniCount = new MetricCount();
		MetricCount prodUnusedEbsCount = new MetricCount();
		// nonprod
		MetricCount nonprodCostCount = new MetricCount();
		MetricCount nonprodEncryptedEBSCount = new MetricCount();
		MetricCount nonprodUnencryptedEBSCount = new MetricCount();
		MetricCount nonprodEncryptedS3Count = new MetricCount();
		MetricCount nonprodUnencryptedS3Count = new MetricCount();
		MetricCount nonprodMigrationEnabledCount = new MetricCount();
		MetricCount nonprodCostOptimizedCount = new MetricCount();
		MetricCount nonprodAmiCount = new MetricCount();
		MetricCount nonprodElbCount = new MetricCount();
		MetricCount nonprodS3Count = new MetricCount();
		MetricCount nonprodEbsCount = new MetricCount();
		MetricCount nonprodUnusedElbCount = new MetricCount();
		MetricCount nonprodUnusedEniCount = new MetricCount();
		MetricCount nonprodUnusedEbsCount = new MetricCount();
		// staging
		MetricCount stagingCostCount = new MetricCount();
		MetricCount stagingEncryptedEBSCount = new MetricCount();
		MetricCount stagingUnencryptedEBSCount = new MetricCount();
		MetricCount stagingEncryptedS3Count = new MetricCount();
		MetricCount stagingUnencryptedS3Count = new MetricCount();
		MetricCount stagingMigrationEnabledCount = new MetricCount();
		MetricCount stagingCostOptimizedCount = new MetricCount();
		MetricCount stagingAmiCount = new MetricCount();
		MetricCount stagingElbCount = new MetricCount();
		MetricCount stagingS3Count = new MetricCount();
		MetricCount stagingEbsCount = new MetricCount();
		MetricCount stagingUnusedElbCount = new MetricCount();
		MetricCount stagingUnusedEniCount = new MetricCount();
		MetricCount stagingUnusedEbsCount = new MetricCount();
		Map<String, String> cost = new HashMap<>();
		Map<String, String> encryptedEBS = new HashMap<>();
		Map<String, String> unencryptedEBS = new HashMap<>();
		Map<String, String> encryptedS3 = new HashMap<>();
		Map<String, String> unencryptedS3 = new HashMap<>();
		Map<String, String> migrationEnabled = new HashMap<>();
		Map<String, String> costOptimized = new HashMap<>();
		Map<String, String> ami = new HashMap<>();
		Map<String, String> elb = new HashMap<>();
		Map<String, String> s3 = new HashMap<>();
		Map<String, String> ebs = new HashMap<>();
		Map<String, String> unusedElb = new HashMap<>();
		Map<String, String> unusedEni = new HashMap<>();
		Map<String, String> unusedEbs = new HashMap<>();
		cost.put(TYPE, COST);
		encryptedEBS.put(TYPE, ENCRYPTEDEBS);
		unencryptedEBS.put(TYPE, UNENCRYPTEDEBS);
		encryptedS3.put(TYPE, ENCRYPTEDS3);
		unencryptedS3.put(TYPE, UNENCRYPTEDS3);
		migrationEnabled.put(TYPE, MIGRATIONENABLED);
		costOptimized.put(TYPE, COSTOPTIMIZED);
		ami.put(TYPE, AMI);
		elb.put(TYPE, ELB);
		s3.put(TYPE, S3);
		ebs.put(TYPE, EBS);
		unusedElb.put(TYPE, UNUSEDELB);
		unusedEni.put(TYPE, UNUSEDENI);
		unusedEbs.put(TYPE, UNUSEDEBS);
		costCount.setValue((long) 20);
		costCount.setLabel(cost);
		encryptedEBSCount.setValue((long) 23);
		encryptedEBSCount.setLabel(encryptedEBS);
		unencryptedEBSCount.setValue((long) 23);
		unencryptedEBSCount.setLabel(unencryptedEBS);
		encryptedS3Count.setValue((long) 23);
		encryptedS3Count.setLabel(encryptedS3);
		unencryptedS3Count.setValue((long) 23);
		unencryptedS3Count.setLabel(unencryptedS3);
		migrationEnabledCount.setValue((long) 23);
		migrationEnabledCount.setLabel(migrationEnabled);
		costOptimizedCount.setValue((long) 3);
		costOptimizedCount.setLabel(costOptimized);
		amiCount.setValue((long) 23);
		amiCount.setLabel(ami);
		elbCount.setValue((long) 23);
		elbCount.setLabel(elb);
		s3Count.setValue((long) 234);
		s3Count.setLabel(s3);
		ebsCount.setValue((long) 23);
		ebsCount.setLabel(ebs);
		unusedElbCount.setValue((long) 22);
		unusedElbCount.setLabel(unusedElb);
		unusedEniCount.setValue((long) 25);
		unusedEniCount.setLabel(unusedEni);
		unusedEbsCount.setValue((long) 26);
		unusedEbsCount.setLabel(unusedEbs);
		counts.add(costCount);
		counts.add(encryptedEBSCount);
		counts.add(unencryptedEBSCount);
		counts.add(encryptedS3Count);
		counts.add(unencryptedS3Count);
		counts.add(migrationEnabledCount);
		counts.add(costOptimizedCount);
		counts.add(amiCount);
		counts.add(elbCount);
		counts.add(s3Count);
		counts.add(ebsCount);
		counts.add(unusedElbCount);
		counts.add(unusedEniCount);
		counts.add(unusedEbsCount);
		// prod
		Map<String, String> prodcost = new HashMap<>();
		Map<String, String> prodencryptedEBS = new HashMap<>();
		Map<String, String> produnencryptedEBS = new HashMap<>();
		Map<String, String> prodencryptedS3 = new HashMap<>();
		Map<String, String> produnencryptedS3 = new HashMap<>();
		Map<String, String> prodmigrationEnabled = new HashMap<>();
		Map<String, String> prodcostOptimized = new HashMap<>();
		Map<String, String> prodami = new HashMap<>();
		Map<String, String> prodelb = new HashMap<>();
		Map<String, String> prods3 = new HashMap<>();
		Map<String, String> prodebs = new HashMap<>();
		Map<String, String> produnusedElb = new HashMap<>();
		Map<String, String> produnusedEni = new HashMap<>();
		Map<String, String> produnusedEbs = new HashMap<>();
		// nonprod
		Map<String, String> nonprodcost = new HashMap<>();
		Map<String, String> nonprodencryptedEBS = new HashMap<>();
		Map<String, String> nonprodunencryptedEBS = new HashMap<>();
		Map<String, String> nonprodencryptedS3 = new HashMap<>();
		Map<String, String> nonprodunencryptedS3 = new HashMap<>();
		Map<String, String> nonprodmigrationEnabled = new HashMap<>();
		Map<String, String> nonprodcostOptimized = new HashMap<>();
		Map<String, String> nonprodami = new HashMap<>();
		Map<String, String> nonprodelb = new HashMap<>();
		Map<String, String> nonprods3 = new HashMap<>();
		Map<String, String> nonprodebs = new HashMap<>();
		Map<String, String> nonprodunusedElb = new HashMap<>();
		Map<String, String> nonprodunusedEni = new HashMap<>();
		Map<String, String> nonprodunusedEbs = new HashMap<>();
		// staging
		Map<String, String> stagingcost = new HashMap<>();
		Map<String, String> stagingencryptedEBS = new HashMap<>();
		Map<String, String> stagingunencryptedEBS = new HashMap<>();
		Map<String, String> stagingencryptedS3 = new HashMap<>();
		Map<String, String> stagingunencryptedS3 = new HashMap<>();
		Map<String, String> stagingmigrationEnabled = new HashMap<>();
		Map<String, String> stagingcostOptimized = new HashMap<>();
		Map<String, String> stagingami = new HashMap<>();
		Map<String, String> stagingelb = new HashMap<>();
		Map<String, String> stagings3 = new HashMap<>();
		Map<String, String> stagingebs = new HashMap<>();
		Map<String, String> stagingunusedElb = new HashMap<>();
		Map<String, String> stagingunusedEni = new HashMap<>();
		Map<String, String> stagingunusedEbs = new HashMap<>();

		prodcost.put(TYPE, PRODCOST);
		prodencryptedEBS.put(TYPE, PRODENCRYPTEDEBS);
		produnencryptedEBS.put(TYPE, PRODUNENCRYPTEDEBS);
		prodencryptedS3.put(TYPE, PRODENCRYPTEDS3);
		produnencryptedS3.put(TYPE, PRODUNENCRYPTEDS3);
		prodmigrationEnabled.put(TYPE, PRODMIGRATIONENABLED);
		prodcostOptimized.put(TYPE, PRODCOSTOPTIMIZED);
		prodami.put(TYPE, PRODAMI);
		prodelb.put(TYPE, PRODELB);
		prods3.put(TYPE, PRODS3);
		prodebs.put(TYPE, PRODEBS);
		produnusedElb.put(TYPE, PRODUNUSEDELB);
		produnusedEni.put(TYPE, PRODUNUSEDENI);
		produnusedEbs.put(TYPE, PRODUNUSEDEBS);
		prodCostCount.setValue((long) 10);
		prodCostCount.setLabel(prodcost);
		prodEncryptedEBSCount.setValue((long) 43);
		prodEncryptedEBSCount.setLabel(prodencryptedEBS);
		prodUnencryptedEBSCount.setValue((long) 43);
		prodUnencryptedEBSCount.setLabel(produnencryptedEBS);
		prodEncryptedS3Count.setValue((long) 43);
		prodEncryptedS3Count.setLabel(prodencryptedS3);
		prodUnencryptedS3Count.setValue((long) 43);
		prodUnencryptedS3Count.setLabel(produnencryptedS3);
		prodMigrationEnabledCount.setValue((long) 43);
		prodMigrationEnabledCount.setLabel(prodmigrationEnabled);
		prodCostOptimizedCount.setValue((long) 4);
		prodCostOptimizedCount.setLabel(prodcostOptimized);
		prodAmiCount.setValue((long) 43);
		prodAmiCount.setLabel(prodami);
		prodElbCount.setValue((long) 43);
		prodElbCount.setLabel(prodelb);
		prodS3Count.setValue((long) 43);
		prodS3Count.setLabel(prods3);
		prodEbsCount.setValue((long) 43);
		prodEbsCount.setLabel(prodebs);
		prodUnusedElbCount.setValue((long) 43);
		prodUnusedElbCount.setLabel(produnusedElb);
		prodUnusedEniCount.setValue((long) 43);
		prodUnusedEniCount.setLabel(produnusedEni);
		prodUnusedEbsCount.setValue((long) 43);
		prodUnusedEbsCount.setLabel(produnusedEbs);
		counts.add(prodCostCount);
		counts.add(prodEncryptedEBSCount);
		counts.add(prodUnencryptedEBSCount);
		counts.add(prodEncryptedS3Count);
		counts.add(prodUnencryptedS3Count);
		counts.add(prodMigrationEnabledCount);
		counts.add(prodCostOptimizedCount);
		counts.add(prodAmiCount);
		counts.add(prodElbCount);
		counts.add(prodS3Count);
		counts.add(prodEbsCount);
		counts.add(prodUnusedElbCount);
		counts.add(prodUnusedEniCount);
		counts.add(prodUnusedEbsCount);
		nonprodcost.put(TYPE, NONPRODCOST); // nonprod
		nonprodencryptedEBS.put(TYPE, NONPRODENCRYPTEDEBS);
		nonprodunencryptedEBS.put(TYPE, NONPRODUNENCRYPTEDEBS);
		nonprodencryptedS3.put(TYPE, NONPRODENCRYPTEDS3);
		nonprodunencryptedS3.put(TYPE, NONPRODUNENCRYPTEDS3);
		nonprodmigrationEnabled.put(TYPE, NONPRODMIGRATIONENABLED);
		nonprodcostOptimized.put(TYPE, NONPRODCOSTOPTIMIZED);
		nonprodami.put(TYPE, NONPRODAMI);
		nonprodelb.put(TYPE, NONPRODELB);
		nonprods3.put(TYPE, NONPRODS3);
		nonprodebs.put(TYPE, NONPRODEBS);
		nonprodunusedElb.put(TYPE, NONPRODUNUSEDELB);
		nonprodunusedEni.put(TYPE, NONPRODUNUSEDENI);
		nonprodunusedEbs.put(TYPE, NONPRODUNUSEDEBS);
		nonprodCostCount.setValue((long) 10);
		nonprodCostCount.setLabel(nonprodcost);
		nonprodEncryptedEBSCount.setValue((long) 43);
		nonprodEncryptedEBSCount.setLabel(nonprodencryptedEBS);
		nonprodUnencryptedEBSCount.setValue((long) 43);
		nonprodUnencryptedEBSCount.setLabel(nonprodunencryptedEBS);
		nonprodEncryptedS3Count.setValue((long) 43);
		nonprodEncryptedS3Count.setLabel(nonprodencryptedS3);
		nonprodUnencryptedS3Count.setValue((long) 43);
		nonprodUnencryptedS3Count.setLabel(nonprodunencryptedS3);
		nonprodMigrationEnabledCount.setValue((long) 43);
		nonprodMigrationEnabledCount.setLabel(nonprodmigrationEnabled);
		nonprodCostOptimizedCount.setValue((long) 4);
		nonprodCostOptimizedCount.setLabel(nonprodcostOptimized);
		nonprodAmiCount.setValue((long) 43);
		nonprodAmiCount.setLabel(nonprodami);
		nonprodElbCount.setValue((long) 43);
		nonprodElbCount.setLabel(nonprodelb);
		nonprodS3Count.setValue((long) 43);
		nonprodS3Count.setLabel(nonprods3);
		nonprodEbsCount.setValue((long) 43);
		nonprodEbsCount.setLabel(nonprodebs);
		nonprodUnusedElbCount.setValue((long) 43);
		nonprodUnusedElbCount.setLabel(nonprodunusedElb);
		nonprodUnusedEniCount.setValue((long) 43);
		nonprodUnusedEniCount.setLabel(nonprodunusedEni);
		nonprodUnusedEbsCount.setValue((long) 43);
		nonprodUnusedEbsCount.setLabel(nonprodunusedEbs);
		counts.add(nonprodCostCount);
		counts.add(nonprodEncryptedEBSCount);
		counts.add(nonprodUnencryptedEBSCount);
		counts.add(nonprodEncryptedS3Count);
		counts.add(nonprodUnencryptedS3Count);
		counts.add(nonprodMigrationEnabledCount);
		counts.add(nonprodCostOptimizedCount);
		counts.add(nonprodAmiCount);
		counts.add(nonprodElbCount);
		counts.add(nonprodS3Count);
		counts.add(nonprodEbsCount);
		counts.add(nonprodUnusedElbCount);
		counts.add(nonprodUnusedEniCount);
		counts.add(nonprodUnusedEbsCount);
		stagingcost.put(TYPE, STAGINGCOST); // staging
		stagingencryptedEBS.put(TYPE, STAGINGENCRYPTEDEBS);
		stagingunencryptedEBS.put(TYPE, STAGINGUNENCRYPTEDEBS);
		stagingencryptedS3.put(TYPE, STAGINGENCRYPTEDS3);
		stagingunencryptedS3.put(TYPE, STAGINGUNENCRYPTEDS3);
		stagingmigrationEnabled.put(TYPE, STAGINGMIGRATIONENABLED);
		stagingcostOptimized.put(TYPE, STAGINGCOSTOPTIMIZED);
		stagingami.put(TYPE, STAGINGAMI);
		stagingelb.put(TYPE, STAGINGELB);
		stagings3.put(TYPE, STAGINGS3);
		stagingebs.put(TYPE, STAGINGEBS);
		stagingunusedElb.put(TYPE, STAGINGUNUSEDELB);
		stagingunusedEni.put(TYPE, STAGINGUNUSEDENI);
		stagingunusedEbs.put(TYPE, STAGINGUNUSEDEBS);
		stagingCostCount.setValue((long) 10);
		stagingCostCount.setLabel(stagingcost);
		stagingEncryptedEBSCount.setValue((long) 43);
		stagingEncryptedEBSCount.setLabel(stagingencryptedEBS);
		stagingUnencryptedEBSCount.setValue(Math.abs((long) 43));
		stagingUnencryptedEBSCount.setLabel(stagingunencryptedEBS);
		stagingEncryptedS3Count.setValue((long) 43);
		stagingEncryptedS3Count.setLabel(stagingencryptedS3);
		stagingUnencryptedS3Count.setValue((long) 43);
		stagingUnencryptedS3Count.setLabel(stagingunencryptedS3);
		stagingMigrationEnabledCount.setValue((long) 43);
		stagingMigrationEnabledCount.setLabel(stagingmigrationEnabled);
		stagingCostOptimizedCount.setValue((long) 4);
		stagingCostOptimizedCount.setLabel(stagingcostOptimized);
		stagingAmiCount.setValue((long) 43);
		stagingAmiCount.setLabel(stagingami);
		stagingElbCount.setValue((long) 43);
		stagingElbCount.setLabel(stagingelb);
		stagingS3Count.setValue((long) 43);
		stagingS3Count.setLabel(stagings3);
		stagingEbsCount.setValue((long) 43);
		stagingEbsCount.setLabel(stagingebs);
		stagingUnusedElbCount.setValue((long) 43);
		stagingUnusedElbCount.setLabel(stagingunusedElb);
		stagingUnusedEniCount.setValue((long) 43);
		stagingUnusedEniCount.setLabel(stagingunusedEni);
		stagingUnusedEbsCount.setValue((long) 43);
		stagingUnusedEbsCount.setLabel(stagingunusedEbs);
		counts.add(stagingCostCount);
		counts.add(stagingEncryptedEBSCount);
		counts.add(stagingUnencryptedEBSCount);
		counts.add(stagingEncryptedS3Count);
		counts.add(stagingUnencryptedS3Count);
		counts.add(stagingMigrationEnabledCount);
		counts.add(stagingCostOptimizedCount);
		counts.add(stagingAmiCount);
		counts.add(stagingElbCount);
		counts.add(stagingS3Count);
		counts.add(stagingEbsCount);
		counts.add(stagingUnusedElbCount);
		counts.add(stagingUnusedEniCount);
		counts.add(stagingUnusedEbsCount);
		return counts;
	}
}
