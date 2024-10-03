package com.javaweb.repository;

import com.javaweb.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Long> {
    PriceEntity findTopBySymbolOrderByTimestampDesc(String symbol);

}
