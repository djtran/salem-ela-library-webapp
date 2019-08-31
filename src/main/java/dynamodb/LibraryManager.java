package dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dom.Book;
import dom.StatusResponse;
import dom.Student;
import dynamodb.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LibraryManager {

    private static final Logger log = LoggerFactory.getLogger(LibraryManager.class);

    private final DynamoDBMapper checkRecordMapper;

    public LibraryManager(DynamoDBMapper checkRecordMapper) {
        this.checkRecordMapper = checkRecordMapper;
    }

    public StatusResponse checkoutBook(Student s, Book b) {
        LibraryCard card = getLibraryCard(b);
        List<CheckoutRecord> checkoutHistory = card.getCheckoutHistory();
        // Check if already checked out
        if (card.getAvailability() != AvailabilityStatus.AVAILABLE) {
            // Check the book back in.
            for (CheckoutRecord record : checkoutHistory) {
                if (record.getDateCheckedIn() == null) {
                    record.setDateCheckedIn(new Date());
                }
            }
        }

        //Update checkout history
        checkoutHistory.add(CheckoutRecord.builder()
                .studentName(s.getName())
                .studentId(s.getId())
                .dateCheckedOut(new Date())
                .build());
        card.setCheckoutHistory(checkoutHistory);
        card.setAvailability(AvailabilityStatus.CHECKED_OUT);

        //Save changes
        saveLibraryCard(card);

        return StatusResponse.builder()
                .statusCode(StatusResponse.Code.SUCCESS)
                .statusMessage("Successfully checked out")
                .build();
    }

    public StatusResponse checkInBook(Book b) {
        LibraryCard card = getLibraryCard(b);
        List<CheckoutRecord> history = card.getCheckoutHistory();
        history.get(history.size() -1).setDateCheckedIn(new Date());
        card.setCheckoutHistory(history);
        saveLibraryCard(card);
        return StatusResponse.builder()
                .statusCode(StatusResponse.Code.SUCCESS)
                .statusMessage("Successfully checked in")
                .build();
    }

    public StatusResponse addBook(Book b) {
        LibraryCard card = LibraryCard.builder()
                .bookName(b.getName())
                .encodedBookQrId(b.getId())
                .availability(AvailabilityStatus.AVAILABLE)
                .tags(b.getTags())
                .checkoutHistory(Collections.emptyList())
                .build();

        checkRecordMapper.save(card);
        return StatusResponse.builder()
                .statusCode(StatusResponse.Code.SUCCESS)
                .statusMessage(String.format("Created library card %s for book %s", card, b))
                .build();
    }

    public AvailabilityStatus isBookAvailable(Book b) {
        return getLibraryCard(b).getAvailability();
    }

    private LibraryCard getLibraryCard(Book b) {
        return checkRecordMapper.load(LibraryCard.class, b.getId());
    }

    private void saveLibraryCard(LibraryCard c) {
        checkRecordMapper.save(c);
    }

}
