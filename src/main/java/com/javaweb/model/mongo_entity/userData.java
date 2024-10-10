package com.javaweb.model.mongo_entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "userData")
public class userData {
    @Id
    private String id;

    private String username;
    private String name;
    private String password;
    private String token;
}
