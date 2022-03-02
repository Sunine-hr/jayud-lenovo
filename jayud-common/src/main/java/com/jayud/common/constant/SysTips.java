package com.jayud.common.constant;

/**
 * @author ciro
 * @date 2022/2/16 17:23
 * @description: 系统提示语
 */
public interface SysTips {
    /**
     * 系统用户信息出错
     */
    String USER_INFO_ERROR = "获取系统用户信息出错！";
    /**
     * 修改根据ID获取出错提示
     */
    String OBJECT_NOT_FOUND = "获取对应操作信息出错！";
    /**
     * 请求参数有误提示
     */
    String REQ_PARAMS_ERROR = "请求参数有误，请检查后再试！";
    /**
     * 授权提示
     */
    String NO_AUTH_WARN = "您没有操作权限，请联系管理员授权！";
    /**
     * 操作成功提示
     */
    String SUCCESS_MSG = "操作成功！";
    /**
     * 操作失败提示
     */
    String ERROR_MSG = "操作失败！";
    /**
     * 文件上传成功提示
     */
    String UPLOAD_SUCCESS = "文件上传成功！";
    /**
     * 文件上传失败提示
     */
    String UPLOAD_ERROR = "文件上传失败！";
    /**
     * 登录成功
     */
    String LOGIN_SUCCESS = "登录成功！";
    /**
     * 登录失败
     */
    String LOGIN_ERROR = "用户名或密码错误，请检查后再试！";
    /**
     * 第三方登录失败
     */
    String THIRD_LOGIN_ERROR = "第三方登录异常,请联系管理员！";
    /**
     * 文件不存在
     */
    String FILE_NO_EXIST = "文件不存在！";
    /**
     * 值可用
     */
    String VALUE_AVALIABLE = "该值可用！";
    /**
     * 值不可用
     */
    String VALUE_NO_AVALIABLE = "该值不可用，系统中已存在！";
    /**
     * 验证码无效
     */
    String INVALID_CAPTCHA = "验证码无效！";
    /**
     * 验证码错误
     */
    String CAPTCHA_ERROR = "验证码错误！";
    /**
     * 获取验证码出错
     */
    String GET_CAPTCHA_ERROR = "获取验证码出错！";
    /**
     * 退出登录失败
     */
    String OUT_LOGIN_ERROR = "退出登录失败！";
    /**
     * 退出登录成功
     */
    String OUT_LOGIN_SUCCESS = "退出登录成功！";
    /**
     * Token无效
     */
    String INVALID_TOKEN = "Token无效！";
    /**
     * 手机号不允许为空
     */
    String PHONE_NO_EMPTY = "手机号不允许为空！";
    /**
     * 验证码有效
     */
    String CODE_EFFECTIVE = "验证码10分钟内，仍然有效！";
    /**
     * 手机已注册
     */
    String PHONE_REGISTERED = "手机号已经注册，请直接登录！";
    /**
     * 用户不存在
     */
    String ACCOUNT_NON_EXISTENT = "该用户不存在，请注册！";
    /**
     * 用户注销
     */
    String ACCOUNT_CANCELLATION = "该用户已注销！";
    /**
     * 用户冻结
     */
    String ACCOUNT_FROZEN = "该用户已冻结！";
    /**
     * 用户冻结
     */
    String ACCOUNT_RESIGNED = "该用户已离职！";
    /**
     * 短信验证码发送失败（第三方原因）
     */
    String SMS_CODE_SEND_ERROR = "短信验证码发送失败,请稍后重试！";
    /**
     * 短信发送失败（系统原因）
     */
    String SMS_CODE_SEND_ERROR_EXCEPTION = "短信发送失败，请联系管理员！";
    /**
     * 手机验证码错误
     */
    String SMS_CODE_ERROR = "手机验证码错误！";
    /**
     * 用户未绑定部门
     */
    String NO_DEPARTMENT = "用户暂未归属部门,不可登录!";
    /**
     * 全部已读
     */
    String READ_ALL = "全部已读！";
    /**
     * 错误,类型编码为空!
     */
    String CODING_TYPE_ISEMPTY = "类型编码为空！";
    /**
     * 参数有误.[null]!
     */
    String NULL_PARAMETER_ERROR = "加载分类字典树参数有误.[null]!";
    /**
     * 参数有误.[code]!
     */
    String CODE_PARAMETER_ERROR = "FIRST参数有误.[code]!";
    /**
     * ids 不能为空
     */
    String IDS_ISEMPTY = "ids不能为空！";
    /**
     * id 不能为空
     */
    String ID_ISEMPTY = "id不能为空";

    /**
     * 添加成功!
     */
    String ADD_SUCCESS = "添加成功！";
    /**
     * 添加成功!
     */
    String SAVE_SUCCESS = "保存成功！";
    /**
     * 添加失败!
     */
    String ADD_FAIL = "添加失败！";
    /**
     * 添加失败，存在默认且有效数据
     */
    String ADD_FAIL_NON_UNIQUE = "已存在一条默认且有效，请先取消原有默认再进行新增！";
    /**
     * 删除成功!
     */
    String DEL_SUCCESS = "删除成功！";
    /**
     * 编辑成功!
     */
    String EDIT_SUCCESS = "编辑成功！";
    /**
     * 编辑成功!
     */
    String EDIT_FAIL = "编辑失败！";
    /**
     * 批量删除成功!
     */
    String BATCH_DEL_SUCCESS = "批量删除成功！";
    /**
     * 无匹配数据
     */
    String NO_MATCHING_DATA = "无匹配数据！";
    /**
     * 字典Code格式不正确！
     */
    String DICTIONARY_CODE_FORMAT_IS_INCORRECT = "字典Code格式不正确！";
    /**
     * ElasticSearch获取索引字段映射
     */
    String NOT_FOUND = "404 Not Found";
    /**
     * 重复提交
     */
    String DO_NOT_RESUBMIT = "请勿重复提交！";

    String TENANT_CODE_SAME = "存在相同租户编码！";

    String SYS_URL_TYPE_SAME = "存在系统类型！";

    String ERROR_MSG_ONE = "存在角色绑定用户,无法删除！";

    String DELETE_ADMIN_ERROR = "超级管理员不能删除！";

    String NOT_ONE_DATA_ERROR = "存在多条数据！";

    String CREDIT_CODE_EXIT_ERROR = "统一社会信用代码已存在！";

    String CUSTOMER_NAME_EXIT_ERROR = "客户名称已存在！";
}

