package com.djtran.library.restapi.dom.base;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Book {
    private String id;
    private String name;
    private List<String> tags;
}
