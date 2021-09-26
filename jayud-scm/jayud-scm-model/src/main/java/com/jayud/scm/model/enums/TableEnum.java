package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum TableEnum {

    commodity(1,"commodity"),//商品
    hs_code(2,"hs_code"),//海关编码
    b_public_files(3,"b_public_files"),//附件
    system_action(4,"system_action"),//按钮
    system_role_action_check(5,"system_role_action_check"),//审核按钮
    b_data_dic_entry(6,"b_data_dic_entry"),//数据字典明细表
    customer(7,"customer"),//客户
    customer_relationer(8,"customer_relationer"),//客户联系人
    customer_bank(9,"customer_bank"),//银行资料
    customer_address(10,"customer_address"),//常住地址
    customer_maintenance_setup(11,"customer_maintenance_setup"),//客户维护人
    customer_agreement(12,"customer_agreement"),//客户协议
    customer_guarantee(13,"customer_guarantee"),//客户合同
    customer_tax(14,"customer_tax"),//开票资料
    fee_model(15,"fee_model"),//结算方案
    system_role_action_data(16,"system_role_action_data"),//数据权限
    hub_receiving(17,"hub_receiving"),//入库
    hub_shipping(18,"hub_shipping"),//出库
    hub_shipping_entry(19,"hub_shipping_entry"),//出库详情
    booking_order(20, "booking_order"),  //TODO 委托订单主表 booking_order
    check_order(21,"check_order"),//提验货
    check_order_entry(22,"check_order_entry"),//提验货详情
    hg_truck(23,"hg_truck"),//港车
    hg_bill(24,"hg_bill"),//报关
    customer_truck_place(25,"customer_truck_place"),//港车车牌
    customer_truck_driver(26,"customer_truck_driver"),//司机
    account_bank_bill(27,"account_bank_bill"),//水单
    account_bank_bill_entry(28,"account_bank_bill_entry"),//水单详情
    acct_receipt(29,"acct_receipt"),//收款单
    invoice(30,"invoice"),//应收款单
    export_tax_invoice(31,"export_tax_invoice"),//进项票
    acct_pay(32,"acct_pay"),//付款单
    acct_pay_entry(33,"acct_pay_entry"),//付款单明细
    other_cost(34,"other_cost"),//其他费用
    reports(35,"reports"),//打印报表
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (TableEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
