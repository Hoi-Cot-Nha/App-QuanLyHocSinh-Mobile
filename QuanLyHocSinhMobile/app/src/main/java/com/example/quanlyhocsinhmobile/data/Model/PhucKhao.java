package com.example.quanlyhocsinhmobile.data.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "PhucKhao",
        foreignKeys = {
                @ForeignKey(entity = HocSinh.class, parentColumns = "MaHS", childColumns = "maHS", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = MonHoc.class, parentColumns = "MaMH", childColumns = "maMH", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        },
        indices = {@Index("maHS"), @Index("maMH")})
public class PhucKhao {
    @PrimaryKey(autoGenerate = true)
    private int maPK;
    private String maHS;
    private String maMH;
    private String lyDo;
    private String ngayGui;
    private String trangThai;

    public PhucKhao() {}

    public int getMaPK() { return maPK; }
    public void setMaPK(int maPK) { this.maPK = maPK; }

    public String getMaHS() { return maHS; }
    public void setMaHS(String maHS) { this.maHS = maHS; }

    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public String getNgayGui() { return ngayGui; }
    public void setNgayGui(String ngayGui) { this.ngayGui = ngayGui; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
