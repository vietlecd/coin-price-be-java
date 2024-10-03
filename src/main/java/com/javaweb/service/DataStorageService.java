package com.javaweb.service;

import com.javaweb.model.PriceDTO;

public interface DataStorageService{

    void savePrice(PriceDTO priceDTO);

    PriceDTO getSpotPrice(String symbol);
}
