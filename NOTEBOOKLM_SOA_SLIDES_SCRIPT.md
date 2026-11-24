# KỊCH BẢN THUYẾT TRÌNH BÁO CÁO THỰC HÀNH HỆ THỐNG SOA
## HỆ THỐNG QUẢN LÝ ĐỒ ÁN TỐT NGHIỆP THEO KIẾN TRÚC HƯỚNG DỊCH VỤ
*(Văn bản kịch bản lời nói liền mạch từ Slide 1 đến Slide 7 dành cho AI thuyết trình và luyện nói)*

---

### LỜI MỞ ĐẦU & SLIDE 1: BỐI CẢNH DỰ ÁN (Slide 35)
"Kính thưa quý thầy cô trong hội đồng và các bạn! 

Em xin phép được bắt đầu phần báo cáo thực hành và demo hệ thống Quản lý Đồ án Tốt nghiệp được xây dựng theo Kiến trúc Hướng Dịch vụ (SOA) và mô hình Microservices.

Xuất phát từ bài toán thực tế tại Khoa Công nghệ Thông tin, mỗi mùa tốt nghiệp có hàng ngàn sinh viên cùng truy cập để đăng ký đề tài. Nếu xây dựng theo mô hình Monolith truyền thống, khi lượng sinh viên đăng ký tăng đột biến, toàn bộ hệ thống sẽ bị nghẽn và có nguy cơ sụp đổ hàng loạt. 

Vì vậy, chúng em đã áp dụng triết lý SOA để phân rã bài toán thành các dịch vụ độc lập, có ranh giới nghiệp vụ rõ ràng, giao tiếp với nhau bằng giao thức chuẩn RESTful HTTP, được điều phối tập trung qua API Gateway và container hóa toàn bộ bằng Docker.

---

### SLIDE 2: ĐẶC TẢ BÀI TOÁN & PHÂN RÃ CHỨC NĂNG (Slide 36)
Chuyển sang Slide thứ 2, về mặt đặc tả bài toán, hệ thống được chúng em phân rã thành ba microservice chuyên biệt:

Dịch vụ thứ nhất là `sinhvien-service`, đảm nhận nhiệm vụ quản lý hồ sơ và lý lịch sinh viên. Dịch vụ này cung cấp các chức năng: thêm mới sinh viên, tra cứu danh sách toàn trường, và xem thông tin chi tiết của một sinh viên theo mã số sinh viên.

Dịch vụ thứ hai là `detai-service`, đóng vai trò là kho quản lý danh mục đề tài tốt nghiệp. Dịch vụ cung cấp các chức năng: tạo mới đề tài, tra cứu danh sách đề tài theo trạng thái, xem chi tiết thông tin đề tài, và hỗ trợ chỉnh sửa hoặc xóa đề tài.

Dịch vụ thứ ba là `dangky-service`, đây chính là trung tâm điều phối nghiệp vụ của hệ thống. Dịch vụ này tiếp nhận yêu cầu đăng ký đề tài từ sinh viên, chủ động gọi sang hai dịch vụ trên để kiểm tra tính hợp lệ trước khi cấp phát phiếu đăng ký, đồng thời cho phép tra cứu lịch sử và hủy đăng ký.

Việc phân rã độc lập này giúp từng dịch vụ có kích thước nhỏ gọn và tuân thủ đúng nguyên lý Đơn nhiệm trong thiết kế phần mềm.

---

### SLIDE 3: KIẾN TRÚC TỔNG THỂ HỆ THỐNG (Slide 37)
Tiếp theo, ở Slide thứ 3 là sơ đồ kiến trúc tổng thể của hệ thống khi vận hành thực tế. Hệ thống được tổ chức thành bốn phân tầng rõ rệt:

Tầng thứ nhất là Tầng Client bên ngoài: Người dùng hoặc ứng dụng Web chỉ tương tác với một địa chỉ duy nhất tại cổng 8080 của API Gateway. Client hoàn toàn không được phép truy cập trực tiếp vào các cổng mạng nội bộ.

Tầng thứ hai là Tầng API Gateway: Sử dụng Spring Cloud Gateway tại cổng 8080, đóng vai trò là Reverse Proxy và bộ định tuyến thông minh. Dựa vào tiền tố đường dẫn URL, Gateway sẽ tự động phân giải và chuyển tiếp request đến đúng dịch vụ nội bộ.

Tầng thứ ba là Tầng Nghiệp vụ Microservices: Gồm ba container chạy độc lập: `sinhvien-service` tại cổng 8081, `detai-service` tại cổng 8082, và `dangky-service` tại cổng 8083. Giữa các dịch vụ này, chúng giao tiếp với nhau bằng giao thức HTTP REST thông qua công nghệ OpenFeign.

Tầng thứ tư là Tầng Cơ sở dữ liệu: Hệ thống sử dụng MySQL 8.0, ánh xạ ra máy chủ ở cổng 3307 và chạy nội bộ ở cổng 3306. 

Tất cả các thành phần này cùng hoạt động trong một mạng ảo bridge cô lập mang tên `soa-network`, cho phép các container tự động nhận diện địa chỉ của nhau thông qua cơ chế Docker Embedded DNS.

---

### SLIDE 4: THIẾT KẾ CƠ SỞ DỮ LIỆU (Slide 38)
Bước sang Slide thứ 4, về mặt lưu trữ dữ liệu, chúng em áp dụng triệt để mô hình Database-per-Service, nghĩa là mỗi dịch vụ sở hữu riêng một cơ sở dữ liệu độc lập:

Dịch vụ sinh viên sở hữu `sinhvien_db` với bảng `SINHVIEN` chứa các thông tin cá nhân.
Dịch vụ đề tài sở hữu `detai_db` với bảng `DETAI` chứa tên đề tài, mô tả và giảng viên hướng dẫn.
Dịch vụ đăng ký sở hữu `dangky_db` với bảng `DANGKY` lưu thông tin mã sinh viên, mã đề tài và thời gian đăng ký.

Điểm quan trọng nhất ở đây là: Giữa ba cơ sở dữ liệu này hoàn toàn không có bất kỳ khóa ngoại vật lý nào trong MySQL. Bảng `DANGKY` chỉ lưu hai trường `ma_sv` và `ma_detai` dưới dạng chuỗi để liên kết mềm về mặt nghiệp vụ. 

Việc triệt tiêu khóa ngoại vật lý liên-dịch vụ giúp đảm bảo tính tự chủ tuyệt đối cho từng cơ sở dữ liệu, tránh tình trạng khóa nghẽn phân tán và giúp mỗi dịch vụ có thể nâng cấp độc lập mà không ảnh hưởng lẫn nhau. Tính toàn vẹn dữ liệu được đảm bảo hoàn toàn ở tầng logic ứng dụng.

---

### SLIDE 5: LUỒNG XỬ LÝ ĐĂNG KÝ ĐỒ ÁN (Slide 39)
Đến với Slide thứ 5, đây là quy trình nghiệp vụ cốt lõi nhất của hệ thống: Luồng xử lý Đăng ký Đồ án gồm 7 bước tuần tự:

Bước 1: Client gửi yêu cầu `POST /api/v1/dangky` kèm mã sinh viên và mã đề tài.
Bước 2: API Gateway tiếp nhận tại cổng 8080, nhận diện đường dẫn và chuyển tiếp yêu cầu đến `dangky-service` tại cổng 8083.
Bước 3: `dangky-service` sử dụng OpenFeign Client gọi sang `sinhvien-service` tại cổng 8081 để kiểm tra xem sinh viên có tồn tại trong hệ thống hay không.
Bước 4: `dangky-service` tiếp tục gọi sang `detai-service` tại cổng 8082 để xác thực xem đề tài có hợp lệ và còn trống để đăng ký hay không.
Bước 5: Sau khi cả hai dịch vụ phản hồi kết quả hợp lệ, `dangky-service` mới tiến hành ghi bản ghi đăng ký vào cơ sở dữ liệu `dangky_db`.
Bước 6: Dịch vụ hoàn tất giao dịch và đóng gói dữ liệu phản hồi.
Bước 7: `dangky-service` trả về mã HTTP 201 Created cùng thông tin phiếu đăng ký qua Gateway về lại cho người dùng.

Đặc biệt, hệ thống đã cài đặt bộ xử lý lỗi tập trung: Nếu mã sinh viên hoặc mã đề tài không tồn tại, dịch vụ sẽ bắt lỗi và trả về mã HTTP 400 Bad Request kèm thông báo rõ ràng cho người dùng, còn nếu một dịch vụ bên ngoài bị mất kết nối thì sẽ trả về mã 502 Bad Gateway để bảo vệ hệ thống.

---

### SLIDE 6: CỔNG ĐIỀU PHỐI TẬP TRUNG (Slide 40)
Chuyển sang Slide thứ 6, chúng em xin làm rõ vai trò thiết yếu của Cổng điều phối tập trung Spring Cloud Gateway thông qua việc so sánh hai trường hợp:

Trường hợp thứ nhất, khi hệ thống thiếu Gateway: Ứng dụng Frontend bên ngoài phải ghi nhớ và kết nối trực tiếp đến từng cổng 8081, 8082, 8083. Điều này làm lộ toàn bộ cấu trúc mạng nội bộ ra bên ngoài, đồng thời gây ra lỗi CORS ở khắp mọi nơi do Frontend và các dịch vụ khác nhau về cổng truy cập.

Trường hợp thứ hai, khi triển khai Spring Cloud Gateway tại cổng 8080: Gateway đóng vai trò là điểm tiếp nhận duy nhất cho toàn bộ hệ thống. Gateway hoạt động như một Reverse Proxy định tuyến thông minh theo các tiền tố đường dẫn, đồng thời che giấu hoàn toàn các dịch vụ nội bộ phía sau. 

Đặc biệt nhất, toàn bộ cấu hình CORS đã được giải quyết triệt để tại một nơi duy nhất trên Gateway, cho phép mọi nguồn gốc và các phương thức HTTP chuẩn, giúp các dịch vụ nghiệp vụ phía sau không cần phải bận tâm về vấn đề chia sẻ tài nguyên nguồn gốc chéo nữa.

---

### SLIDE 7: TRIỂN KHAI CONTAINER HÓA VỚI DOCKER COMPOSE (Slide 41)
Cuối cùng, ở Slide thứ 7 là quy trình đóng gói và triển khai tự động toàn bộ hệ thống bằng Docker Compose.

Toàn bộ 5 container được khởi chạy đồng bộ chỉ bằng một câu lệnh duy nhất: `docker-compose up --build -d`.

Trong quá trình này, chúng em đã xử lý ba vấn đề kỹ thuật quan trọng nhất:

Vấn đề thứ nhất là thứ tự khởi động giữa Database và Ứng dụng: MySQL thường cần 20 đến 30 giây để khởi tạo, trong khi Spring Boot chỉ mất vài giây. Nếu Spring Boot chạy trước sẽ bị lỗi mất kết nối CSDL và bị crash. Chúng em đã cấu hình cơ chế `healthcheck` với lệnh `mysqladmin ping` trên MySQL, đồng thời thiết lập điều kiện `condition: service_healthy` trên các ứng dụng Spring Boot. Nhờ đó, Spring Boot sẽ kiên nhẫn chờ cho đến khi MySQL thực sự sẵn sàng mới bắt đầu khởi động.

Vấn đề thứ hai là cơ chế phân giải tên miền nội bộ: Trong mạng ảo `soa-network`, các dịch vụ không cần dùng địa chỉ IP tĩnh mà gọi nhau trực tiếp bằng tên định danh như `sinhvien-service`, `detai-service` hay `mysql` thông qua cơ chế Docker Embedded DNS.

Vấn đề thứ ba là tự động hóa dữ liệu và lưu trữ bền vững: File `init.sql` được mount vào container để tự động tạo sẵn ba cơ sở dữ liệu ngay từ đầu, và Docker Volume `mysql-data` đảm bảo dữ liệu không bị mất khi khởi động lại hệ thống.

---

### LỜI KẾT
Kính thưa quý thầy cô!

Qua việc hiện thực hóa Đồ án Quản lý Đồ án Tốt nghiệp theo kiến trúc SOA, hệ thống đã chứng minh được tính độc lập cao giữa các dịch vụ, khả năng định tuyến an toàn qua API Gateway, quy trình giao tiếp chặt chẽ qua OpenFeign, và khả năng vận hành tự động, bền bỉ bằng Docker Compose.

Em xin chân thành cảm ơn quý thầy cô và các bạn đã chú ý lắng nghe! Sau đây em xin phép được lắng nghe những câu hỏi nhận xét và góp ý của quý thầy cô ạ!"
