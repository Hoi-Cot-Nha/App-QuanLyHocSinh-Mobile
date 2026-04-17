package com.example.quanlyhocsinhmobile.utils;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
public class ExcelHelper {
    public static <T> void exportToExcel(Context context, String fileNamePrefix, String sheetName, 
                                         String[] headers, List<T> data, BiConsumer<Row, T> rowMapper) {
        if (data == null || data.isEmpty()) {
            Toast.makeText(context, "Danh sách trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            rowMapper.accept(row, data.get(i));
        }
        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadDir.exists()) downloadDir.mkdirs();
            String fileName = fileNamePrefix + "_" + System.currentTimeMillis() + ".xlsx";
            File file = new File(downloadDir, fileName);
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            Toast.makeText(context, "Đã lưu tại Download: " + fileName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Lỗi khi xuất Excel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}