# Coin Price Backend Project
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

- [BACKEND SERVER](https://a2-price.thuanle.me/)
- [API docs](https://dath.hcmutssps.id.vn/docs/)

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

## Thông tin dự án
Dự án này là một ứng dụng backend hỗ trợ việc theo dõi thông tin thị trường (giá tiền, phí,...), các giao dịch được hình thành, tra cứu thống kê các giao dịch, tự động nhận cảnh báo giá.

## Yêu Cầu Chức Năng và Phi Chức Năng
### Yêu Cầu Chức Năng
- **Đối với Guest**
  - Đăng nhập và đăng kí
- **Đối với User**
  - Đăng nhập vào/đăng xuất khỏi hệ thống.
  - Quên mật khẩu nếu không nhớ mật khẩu bằng cách xác thực OTP qua mail
  - Xem thông tin hồ sơ của mình, bao gồm các thông tin cá nhân và quyền hạn trong hệ thống.
  - Sửa thông tin cơ bản (mật khẩu, email)
  - Nạp tiền vào tài khoản
  - Mua vip (1..3)
  - Tra cứu thông tin
  - Cảnh báo giá: Hỗ trợ cảnh báo giá, trigger khi đạt các điều kiện như:
    - Trigger Condition 
    - Snooze Condition:

- **Đối với Admin**
  - Truy cập vào trang admin.
  - Sử dụng các APIs được cung cấp nếu đã xác thực với admin's token
  - Lấy thông tin tất cả user trong csdl (trừ mật khẩu)
  - Lấy thông tin tất cả lịch sử giao dịch của user
  - Có quyền xóa 1 user ra khỏi hệ thống
### Yêu Cầu Phi Chức Năng

### Tiêu chí phát triển hệ thống

**1. Hiệu năng (Performance)**
- Hệ thống phải đáp ứng nhanh chóng ngay cả trong điều kiện tải thông thường.
- Đảm bảo xử lý đồng thời số lượng lớn yêu cầu từ người dùng mà không gây giảm hiệu suất.

**2. Bảo mật (Security)**
- Tích hợp cơ chế xác thực bằng token (JWT) cho các route yêu cầu bảo vệ.
- Quản lý phân quyền chi tiết:
  - **Các Vips**: Đảm bảo các Vips sử dụng được đúng api của mình
  - **Admin**: Đảm bảo chỉ có admin mới có thể dùng api của admin
- Bảo vệ thông tin liên lạc bằng giao thức HTTPS.

**3. Khả năng mở rộng (Scalability)**
- Hỗ trợ mở rộng hệ thống ngang hàng (horizontal scaling) để đáp ứng nhu cầu tăng trưởng.
- Thiết kế cơ sở dữ liệu hiệu quả, tối ưu hóa việc truy vấn khi số lượng bản ghi lớn.

**4. Độ tin cậy (Reliability)**
- Duy trì thời gian hoạt động của hệ thống, đảm bảo serveer dự phòng khi server chính gặp sự cố.
- Đảm bảo khả năng sao lưu và khôi phục dữ liệu nhanh chóng trong các tình huống khẩn cấp.

**5. Tính dễ bảo trì (Maintainability)**
- Tổ chức hệ thống theo cấu trúc module hóa, giúp thuận tiện trong việc nâng cấp và sửa đổi.
- Tuân thủ chuẩn mực lập trình (clean code) và cung cấp tài liệu chi tiết cho API.

**6. Thân thiện người dùng (Usability)**
- Đưa ra các thông báo lỗi cụ thể, dễ hiểu khi xảy ra sự cố (ví dụ: lỗi xác thực, từ chối quyền truy cập).
- Thiết kế API RESTful nhất quán, dễ dàng tích hợp với hệ thống khác.

**7. Tương thích (Compatibility)**
- Hệ thống hoạt động ổn định với các framework frontend phổ biến như ReactJS hoặc Angular.
- Hỗ trợ định dạng JSON cho các dữ liệu gửi đi và nhận về qua API.


### Tính năng đã hoàn thành:
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

### Hướng dẫn cài đặt
```basg
Cách 1: Chạy qua tomcat:
    1. Set up JDK - 1.8.
    2. Cài plugin: EnvFile, Spring WebSocket và Tomcat and TomEE.
    3. Setting Tomcat server: 
        - Cài artifact "spring-boot:war exploded" trong các mục before launch, deployment.
        - Thêm đường dẫn tới file .env trong mục EnvFile.
    4. Run Tomcat.
Cách 2: Chạy bằng DockerFile:
    1. Tạo docker-compose.yaml như sau:
        services:
            spring-boot-app:
                ports:
                - "8080:8080"
                env_file:
                - .env
                image: lmao1415/dath_cnpm
    2. Docker compose pull để kéo image về
    3. Touch .env
            echo 'MONGO_DB_URI=${{ secrets.MONGO_DB_URI }}' >> .env 
            echo 'MONGO_DB_NAME=${{ secrets.MONGO_DB_NAME }}' >> .env 
            echo 'JWT_SECRET=${{ secrets.JWT_SECRET }}' >> .env 
            echo 'TELEGRAM_WEBHOOK_URL=${{ secrets.TELEGRAM_WEBHOOK_URL }}' >> .env 
            echo 'email=${{ secrets.EMAIL }}' >> .env 
            echo 'password_for_email=${{ secrets.password_for_email }}' >> .env 
            echo 'adminToken=${{ secrets.ADMINTOKEN }}' >> .env  
    4. sudo docker compose up
```
### ENV TEMPLATE (Nhớ bỏ ngoặc '{}')
```basg
MONGO_DB_URI={}
MONGO_DB_NAME={}
JWT_SECRET={}
TELEGRAM_WEBHOOK_URL={}
email={}
password_for_email={}
adminToken={}
```
### Công việc
|STT | MSSV    | Tên thành viên      | Công việc | 
|----|---------|---------------------|------|
|1   | 2213524 | Hà Thái Toàn        | Viết report, phân công, viết Swagger   |
|2   | 2252903 | Lê Hoàng Việt       | Api F1, F2, F3, trigger condition  |
|3   | 2211472 | Trần Minh Khang     | Auth request + viết CI/CD  |
|4   | 2252266 | Trần Nhật Huy       | Api F2 + Snooze Condition  |
|5   | 2213449 | Đoàn Nhật Tiến      | F4 + Advanced Indicator  |
|6   | 2211271 | Sam Gia Huy         | Trigger Indicator  |
|7   | 2113992 | Tô Tấn Luật         | Listing + Delisting  |
