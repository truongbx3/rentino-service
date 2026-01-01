package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.constant.MessageConstants;
import com.viettel.vss.constant.UserStatus;
import com.viettel.vss.constant.common.NotifySendMail;
import com.viettel.vss.dto.ImportDetailDto;
import com.viettel.vss.dto.UserDto;
import com.viettel.vss.dto.UserImportDto;
import com.viettel.vss.dto.support.DateSupport;
import com.viettel.vss.dto.user.*;
import com.viettel.vss.dto.roleGroup.RoleGroupResponseDto;
import com.viettel.vss.entity.RoleGroup;
import com.viettel.vss.entity.UserEntity;
import com.viettel.vss.entity.UserInfoHistory;
import com.viettel.vss.enums.Company;
import com.viettel.vss.enums.ImportStatus;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.FunctionRepository;
import com.viettel.vss.repository.RoleGroupRepository;
import com.viettel.vss.repository.UserInfoHistoryRepository;
import com.viettel.vss.repository.UserRepository;
import com.viettel.vss.service.AppConfigService;
import com.viettel.vss.service.UserService;
import com.viettel.vss.util.*;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.poi.ss.usermodel.*;
import org.jxls.common.Context;
import org.jxls.transform.SafeSheetNameBuilder;
import org.jxls.transform.poi.PoiSafeSheetNameBuilder;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, UserDto> implements UserService {

    @Autowired
    private MessageCommon messageCommon;

    @Autowired
    private RoleGroupRepository roleGroupRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private CustomizeMessageCommon customizeMessageCommon;

    @Autowired
    private NotifySendMail notifySendMail;

    @Value("${notify.sendEmail.activeAccountAIG}")
    private String activeAccountAIGTemplate;

    @Value("${notify.sendEmail.login_link}")
    private String loginUrl;

    private final UserRepository userRepository;

    private final UserInfoHistoryRepository userInfoHistoryRepository;

    private final PasswordUtils passwordUtils;

    private final AppConfigService appConfigService;

    public UserServiceImpl(UserRepository userRepository, UserInfoHistoryRepository userInfoHistoryRepository,
                           PasswordUtils passwordUtils, AppConfigService appConfigService) {
        super(userRepository, UserEntity.class, UserDto.class);
        this.userRepository = userRepository;
        this.userInfoHistoryRepository = userInfoHistoryRepository;
        this.passwordUtils = passwordUtils;
        this.appConfigService = appConfigService;
    }

    @Override
    public Page<UserResponseDto> getUsersByRoleGroup(UserRequestDto userRequestDto, Pageable pageable) {
        Page<UserEntity> users = userRepository.getUsersByRoleGroup(userRequestDto, pageable);

        return users.map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    @Override
    public DetailInfoStaff getInfoAndFunctionUser(Long userId) {
        DetailInfoStaff staffUpdateRequest = new DetailInfoStaff();
        UserEntity userEntity = userRepository.findByIdAndIsDeleted(userId, 0);
        UserResponseDto userResponseDto = DataUtil.convertTo(userEntity, UserResponseDto.class);
        if (userResponseDto == null) {
            throw new BusinessException(BusinessExceptionCode.USER_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.USER_NOT_FOUND));
        }

        staffUpdateRequest.setUserDTO(userResponseDto);
        staffUpdateRequest.setLsRoles(roleGroupRepository.findByUserId(userId));
        staffUpdateRequest.setLsFunctions(functionRepository.findByUsername(userResponseDto.getUsername()));
        return staffUpdateRequest;
    }

    @Override
    @Transactional
    public Boolean updateInfoUser(UserUpdateRequest userUpdateRequest) {
        UserEntity updateStaff = null;

        try {
            UserCrudRequest userDTO = userUpdateRequest.getUserDTO();
            if (ObjectUtils.isEmpty(userDTO) || ObjectUtils.isEmpty(userDTO.getId()) || Objects.equals(0L, userDTO.getId())) {
                throw new BusinessException(BusinessExceptionCode.USER_NOT_FOUND,
                        messageCommon.getValueByMessageCode(BusinessExceptionCode.USER_NOT_FOUND));
            }
            if (userDTO.getId() != null) {
                updateStaff = userRepository.findById(userDTO.getId())
                        .orElseThrow(() -> new BusinessException(BusinessExceptionCode.USER_NOT_FOUND,
                                messageCommon.getValueByMessageCode(BusinessExceptionCode.USER_NOT_FOUND)));
                if (UserStatus.DEACTIVE.equals(updateStaff.getStatusId())) {
                    throw new BusinessException(BusinessExceptionCode.USER_UPDATE_VALIDATE_STATUS,
                            messageCommon.getValueByMessageCode(BusinessExceptionCode.USER_UPDATE_VALIDATE_STATUS));
                }
            }

            if (CollectionUtils.isEmpty(userUpdateRequest.getLsRoles())) {
                updateStaff.setRoleGroups(new ArrayList<>());
            } else {
                List<Long> lstRolesId = userUpdateRequest.getLsRoles().stream().map(RoleGroupResponseDto::getId)
                        .collect(Collectors.toList());
                List<RoleGroup> lst = roleGroupRepository.findByListRolesId(lstRolesId);
                updateStaff.setRoleGroups(lst);
            }

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(MessageConstants.SYSTEM_ERROR,
                    messageCommon.getValueByMessageCode(MessageConstants.SYSTEM_ERROR));
        }

        return true;
    }

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        Long id = userDto.getId();
        if(ObjectUtils.isEmpty(id)){
            String empCode = userDto.getEmpCode();
            String email = userDto.getEmail();
            if(!StringUtils.hasText(empCode)){
                throw new BusinessException("Mã nhân viên không được bỏ trống.");
            }
            if(!StringUtils.hasText(email)){
                throw new BusinessException("Email không được bỏ trống.");
            }
            List<UserEntity> existingUsers = userRepository.findByEmpCodeOrEmail(empCode, email, id);
            if (!existingUsers.isEmpty()) {
                throw new BusinessException("Mã nhân viên / email đã tồn tại");
            }
            String rawPassword = RandomUtils.generatePassword();
            String salt = RandomUtils.generateSalt();
            String encryptedPassword = passwordUtils.encode(rawPassword, salt);
            userDto.setPassword(encryptedPassword);
            userDto.setSalt(salt);
            userDto.setCompany(Company.VTIT.getName());
            userDto.setCompanyCode(Company.VTIT.getCode());
            userDto.setUsername(userDto.getEmail().split("@")[0]);;
            userDto.setIsFirstLogin(true);
            Map<String, Object> params = Map.of(
                    "full_name", userDto.getFullName(),
                    "user", userDto.getEmail(),
                    "password", rawPassword,
                    "login_link", loginUrl
            );
            notifySendMail.sendEmailTemplate(activeAccountAIGTemplate, userDto.getEmail(), userDto.getCompanyCode(), params);
            return super.saveObject(userDto);
        }
        else{
            UserEntity existingUser = userRepository.getUserById(id);
            if(ObjectUtils.isEmpty(existingUser)){
                throw new BusinessException("Người dùng không tồn tại");
            }
            String empCode = userDto.getEmpCode();
            String email = userDto.getEmail();
            if(!StringUtils.hasText(empCode)){
                throw new BusinessException("Mã nhân viên không được bỏ trống.");
            }
            if(!StringUtils.hasText(email)){
                throw new BusinessException("Email không được bỏ trống.");
            }
            List<UserEntity> existingUsers = userRepository.findByEmpCodeOrEmail(empCode, email, id);
            if (!existingUsers.isEmpty()) {
                throw new BusinessException("“Mã nhân viên / email đã tồn tại”");
            }
            existingUser.setUsername(userDto.getEmail().split("@")[0]);
            existingUser.setEmpCode(empCode);
            existingUser.setFullName(userDto.getFullName());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setDescription(userDto.getDescription());
            existingUser.setStatusId(userDto.getStatusId());
            UserInfoHistory userInfoHistory = new UserInfoHistory();
            userInfoHistory.setEmail(existingUser.getEmail());
            userInfoHistory.setUsername(existingUser.getUsername());
            userInfoHistory.setStatusId(existingUser.getStatusId());
            userInfoHistory.setDescription("Account "+ existingUser.getId() + " changed info successfully");
            userInfoHistoryRepository.save(userInfoHistory);
            return ObjectMapperUtils.map(userRepository.save(existingUser), UserDto.class);
        }
    }

    @Override
    public Page<UserListDto> filterUser(FilterUserRequest request, Integer size, Integer page, String sortBy, String sortDirection) {
        if(ObjectUtils.isEmpty(request.getCreatedDate())){
            request.setCreatedDate(new DateSupport());
        }
        if(ObjectUtils.isEmpty(request.getUpdatedDate())){
            request.setUpdatedDate(new DateSupport());
        }
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection).orElse(Sort.Direction.DESC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return userRepository.filterUsers(request, pageable);
    }

    @Override
    public UserListDto getDetailUser(Long userId) {
        return userRepository.getDetailUser(userId);
    }

    @Override
    public ByteArrayInputStream export(FilterUserRequest request, String sortBy, String sortDirection) throws IOException {
        if(ObjectUtils.isEmpty(request.getCreatedDate())){
            request.setCreatedDate(new DateSupport());
        }
        if(ObjectUtils.isEmpty(request.getUpdatedDate())){
            request.setUpdatedDate(new DateSupport());
        }
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection).orElse(Sort.Direction.DESC);
        Sort sort = Sort.by(direction, sortBy);
        List<UserExportDto> users = userRepository.exportUser(request, sort);
        Map<String, Object> map = new HashMap<>();
        map.put("data", users);
        map.put(SafeSheetNameBuilder.CONTEXT_VAR_NAME, new PoiSafeSheetNameBuilder());
        ByteArrayOutputStream byteArrayOutputStream = genXlsxLocal(map, "export/export-user.xlsx");
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public ByteArrayInputStream exportTemplate() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = genXlsxTemplate("import/user-import-sample.xlsx");
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    @Transactional
    public ImportDetailDto importUser(MultipartFile file){
        String fileName = file.getOriginalFilename();

        if (!"xlsx".equals(FileNameUtils.getExtension(fileName))) {
            throw new BusinessException("File không đúng định dạng");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException("Dung lượng file > 5MB");
        }

        try {
            if (!ExcelUtils.checkTemplateFile(new InputStream[]{file.getInputStream()}, 0, 0, listHeader())) {
                throw new BusinessException("File không đúng định dạng");
            }
        } catch (IOException e) {
            throw new BusinessException("Lỗi khi đọc file excel: " + e.getMessage());
        }

        ImportDetailDto importDetailDto = new ImportDetailDto();
        Map<String, Object> map = new HashMap<>();

        int error = 0;

        List<UserImportDto> content = genFromExcel(file);
        map.put("data", content);

        for(UserImportDto dto : content){
            if (!ObjectUtils.isEmpty(dto.getError())) {
//                System.out.println(dto.getError());
                error++;
            }
        }

        try {
            if (error != 0) {
                importDetailDto.setStatus(false);
                ByteArrayOutputStream byteArrayOutputStream = genXlsxLocal(map, "import/import-user-error.xlsx");
                importDetailDto.setFileError(byteArrayOutputStream.toByteArray());
            } else {
                importDetailDto.setStatus(true);
                this.saveImportUser(content);
            }
        } catch (IOException e) {
            throw new BusinessException("Lỗi khi xuất file lỗi: " + e.getMessage());
        }

        return importDetailDto;
    }

    @Transactional
    @Override
    public void saveImportUser(List<UserImportDto> content){
        List<UserDto> toSave = new ArrayList<>();
        content.forEach(dto -> {
            UserDto converted = ObjectMapperUtils.map(dto, UserDto.class);
            converted.setCompanyCode(Company.VTIT.getCode());
            converted.setCompany(Company.VTIT.getName());
            String rawPassword = RandomUtils.generatePassword();
            String salt = RandomUtils.generateSalt();
            String encryptedPassword = passwordUtils.encode(rawPassword, salt);
            converted.setPassword(encryptedPassword);
            converted.setSalt(salt);
            converted.setStatusId(0L);
            converted.setUsername(dto.getEmail().split("@")[0]);
            converted.setIsFirstLogin(true);
            Map<String, Object> params = Map.of(
                    "full_name", converted.getFullName(),
                    "user", converted.getEmail(),
                    "password", rawPassword,
                    "login_link", loginUrl
            );
            notifySendMail.sendEmailTemplate(activeAccountAIGTemplate, converted.getEmail(), converted.getCompanyCode(), params);
            toSave.add(converted);
        });
        this.saveListObject(toSave);
    }

    @Override
    public void recoveryPassword(Long id) {
        UserEntity user = userRepository.getUserById(id);
        if(ObjectUtils.isEmpty(user)){
            throw new BusinessException("Người dùng không tồn tại");
        }
        String rawPassword = RandomUtils.generatePassword();
        String salt = RandomUtils.generateSalt();
        String encryptedPassword = passwordUtils.encode(rawPassword, salt);
        user.setPassword(encryptedPassword);
        user.setSalt(salt);
        user.setIsFirstLogin(true);
        userRepository.save(user);
        Map<String, Object> params = Map.of(
                "full_name", user.getFullName(),
                "user", user.getEmail(),
                "password", rawPassword,
                "login_link", loginUrl
        );
        notifySendMail.sendEmailTemplate(activeAccountAIGTemplate, user.getEmail(), user.getCompanyCode(), params);
    }

//    @Override
//    public void lockOrUnlockUsers(UserActionLogRequest userActionLogRequest) {
//        String email  = userActionLogRequest.getEmail();
//        String companyCode = userActionLogRequest.getCompanyCode();
//        //validate input
//        if (DataUtil.isNullOrEmpty(email) || DataUtil.isNullOrEmpty(companyCode)) {
//            throw new BusinessException(BusinessExceptionCode.MISSING_REQUIRE_FIELD,
//                    customizeMessageCommon.getMessage(BusinessExceptionCode.MISSING_REQUIRE_FIELD));
//        }
//
//
//        UserEntity user = userRepository.findUserEntityByEmailAndCompanyCodeAndIsDeleted(email, companyCode, 0)
//                .orElseThrow(() -> new BusinessException(BusinessExceptionCode.USER_NOT_EXISTED,
//                        customizeMessageCommon.getMessage(BusinessExceptionCode.USER_NOT_EXISTED)));
//
//        switch (userActionLogRequest.getAction().toUpperCase()) {
//            case "LOCK":
//                user.setStatusId(1L);
//                user.setAccountLocked(true);
//                break;
//            case "UNLOCK":
//                user.setStatusId(0L);
//                user.setAccountLocked(false);
//                //send mail toast
//                Map<String, Object> params = new HashMap<>();
//                params.put("full_name", user.getFullName());
//                params.put("user", user.getEmail());
//                params.put("password", "********");
//                params.put("login_link", loginUrl);
//                notifySendMail.sendEmailTemplate(activeAccountAIGTemplate, email, companyCode, params);
//                break;
//            default: throw new BusinessException(BusinessExceptionCode.ACTION_INVALID,
//                    customizeMessageCommon.getMessage(BusinessExceptionCode.ACTION_INVALID +" : "+ userActionLogRequest.getAction()));
//        }
//        userRepository.save(user);
//
//        //save history action
//        UserInfoHistory userInfoHistory = new UserInfoHistory();
//        userInfoHistory.setEmail(user.getEmail());
//        userInfoHistory.setUsername(user.getUsername());
//        userInfoHistory.setStatusId(user.getStatusId());
//        userInfoHistory.setDescription("Account "+ userActionLogRequest.getAction() + " successfully");
//        userInfoHistoryRepository.save(userInfoHistory);
//    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids){
        boolean isLog = userInfoHistoryRepository.checkLogByUserIds(ids);
        if(isLog){
            throw new BusinessException("Người dùng đã được sử dụng, không thể xóa");
        }
        super.deleteByIds(ids);
    }

    @Override
    @Transactional
    public StringBuilder lockOrUnlockUsers(UserActionLogRequest request) {
        String action = request.getAction();
        List<UserInfoActionDto> userDtos = request.getUsers();

        // Validate đầu vào
        if (DataUtil.isNullOrEmpty(action) || DataUtil.isNullOrEmpty(userDtos)) {
            throw new BusinessException(BusinessExceptionCode.MISSING_REQUIRE_FIELD,
                    customizeMessageCommon.getMessage(BusinessExceptionCode.MISSING_REQUIRE_FIELD));
        }

        // Loại bỏ trùng email + companyCode
        List<UserInfoActionDto> distinctUsers = userDtos.stream()
                .filter(dto -> !DataUtil.isNullOrEmpty(dto.getEmail()) && !DataUtil.isNullOrEmpty(dto.getCompanyCode()))
                .distinct() // dựa trên equals/hashCode của DTO
                .collect(Collectors.toList());

        // Gom nhóm theo companyCode
        Map<String, List<String>> usersByCompany = distinctUsers.stream()
                .filter(dto -> !DataUtil.isNullOrEmpty(dto.getEmail()) && !DataUtil.isNullOrEmpty(dto.getCompanyCode()))
                .collect(Collectors.groupingBy(
                        UserInfoActionDto::getCompanyCode,
                        Collectors.mapping(UserInfoActionDto::getEmail, Collectors.toList())
                ));

        List<UserEntity> allUsers = new ArrayList<>();
        Set<String> existingEmails = new HashSet<>();

        // Truy vấn mỗi nhóm 1 lần
        for (Map.Entry<String, List<String>> entry : usersByCompany.entrySet()) {
            String companyCode = entry.getKey();
            List<String> emails = entry.getValue();


        }

        // Lọc danh sách không tồn tại
        List<String> requestedEmails = distinctUsers.stream()
                .map(UserInfoActionDto::getEmail)
                .collect(Collectors.toList());

        List<String> notFoundEmails = requestedEmails.stream()
                .filter(email -> !existingEmails.contains(email))
                .collect(Collectors.toList());


        // Nếu không tìm thấy ai cả → throw
        if (allUsers.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.USER_NOT_EXISTED,
                    "Không tìm thấy bất kỳ người dùng hợp lệ nào trong hệ thống.");
        }

        List<UserInfoHistory> histories = new ArrayList<>();

        for (UserEntity user : allUsers) {
            switch (action.toUpperCase()) {
                case "LOCK":
                    user.setStatusId(1L);
                    user.setAccountLocked(true);
                    break;

                case "UNLOCK":
                    user.setStatusId(0L);
                    user.setAccountLocked(false);
                    break;
                default:
                    throw new BusinessException(BusinessExceptionCode.ACTION_INVALID,
                            customizeMessageCommon.getMessage(BusinessExceptionCode.ACTION_INVALID + " : " + action));
            }

            // Lưu log
            UserInfoHistory history = new UserInfoHistory();
            history.setEmail(user.getEmail());
            history.setUsername(user.getUsername());
            history.setStatusId(user.getStatusId());
            history.setDescription("Account " + action + " successfully");
            histories.add(history);
        }

        // Batch save
        userRepository.saveAll(allUsers);
        userInfoHistoryRepository.saveAll(histories);

        // Log kết quả hoặc trả về response
        StringBuilder result = new StringBuilder();
        result.append("Đã xử lý thành công ")
                .append(allUsers.size())
                .append(" người dùng (action: ")
                .append(action)
                .append(").");

        if (!notFoundEmails.isEmpty()) {
            result.append(" Các tài khoản không tồn tại: ")
                    .append(String.join(", ", notFoundEmails))
                    .append(".");
        }

        return result;
    }


    private List<UserImportDto> genFromExcel(MultipartFile file) {
        List<UserImportDto> details = new ArrayList<>();
        try (InputStream is = file.getInputStream();

             Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();
            if(rows.hasNext()){
                rows.next();
            }
            Set<String> existingUsername = new HashSet<>();
            Set<String> existingEmail = new HashSet<>();
            if(rows.hasNext()){
                List<UserEntity> existingUsersList = userRepository.getExistingUsers();
                existingUsersList.forEach(user -> {
                    if(!ObjectUtils.isEmpty(user.getUsername())){
                        existingUsername.add(user.getUsername());
                    }
                    if(!ObjectUtils.isEmpty(user.getEmpCode())){
                        existingUsername.add(user.getEmpCode());
                    }
                    existingEmail.add(user.getEmail());
                });
            }
            while (rows.hasNext()) {
                Row row = rows.next();
                UserImportDto dto = mapRowToDto(row, existingUsername, existingEmail);
                if (ObjectUtils.isEmpty(dto)) {
                    continue;
                }
                details.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đọc file excel: " + e.getMessage(), e);
        }

        return details;
    }


    private UserImportDto mapRowToDto(Row row, Set<String> existingUsername, Set<String> existingEmail) {
        UserImportDto dto = new UserImportDto();
        StringBuilder error = new StringBuilder();
        String empCode = getStringCellValue(row.getCell(0));
        dto.setEmpCode(empCode);
        if(existingUsername.contains(empCode)){
            error.append("Tên tài khoản / Mã nhân viên đã tồn tại; ");
        }else{
            existingUsername.add(empCode);
        }
        String fullName = getStringCellValue(row.getCell(1));
        dto.setFullName(fullName);
        String email = getStringCellValue(row.getCell(2));
        dto.setEmail(email);
        if(existingEmail.contains(email)){
            error.append("Email đăng nhập đã tồn tại; ");
        }else{
            existingEmail.add(email);
        }
        String username = email.split("@")[0];
        if(existingUsername.contains(username)){
            error.append("Tên tài khoản / Mã nhân viên đã tồn tại; ");
        }else{
            existingUsername.add(username);
        }
        dto.setStatus(ImportStatus.SUCCESS.getDescription());

        if (error.length() > 0) {
            dto.setError(error.toString());
            dto.setStatus(ImportStatus.FAILED.getDescription());
        }
        return dto;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return null;
    }


    private ByteArrayOutputStream genXlsxLocal(Map<String, Object> data, String templateRelativePathAndName) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateRelativePathAndName);
        if (in == null) {
            throw new BusinessException("Template file not found:" + templateRelativePathAndName);
        }
        Context context = PoiTransformer.createInitialContext();
        for (Map.Entry<String, Object> d : data.entrySet()) {
            if (d.getKey() != null && d.getValue() != null) {
                context.putVar(d.getKey(), d.getValue());
            }
        }

        JxlsHelper.getInstance().processTemplate(in, out, context);
        return out;
    }

    private ByteArrayOutputStream genXlsxTemplate(String templateRelativePathAndName) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateRelativePathAndName);
        if (in == null) {
            throw new BusinessException("Template file not found:" + templateRelativePathAndName);
        }
        Context context = PoiTransformer.createInitialContext();

        JxlsHelper.getInstance().processTemplate(in, out, context);
        return out;
    }

    private Map<Integer, String> listHeader() {
        Map<Integer, String> checkMaps = new HashMap<>();
        checkMaps.put(0, "Mã NV");
        checkMaps.put(1, "Họ tên");
        checkMaps.put(2, "Email");
        return checkMaps;
    }
}