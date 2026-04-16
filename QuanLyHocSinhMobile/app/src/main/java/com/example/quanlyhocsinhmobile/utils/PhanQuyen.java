package com.example.quanlyhocsinhmobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PhanQuyen {
    private static final String tenfilepref = "QuanLyHocSinhPrefs";
    private static final String khoatendn = "ten_dang_nhap";
    private static final String khoaquyen = "quyen";
    private static final String khoamanguoidung = "ma_nguoi_dung";
    private static final String khoadadanhnhap = "da_dang_nhap";

    private static PhanQuyen instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private PhanQuyen(Context context) {
        sharedPreferences = context.getSharedPreferences(tenfilepref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized PhanQuyen getInstance(Context context) {
        if (instance == null) {
            instance = new PhanQuyen(context.getApplicationContext());
        }
        return instance;
    }

    public void luuPhienDangNhap(String tenDangNhap, String quyen, String maNguoiDung) {
        editor.putString(khoatendn, tenDangNhap);
        editor.putString(khoaquyen, quyen);
        editor.putString(khoamanguoidung, maNguoiDung);
        editor.putBoolean(khoadadanhnhap, true);
        editor.apply();
    }

    public String getTenDangNhap() {
        return sharedPreferences.getString(khoatendn, "");
    }

    public String getQuyen() {
        return sharedPreferences.getString(khoaquyen, "");
    }

    public String getMaNguoiDung() {
        return sharedPreferences.getString(khoamanguoidung, "");
    }

    public boolean daDangNhap() {
        return sharedPreferences.getBoolean(khoadadanhnhap, false);
    }

    public void dangXuat() {
        editor.clear();
        editor.apply();
    }
}
