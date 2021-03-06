package com.djtran.library.restapi.dom.base;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.djtran.library.restapi.dom.base.converter.AvailabilityStatusConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Class representing the DynamoDB document. Contents are summation of a Book and all Students who have taken it out.\
 * The tableName in the annotation must match the tableName provided from application.properties.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "salem-ela-library")
public class LibraryCard {
    @DynamoDBHashKey(attributeName = "bookId")
    private String encodedBookQrId;

    @DynamoDBAttribute
    private String bookName;

    @DynamoDBAttribute
    private List<String> tags;

    @DynamoDBAttribute
//    @DynamoDBTypeConverted(converter = CheckoutRecordConverter.class)
    private List<LibraryRecord> checkoutHistory;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = AvailabilityStatusConverter.class)
    private AvailabilityStatus availability;
}
