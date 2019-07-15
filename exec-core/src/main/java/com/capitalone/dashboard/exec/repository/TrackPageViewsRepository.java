package com.capitalone.dashboard.exec.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.capitalone.dashboard.exec.model.TrackPageViews;

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
