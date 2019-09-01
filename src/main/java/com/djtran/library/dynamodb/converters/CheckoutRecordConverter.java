package com.djtran.library.dynamodb.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.djtran.library.dynamodb.dom.CheckoutRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CheckoutRecordConverter implements DynamoDBTypeConverter<String, CheckoutRecord> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(CheckoutRecordConverter.class);
    @Override
    public String convert(CheckoutRecord checkoutRecord) {
        try {
            return mapper.writeValueAsString(checkoutRecord);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize CheckoutRecord {}: {}", checkoutRecord, e);
            return null;
        }
    }

    @Override
    public CheckoutRecord unconvert(String s) {
        try {
            return mapper.readValue(s, CheckoutRecord.class);
        } catch (IOException e) {
            log.error("Could not deserialize CheckoutRecord {}: {}", s, e);
            return null;
        }
    }
}
