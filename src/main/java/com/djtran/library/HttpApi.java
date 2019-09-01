package com.djtran.library;

import com.djtran.library.dom.Book;
import com.djtran.library.dom.StatusResponse;
import com.djtran.library.dom.Student;
import com.djtran.library.dom.exceptions.InvalidInputException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

import static spark.Spark.get;
import static spark.Spark.post;

@Singleton
public class HttpApi {

    private static Logger log = LoggerFactory.getLogger(HttpApi.class);
    private static ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    private static LibraryManager manager;

    public HttpApi(LibraryManager manager) {
        this.manager = manager;
    }

    public void initialize() {
        get("/", (request, response) -> "Welcome to the Salem ELA Library API.");
        post("/checkOut", HttpApi::checkOutBookApi);
        post("/addBook", HttpApi::addBookApi);
        post("/checkIn", HttpApi::checkInBookApi);
    }

    private static StatusResponse addBookApi(Request request, Response response) {
        log.debug("addBookApi : {}", request.body());
        try {
            JsonNode tree = mapper.readTree(request.body());
            Book bookToAdd = mapper.treeToValue(tree.get(ApiFieldNames.BOOK.getFieldName()), Book.class);
            return manager.addBook(bookToAdd);
        } catch (IOException e) {
            log.error("Could not deserialize book {}, {}", request.body(), e);
            return StatusResponse.builder()
                    .statusCode(StatusResponse.Code.ERROR)
                    .statusMessage("Internal Server Error : " + e.toString())
                    .build();
        }
    }

    private static StatusResponse checkOutBookApi(Request request, Response response) throws InvalidInputException {
        log.debug("checkOutBookApi : {}", request.body());
        try {
            JsonNode tree = mapper.readTree(request.body());
            Book bookToCheck = mapper.treeToValue(tree.get(ApiFieldNames.BOOK.getFieldName()), Book.class);
            Student student = mapper.treeToValue(tree.get(ApiFieldNames.STUDENT.getFieldName()), Student.class);
            return manager.checkoutBook(student, bookToCheck);
        } catch (IOException e) {
            log.error("Could not deserialize book and student {}, {}", request.body(), e);
            return StatusResponse.builder()
                    .statusCode(StatusResponse.Code.ERROR)
                    .statusMessage("Internal Server Error : " + e.toString())
                    .build();
        }
    }

    private static StatusResponse checkInBookApi(Request request, Response response) throws InvalidInputException {
        log.debug("checkInBookApi : {}", request.body());
        try {
            JsonNode tree = mapper.readTree(request.body());
            Book bookToCheckIn = mapper.treeToValue(tree.get(ApiFieldNames.BOOK.getFieldName()), Book.class);
            return manager.checkInBook(bookToCheckIn);
        } catch (IOException e) {
            log.error("Could not deserialize book {}, {}", request.body(), e);
            return StatusResponse.builder()
                    .statusCode(StatusResponse.Code.ERROR)
                    .statusMessage("Internal Server Error : " + e.toString())
                    .build();
        }
    }
}
