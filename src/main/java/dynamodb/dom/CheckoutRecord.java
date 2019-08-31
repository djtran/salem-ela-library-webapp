package dynamodb.dom;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CheckoutRecord {
    private String studentId;
    private String studentName;
    private Date dateCheckedOut;
    private Date dateCheckedIn;
}
