package com.djtran.library.restapi.dom.base.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.djtran.library.restapi.dom.base.AvailabilityStatus;

public class AvailabilityStatusConverter implements DynamoDBTypeConverter<String, AvailabilityStatus> {
    @Override
    public String convert(AvailabilityStatus object) {
        return object.toString();
    }

    @Override
    public AvailabilityStatus unconvert(String object) {
        return AvailabilityStatus.valueOf(object);
    }
}
