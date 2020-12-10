package com.vfasad.service;

import com.vfasad.entity.Order;
import com.vfasad.entity.OrderConsume;
import com.vfasad.entity.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PrintService {
    private final static int FIRST_TABLE_ROW = 5;

    @Value("${PRINT_INPROGRESS_TEMPLATE_PATH}")
    private File xlsxReportTemplate;

    @Autowired
    private OrderService orderService;

    public byte[] generateInProgressXlsx() {
        try (XSSFWorkbook workbook = new XSSFWorkbook(xlsxReportTemplate);
             ByteArrayOutputStream reportInProgress = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);
            Row rowDate = sheet.getRow(1);
            Cell cellDate = rowDate.getCell(6);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            cellDate.setCellValue(format.format(new Date()));

            List<Order> list = orderService.findByStatus(OrderStatus.IN_PROGRESS);

            int currentRowNumber = FIRST_TABLE_ROW;
            for (Order order : list) {
                Row row = sheet.getRow(currentRowNumber);
                row.getCell(0).setCellValue(order.getId());
                row.getCell(1).setCellValue(order.getDocument());
                row.getCell(2).setCellValue(order.getArea());
                for (OrderConsume orderConsume : order.getConsumes()) {
                    row = sheet.getRow(currentRowNumber);
                    row.getCell(3).setCellValue(orderConsume.getProduct().getName());
                    row.getCell(4).setCellValue(orderConsume.getProduct().getProducer());
                    row.getCell(5).setCellValue(orderConsume.getCalculatedQuantity());
                    currentRowNumber++;
                }
            }

            workbook.write(reportInProgress);
            return reportInProgress.toByteArray();
        } catch (IOException | InvalidFormatException e) {
            log.error("Error during xlsx report generating.");
            throw new RuntimeException("Error during xlsx report generating.", e);
        }
    }
}

