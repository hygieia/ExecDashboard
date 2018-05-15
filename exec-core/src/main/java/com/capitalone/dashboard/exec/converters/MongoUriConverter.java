package com.capitalone.dashboard.exec.converters;

import com.capitalone.dashboard.exec.model.MongoUri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@ConfigurationPropertiesBinding
public class MongoUriConverter implements Converter<String, List<MongoUri>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoUriConverter.class);
    private static final String INTER_URI_DELIMITER = "|";
    private static final String INTRA_URI_DELIMITER = ";";

    @Override
    public List<MongoUri> convert(String source) {
        if((source == null) || source.isEmpty()){
            LOGGER.info("No URI information found, returning ...");
            return null;
        }

        String[] readUris = source.split(INTER_URI_DELIMITER);
        if ((readUris == null) || (readUris.length == 0)) {
            LOGGER.info("URI information is not set appropriately, returning ...");
            return null;
        }

        List<MongoUri> result = new ArrayList<>();
        List<String> readUriList = Arrays.asList(readUris);
        Optional.ofNullable(readUriList)
                .orElse(new ArrayList<>()).stream()
                .forEach(r -> {
                    String[] uriParts = r.split(INTRA_URI_DELIMITER);
                    if ((uriParts != null) && (uriParts.length >= 2)) {
                        MongoUri mongoUri = new MongoUri();
                        mongoUri.setUri(uriParts[0]);
                        mongoUri.setDatabase(uriParts[1]);
                        if (uriParts.length > 2) {
                            mongoUri.setCollection(uriParts[2]);
                        }
                        result.add(mongoUri);
                    }
                });

        return result;
    }
}