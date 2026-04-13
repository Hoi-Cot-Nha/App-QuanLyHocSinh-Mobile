# App-QuanLyHocSinh-Mobile
Đây là bài kiểm tra kết thúc học phần môn Lập trình di động nhóm 11 lớp 74DCHT22


## Cấu trúc thư mục (MVVM Architecture)

```text
com.example.quanlyhocsinhmobile
├── data
│   ├── local (Room Database)
│   │   ├── AppDatabase.java
│   │   ├── dao (Interface DAO)
│   │   └── entities (Diem, HocSinh,...)
│   └── repository (Lớp trung gian lấy dữ liệu)
│       ├── DiemRepository.java
│       └── HocPhiRepository.java
├── ui (Activity/Fragment & ViewModel)
│   ├── diem
│   │   ├── DiemActivity.java
│   │   ├── DiemViewModel.java
│   │   └── DiemAdapter.java
│   ├── hanhkiem
│   │   ├── HanhKiemActivity.java
│   │   └── HanhKiemViewModel.java
│   └── main
│       └── MainActivity.java
└── utils (Helper classes)

```

# Dự án Bài tập lớn: Quản lý học sinh (Android)

Dự án được xây dựng trên nền tảng Android sử dụng ngôn ngữ Java/Kotlin, áp dụng kiến trúc
**MVVM (Model-View-ViewModel)** kết hợp với **Repository Pattern** để đảm bảo tính đóng gói, dễ bảo trì và mở rộng.

## 🏗 Kiến trúc dự án (Architecture)

Dự án tuân thủ nghiêm ngặt mô hình MVVM, chia tách ứng dụng thành 4 tầng xử lý dữ liệu biệt lập:



### 1. Model (Data Layer)
* **Vị trí:** `data.local`
* **Entities:** Định nghĩa cấu trúc các bảng trong cơ sở dữ liệu (ví dụ: `HocSinh`, `Diem`, `Lop`).
* **DAO (Data Access Object):** Cung cấp các phương thức truy vấn dữ liệu trực tiếp từ SQLite/Room (Insert, Update, Delete, Query).
* **Database:** Quản lý phiên bản và khởi tạo kết nối cơ sở dữ liệu.

### 2. Repository Layer
* **Vị trí:** `repository/`
* **Chức năng:** Đóng vai trò là "nguồn dữ liệu duy nhất" (Single Source of Truth). Repository điều phối dữ liệu từ Local Database (hoặc API trong tương lai) và cung cấp cho ViewModel. Điều này giúp tách biệt hoàn toàn Logic nghiệp vụ khỏi Logic dữ liệu.

### 3. ViewModel Layer
* **Vị trí:** Nằm trong các package UI cá nhân (ví dụ: `letrang`, `tien`).
* **Chức năng:** Lưu trữ và quản lý dữ liệu liên quan đến UI theo cách có nhận thức về vòng đời (Lifecycle-aware). ViewModel sử dụng `LiveData` để thông báo cho View mỗi khi dữ liệu thay đổi.

### 4. View Layer (UI Layer)
* **Vị trí:** `ui/` và `res/layout/`
* **Thành phần:** Gồm `Activity`, `Fragment` và các `Adapter`.
* **Chức năng:** Chỉ tập trung vào việc hiển thị dữ liệu và tiếp nhận tương tác từ người dùng. View sẽ "quan sát" (Observe) dữ liệu từ ViewModel để tự động cập nhật giao diện.

## 🔄 Luồng dữ liệu (Workflow)

1.  **User Interaction:** Người dùng thao tác trên `Activity` (View).
2.  **Request:** View yêu cầu dữ liệu từ `ViewModel`.
3.  **Data Fetching:** `ViewModel` gọi hàm tương ứng trong `Repository`.
4.  **Database Query:** `Repository` lấy dữ liệu từ `DAO`.
5.  **Reactive Update:** Dữ liệu trả về `Repository` -> `ViewModel` -> Cập nhật `LiveData`.
6.  **UI Refresh:** `Activity` nhận thấy `LiveData` thay đổi và yêu cầu `Adapter` vẽ lại danh sách trên màn hình.

---

## 👥 Thành viên thực hiện (Team)

Dự án được thực hiện bởi nhóm 5 thành viên, mỗi thành viên phụ trách các module độc lập theo cấu trúc MVVM để tránh xung đột mã nguồn:

* **Trần Văn Tiến (Trưởng nhóm):** Quản lý cấu trúc chung, Module Điểm, Hạnh kiểm, Lịch thi, Thiết kế Database, Xuất file Excel, Tạo Menu form, System Icons.
* **Lê Thu Trang:** Phụ trách Module Môn học, Phòng học, Thời khóa biểu, Chức năng Đăng xuất/Thoát, Phân quyền Student/Teacher.
* **Nguyễn Thị Hà Trang:** Học phí, Thông báo, Phúc khảo, Thiết kế UI/UX, Theme chủ đề màu sắc cho toàn bộ App.
* **Doãn Trung Đạt:** Phát triển Module Quản lý học sinh/giáo viên, Giao diện đăng nhập tài khoản..
* **Nguyễn Đắc Đại:** Học sinh, Đối tượng chính sách, Tài khoản, Xử lý Avatar cho hồ sơ, quản lý chi tiết tài khoản.
---

## 🛠 Công nghệ & Thư viện sử dụng

* **Ngôn ngữ:** Java/Kotlin
* **Database:** [Room Persistence Library](https://developer.android.com/training/data-storage/room) (SQLite) - Quản lý dữ liệu cục bộ.
* **Architecture Components:** LiveData, ViewModel, Lifecycle.
* **UI:** Material Design Components, RecyclerView, ConstraintLayout, ViewBinding.
* **Utilities:** Apache POI (Hỗ trợ xuất/nhập dữ liệu Excel).

---
🚀 Hướng dẫn cài đặt
* Clone dự án: git clone [URL_Github_Cua_Nhom]

* Mở dự án bằng Android Studio (phiên bản Ladybug hoặc mới hơn).

* Sync Gradle và Run trên máy ảo hoặc thiết bị thật (API 24+).
