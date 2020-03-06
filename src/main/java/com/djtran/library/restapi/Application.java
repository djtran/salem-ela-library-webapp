package com.djtran.library.restapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        SpringApplication.run(Application.class, args);

        //Activate DI
//        Injector injector = Guice.createInjector(new LibraryModule());

        //Make sure the db table exists
//        DynamoDbTableInitializer initializer = injector.getInstance(DynamoDbTableInitializer.class);
//        log.info("TableInitialization status: {} ", initializer.init());

        //Start the API.
//        injector.getInstance(HttpApi.class).initialize();
    }
}
