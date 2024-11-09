package com.javaweb.repository;

import com.javaweb.dto.trigger.ListingDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ListingRepository extends MongoRepository<ListingDTO, String> {
    ListingDTO findBySymbol(String symbol); // Tìm trigger theo symbol

    void deleteBySymbol(String symbol); // Xóa trigger theo symbol

    List<ListingDTO> findByShouldNotifyTrue();
}