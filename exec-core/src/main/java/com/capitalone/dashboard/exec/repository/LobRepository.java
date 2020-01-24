package com.capitalone.dashboard.exec.repository;

import com.capitalone.dashboard.exec.model.Lob;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LobRepository extends CrudRepository<Lob, ObjectId>{
    @Query(fields = "{'name' : 1, 'owners' : 1}")
    List<Lob> findAllByOwnersNotNull();

    @Query(fields = "{'name' : 1, 'products' : 1, 'owners' : 1}}")
    Lob findByName(String name);

}
