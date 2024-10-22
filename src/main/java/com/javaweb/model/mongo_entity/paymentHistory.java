package com.javaweb.model.mongo_entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "paymentHistory")
public class paymentHistory {
    private Date date;
    private String username;
    private String email;
    private Integer amount;
}
