package dynamodb.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import dynamodb.dom.AvailabilityStatus;

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
