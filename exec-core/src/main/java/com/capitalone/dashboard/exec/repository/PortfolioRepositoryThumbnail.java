package com.capitalone.dashboard.exec.repository;

import com.capitalone.dashboard.exec.model.PortfolioThumbnail;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PortfolioRepositoryThumbnail extends CrudRepository<PortfolioThumbnail, ObjectId>{

    List<PortfolioThumbnail> findAll();

}
