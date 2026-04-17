package com.example.quanlyhocsinhmobile.data.local.Model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
@Entity(tableName = "monhoc")
public class MonHoc {
    @PrimaryKey
    @NonNull
    @androidx.room.ColumnInfo(name = "MaMH")
    private String maMH;
    @androidx.room.ColumnInfo(name = "TenMH")
    private String tenMH;
    public MonHoc() {}
    @Ignore
    public MonHoc(String maMH, String tenMH) {
        this.maMH = maMH;
        this.tenMH = tenMH;
    }
    public String getMaMH() {
        return maMH;
    }
    public String getTenMH() {
        return tenMH;
    }
    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }
    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }
    @Override
    public String toString() {
        return maMH + " - " + (tenMH != null ? tenMH : "");
    }
}