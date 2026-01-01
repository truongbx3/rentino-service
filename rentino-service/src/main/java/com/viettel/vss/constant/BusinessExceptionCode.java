package com.viettel.vss.constant;

public class BusinessExceptionCode {

    public static final String ERROR_USER_NOTFOUND = "error.user.notfound";
    public static final String VALIDATE_FAILED = "validate.failed";
    public static final String ATTACH_FILE_NOT_FOUND = "exception.message.AttachFileNotFound";
    public static final String INVALID_PHONE_NUMBER = "exception.message.user.inValidPhoneNumber";
    public static final String SCREENCHECK_NOT_ENOUGHT = "screencheck.not.enough";
    public static final String DEVICE_NOT_FOUND = "device.notfound";
    public static final String BORROW_NOT_FOUND = "borrow.notfound";
    public static final String RATE_NOTVALID = "ratelimit.novalid";

    public static final String SCREENCHECK_FRONT_CHECK = "screencheck.front.check";
    public static final String SCREENCHECK_BACK_CHECK = "screencheck.back.check";
    public static final String DEVICE_NOT_VALID = "device.notvalid";
    public static final String RATE_LIMIT_NOTVALID = "ratelimit.novalid";
    public static final String OCR_FAIL = "ocr.fail";
    public static final String LIVENESS_FAIL = "liveness.fail";
    public static final String FACEMATCHING_FAIL = "facematching.fail";
    public static final String OCR_NOT_FOUND = "ocr.notfound";
    public static final String USER_NOT_EKYC = "user.notkyc";

    public static final String ERROR_WHEN_READ_WRITING_DATA = "exception.message.ErrorWhenReadingWritingData";
    public static final String DATA_NOT_FOUND = "Data.Not.Found";
    public static final String TOOL_VERSION_NOT_FOUND = "exception.message.toolVersionNotFound";
    public static final String SERVICE_REGISTRATION_NOT_FOUND = "exception.message.ServiceRegistrationNotFound";
    public static final String USER_NOT_EXISTED = "exception.message.userNotExisted";
    public static final String OTP_INVALID = "exception.message.otpInvalid";
    public static final String TOKEN_INVALID = "exception.message.tokenInvalid";
    public static final String INFO_LOGIN_INVALID = "exception.message.infoLoginInvalid";
    public static final String ACTION_INVALID = "exception.message.actionInvalid";

    public static final String WRONG_PASSWORD_FORMAT = "exception.message.wrongPasswordFormat";

    public static final String WRONG_COMPANY_CODE_FORMAT = "exception.message.wrongCompanyCodeFormat";
    public static final String ACCOUNT_IS_EXISTED = "exception.message.accountIsExisted";
    public static final String MISSING_REQUIRE_FIELD= "exception.message.missingRequireField";
    public static final String COMPANY_CODE_IS_EXISTED= "exception.message.companyCodeIsExisted";
    public static final String OTP_CHANGE_EXP= "exception.message.otpChangeExpired";
    public static final String PASSWORD_NOT_SAME= "exception.message.passwordNotSame";
    public static final String NOT_NULL = "exception.message.notNull";
    public static final String NAME_FAIL_DATA = "exception.message.nameFailData";
    public static final String PHONE_FAIL_DATA = "exception.message.phoneFailData";
    public static final String EMAIL_FAIL_DATA = "exception.message.emailFailData";

    public static final String USER_NOT_FOUND = "exception.message.user.userNotFound";
    public static final String  PASSWORD_AND_RETYPE_PASSWORD_NOT_MATCH = "exception.message.user.passwordAndRetypePasswordNotMatch";
    public static final String INVALID_PASSWORD = "exception.message.user.validatePassword";
    public static final String WRONG_PASSWORD = "exception.message.user.wrongPassword";
    public static final String PASSWORD_DUPLICATE = "exception.message.password.duplicate";
    public static final String DONT_HAVE_PERMISSION = "exception.message.user.noPermission";
    public static final String EMAIL_EXISTED= "exception.message.user.emailExisted";


    public static final String SAVE_IMAGE_FAIL = "exception.message.saveImageFail";
    public static final String INVALID_FILE_IMPORT = "exception.message.invalidFormatFile";

    public static final String NOT_REGISTER_TOOL = "exception.message.user.notRegisterTool";
    public static final String LIMITED_USER_REGISTER = "exception.message.user.limitedUser";
    public static final String EMPTY_FILE = "exception.message.user.emptyFile";
    public static final String LICENSES_NOT_FOUND = "exception.message.licensesNotFound";


    public static final String INVALID_EMAIL = "exception.message.user.inValidEmail";
    public static final String DUPLICATE_VERSION_TRIAL = "exception.message.duplicateVersionTrial";
    public static final String REGISTERED_VERSION_PREMIUM = "exception.message.registeredVersionPremium";

    public static final String REGISTER_SUCCESSFULLY = "exception.message.register.successfully";
    public static final String REGISTER_SUCCESSFULLY_SEND_NOTIFY = "exception.message.register.sendNotify";

    public static final String PASSWORD_NOT_MATCH = "exception.message.password.notMatch";

    public static final String EMAIL_COMPANY_CODE_NOT_EXIST = "exception.message.password.emailCompanyCodeNotExist";

    public static final String INVALID_FORMAT_EMAIL = "exception.message.email.invalidFormatEmail";

    public static final String INVALID_INFO_FORGOT_PASSWORD = "exception.message.infoForgotPasswordInvalid";

    public static final String IMPORT_FILE_ERROR = "exception.message.importFileError";

    public static final String EMAIL_COMPANY_CODE_EXISTED= "exception.message.emailCompanyCodeExisted";

    public static final String TOOL_VERSION_NOT_EXISTED = "exception.message.ToolVersionNotExisted";

    public static final String DELETE_USER_SUCCESSFULLY = "exception.messsage.deleteUserSuccessfully";

    public static final String EXPORT_INVALID_FORMAT_PASSWORD = "export.message.invalidFormatPassword";

    public static final String EXPORT_INVALID_FORMAT_EMAIL = "export.message.invalidFormatEmail";

    public static final String EXPORT_EXISTED_EMAIL = "export.message.existedEmail";

    public static final String EXPORT_EMAIL_CANNOT_BE_BLANK = "export.message.emailNotBank";

    public static final String RESET_PASSWORD_SUCCESSFULLY = "exception.message.resetPasswordSuccessfully";

    public static final String TEMPLATE_FILE_NOTFOUND = "exception.message.templateFileNotFound";
    public static final String SERVICE_HAS_BEEN_REGISTERED = "exception.message.serviceHasBeenRegistered";

    public static final String EXTENDED_TOOL_REGISTER_SUCCESSFULLY = "success.message.extendedToolRegister";
    public static final String REGISTER_TOOL_SUCCESSFULLY = "success.message.registerToolSuccessfully";

    public static final String FUNCTION_EXISTS = "exception.message.function.exists";
    public static final String FUNCTION_NOT_FOUND = "exception.message.function.notFound";
    public static final String FUNCTION_IN_USED = "exception.message.function.inUsedDeleted";
    public static final String FUNCTION_IN_USED_UPDATED = "exception.message.function.inUsedUpdated";

    public static final String MODULE_EXISTS = "exception.message.module.exists";
    public static final String MODULE_NOT_FOUND = "exception.message.module.notFound";
    public static final String MODULE_HAS_MENU = "exception.message.module.hasMenus";
    public static final String MODULE_INVALID_ICON = "exception.message.module.invalidIcon";

    public static final String MENU_EXISTS = "exception.message.menu.exists";
    public static final String MENU_NOT_FOUND = "exception.message.menu.notFound";
    public static final String MENU_HAS_FUNCTION = "exception.message.menu.hasFunctions";

    public static final String ROLE_GROUP_NOT_FOUND = "exception.message.roleGroup.notFound";
    public static final String ROLE_GROUP_NAME_ALREADY_EXISTS = "exception.message.roleGroup.nameExists";
    public static final String ROLE_GROUP_ADMIN_ALREADY_EXISTS = "exception.message.roleGroup.adminExists";
    public static final String ROLE_GROUP_HAS_USER = "exception.message.roleGroup.hasUsers";
    public static final String ROLE_GROUP_DELETE_USER_NULL = "exception.message.roleGroup.deleteUserNull";

    public static final String USER_UPDATE_VALIDATE_STATUS = "exception.message.user.validateStatus";

    public static final String FILE_EXCEL_EXTENSION_INVALID = "exception.message.file.invalidExtension";
    public static final String FILE_WRONG_TEMPLATE = "exception.message.file.wrongTemplate";

    public static final String SYSTEM_EMPTY_NAME = "exception.message.system.emptyName";
    public static final String SYSTEM_EXISTED_NAME = "exception.message.system.existedName";
    public static final String SYSTEM_EMPTY_BASE_URL = "exception.message.system.emptyBaseURL";
    public static final String SYSTEM_INVALID_BASE_URL = "exception.message.system.invalidBaseURL";
    public static final String SYSTEM_EXISTED_BASE_URL = "exception.message.system.existedBaseURL";
    public static final String SYSTEM_NOT_FOUND = "exception.message.system.notFound";


    public static final String FIELD_REQUIRED = "field.required";
    public static final String FILED_SIZE = "field.size";
    public static final String FILED_MIN = "field.min";
    public static final String FILED_MAX = "field.max";


    public static final String AUTH_TYPE_NOT_FOUND = "exception.message.authType.notFound";

    public static final String EXECUTION_TYPE_NOT_FOUND = "exception.message.executionType.notFound";

    public static final String EXECUTABLE_FUNC_NOT_FOUND = "exception.message.executableFunc.notFound";
    public static final String EXECUTABLE_FUNC_INVALID_METADATA = "exception.message.executableFunc.ínvalidMetadata";
}
