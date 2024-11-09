package com.javaweb.service.trigger.CRUD;

import com.javaweb.dto.trigger.ListingDTO;
import com.javaweb.repository.ListingRepository;
import com.javaweb.utils.TriggerNotFoundException;
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

    public List<String> fetchNewListings() {
        List<String> newSymbols = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(URL).get();
            Elements scripts = doc.getElementsByTag("script");

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

    public String createTrigger(ListingDTO dto, String username) {

        ListingDTO existingTrigger = listingRepository.findBySymbol(dto.getSymbol());

        if (existingTrigger != null) {

            return "Trigger already exists for this symbol";
        }


        ListingDTO newTrigger = new ListingDTO.Builder()
                .setSymbol(dto.getSymbol())
                .setNotificationMethod(dto.getNotificationMethod())
                .setShouldNotify(true)
                .build();

        listingRepository.save(newTrigger);
        return "Trigger created";
    }

    public void deleteTrigger(String symbol, String username) {
        ListingDTO trigger = listingRepository.findBySymbol(symbol);

        if (trigger != null) {

            trigger.setShouldNotify(false);
            listingRepository.save(trigger);


            listingRepository.deleteBySymbol(symbol);
        } else {
            throw new TriggerNotFoundException("Trigger not found for symbol: " + symbol);
        }
    }
}
