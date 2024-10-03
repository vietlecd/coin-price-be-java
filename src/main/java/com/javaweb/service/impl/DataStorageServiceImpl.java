package com.javaweb.service.impl;

import com.javaweb.entity.PriceEntity;
import com.javaweb.model.PriceDTO;
import com.javaweb.repository.PriceRepository;
import com.javaweb.service.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataStorageServiceImpl implements DataStorageService {

    @Autowired
    private PriceRepository priceRepository;

    public PriceDTO getSpotPrice(String symbol) {
        PriceEntity priceEntity = priceRepository.findTopBySymbolOrderByTimestampDesc(symbol);
        if (priceEntity != null) {
            return new PriceDTO(priceEntity.getSymbol(), priceEntity.getPrice());
        } else {
            return new PriceDTO(symbol, "No data");
        }
    }

    public void savePrice(PriceDTO priceDTO){
        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setSymbol(priceDTO.getSymbol());
        priceEntity.setPrice(priceDTO.getPrice());

        priceRepository.save(priceEntity);
    }
}
