package com.javaweb.service.trigger.CRUD;

import com.javaweb.dto.trigger.ListingDTO;
import com.javaweb.model.trigger.ListingEntity;
import com.javaweb.repository.trigger.ListingRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListingTriggerService {

    private static final String URL = "https://www.binance.com/en/support/announcement/new-cryptocurrency-listing?c=48&navId=48&hl=en";

    @Autowired
    private ListingRepository listingRepository;

    private boolean allowNotification = false;
    public List<String> fetchNewListings() {
        List<String> newSymbols = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(URL).get();
            Elements scripts = doc.getElementsByTag("script");

            // Tìm kiếm trong script chứa thông tin về USDⓈ-Margined
            for (Element script : scripts) {
                String scriptContent = script.html();
                if (scriptContent.contains("USDⓈ-Margined")) {
                    String[] words = scriptContent.split("USDⓈ-Margined");
                    for (int i = 1; i < words.length; i++) {
                        String symbol = words[i].trim().split(" ")[0];
                        newSymbols.add(symbol);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newSymbols;
    }


    public boolean existsBySymbol(String symbol) {
        return listingRepository.existsBySymbol(symbol);
    }


    public String createTrigger(ListingDTO listingDTO, String username) {
        ListingEntity listingEntity = new ListingEntity(listingDTO.getSymbol(), listingDTO.getNotificationMethod(), true);
        listingRepository.save(listingEntity);

        allowNotification = true;
        return "Trigger created";
    }
    public void disableNotifications() {
        this.allowNotification = false;
    }
    public void deleteTrigger(String notificationMethod, String username) {
        if (listingRepository.existsByNotificationMethod(notificationMethod)) {
            listingRepository.deleteByNotificationMethod(notificationMethod);
        }
        allowNotification = false;
    }



    public void deactivateTrigger(String symbol) {
        ListingEntity listing = listingRepository.findBySymbol(symbol);
        if (listing != null) {
            listing.setActive(false);
            listingRepository.save(listing);
        } else {
            throw new RuntimeException("Trigger not found for symbol " + symbol);
        }
    }


    public ListingEntity findListingBySymbol(String symbol) {
        return listingRepository.findById(symbol).orElse(null);
    }

    public void saveListing(ListingEntity listingEntity) {
        listingRepository.save(listingEntity);
    }

    public ListingEntity findBySymbol(String symbol) {
        return listingRepository.findBySymbol(symbol);
    }

    public boolean isNotificationAllowed() {
        return allowNotification;
    }

    public void deleteAllListings() {
        listingRepository.deleteAll();
        System.out.println("All listings have been deleted.");
    }
}
