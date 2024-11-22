# Coin Price Backend Project
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

- [BACKEND SERVER](https://a2-price.thuanle.me/)
- [API docs](https://dath.hcmutssps.id.vn/docs/)

## Thông tin dự án
Dự án này là một ứng dụng backend hỗ trợ việc theo dõi thông tin thị trường (giá tiền, phí,...), các giao dịch được hình thành, tra cứu thống kê các giao dịch, tự động nhận cảnh báo giá.

### Tính năng:
- *Tra cứu thông tin*:
    - **F1**: Giá Spot, Giá Future, Funding Rate, Funding Rate Countdown, Funding Rate Interval. Được cập nhật từ Binance, update interval: realtime ~ 1s.
    - **F2**: Giá theo kline.
    - **F3**: MarketCap, Trading Volume. Được cập nhập từ CMC, CG, update interval: 15min.
    - **F4**: *Tính giá trị theo Indicators*:
        - Theo các Indictor phổ biến: MA, EMA, BOLL,…
        - Cho phép người dùng tự code như plugin.
- *Cảnh báo giá*: Hỗ trợ cảnh báo giá, trigger khi đạt các điều kiện như:
    - **Trigger Condition**: 
        - Giá Spot/Future tăng/giảm hơn 1 mốc nào đó, vào/ra range nào đó.
        - Mức độ chênh giá giữa spot/future so với 1 mốc nào đó, vào/ra range nào đó.
        - Funding rate tăng/giảm hơn 1 mốc nào đó, vào/ra range nào đó.
        - Funding rate Interval thay đổi.
        - New Symbol listing, delisting.
        - *Khi chạm các mốc của indicator hoặc chạm điều kiện trigger của Indicator*.
    - **Snooze Condition**:
        - Một lần.
        - Một lần trong khoảng thời gian.
        - Lặp lại n lần hoặc khoảng thời gian đặc biệt hoặc vĩnh viễn.
    - **Action**:
        - Webhook.
        - Post Telegram Message.
- **Auth request bằng API-TOKEN**: Token gồm các thông tin (Key - Secret - Created Date...)
    - Hỗ trợ CRUD, statistic.
    - Phân quyền Token privilege:
        - VIP-0: Chỉ các api tra cứu giá.
        - VIP-1: Các API tra cứu Kline.
        - VIP-2: Trigger Condition.
        - VIP-3: Adv Indicators.


### Ngôn ngữ và công nghệ
- ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) **Ngôn ngữ:** Java
- ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white) **Cơ sở dữ liệu:** MongoDB
- ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) **Công nghệ:** Spring Boot
### Hướng dẫn cài đặt
```basg
mvn install
```
### Để chạy chương trình
Chạy qua tomcat.
## Thành viên nhóm
|STT | MSSV    | Tên thành viên      | Role | 
|----|---------|---------------------|------|
|1   | 2213524 | Hà Thái Toàn        | PO   |
|2   | 2252903 | Lê Hoàng Việt       | Dev  |
|3   | 2211472 | Trần Minh Khang     | Dev  |
|4   | 2252266 | Trần Nhật Huy       | Dev  |
|5   | 2213449 | Đoàn Nhật Tiến      | Dev  |
|6   | 2211271 | Sam Gia Huy         | Dev  |
|7   | 2113992 | Tô Tấn Luật         | Dev  |
