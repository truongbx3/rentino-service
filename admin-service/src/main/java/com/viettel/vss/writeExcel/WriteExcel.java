package com.viettel.vss.writeExcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import java.util.Date;
import java.util.Objects;

public class WriteExcel {
    public static void setCellData(Object data, Row row, int cellIndex, CellStyle cellStyle) {
        Cell cell = row.getCell(cellIndex);
        if (Objects.isNull(cell)) {
            cell = row.createCell(cellIndex);
        }
        cell.setCellStyle(cellStyle);

        if (data instanceof Boolean)
            cell.setCellValue((Boolean) data);
        else if (data instanceof Double)
            cell.setCellValue((double) data);
        else if (data instanceof Float)
            cell.setCellValue((float) data);
        else if (data instanceof Long)
            cell.setCellValue((long) data);
        else if (data instanceof Integer)
            cell.setCellValue((int) data);
        else if (data instanceof Date) {
            cell.setCellValue((Date) data);
        } else cell.setCellValue((String) data);
    }

}
