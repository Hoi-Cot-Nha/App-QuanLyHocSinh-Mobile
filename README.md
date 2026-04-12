# App-QuanLyHocSinh-Mobile
Đây là bài kiểm tra kết thúc học phần môn Lập trình di động nhóm 11 lớp 74DCHT22


Mô hình MVVM
com.example.quanlyhocsinhmobile
├── data
│   ├── local (Dùng cho Room Database)
│   │   ├── AppDatabase.java
│   │   ├── dao (Chứa các interface DAO)
│   │   └── entities (Các class Model hiện tại của bạn: Diem, HocSinh...)
│   └── repository (Lớp trung gian lấy dữ liệu từ DAO/API)
│       ├── DiemRepository.java
│       └── HocPhiRepository.java
├── ui (Chứa các Activity/Fragment và ViewModel)
│   ├── diem
│   │   ├── DiemActivity.java (Thay cho DiemUI)
│   │   ├── DiemViewModel.java (Xử lý logic cho màn hình điểm)
│   │   └── DiemAdapter.java
│   ├── hanhkiem
│   │   ├── HanhKiemActivity.java
│   │   └── HanhKiemViewModel.java
│   └── main
│       └── MainActivity.java
└── utils (Các class dùng chung như định dạng ngày tháng, chuỗi...)
