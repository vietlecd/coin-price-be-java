package com.javaweb.service.trigger.CRUD;

import com.javaweb.dto.trigger.DelistingDTO;
import com.javaweb.model.trigger.DelistingEntity;
import com.javaweb.repository.trigger.DelistingRepository;
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
public class DelistingTriggerService {

    private static final String URL = "https://www.binance.com/en/support/announcement/delisting?c=161&navId=161&hl=en";

    @Autowired
    private DelistingRepository delistingRepository;

    private boolean allowNotification = false;

    public String createTrigger(DelistingDTO delistingDTO, String username) {
        // Cào dữ liệu tiêu đề từ Binance
        List<String> newTitles = fetchNewDelistings();

        if (newTitles.isEmpty()) {
            throw new RuntimeException("Không tìm thấy tiêu đề nào từ Binance để tạo trigger.");
        }

        // Lấy tiêu đề mới nhất
        String title = newTitles.get(0);


        if (existsByTitle(title)) {

            return "Title existed " ;
        }

        // Tạo entity và lưu vào cơ sở dữ liệu
        DelistingEntity delistingEntity = new DelistingEntity(title, delistingDTO.getNotificationMethod(), true);
        saveDelisting(delistingEntity);

        allowNotification = true;
        return "Trigger created" ;
    }


    public List<String> fetchNewDelistings() {
        List<String> newTitles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(URL).get();
            Elements scripts = doc.getElementsByTag("script");

            // Tìm kiếm các tiêu đề liên quan đến "Notice of"
            for (Element script : scripts) {
                String scriptContent = script.html();
                if (scriptContent.contains("\"title\":\"")) {
                    String[] titles = scriptContent.split("\"title\":\"");
                    for (int i = 1; i < titles.length; i++) {
                        String title = titles[i].split("\"")[0]; // Lấy tiêu đề trước dấu ngoặc kép tiếp theo
                        if (title.contains("Notice of")) {
                            newTitles.add(title);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể cào dữ liệu từ Binance.");
        }
        return newTitles;
    }

    public boolean existsByTitle(String title) {
        return delistingRepository.existsByTitle(title);
    }

    public void saveDelisting(DelistingEntity delistingEntity) {
        if (!existsByTitle(delistingEntity.getTitle())) {
            delistingRepository.save(delistingEntity);
            System.out.println("Đã lưu trigger delisting: " + delistingEntity.getTitle());
        } else {
            System.out.println("Trigger delisting đã tồn tại: " + delistingEntity.getTitle());
        }
    }

    public void disableNotifications() {
        this.allowNotification = false;
    }

    public void deleteTrigger(String notificationMethod, String username) {
        if (delistingRepository.existsByNotificationMethod(notificationMethod)) {
            delistingRepository.deleteByNotificationMethod(notificationMethod);
        }
        allowNotification = false;
    }

    public boolean isNotificationAllowed() {
        return allowNotification;
    }

    public void deleteAllDelistings() {
        delistingRepository.deleteAll();
        System.out.println("Đã xóa tất cả các bản ghi delisting.");
    }
}
