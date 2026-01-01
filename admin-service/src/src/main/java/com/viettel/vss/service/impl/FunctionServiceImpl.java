package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.constant.TemplateExport;
import com.viettel.vss.dto.function.*;
import com.viettel.vss.dto.module.DeleteModuleResponseDto;
import com.viettel.vss.entity.Function;
import com.viettel.vss.entity.FunctionImportLog;
import com.viettel.vss.entity.RoleGroup;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.FunctionImportLogRepository;
import com.viettel.vss.repository.FunctionRepository;
import com.viettel.vss.repository.RoleGroupRepository;
import com.viettel.vss.service.AttachFileService;
import com.viettel.vss.service.FunctionService;
import com.viettel.vss.service.ReportService;
import com.viettel.vss.util.DataUtil;
import com.viettel.vss.util.ExcelUtils;
import com.viettel.vss.util.MessageCommon;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FunctionServiceImpl extends BaseServiceImpl<Function, FunctionDto> implements FunctionService {

    @Value("${minio.bucketName}")
    private String bucketNameBase;

    @Autowired
    private MessageCommon messageCommon;

    @Autowired
    private ReportService reportService;

    @Autowired
    private AttachFileService attachFileService;

    @Autowired
    private RoleGroupRepository roleGroupRepository;

    @Autowired
    private FunctionImportLogRepository functionImportLogRepository;

    private final FunctionRepository functionRepository;

    public FunctionServiceImpl(FunctionRepository functionRepository) {
        super(functionRepository, Function.class, FunctionDto.class);
        this.functionRepository = functionRepository;
    }

    @Override
    public FunctionDto saveObject(FunctionDto data) {
        if (ObjectUtils.isEmpty(data.getId())) {
            if (functionRepository.findByFunctionCode(data.getFunctionCode()).isPresent()) {
                throw new BusinessException(BusinessExceptionCode.FUNCTION_EXISTS,
                        messageCommon.getMessage(BusinessExceptionCode.FUNCTION_EXISTS));
            }
        } else {
            Integer exist = functionRepository.countFunction(data.getId());
            if (exist > 0 && !Boolean.TRUE.equals(data.getIsActive())) {
                throw new BusinessException(BusinessExceptionCode.FUNCTION_IN_USED,
                        messageCommon.getMessage(BusinessExceptionCode.FUNCTION_IN_USED));
            }

            Function function = functionRepository.findByIdAndIsDeleted(data.getId(), 0);
            if (ObjectUtils.isEmpty(function)) {
                throw new BusinessException(BusinessExceptionCode.FUNCTION_IN_USED_UPDATED,
                        messageCommon.getMessage(BusinessExceptionCode.FUNCTION_IN_USED_UPDATED));
            }

            data.setCreatedDate(function.getCreatedDate());
            data.setCreatedBy(function.getCreatedBy());
        }

        FunctionDto savedFUnction = super.saveObject(data);
        return modelMapper.map(functionRepository.findByIdAndIsDeleted(savedFUnction.getId(), 0), FunctionDto.class);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        List<Function> listFunctions = functionRepository.findAllByIdInAndIsDeleted(ids, 0);
        if (CollectionUtils.isEmpty(listFunctions)) {
            throw new BusinessException(BusinessExceptionCode.FUNCTION_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.FUNCTION_NOT_FOUND));
        }

        for (Function function : listFunctions) {
            Integer exist = functionRepository.countFunction(function.getId());
            if (exist > 0) {
                throw new BusinessException(BusinessExceptionCode.FUNCTION_IN_USED,
                        messageCommon.getMessage(BusinessExceptionCode.FUNCTION_IN_USED));
            }
        }
        super.deleteByIds(ids);
    }

    @Override
    public ByteArrayInputStream export(FilterFunctionRequest filterFunctionRequest) throws IOException {
        Sort.Direction direction = Sort.Direction.fromString(filterFunctionRequest.getSortDirection());
        Pageable pageable = PageRequest.of(filterFunctionRequest.getPage(), filterFunctionRequest.getSize(), Sort.by(direction, filterFunctionRequest.getSortBy()));
        Page<FunctionResponseDto> data = functionRepository.findAllByIsDeleted(filterFunctionRequest, pageable);
        List<Function> listData = new ArrayList<>();
        if (!ObjectUtils.isEmpty(data) && !ObjectUtils.isEmpty(data.getContent())) {
            listData = DataUtil.convertList(data.getContent(), x -> modelMapper.map(x, Function.class));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", listData);
        map.put("exportDate", new Date());
        ByteArrayOutputStream byteArrayOutputStream = reportService.genXlsxLocal(map, TemplateExport.EXPORT_FUNCTION);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public List<FunctionResponseProjection> getListFunctionByListMenuId(List<Long> menuId) {
        if (menuId.isEmpty()) {
            return new ArrayList<>();
        } else if (menuId.size() == 1 && menuId.get(0).equals(0L)) {
            return functionRepository.getAllFunctions();
        }

        return functionRepository.getListFunctionByListMenuId(menuId);
    }

    @Override
    public Page<FunctionResponseDto> getListFunction(FilterFunctionRequest filterFunctionRequest) {
        Sort.Direction direction = Sort.Direction.fromString(filterFunctionRequest.getSortDirection());
        Pageable pageable = PageRequest.of(filterFunctionRequest.getPage(),
                                            filterFunctionRequest.getSize(),
                                            Sort.by(direction, filterFunctionRequest.getSortBy()));
        return functionRepository.findAllByIsDeleted(filterFunctionRequest, pageable);
    }

    @Override
    public DeleteModuleResponseDto restoreFunction(List<Long> id) {
        DeleteModuleResponseDto responseDto = new DeleteModuleResponseDto();
        List<String> error = new ArrayList<>();
        List<Function> listFunctionId = functionRepository.findAllByIdIn(id);
        if (CollectionUtils.isEmpty(listFunctionId)) {
            throw new BusinessException(BusinessExceptionCode.FUNCTION_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.FUNCTION_NOT_FOUND));
        }
        for (Function function : listFunctionId) {
            Optional<Function> exist = functionRepository.findFunctionByFunctionCode(function.getFunctionCode());
            if (exist.isPresent()) {
                error.add(function.getFunctionCode());
                continue;
            }
            function.setIsDeleted(0);
        }
        functionRepository.saveAll(listFunctionId);
        if (error.isEmpty()) {
            responseDto.setIsSuccess(true);
        } else {
            responseDto.setIsSuccess(false);
            responseDto.setError(error);
        }
        return responseDto;
    }

    @Override
    public ImportResponseDto importFunction(MultipartFile file) throws IOException, ParseException {
        String fileName = file.getOriginalFilename();
        if (!"xlsx".equals(FileNameUtils.getExtension(fileName))) {
            throw new BusinessException(BusinessExceptionCode.FILE_EXCEL_EXTENSION_INVALID,
                    messageCommon.getMessage(BusinessExceptionCode.FILE_EXCEL_EXTENSION_INVALID));
        }

        try {
            if (!ExcelUtils.checkTemplateFile(new InputStream[]{file.getInputStream()}, 0, 1, listHeader())) {
                throw new BusinessException(BusinessExceptionCode.FILE_WRONG_TEMPLATE,
                        messageCommon.getMessage(BusinessExceptionCode.FILE_WRONG_TEMPLATE));
            }
        } catch (IOException e) {
            throw new BusinessException("Lỗi khi đọc file excel: " + e.getMessage());
        }


        ImportResponseDto importResponseDto = new ImportResponseDto();
        int error = 0;
        int success = 0;

        List<FunctionDto> dto = importByExcel(file);
        for (FunctionDto t : dto){
            if(!ObjectUtils.isEmpty(t.getError())){
                error++;
            }
        }

        if (error != 0) {
            importResponseDto.setStatus(false);
            String templatePath = TemplateExport.IMPORT_FUNCTION_ERROR;
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath);
            if (in == null) {
                throw new BusinessException("Template file not found: " + templatePath);
            }
            try (XSSFWorkbook workbook = new XSSFWorkbook(in)) {
                fillData(dto, workbook);
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    workbook.write(bos);
                    importResponseDto.setFileError(bos.toByteArray());
                }
            } catch (Exception e) {
                throw new BusinessException("400", "Lỗi khi đọc/ghi dữ liệu: " + e.getMessage());
            }
        } else {
            if (ObjectUtils.isEmpty(dto)) {
                throw new BusinessException("file trống");
            }

            for (FunctionDto t : dto){
                super.saveObject(t);
                success++;
            }

            List<String> nameOfFiles = attachFileService.createAttachFile(file, bucketNameBase, 1);
            FunctionImportLog functionImportLog = new FunctionImportLog();
            functionImportLog.setFileName(nameOfFiles.get(0));
            functionImportLogRepository.save(functionImportLog);
            importResponseDto.setStatus(true);
        }

        importResponseDto.setErrorRecord(error);
        importResponseDto.setSuccessRecord(success);
        importResponseDto.setTotalRecord(dto.size());
        return importResponseDto;
    }

    @Override
    public ByteArrayInputStream exportTemplate() throws IOException {
        Map<String, Object> map = new HashMap<>();
        ByteArrayOutputStream byteArrayOutputStream = reportService.genXlsxLocal(map, TemplateExport.IMPORT_FUNCTION_TEMPLATE);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public List<FunctionResponseDto> getFunctionByRole(List<Long> roleIds) {
        List<Function> list;
        List<RoleGroup> lsRoleGroup = roleGroupRepository.findByListRolesId(roleIds);
        boolean isAdmin = lsRoleGroup.stream().anyMatch(x -> Boolean.TRUE.equals(x.getIsAdmin()));
        if (isAdmin) {
            list = functionRepository.findAllByIsDeletedAndIsActive(0, 1);
        } else {
            list = functionRepository.getFunctionByRole(roleIds);
        }
        return list.stream()
                .map(function -> modelMapper.map(function, FunctionResponseDto.class))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMyRole(String email) {
        return functionRepository.findByUserRole(email);
    }

    private Map<Integer, String> listHeader() {
        Map<Integer, String> checkMaps = new HashMap<>();
        checkMaps.put(0, "Mã chức năng");
        checkMaps.put(1, "Mô tả");
        checkMaps.put(2, "Trạng thái");
        return checkMaps;
    }

    private List<FunctionDto> importByExcel(MultipartFile file) throws IOException, ParseException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        List<FunctionDto> functionDtos = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        XSSFSheet sheet = workbook.getSheetAt(0);
        for(int j = 2; j< sheet.getPhysicalNumberOfRows(); j++){
            XSSFRow row = sheet.getRow(j);
            StringBuilder error = new StringBuilder();
            if (!ObjectUtils.isEmpty(row)){
                if (ObjectUtils.isEmpty(formatter.formatCellValue(row.getCell(0)))
                        && ObjectUtils.isEmpty(formatter.formatCellValue(row.getCell(1)))
                        && ObjectUtils.isEmpty(formatter.formatCellValue(row.getCell(2)))
                ) {
                    continue; // Bỏ qua hàng trống
                }

                FunctionDto functionDto = new FunctionDto();
                if (ObjectUtils.isEmpty(row.getCell(0))) {
                    error.append("Mã chức năng không được để trống. ");
                } else {
                    String functionCode = formatter.formatCellValue(row.getCell(0)).trim();
                    if (functionRepository.findByFunctionCode(functionCode).isPresent()) {
                        functionDto.setFunctionCode(functionCode);
                        error.append("Mã chức năng đã tồn tại. ");
                    } else {
                        functionDto.setFunctionCode(functionCode);
                    }
                }

                if (ObjectUtils.isEmpty(row.getCell(1))) {
                    error.append("Mô tả không được để trống. ");
                } else {
                    String description = formatter.formatCellValue(row.getCell(1)).trim();
                    functionDto.setDescription(description);
                }

                if (ObjectUtils.isEmpty(row.getCell(2))) {
                    error.append("Trạng thái không được để trống. ");
                } else {
                    String status = formatter.formatCellValue(row.getCell(2)).trim();
                    if ("true".equalsIgnoreCase(status) || "false".equalsIgnoreCase(status)) {
                        functionDto.setInputActiveStatus(status);
                        functionDto.setIsActive(Boolean.valueOf(status));
                    } else {
                        functionDto.setInputActiveStatus(status);
                        error.append("Trạng thái không hợp lệ. Phải là 'true' hoặc 'false'. \n");
                    }
                }

                functionDto.setError(error.toString());
                functionDtos.add(functionDto);

            }
        }
        return functionDtos;
    }

    private void fillData(List<FunctionDto> functionDtos, Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        int rowIndex = 2;
        sheet.createRow(rowIndex);

        // Tao cell style
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Times New Roman");
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        writeData(functionDtos, rowIndex, sheet, cellStyle);
    }

    private void writeData(List<FunctionDto> functionDtos, int rowIndex, Sheet sheet, CellStyle cellStyle) {
        for (FunctionDto functionDto : functionDtos) {
            Row row = sheet.createRow(rowIndex);
            setCellData(functionDto.getFunctionCode(), row, 0, cellStyle);
            setCellData(functionDto.getDescription(), row, 1, cellStyle);
            setCellData(functionDto.getInputActiveStatus(), row, 2, cellStyle);

            String error = functionDtos.stream().filter(m -> Objects.equals(m.getFunctionCode(), functionDto.getFunctionCode())).filter(m -> !ObjectUtils.isEmpty(m.getError())).map(FunctionDto::getError).findFirst().orElse(null);
            if (ObjectUtils.isEmpty(error)) {
                setCellData("Thành Công", row, 3, cellStyle);
                setCellData("", row, 4, cellStyle);
            }else{
                setCellData("Lỗi", row, 3, cellStyle);
                setCellData(error, row, 4, cellStyle);
            }

            rowIndex++;
        }
    }

    private void setCellData(Object data, Row row, int cellIndex, CellStyle cellStyle) {
        Cell cell = row.getCell(cellIndex);
        if (Objects.isNull(cell)) {
            cell = row.createCell(cellIndex);
        }
        cell.setCellStyle(cellStyle);
        if (ObjectUtils.isEmpty(data)){
            cell.setCellValue("");
        }else{
            if (data instanceof Boolean)
                cell.setCellValue((Boolean) data);
            else if (data instanceof Double)
                cell.setCellValue((double) data);
            else if (data instanceof Float)
                cell.setCellValue((float) data);
            else if (data instanceof Long)
                cell.setCellValue((long) data);
            else if (data instanceof Date) {
                cell.setCellValue((Date) data);
            } else cell.setCellValue((String) data);
        }
    }
}