package com.capitalone.dashboard.exec.repository.vz;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.vz.TrackPageViews;

/**
 * @author V143611
 *
 */
public interface TrackPageViewsRepository extends PagingAndSortingRepository<TrackPageViews, ObjectId> {

	/**
	 * findByView
	 * 
	 * @param view
	 * @return TrackPageViews
	 */
	TrackPageViews findByView(String view);

}
