package dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import dom.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for initializing table if no table already exists.
 */
public class LibraryInitializer {
    private static final Logger log = LoggerFactory.getLogger(LibraryInitializer.class);

    private final AmazonDynamoDB client;
    private final String tableName;

    public LibraryInitializer(AmazonDynamoDB client,
                              String tableName) {

        this.client = client;
        this.tableName = tableName;
    }

    public StatusResponse init() throws InterruptedException {
        ListTablesResult tables = client.listTables();
        boolean tableExists = tables.getTableNames().stream()
                .anyMatch(name -> name.equals(tableName));

        if (!tableExists) {
            CreateTableRequest newTable = new CreateTableRequest()
                    .withTableName(tableName)
                    .withAttributeDefinitions(
                            new AttributeDefinition()
                            .withAttributeName("bookId")
                            .withAttributeType("S")
                    )
                    .withKeySchema(
                            new KeySchemaElement()
                            .withKeyType("HASH")
                            .withAttributeName("bookId")
                    )
                    .withProvisionedThroughput(
                            new ProvisionedThroughput()
                            .withReadCapacityUnits(1L)
                            .withWriteCapacityUnits(1L)
                    );
            client.createTable(newTable);
        }
        //Check that the table is reachable.
        int counter = 0;
        while(!tableExists) {
            //sleep for 5 seconds
            Thread.sleep(5000);
            tables = client.listTables();
            tableExists = tables.getTableNames().stream()
                    .anyMatch(name -> name.equals(tableName));

            counter++;
            if (counter >= 60) {
                log.error("Table failed to initialize after 5 minutes");
                System.exit(1);
            }
        }

        return StatusResponse.builder()
                .statusCode(StatusResponse.Code.SUCCESS)
                .statusMessage("Initialized table successfully")
                .build();
    }
}
