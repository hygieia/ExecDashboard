package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.exec.model.vz.DateWiseMetricsSeries;

/**
 * @author V143611
 *
 */
public interface DateWiseMetricsSeriesRepository extends CrudRepository<DateWiseMetricsSeries, ObjectId> {

	/**
	 * findByAppIdAndModuleNameAndMetricsNameOrderByTimeStampDesc()
	 * 
	 * @param appId
	 * @param moduleName
	 * @param metricsName
	 * @return DateWiseMetricsSeries
	 */
	DateWiseMetricsSeries findByAppIdAndModuleNameAndMetricsNameOrderByTimeStampDesc(String appId, String moduleName,
			String metricsName);

	/**
	 * findByAppIdAndModuleNameAndMetricsNameAndTimeStamp()
	 * 
	 * @param appId
	 * @param moduleName
	 * @param metricsName
	 * @param dateValue
	 * @param timeStamp
	 * @return DateWiseMetricsSeries
	 */
	@Query(value = " {'appId' : ?0, 'moduleName' : ?1, 'metricsName' : ?2, 'timeStamp' : ?3}")
	DateWiseMetricsSeries findByAppIdAndModuleNameAndMetricsNameAndTimeStamp(String appId, String moduleName,
			String metricsName, Long timeStamp);

	/**
	 * getThreeMonthsList()
	 * 
	 * @param appId
	 * @param moduleName
	 * @param metricsName
	 * @param timeStamp
	 * @return List<DateWiseMetricsSeries>
	 */
	@Query(value = " {'appId' : ?0, 'moduleName' : ?1, 'metricsName' : ?2, 'timeStamp' : {$gte: ?3}}")
	List<DateWiseMetricsSeries> getThreeMonthsList(String appId, String moduleName, String metricsName, Long timeStamp);

	/**
	 * getThreeMonthsListWithAppId()
	 * 
	 * @param appId
	 * @param metricsName
	 * @param timeStamp
	 * @return List<DateWiseMetricsSeries>
	 */
	@Query(value = " {'appId' : ?0, 'metricsName' : ?1, 'timeStamp' : {$gte: ?2}}")
	List<DateWiseMetricsSeries> getThreeMonthsListWithAppId(String appId, String metricsName, Long timeStamp);

	/**
	 * findByAppIdAndMetricsNameOrderByTimeStampDesc()
	 * 
	 * @param appId
	 * @param metricsName
	 * @return DateWiseMetricsSeries
	 */
	DateWiseMetricsSeries findByAppIdAndMetricsNameOrderByTimeStampDesc(String appId, String metricsName);

	/**
	 * findByAppIdAndModuleNameAndMetricsName()
	 * 
	 * @param appId
	 * @param moduleName
	 * @param metricsName
	 * @return DateWiseMetricsSeries
	 */
	DateWiseMetricsSeries findByAppIdAndModuleNameAndMetricsName(String appId, String moduleName, String metricsName);

	/**
	 * getThreeMonthsList()
	 * 
	 * @param appId
	 * @param metricsName
	 * @param timeStamp
	 * @return List<DateWiseMetricsSeries>
	 */
	@Query(value = " {'appId' : ?0, 'metricsName' : ?1, 'timeStamp' : {$gte: ?2}}")
	List<DateWiseMetricsSeries> getThreeMonthsListWithoutProject(String appId, String prodincidents, Long timeStamp);

	/**
	 * findByAppIdAndMetricsNameGreaterThanTimestamp
	 * 
	 * @param appId
	 * @param metric
	 * @param timeStamp
	 * @return List<DateWiseMetricsSeries>
	 */
	@Query(value = " {'appId' : ?0, 'metricsName' : ?1, 'timeStamp' : {$gte: ?2}}")
	List<DateWiseMetricsSeries> findByAppIdAndMetricsNameGreaterThanTimestamp(String appId, String metric,
			long timeStamp);

	/**
	 * @param appId
	 * @param moduleName
	 * @param metricsName
	 * @param dateValue
	 * @return
	 */
	@Query(value = " {'appId' : ?0, 'moduleName' : ?1, 'metricsName' : ?2,  'dateValue' : ?3}")
	DateWiseMetricsSeries getMetricsByDateValue(String appId, String moduleName, String metricsName, String dateValue);

	@Query(value = " {'appId' : ?0, 'metricsName' : ?1, 'timeStamp' : {$gte: ?2}}")
	List<DateWiseMetricsSeries> findByAppIdAndMetricsNameAndTimeStamp(String appId, String securityviolations,
			long timeStamp, Sort sort);

	DateWiseMetricsSeries findByDateValue(String dateValue);

	@Query(value = " {'appId' : ?0, 'metricsName' : ?1,  'dateValue' : ?2}")
	DateWiseMetricsSeries findByAppIdAndMetricsNameAndDateValue(String appId, String metricsName, String dateValue);
	
	/**
	 * 
	 * @param appId
	 * @param moduleName
	 * @param metricsName
	 * @param dateValue
	 * @return
	 */
	@Query(value = " {'appId' : ?0, 'moduleName' : ?1, 'metricsName' : ?2, 'dateValue' : ?3}")
	DateWiseMetricsSeries findByAppIdAndModuleNameAndMetricsNameAndDateValue(String appId, String moduleName,
			String metricsName, String dateValue);


}
