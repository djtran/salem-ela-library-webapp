package com.djtran.library;

import com.djtran.library.dom.Book;
import com.djtran.library.dom.StatusResponse;
import com.djtran.library.dom.Student;
import com.djtran.library.qr.BookQrTranslater;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

import static spark.Spark.get;
import static spark.Spark.post;

@Singleton
public class HttpApi {

    private static final String IMG_PREFIX = "data:image/png;base64,";

    private static Logger log = LoggerFactory.getLogger(HttpApi.class);
    private static ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    private static LibraryManager manager;
    private static BookQrTranslater translater;

    public HttpApi(LibraryManager manager,
                   BookQrTranslater translater) {
        this.manager = manager;
        this.translater = translater;
    }

    public void initialize() {
        get("/", (request, response) -> "Welcome to the Salem ELA Library API.");
        post("/checkOut", HttpApi::checkOutBookApi);
        post("/addBook", HttpApi::addBookApi);
        post("/checkIn", HttpApi::checkInBookApi);
        post("/generateQrCodeForBook", HttpApi::generateQrCodeForBook);
        post("/readQrCode", HttpApi::readQrCode);
    }

    private static String addBookApi(Request request, Response response) throws JsonProcessingException {
        log.debug("addBookApi : {}", request.body());
        response.header("Access-Control-Allow-Origin", "*");
        try {
            JsonNode tree = mapper.readTree(request.body());
            Book bookToAdd = mapper.treeToValue(tree.get(ApiFieldNames.BOOK.getFieldName()), Book.class);
            return mapper.writeValueAsString(manager.addBook(bookToAdd));
        } catch (IOException e) {
            log.error("Could not deserialize book {}, {}", request.body(), e);
            return getErrorResponse(e);
        }
    }

    private static String checkOutBookApi(Request request, Response response) throws JsonProcessingException {
        log.debug("checkOutBookApi : {}", request.body());
        response.header("Access-Control-Allow-Origin", "*");
        try {
            JsonNode tree = mapper.readTree(request.body());
            Book bookToCheck = mapper.treeToValue(tree.get(ApiFieldNames.BOOK.getFieldName()), Book.class);
            Student student = mapper.treeToValue(tree.get(ApiFieldNames.STUDENT.getFieldName()), Student.class);
            return mapper.writeValueAsString(manager.checkoutBook(student, bookToCheck));
        } catch (IOException e) {
            log.error("Could not deserialize book and student {}, {}", request.body(), e);
            return getErrorResponse(e);
        }
    }

    private static String checkInBookApi(Request request, Response response) throws JsonProcessingException {
        log.debug("checkInBookApi : {}", request.body());
        response.header("Access-Control-Allow-Origin", "*");
        try {
            JsonNode tree = mapper.readTree(request.body());
            Book bookToCheckIn = mapper.treeToValue(tree.get(ApiFieldNames.BOOK.getFieldName()), Book.class);
            return mapper.writeValueAsString(manager.checkInBook(bookToCheckIn));
        } catch (IOException e) {
            log.error("Could not deserialize book {}, {}", request.body(), e);
            return getErrorResponse(e);
        }
    }

    private static String generateQrCodeForBook(Request request, Response response) throws JsonProcessingException {
        log.debug("generateQrCodeForBook : {}", request.body());
        response.header("Access-Control-Allow-Origin", "*");

        try {
            JsonNode tree = mapper.readTree(request.body());
            Book bookToCheckIn = mapper.treeToValue(tree.get(ApiFieldNames.BOOK.getFieldName()), Book.class);
            String encodedImg = translater.generateQrCode(bookToCheckIn);
            return mapper.writeValueAsString(StatusResponse.builder()
                    .statusCode(StatusResponse.Code.SUCCESS)
                    .statusMessage(encodedImg)
                    .build());
        } catch (IOException e) {
            log.error("generateQrCodeForBook error", e);
            return getErrorResponse(e);
        }
    }

    private static String readQrCode(Request request, Response response) throws JsonProcessingException {
        log.debug("generateQrCodeForBook : {}", request.body());
        response.header("Access-Control-Allow-Origin", "*");

        try {
            JsonNode tree = mapper.readTree(request.body());
            String encodedImg = tree.get(ApiFieldNames.IMAGE.getFieldName()).asText();
            log.debug("image source: {}", encodedImg);
            // for now, hardcode removal of the data prefix.
            encodedImg = encodedImg.substring(IMG_PREFIX.length());
            Book book = translater.readQrCode(encodedImg);

            return mapper.writeValueAsString(StatusResponse.builder()
                    .statusCode(StatusResponse.Code.SUCCESS)
                    .statusMessage(book.toString())
                    .build());

        } catch (IOException e) {
            log.error("Unable to read image from byte array input stream", e);

            return getErrorResponse(e);
        } catch (NotFoundException e) {
            log.error("Unable to decode a result from the BinaryBitmap. Is there a QRCode in the image?", e);

            return getErrorResponse(e);
        } catch (FormatException e) {
            log.error("Not in QR Code format: ", e);

            return getErrorResponse(e);
        } catch (ChecksumException e) {
            log.error("QR checksum invalid", e);

            return getErrorResponse(e);
        }
    }

    private static String getErrorResponse(Exception e) throws JsonProcessingException {
        return mapper.writeValueAsString(StatusResponse.builder()
                .statusCode(StatusResponse.Code.ERROR)
                .statusMessage("Internal Server Error : " + e.toString())
                .build());
    }

}
