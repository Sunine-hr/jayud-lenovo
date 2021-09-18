package com.jayud.finance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum BillEnum {

    //客服编辑业务:进入编辑界面-删除/添加(勾选-确定)-暂存/提交-进入到客服主管审核,真实的业务场景是一个连贯的动作,但是为了配合前台解决一系列的问题,得加以下几个状态。
    //edit_del:删除,账单详情表的audit_status
    //edit_no_commit:暂存,账单详情表audit_status
    //save_confirm:添加(勾选-确定),录入费用表is_bill

    //账单状态
    B_1("B_1","待经理审核"),//客服
    B_2("B_2","客服审核对账单通过"),//客服主管  //账单提交财务
    B_2_1("B_2_1","审核对账单不通过"),//客服主管
    B_3("B_3","待财务审核"),//客服提交财务
    B_4("B_4","财务审核对账单通过"),//财务  //待开票申请/待付款申请
    B_4_1("B_4_1","审核对账单不通过"),//财务
    B_5("B_5","开票/付款申请"),//客服
    B_5_1("B_5_1","开票/付款申请作废"),//客服
    B_6("B_6","开票/付款申请审核"),//财务
    B_6_1("B_6_1","开票/付款申请审核不通过"),//财务
    B_7("B_7","客服主管反审核"),//客服主管
    B_8("B_8","财务反审核"),//财务
    B_9("B_9","推金碟"),//财务

    EDIT_DEL("edit_del","审核对账单不通过"),//账单编辑-费用处于删除状态,账单详情表的audit_status
    SAVE_CONFIRM("save_confirm","费用状态-暂存提交"),
    EDIT_NO_COMMIT("edit_no_commit","审核对账单不通过"),//账单编辑-添加费用暂存,账单详情表audit_status

    //付款/开票申请状态
    F_0("0","未申请"),
    F_1("1","待审核"),
    F_2("2","审核通过"),
    F_3("3","审核驳回"),
    F_4("4","申请作废"),




    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (BillEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
