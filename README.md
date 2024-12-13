# NguyenThanhNghiem_21024501_Week05

# Hệ thống Quản lý Công việc và Ứng viên

Đây là một ứng dụng web dùng để quản lý công việc và ứng viên, được xây dựng bằng **Spring Boot**. Hệ thống cho phép các công ty đăng tin tuyển dụng, và ứng viên có thể ứng tuyển dựa trên kỹ năng và sở thích của mình. Ứng dụng cũng cung cấp tính năng gợi ý công việc phù hợp với ứng viên và cho phép các công ty mời ứng viên qua email.

## Các tính năng

- **Quản lý Công việc:**
  - Các công ty có thể đăng tin tuyển dụng với các tiêu đề công việc, mô tả và yêu cầu kỹ năng.
  - Công việc được hiển thị với phân trang để dễ dàng duyệt qua.

- **Quản lý Ứng viên:**
  - Các ứng viên có thể đăng ký với các thông tin cá nhân, bao gồm tên, ngày sinh, số điện thoại, email và địa chỉ.
  - Hồ sơ của ứng viên được lưu trữ và có thể được quản lý bởi quản trị viên.

- **Gợi ý Công việc cho Ứng viên:**
  - Dựa trên kỹ năng của ứng viên, hệ thống gợi ý các công việc phù hợp.

- **Thông báo qua Email:**
  - Các công ty có thể gửi thư mời ứng viên qua email nếu ứng viên phù hợp với yêu cầu công việc.

- **Quản lý Địa chỉ và Quốc gia:**
  - Địa chỉ của ứng viên bao gồm quốc gia, thành phố, đường phố, mã bưu điện và thông tin địa chỉ cụ thể.

- **Phân trang:**
  - Cả công việc và ứng viên đều được hiển thị với phân trang để tối ưu trải nghiệm người dùng.

## Công nghệ sử dụng

- **Backend:**
  - **Spring Boot**: Framework phát triển dịch vụ backend dựa trên Java.
  - **Spring Data JPA**: Dùng để tương tác với cơ sở dữ liệu và thực hiện các thao tác CRUD.
  - **Thymeleaf**: Công cụ tạo template động cho các trang HTML.
  - **JavaMail API**: Dùng để gửi thông báo qua email.

- **Frontend:**
  - **HTML/CSS**: Công nghệ frontend cơ bản để tạo các form và layout.
  - **Bootstrap**: Framework CSS giúp thiết kế giao diện responsive.

- **Cơ sở dữ liệu:**
  - **MySQL**: Cơ sở dữ liệu quan hệ để lưu trữ dữ liệu công việc và ứng viên.


### Yêu cầu

- Java 11 hoặc phiên bản cao hơn
- Gradle
- MariaDB hoặc bất kỳ cơ sở dữ liệu quan hệ nào khác
- IDE (ví dụ: IntelliJ IDEA, Eclipse) để phát triển Java




