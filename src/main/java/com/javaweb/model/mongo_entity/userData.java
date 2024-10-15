package com.javaweb.model.mongo_entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "userData")
public class userData {
    @Indexed(unique = true)
    private String username;

    private String name;
    private String password;
    private String email;
    private Integer vip_role;
    private List<String> ip_list;
}
