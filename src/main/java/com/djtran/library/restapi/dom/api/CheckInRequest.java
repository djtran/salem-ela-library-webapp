package com.djtran.library.restapi.dom.api;

import com.djtran.library.restapi.dom.base.Book;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
public class CheckInRequest {

    @NonNull
    private final Book book;
}
