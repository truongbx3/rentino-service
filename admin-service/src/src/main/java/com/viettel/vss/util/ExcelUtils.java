package com.viettel.vss.util;


import com.google.common.base.Strings;
import com.viettel.vss.constant.CommonConstants;
import com.viettel.vss.exception.BusinessException;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class ExcelUtils {

    private ExcelUtils() {
    }

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ExcelUtils.class);

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    private static final String FIELD_IS_ERROR = "isError";

    protected static final String[] FILE_EXCEL = {"xls", "xlsx", "xlsm"};

    public static final String NEW_LINE = "\n";

    /**
     * Check cell is null
     *
     * @return true if null
     */
    public static boolean isNullOrBlankCell(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK || cell.getCellType() == CellType._NONE;
    }

    public static void setDefaultStyle(CellStyle cellStyle) {
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setWrapText(true);
    }

    public static void setStyleHeader(CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public static CellStyle createNormalCell(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        setDefaultStyle(cellStyle);
        return cellStyle;
    }

    public static CellStyle createErrorCell(Workbook workbook) {
        CellStyle cellStyle = createNormalCell(workbook);
        var font = workbook.createFont();
        font.setColor(Font.COLOR_RED);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static CellStyle makeErrorHeaderCellStyle(Workbook workbook) {
        var cellStyle = workbook.createCellStyle();
        var font = workbook.createFont();
        font.setColor(Font.COLOR_RED);
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    public static CellStyle createCellHeader(Workbook workbook) {
        CellStyle cellStyleHeader = workbook.createCellStyle();
        setDefaultStyle(cellStyleHeader);
        setStyleHeader(cellStyleHeader);
        return cellStyleHeader;
    }

    public static List<String> getDataSpecificRow(MultipartFile file, int startRow, Integer startColumn) {
        checkFileExcel(file);
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            List<String> lstHeader = new ArrayList<>();
            Row row = xssfSheet.getRow(startRow);
            int lastIndexCol = row.getLastCellNum();
            startColumn = startColumn == null ? row.getFirstCellNum() : startColumn;
            for (int i = startColumn; i < lastIndexCol; i++) {
                if (isNullOrBlankCell(row.getCell(i))) break;
                lstHeader.add(row.getCell(i).getStringCellValue());
            }
            return lstHeader;
        } catch (Exception e) {
            throw new BusinessException("EXCEL_READ_FAILED", e.getMessage());
        }
    }

    public static Object getDataCell(Cell cell) {
        if (isNullOrBlankCell(cell)) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }

    public static void checkFileExcel(MultipartFile file) {
        if (checkFileExcel(file.getOriginalFilename()))
            throw new BusinessException("WRONG_EXTENSION");
    }

    @SneakyThrows
    public static boolean checkFileExcel(String fileName) {
        return Arrays.stream(FILE_EXCEL).noneMatch(x -> x.equals(FileNameUtils.getExtension(fileName)));
    }


    public static List<String> checkListFile(MultipartFile[] files) {
        var result = new ArrayList<String>();
        String fileName;
        for (MultipartFile file : files) {
            fileName = file.getOriginalFilename();
            try {
                checkFileExcel(file);
            } catch (IllegalArgumentException e) {
                result.add(String.format("File %s is not Excel file!", fileName));
            } catch (Exception e) {
                result.add(String.format("Error when check file %s!", fileName));
                break;
            }
        }
        return result;
    }

    private static List<CellType> getDefaultFormatTypeColumn(Row row, Integer startColumn) {
        int lastRowCell = row.getLastCellNum();
        int start = startColumn == null ? 0 : startColumn;
        var result = new ArrayList<CellType>();
        for (int i = start; i < lastRowCell; i++) {
            var currentCell = row.getCell(i);
            if (currentCell == null) {
                result.add(CellType.BLANK);
                continue;
            }
            result.add(currentCell.getCellType());
        }
        return result;
    }

    private static List<String> getRawData(Row row) {
        List<String> rawData = new ArrayList<>();
        int lastRowCell = row.getLastCellNum();
        DataFormatter formatter = new DataFormatter();
        for (int cn = 0; cn < lastRowCell; cn++) {
            rawData.add(DataUtil.safeToString(formatter.formatCellValue(row.getCell(cn)), "").trim());
        }
        return rawData;
    }

    private static Map<String, Object> getRawData(List<String> headers, Row row) {
        Map<String, Object> rawData = new LinkedHashMap<>();
        int lastRowCell = headers.size();
        DataFormatter formatter = new DataFormatter();
        for (int cn = 0; cn < lastRowCell; cn++) {
            rawData.put(headers.get(cn), DataUtil.safeToString(formatter.formatCellValue(row.getCell(cn)), "").trim());
        }
        return rawData;
    }

    public static boolean checkTemplateFile(InputStream[] files, Integer sheetIndex, int headerRow,
                                            Map<Integer, String> checkMaps) {
        for (InputStream file : files) {
            try {
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file);
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetIndex);
                Row row = xssfSheet.getRow(headerRow);
                if (row.getPhysicalNumberOfCells() != checkMaps.size()) {
                    return false;
                }
                DataFormatter formatter = new DataFormatter();
                AtomicBoolean check = new AtomicBoolean(true);
                checkMaps.forEach((pos, valueMatch) -> {
                    String lookUpField = DataUtil.safeToString(formatter.formatCellValue(row.getCell(pos)), "").trim();
                    if (!valueMatch.equals(lookUpField)) {
                        check.set(false);
                    }
                });
                return check.get();
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        }

        return false;
    }

    public static List<String> getDataDynamicColumn(XSSFSheet xssfSheet, Integer startColumn) {
        List<String> resultData = new ArrayList<>();
        Row row = xssfSheet.getRow(0);
        startColumn = startColumn == null ? row.getFirstCellNum() : startColumn;

        for (int i = startColumn; i < row.getLastCellNum(); i++) {
            if (isNullOrBlankCell(row.getCell(i))) break;
            resultData.add(row.getCell(i).getStringCellValue());
        }
        return resultData;
    }

    public static ByteArrayOutputStream workbookToByteArray(XSSFWorkbook xssfWorkbook) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            xssfWorkbook.write(byteArrayOutputStream);
            return byteArrayOutputStream;
        } catch (IOException e) {
            return null;
        }
    }

    public static Date convertStringToDate(String dateInString, String dateFormat) {
        try {
            DateFormat formatter = new SimpleDateFormat(dateFormat);
            if (Strings.isNullOrEmpty(dateInString)) {
                return null;
            }
            return formatter.parse(dateInString);
        } catch (ParseException e) {
            return null;
        }
    }


    public static Long getDataLong(String strNumber) {
        try {
            return Long.parseLong(strNumber);
        } catch (Exception e) {
            return null;
        }
    }

    public static void appendError(String value, StringBuilder errorBuilder, String textError) {
        if (StringUtilsJr.isNullOrEmpty(value)) {
            errorBuilder.append(textError);
        }
    }

    public static void mergeSameDataCells(Sheet sheet, int startRow, int columnIndex) {
        int endRow = sheet.getLastRowNum();
        String previousValue = null;
        int mergeStart = startRow;

        for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;
            Cell cell = row.getCell(columnIndex);
            String currentValue = getCellValueAsString(cell);

            boolean isLastRow = (rowIndex == endRow);

            if (previousValue == null || !previousValue.equals(currentValue)) {
                // Merge if more than 1 row has same value
                if (rowIndex - mergeStart > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(mergeStart, rowIndex - 1, columnIndex, columnIndex));
                }
                mergeStart = rowIndex;
            }

            previousValue = currentValue;

            // Handle last row
            if (isLastRow && rowIndex - mergeStart >= 1) {
                sheet.addMergedRegion(new CellRangeAddress(mergeStart, rowIndex, columnIndex, columnIndex));
            }
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return CommonConstants.EMPTY;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return DateUtil.isCellDateFormatted(cell)
                        ? cell.getDateCellValue().toString()
                        : String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return CommonConstants.EMPTY;
        }
    }
}

