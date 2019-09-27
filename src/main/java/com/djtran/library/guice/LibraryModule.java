package com.djtran.library.guice;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.djtran.library.HttpApi;
import com.djtran.library.qr.BookQrTranslater;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.djtran.library.dynamodb.DynamoDbTableInitializer;
import com.djtran.library.LibraryManager;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.util.Properties;

public class LibraryModule extends AbstractModule {
    private static final Logger log = LoggerFactory.getLogger(LibraryModule.class);
    @Override
    protected void configure() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("config.properties"));
            Names.bindProperties(binder(), properties);
        } catch (Exception e) {
            log.error("Couldn't slurp properties: {}", e);
        }
        //bind simple things here.

    }

    @Provides
    @Singleton
    DynamoDbTableInitializer getLibraryInitializer(AmazonDynamoDBClient client,
                                                   @Named("dynamodb.tablename") String tableName) {
        return new DynamoDbTableInitializer(client, tableName);
    }

    @Provides
    LibraryManager getLibraryManager(DynamoDBMapper mapper) {
        return new LibraryManager(mapper);
    }

    @Provides
    @Singleton
    HttpApi getSparkApi(LibraryManager manager, BookQrTranslater translater,
                        @Named("keystore.path") String keystorePath,
                        @Named("keystore.password") String keystorePassword) {
        return new HttpApi(manager, translater);
    }

    @Provides
    @Singleton
    AmazonDynamoDB getDynamoClient() {
        return AmazonDynamoDBClientBuilder.defaultClient();
    }

    @Provides
    @Singleton
    DynamoDBMapper getMapper(AmazonDynamoDB client) {
        return new DynamoDBMapper(client);
    }

    @Provides
    BookQrTranslater getBookQrTranslater(@Named("qr.side.length") int sideLength) {
        return new BookQrTranslater(sideLength,
                new QRCodeWriter(),
                new QRCodeReader());
    }
}
