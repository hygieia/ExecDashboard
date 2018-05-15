package com.capitalone.dashboard.exec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.convert.ConversionService;
//import org.springframework.context.support.ConversionServiceFactoryBean;
//import org.springframework.core.convert.converter.Converter;
//import java.util.*;
// import com.capitalone.dashboard.exec.collector.MongoUriConverter;

@SpringBootApplication
@EnableAutoConfiguration
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}