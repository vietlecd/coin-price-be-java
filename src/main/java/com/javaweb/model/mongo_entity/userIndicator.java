package com.javaweb.model.mongo_entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "userIndicator")
public class userIndicator {
    private String username;
    private String name;
    private String code;
}
