package com.vfasad.service;

import com.vfasad.entity.Order;
import com.vfasad.entity.OrderConsume;
import com.vfasad.entity.OrderStatus;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PrintService {
    final int FIRST_TABLE_ROW = 5;

    @Value("classpath:templates/xlsx/ReportInProgress.xlsx")
    private File xlsxReportTemplate;

    @Autowired
    private OrderService orderService;

    public byte[] generateInProgressXlsx() {

        try {
            FileInputStream file = new FileInputStream(xlsxReportTemplate);
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            Sheet sheet = workbook.getSheetAt(0);

            Row rowDate = sheet.getRow(1);
            Cell cellDate = rowDate.getCell(6);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            cellDate.setCellValue(format.format(new Date()));

            List<Order> list = orderService.findByStatus(OrderStatus.IN_PROGRESS);

            int i = FIRST_TABLE_ROW;
            for (Order order : list) {
                Cell cell0 = sheet.getRow(i).getCell(0);
                cell0.setCellValue(order.getId());
                Cell cell1 = sheet.getRow(i).getCell(1);
                cell1.setCellValue(order.getDocument());
                Cell cell2 = sheet.getRow(i).getCell(2);
                cell2.setCellValue(order.getArea());
                for (OrderConsume orderConsume : order.getConsumes()) {
                    Cell cell3 = sheet.getRow(i).getCell(3);
                    cell3.setCellValue(orderConsume.getProduct().getName());
                    Cell cell4 = sheet.getRow(i).getCell(4);
                    cell4.setCellValue(orderConsume.getProduct().getProducer());
                    Cell cell5 = sheet.getRow(i).getCell(5);
                    cell5.setCellValue(orderConsume.getCalculatedQuantity());
                    i++;
                }
            }

            ByteArrayOutputStream reportInProgress = new ByteArrayOutputStream();
            workbook.write(reportInProgress);
            byte[] reportArray = reportInProgress.toByteArray();

            workbook.close();//
            file.close();

            reportInProgress.close();
            return reportArray;

        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException();
        }
    }
}

