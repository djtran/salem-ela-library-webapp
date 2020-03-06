package com.djtran.library.restapi.dom.base;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class LibraryRecord {
    @DynamoDBAttribute
    private String studentId;

    @DynamoDBAttribute
    private String studentName;

    @DynamoDBAttribute
    private Date dateCheckedOut;

    @DynamoDBAttribute
    private Date dateCheckedIn;
}
