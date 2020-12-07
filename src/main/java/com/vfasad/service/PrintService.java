package com.vfasad.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PrintService {
    @Autowired
    private OrderService orderService;


    public byte[] generateInProgressXlsx() {
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\Mirrada\\Desktop\\Report.xlsx");//take a template
            XSSFWorkbook workbook = new XSSFWorkbook(file);//created the object of xlsx library

            Sheet sheet = workbook.getSheetAt(0);//retrieve the first sheet
            Row row = sheet.getRow(10);
            Cell cell_A11 = row.getCell(0);
            cell_A11.setCellValue(777);

            Row rowDate = sheet.getRow(1);
            Cell cellDate = rowDate.getCell(6);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            cellDate.setCellValue(format.format(new Date()));

            ByteArrayOutputStream report = new ByteArrayOutputStream();
            workbook.write(report);
            byte[] reportArray = report.toByteArray();

            workbook.close();//
            file.close();

            report.close();
            return reportArray;

        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException();
        }
    }
}


