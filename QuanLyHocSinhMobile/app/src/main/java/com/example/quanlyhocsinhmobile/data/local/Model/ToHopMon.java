package com.example.quanlyhocsinhmobile.data.local.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ToHopMon")
public class ToHopMon {
    @PrimaryKey
    @NonNull
    private String maToHop;
    @NonNull
    private String tenToHop;

    @Ignore
    public ToHopMon(@NonNull String maToHop, @NonNull String tenToHop) {
        this.maToHop = maToHop;
        this.tenToHop = tenToHop;
    }
    public ToHopMon() {}

    @NonNull
    public String getMaToHop() { return maToHop; }
    public void setMaToHop(@NonNull String maToHop) { this.maToHop = maToHop; }

    @NonNull
    public String getTenToHop() { return tenToHop; }
    public void setTenToHop(@NonNull String tenToHop) { this.tenToHop = tenToHop; }

    @Override
    public String toString() {
        // Hien thi day du ma + ten to hop trong combobox
        return maToHop + " - " + tenToHop;
    }
}
