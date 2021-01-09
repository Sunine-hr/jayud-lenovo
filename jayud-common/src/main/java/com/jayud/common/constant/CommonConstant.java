package com.jayud.common.constant;

/**
 * 通用常量
 */
public abstract class CommonConstant {

    /**
     * 提交
     */
    public static final String SUBMIT = "submit";
    /**
     * 暂存
     */
    public static final String PRE_SUBMIT = "preSubmit";
    /**
     * 费用审核
     */
    public static final String COST_AUDIT = "costAudit";
    /**
     * 外部报关放行
     */
    public static final String OUT_CUSTOMS_RELEASE = "outCustomsRelease";
    public static final String OUT_CUSTOMS_RELEASE_DESC = "外部报关放行";
    /**
     * 通关前审核
     */
    public static final String GO_CUSTOMS_AUDIT = "goCustomsAudit";
    public static final String GO_CUSTOMS_AUDIT_DESC = "通关前审核";
    /**
     * 通关前复核
     */
    public static final String GO_CUSTOMS_CHECK = "goCustomsCheck";
    public static final String GO_CUSTOMS_CHECK_DESC = "通关前复核";

    /**
     * 车辆通关
     */
    public static final String CAR_GO_CUSTOMS = "carGoCustoms";
    public static final String CAR_GO_CUSTOMS_DESC = "车辆通关";

    /**
     * 确认接单
     */
    public static final String COMFIRM_ORDER = "confirmOrder";
    public static final String COMFIRM_ORDER_DESC = "确认接单";

    /**
     * 派车相关
     */
    public static final String SEND_CAR = "sendCar";//派车
    public static final String AUDIT_CAR = "auditCar";//运输审核
    public static final String EDIT_CAR = "editCar";//驳回后编辑

    /**
     * 待补全
     */
    public static final String DATA_NOT_ALL = "dataNotAll";

    /**
     * 车辆提货
     */
    public static final String  CAR_TAKE_GOODS = "carTakeGoods";

    /**
     * 车辆过磅
     */
    public static final String CAR_WEIGH = "carWeigh";

    /**
     * 香港清关
     */
    public static final String HK_CLEAR_CUSTOMS = "hkClearCustoms";

    /**
     * 车辆入仓
     */
    public static final String CAR_ENTER_WAREHOUSE = "carEnterWarehouse";

    /**
     * 车辆出仓
     */
    public static final String CAR_OUT_WAREHOUSE = "carOutWarehouse";

    /**
     * 车辆派送
     */
    public static final String CAR_SEND = "carSend";

    /**
     * 确认签收
     */
    public static final String CONFIRM_SIGN_IN = "confirmSignIn";

    /**
     * 生成主订单前缀
     */
    public static final String M = "M";
    /**
     * 生成中港订单前缀
     */
    public static final String T = "T";
    /**
     * 生成派车单号
     */
    public static final String P = "P";
    /**
     * 生成空运订单号
     */
    public static final String A = "A";
    /**
     * 生成服务订单号
     */
    public static final String F = "F";
    /**
     * 生成账单号
     */
    public static final String B = "B";

    public static final String VALUE_1 = "1";

    public static final String VALUE_0 = "0";

    public static final String VALUE_2 = "2";

    public static final String GOODS_TYPE_DESC_1 = "进口";

    public static final String GOODS_TYPE_DESC_2 = "出口";

    public static final String CUSTOMERS = "customers";

    public static final String YWS = "yws";

    public static final String CONTRACTS = "contracts";

    public static final String DEPARTMENTS = "departments";

    public static final String PROTINFOS = "proInfos";

    public static final String BIZCODES = "bizCodes";

    public static final String WAREHOUSEINFO = "warehouseInfo";

    public static final String SUPPLIERINFO = "supplierInfo";

    public static final String BG = "BG";
    public static final String BG_DESC = "报关订单";

    public static final String ZGYS = "ZGYS";
    public static final String ZGYS_DESC = "中港运输订单";

    public static final String KY = "KY";
    public static final String KY_DESC = "空运订单";

    public static final String PAGE_LIST = "pageList";

    public static final String PRE_SUBMIT_COUNT = "preSubmitCount";

    public static final String ALL_COUNT = "allCount";

    public static final String DATA_NOT_ALL_COUNT = "dataNotAllCount";

    public static final String CANCELLED_COUNT = "cancelledCount";

    public static final String REJECTED_COUNT = "rejectedCount";

    public static final String REJECT = "驳回";

    public static final String ORDER_NO = "orderNo";

    public static final String BIZ_CODE = "bizCode";

    public static final String COMMA = ",";

    public static final String ID_CODE = "idCode";

    public static final String DEPARTMENT_ID = "departmentId";

    public static final String WORK = "work";

    public static final String DEPARTMENT = "department";

    public static final String ID = "id";

    public static final String LIST = "list";

    public static final String BILL_NUM_TOTAL = "billNumTotal";//订单数合计

    public static final String RMB_TOTAL = "rmbTotal";//人民币合计

    public static final String DOLLAR_TOTAL = "dollarTotal";//美元合计

    public static final String EURO_TOTAL = "euroTotal";//欧元合计

    public static final String HK_DOLLAR_TOTAL = "hKDollarTotal";//港币合计

    public static final String LOCAL_AMOUNT_TOTAL = "localAmountTotal";//本币金额合计

    public static final String HE_XIAO_AMOUNT = "heXiaoAmount";//已收金额，即财务已核销金额合计

    public static final String SHEET_HEAD = "sheetHead";//表头

    public static final String WHOLE_DATA = "wholeData";//全局数据

    public static final String DOUBLE_QUOTE = "";//双引号

    public static final String RECEIVABLE = "receivable";//应收标识

    public static final String PAYMENT = "payment";//应付标识
}
