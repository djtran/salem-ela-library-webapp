package com.djtran.library;

import lombok.Getter;

public enum ApiFieldNames {
    BOOK("Book"),
    STUDENT("Student"),
    IMAGE("Image");

    @Getter
    private final String fieldName;

    ApiFieldNames(String fieldName) {
        this.fieldName = fieldName;
    }
}
