package com.djtran.library.restapi.dom.base.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.djtran.library.restapi.dom.base.LibraryRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LibraryRecordConverter implements DynamoDBTypeConverter<String, LibraryRecord> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(LibraryRecordConverter.class);
    @Override
    public String convert(LibraryRecord libraryRecord) {
        try {
            return mapper.writeValueAsString(libraryRecord);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize CheckoutRecord {}: {}", libraryRecord, e);
            return null;
        }
    }

    @Override
    public LibraryRecord unconvert(String s) {
        try {
            return mapper.readValue(s, LibraryRecord.class);
        } catch (IOException e) {
            log.error("Could not deserialize CheckoutRecord {}: {}", s, e);
            return null;
        }
    }
}
