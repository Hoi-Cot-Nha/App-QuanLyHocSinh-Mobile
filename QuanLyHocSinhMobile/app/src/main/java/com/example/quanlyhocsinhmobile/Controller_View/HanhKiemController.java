package com.example.quanlyhocsinhmobile.Controller_View;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.quanlyhocsinhmobile.data.Connection.AppDatabase;
import com.example.quanlyhocsinhmobile.data.DAO.HanhKiemDAO;
import com.example.quanlyhocsinhmobile.data.DAO.LopDAO;
import com.example.quanlyhocsinhmobile.data.Model.HanhKiem;
import com.example.quanlyhocsinhmobile.data.Model.Lop;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HanhKiemController {
    private final HanhKiemDAO hanhKiemDAO;
    private final LopDAO lopDAO;
    private final Context context;

    public HanhKiemController(Context context) {
        this.context = context;
        AppDatabase db = AppDatabase.getDatabase(context);
        this.hanhKiemDAO = db.hanhKiemDAO();
        this.lopDAO = db.lopDAO();
    }

    public List<Lop> getAllLop() {
        return lopDAO.getAll();
    }

    public List<HanhKiem.Display> getAllHanhKiem() {
        return hanhKiemDAO.getAll();
    }

    public List<HanhKiem.Display> filterHanhKiem(String maLop, int hocKy, String namHoc) {
        return hanhKiemDAO.filterHanhKiem(maLop, hocKy, namHoc);
    }

    public List<HanhKiem.Display> searchHanhKiem(String query) {
        return hanhKiemDAO.searchHanhKiem("%" + query + "%");
    }

    public void updateHanhKiem(HanhKiem hk, String xepLoai, String nhanXet) {
        hk.setXepLoai(xepLoai);
        hk.setNhanXet(nhanXet);
        hanhKiemDAO.update(hk);
        Toast.makeText(context, "Cập nhật hạnh kiểm thành công", Toast.LENGTH_SHORT).show();
    }

    public void exportToExcel(List<HanhKiem.Display> list) {
        if (list == null || list.isEmpty()) {
            Toast.makeText(context, "Danh sách trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("HanhKiem");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã HS");
        headerRow.createCell(1).setCellValue("Họ Tên");
        headerRow.createCell(2).setCellValue("Lớp");
        headerRow.createCell(3).setCellValue("Học Kỳ");
        headerRow.createCell(4).setCellValue("Năm Học");
        headerRow.createCell(5).setCellValue("Xếp Loại");
        headerRow.createCell(6).setCellValue("Nhận Xét");

        for (int i = 0; i < list.size(); i++) {
            HanhKiem.Display display = list.get(i);
            HanhKiem hk = display.getHanhKiem();
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(hk.getMaHS());
            row.createCell(1).setCellValue(display.getTenHS());
            row.createCell(2).setCellValue(display.getTenLop());
            row.createCell(3).setCellValue(hk.getHocKy());
            row.createCell(4).setCellValue(hk.getNamHoc());
            row.createCell(5).setCellValue(hk.getXepLoai());
            row.createCell(6).setCellValue(hk.getNhanXet());
        }

        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "HanhKiem_" + System.currentTimeMillis() + ".xlsx";
            File file = new File(downloadDir, fileName);
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            Toast.makeText(context, "Đã lưu tại Download: " + fileName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Lỗi khi xuất Excel", Toast.LENGTH_SHORT).show();
        }
    }
}
