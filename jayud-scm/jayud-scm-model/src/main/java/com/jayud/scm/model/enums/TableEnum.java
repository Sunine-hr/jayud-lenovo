package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum TableEnum {

    commodity(1,"commodity"),
    hs_code(2,"hs_code"),
    b_public_files(3,"b_public_files"),
    system_action(4,"system_action"),
    system_role_action_check(5,"system_role_action_check"),
    b_data_dic_entry(6,"b_data_dic_entry"),
    customer(7,"customer"),
    customer_relationer(8,"customer_relationer"),
    customer_bank(9,"customer_bank"),
    customer_address(10,"customer_address"),
    customer_maintenance_setup(11,"customer_maintenance_setup"),
    customer_agreement(12,"customer_agreement"),
    customer_guarantee(13,"customer_guarantee"),
    customer_tax(14,"customer_tax"),
    fee_model(15,"fee_model"),
    system_role_action_data(16,"system_role_action_data"),
    hub_receiving(17,"hub_receiving"),
    hub_shipping(18,"hub_shipping"),
    hub_shipping_entry(19,"hub_shipping_entry"),
    booking_order(20, "booking_order"),  //TODO 委托订单主表 booking_order
    check_order(21,"check_order"),
    check_order_entry(22,"check_order_entry"),
    hg_truck(23,"hg_truck"),
    hg_bill(24,"hg_bill"),
    customer_truck_place(25,"customer_truck_place"),
    customer_truck_driver(26,"customer_truck_driver"),
    account_bank_bill(27,"account_bank_bill"),
    account_bank_bill_entry(28,"account_bank_bill_entry"),
    acct_receipt(29,"acct_receipt"),
    invoice(30,"invoice"),
    export_tax_invoice(31,"export_tax_invoice"),
    acct_pay(32,"acct_pay"),
    acct_pay_entry(33,"acct_pay_entry"),
    other_cost(34,"other_cost"),
    reports(35,"reports"),
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
