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
