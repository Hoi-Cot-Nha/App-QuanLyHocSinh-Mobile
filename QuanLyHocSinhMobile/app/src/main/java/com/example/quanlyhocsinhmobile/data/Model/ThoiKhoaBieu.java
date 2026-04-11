package com.example.quanlyhocsinhmobile.data.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "ThoiKhoaBieu",
        foreignKeys = {
                @ForeignKey(entity = Lop.class, parentColumns = "MaLop", childColumns = "maLop", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = MonHoc.class, parentColumns = "MaMH", childColumns = "maMH", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = GiaoVien.class, parentColumns = "maGV", childColumns = "maGV"),
                @ForeignKey(entity = PhongHoc.class, parentColumns = "maPhong", childColumns = "maPhong")
        },
        indices = {@Index("maLop"), @Index("maMH"), @Index("maGV"), @Index("maPhong")})
public class ThoiKhoaBieu {
    @PrimaryKey(autoGenerate = true)
    private int maTKB;
    private String maLop;
    private String maMH;
    private String maGV;
    private String maPhong;
    private int thu;
    private int tietBatDau;
    private int tietKetThuc;

    public ThoiKhoaBieu() {}

    public int getMaTKB() { return maTKB; }
    public void setMaTKB(int maTKB) { this.maTKB = maTKB; }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public int getThu() { return thu; }
    public void setThu(int thu) { this.thu = thu; }

    public int getTietBatDau() { return tietBatDau; }
    public void setTietBatDau(int tietBatDau) { this.tietBatDau = tietBatDau; }

    public int getTietKetThuc() { return tietKetThuc; }
    public void setTietKetThuc(int tietKetThuc) { this.tietKetThuc = tietKetThuc; }
}
