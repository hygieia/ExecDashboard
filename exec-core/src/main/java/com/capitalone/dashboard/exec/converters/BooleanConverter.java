package com.capitalone.dashboard.exec.converters;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class BooleanConverter implements Converter<String, Boolean> {
    @Override
    public Boolean convert(String source) {
        if(source==null){
            return false;
        }
        return Boolean.valueOf(source);
    }
}