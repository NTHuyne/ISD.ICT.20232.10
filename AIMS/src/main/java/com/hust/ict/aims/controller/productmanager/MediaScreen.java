package com.hust.ict.aims.controller.productmanager;

/*
Mục Đích của MediaScreen
- Đa Hình (Polymorphism): MediaScreen cho phép các đối tượng của các lớp khác nhau (như BookScreen và CDScreen)
được xử lý thông qua một giao diện chung.
- Mở Rộng Dễ Dàng: Khi cần thêm một loại media mới, bạn chỉ cần tạo một lớp mới thực hiện MediaScreen mà không cần
sửa đổi code hiện tại.
- Loose Coupling: Hệ thống trở nên ít phụ thuộc vào các lớp cụ thể, giúp dễ dàng bảo trì và mở rộng.
* */

public interface MediaScreen {
    void showScreen();
}
