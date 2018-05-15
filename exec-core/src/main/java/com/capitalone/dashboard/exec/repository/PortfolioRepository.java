package com.capitalone.dashboard.exec.repository;

import com.capitalone.dashboard.exec.model.Portfolio;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PortfolioRepository extends CrudRepository<Portfolio, ObjectId>{
    @Query(fields = "{'name' : 1, 'lob' : 1, 'owners' : 1}")
    List<Portfolio> findAllByOwnersNotNull();

    @Query(fields = "{'name' : 1, 'lob' : 1, 'products' : 1, 'owners' : 1}}")
    Portfolio findByNameAndLob(String name, String lob);


}
