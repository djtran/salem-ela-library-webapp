package guice;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import dynamodb.LibraryInitializer;
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
            System.setProperty("aws.accessKeyId", properties.getProperty("aws.accessKeyId"));
            System.setProperty("aws.secretKey", properties.getProperty("aws.secretKey"));
        } catch (Exception e) {
            log.error("Couldn't slurp properties: {}", e);
        }
        //bind simple things here.

    }

    @Provides
    @Singleton
    LibraryInitializer getLibraryInitializer(AmazonDynamoDBClient client,
                                             @Named("dynamodb.tablename") String tableName) {
        return new LibraryInitializer(client, tableName);
    }

    @Provides
    @Singleton
    AmazonDynamoDB getDynamoClient() {
        return AmazonDynamoDBClientBuilder.defaultClient();
    }
}
