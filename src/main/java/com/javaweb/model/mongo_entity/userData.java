//package com.javaweb.model.mongo_entity;
//
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Document(collection = "userData")
//public class userData {
//    private String username;
//    private String name;
//    private String password;
//    private String email;
//    private Integer vip_role;
//    private List<String> ip_list;
//
//    public void addIp(String ip) {
//        ip_list.add(ip);
//    }
//
//    public void removeIp(String ip) {
//        if(ip_list.contains(ip)) ip_list.remove(ip);
//    }
//}
