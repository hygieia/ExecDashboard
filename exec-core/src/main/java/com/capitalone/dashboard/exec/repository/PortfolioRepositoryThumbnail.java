package com.capitalone.dashboard.exec.repository;

import com.capitalone.dashboard.exec.model.PortfolioThumbnail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PortfolioRepositoryThumbnail extends CrudRepository<PortfolioThumbnail, ObjectId>{

    @Query(value="{ 'name' : 1, 'thumbnail': 1}")
    List<PortfolioThumbnail> findAllByNameAndThumbnail();

}
