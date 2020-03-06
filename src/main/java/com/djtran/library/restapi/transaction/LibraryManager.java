package com.djtran.library.restapi.transaction;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.djtran.library.restapi.dom.api.StatusResponse;
import com.djtran.library.restapi.dom.base.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Library CRUD.
 */
@Component
public class LibraryManager {

    private static final Logger log = LoggerFactory.getLogger(LibraryManager.class);

    private final DynamoDBMapper checkRecordMapper;

    public LibraryManager(DynamoDBMapper checkRecordMapper) {
        this.checkRecordMapper = checkRecordMapper;
    }

    public StatusResponse checkoutBook(Student s, Book b) {
        log.info("Checkout {} for {}", b, s);
        LibraryCard card = getLibraryCard(b);
        List<LibraryRecord> checkoutHistory = card.getCheckoutHistory();
        // Check if already checked out
        if (card.getAvailability() != AvailabilityStatus.AVAILABLE) {
            // Check the book back in.
            for (LibraryRecord record : checkoutHistory) {
                if (record.getDateCheckedIn() == null) {
                    record.setDateCheckedIn(new Date());
                }
            }
        }

        //Update checkout history
        checkoutHistory.add(LibraryRecord.builder()
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
                .statusMessage("Successfully checked out " + b.toString() + " for " + s.toString())
                .build();
    }

    public StatusResponse checkInBook(Book b) {
        log.info("Check in {}", b);
        //Pull Library Card
        LibraryCard card = getLibraryCard(b);

        //Log check in time
        List<LibraryRecord> history = card.getCheckoutHistory();
        history.get(history.size() - 1).setDateCheckedIn(new Date());
        card.setCheckoutHistory(history);
        log.info("Updated entry: {}", history.get(history.size() - 1));

        //Refresh availability
        card.setAvailability(AvailabilityStatus.AVAILABLE);

        //save
        saveLibraryCard(card);
        return StatusResponse.builder()
                .statusCode(StatusResponse.Code.SUCCESS)
                .statusMessage("Successfully checked in " + b.toString())
                .build();
    }

    public StatusResponse addBook(Book b) {
        log.info("New Book: {}", b);

        LibraryCard card = LibraryCard.builder()
                .bookName(b.getName())
                .encodedBookQrId(b.getId())
                .availability(AvailabilityStatus.AVAILABLE)
                .tags(b.getTags())
                .checkoutHistory(Collections.emptyList())
                .build();

        //Save behavior is create if primary key doesn't exist, update if existing
        checkRecordMapper.save(card);
        return StatusResponse.builder()
                .statusCode(StatusResponse.Code.SUCCESS)
                .statusMessage(String.format("Created library card %s for book %s", card, b))
                .build();
    }

    private LibraryCard getLibraryCard(Book b) {
        return checkRecordMapper.load(LibraryCard.class, b.getId());
    }

    private void saveLibraryCard(LibraryCard c) {
        checkRecordMapper.save(c);
    }

}
