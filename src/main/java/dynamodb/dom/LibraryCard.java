package dynamodb.dom;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import dynamodb.converters.AvailabilityStatusConverter;
import dynamodb.converters.CheckoutRecordConverter;
import lombok.Data;

import java.util.List;

/**
 * Class representing the DynamoDB document. Contents are summation of a Book and all Students who have taken it out.
 */
@Data
@DynamoDBDocument
public class LibraryCard {
    @DynamoDBHashKey(attributeName = "bookId")
    private String encodedBookQrId;

    @DynamoDBAttribute
    private String bookName;

    @DynamoDBAttribute
    private List<String> tags;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = CheckoutRecordConverter.class)
    private List<CheckoutRecord> checkoutHistory;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = AvailabilityStatusConverter.class)
    private AvailabilityStatus availability;
}
