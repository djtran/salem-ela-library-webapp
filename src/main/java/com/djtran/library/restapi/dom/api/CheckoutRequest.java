package com.djtran.library.restapi.dom.api;

import com.djtran.library.restapi.dom.base.Student;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
public class CheckoutRequest {

    @NonNull
    private final Student student;

    @NonNull
    private final String image;
}
