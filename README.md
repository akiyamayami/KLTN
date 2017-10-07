# Tiểu Luận Chuyên Ngành - Website Quản lý điều xe

# Phần mềm & môi trường cần thiết:
- Eclipse Neon.3
- Tomcat > 8.5
- Mysql workbench 6.3(có thể có hoặc không)
- Mysql 5.7.18
- Maven 3.8
- Java 1.8
# Cài đặt & cấu hình:
- Tải project
    ```sh
    $ git clone https://github.com/akiyamayami/TLCN.git  
    ```
- Import project vào eclipse (import Existing Maven Projects).
- Nhấp chuột phải vào project trong Elicpse > Maven > Update Project.
- Mở file TLCN/src/main/resources/application.properties và đổi username và password theo mysql trên máy.
    >spring.datasource.username=xxx
    >\
    >spring.datasource.password=yyy
-  Bỏ dấu # ở #spring.jpa.properties.hibernate.hbm2ddl.auto= và điền giá trị create(để tự tạo databases và thêm lại dấu # ở các lần chạy tiếp theo)
-	Nhấp chuột phải vào project > Run as > Spring boot App
