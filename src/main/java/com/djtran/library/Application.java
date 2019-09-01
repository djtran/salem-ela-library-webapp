package com.djtran.library;

import com.djtran.library.dynamodb.DynamoDbTableInitializer;
import com.djtran.library.guice.LibraryModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws InterruptedException, IOException {

        //Activate DI
        Injector injector = Guice.createInjector(new LibraryModule());

        //Make sure the db table exists
        DynamoDbTableInitializer initializer = injector.getInstance(DynamoDbTableInitializer.class);
        log.info("TableInitialization status: {} ", initializer.init());

        //Start the API.
        injector.getInstance(HttpApi.class).initialize();
    }
}
