package com.capitalone.dashboard.executive.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;

/**
 * Custom object mapper that knows how to serialize Mongo ObjectIds.
 */
public class CustomObjectMapper extends ObjectMapper {
    
	public CustomObjectMapper() {
        SimpleModule module = new SimpleModule("ObjectIdModule");
        module.addSerializer(ObjectId.class, new ObjectIdSerializer());
        this.registerModule(module);
    }
}

