package com.djtran.library.restapi.spring;

import com.djtran.library.restapi.dom.api.CheckInRequest;
import com.djtran.library.restapi.dom.api.NewBookRequest;
import com.djtran.library.restapi.dom.base.Book;
import com.djtran.library.restapi.dom.api.StatusResponse;
import com.djtran.library.restapi.dom.base.Student;
import com.djtran.library.restapi.dom.api.CheckoutRequest;
import com.djtran.library.restapi.qr.BookQrTranslater;
import com.djtran.library.restapi.transaction.LibraryManager;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class LibraryApiController {

    private static final String IMG_PREFIX = "data:image/png;base64,";
    private static ObjectMapper mapper = new ObjectMapper(new JsonFactory());

    private static BookQrTranslater translater;
    private static LibraryManager manager;

    @PostMapping("/checkOut")
    public StatusResponse checkout(
            @RequestBody CheckoutRequest request
    ) {
        Student student = request.getStudent();
        String encodedQrImage = request.getImage();
        Book bookToCheck = readQrCode(encodedQrImage);
        if (bookToCheck == null) {
            return getErrorResponse(
                    new Exception("Could not read Qr Code, check server logs for more information")
            );
        }
        return manager.checkoutBook(student, bookToCheck);
    }

    @PostMapping("/checkIn")
    public StatusResponse checkIn(
            @RequestBody CheckInRequest request
    ) {
        Book bookToCheckIn = request.getBook();
        return manager.checkInBook(bookToCheckIn);
    }

    @PostMapping("/addBook")
    public StatusResponse addBook(
            @RequestBody NewBookRequest request
            ) {
        Book book = request.getBook();
        String encodedQrImage = translater.generateQrCode(book);

        StatusResponse addBookResponse = manager.addBook(book);
        if (addBookResponse.getStatusCode() == StatusResponse.Code.SUCCESS) {
            return StatusResponse.builder()
                    .statusCode(StatusResponse.Code.SUCCESS)
                    .statusMessage(encodedQrImage)
                    .build();
        } else {
            return getErrorResponse(
                    new Exception("Could not add book, check the server logs for more information"));
        }
    }


    private static Book readQrCode(String encodedImg) {
        try {
            // for now, hardcode removal of the data prefix.
            encodedImg = encodedImg.substring(IMG_PREFIX.length());
            return translater.readQrCode(encodedImg);
        } catch (IOException e) {
            log.error("Unable to read image from byte array input stream", e);
            return null;
        } catch (NotFoundException e) {
            log.error("Unable to decode a result from the BinaryBitmap. Is there a QRCode in the image?", e);
            return null;
        } catch (FormatException e) {
            log.error("Not in QR Code format: ", e);
            return null;
        } catch (ChecksumException e) {
            log.error("QR checksum invalid", e);
            return null;
        }
    }

    private static StatusResponse getErrorResponse(Exception e) {
        return StatusResponse.builder()
                .statusCode(StatusResponse.Code.ERROR)
                .statusMessage("Internal Server Error : " + e.toString())
                .build();
    }


    /**
     * Some time at or after 4 PM. 3:30 at the earliest.
     */
}
