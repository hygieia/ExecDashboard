package com.capitalone.dashboard.exec.repository.vz;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.JenkinsUnlimitedData;

public interface JenkinsUnlimitedDataRepository extends PagingAndSortingRepository<JenkinsUnlimitedData, ObjectId> {

	JenkinsUnlimitedData findByAppIdAndBuildJobAndPeriod(String appId, String buildJob, int period);

	List<JenkinsUnlimitedData> findByBuildJob(String buildJob);

}
