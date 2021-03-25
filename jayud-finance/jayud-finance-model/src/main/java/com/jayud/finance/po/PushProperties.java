package com.jayud.finance.po;

import cn.hutool.core.collection.CollectionUtil;
import com.jayud.finance.enums.FormIDEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推送金蝶时，封装所需数据的实体类
 */
@Data
public class PushProperties {
    /**
     * 报关单号
     */
    private String applyNo;
    /**
     * 已经存在的invoice订单
     */
    private Map<FormIDEnum, List<String>> existingOrders;
    /**
     * 无法删除的订单
     */
    private Map<FormIDEnum, List<String>> unRemovableOrders;
    /**
     * 无法删除的订单对应的公司（客户或供应商抬头）名称
     */
    private Map<FormIDEnum, List<String>> unRemovableCompName;
    /**
     * 是否允许继续推送
     */
    private Map<FormIDEnum, Boolean> allowPush;

    private Boolean error;

    public PushProperties() {
        allowPush = new HashMap<>();
        allowPush.put(FormIDEnum.RECEIVABLE, true);
        allowPush.put(FormIDEnum.RECEIVABLE_OTHER, true);
        allowPush.put(FormIDEnum.PAYABLE, true);
        allowPush.put(FormIDEnum.PAYABLE_OTHER, true);
    }

    /**
     * 是否允许继续进行推送（false将直接退出推送逻辑）
     *
     * @param formIDEnum
     * @return
     */
    public Boolean ifAllowPush(FormIDEnum formIDEnum) {
        if (formIDEnum.equals(FormIDEnum.PAYABLE_OTHER) || formIDEnum.equals(FormIDEnum.PAYABLE)) {
            Integer existingPayableSize = getSize(existingOrders.get(FormIDEnum.PAYABLE));
            Integer existingPayableOtherSize = getSize(existingOrders.get(FormIDEnum.PAYABLE_OTHER));
            Integer unRemovablePayableSize = getSize(unRemovableOrders.get(FormIDEnum.PAYABLE));
            Integer unRemovablePayableOtherSize = getSize(unRemovableOrders.get(FormIDEnum.PAYABLE_OTHER));
            //当且仅当存在的订单数等于不可删除的订单数时，不必推送
            if (existingPayableSize != 0 && existingPayableSize == unRemovablePayableSize) {
                allowPush.put(FormIDEnum.PAYABLE, false);
            }
            if (existingPayableOtherSize != 0 && existingPayableOtherSize == unRemovablePayableOtherSize) {
                allowPush.put(FormIDEnum.PAYABLE_OTHER, false);
            }
            if (!allowPush.get(FormIDEnum.PAYABLE_OTHER) && !allowPush.get(FormIDEnum.PAYABLE)) {
                return false;
            }

        } else if (formIDEnum.equals(FormIDEnum.RECEIVABLE) || formIDEnum.equals(FormIDEnum.RECEIVABLE_OTHER)) {
            Integer existingReceivableSize = getSize(existingOrders.get(FormIDEnum.RECEIVABLE));
            Integer existingReceivableOtherSize = getSize(existingOrders.get(FormIDEnum.RECEIVABLE_OTHER));
            Integer unRemovableReceivableSize = getSize(unRemovableOrders.get(FormIDEnum.RECEIVABLE));
            Integer unRemovableReceivableOtherSize = getSize(unRemovableOrders.get(FormIDEnum.RECEIVABLE_OTHER));
            //当且仅当存在的订单数等于不可删除的订单数时，不必推送
            if (existingReceivableSize != 0 && existingReceivableSize == unRemovableReceivableSize) {
                allowPush.put(FormIDEnum.RECEIVABLE, false);
            }
            if (existingReceivableOtherSize != 0 && existingReceivableOtherSize == unRemovableReceivableOtherSize) {
                allowPush.put(FormIDEnum.RECEIVABLE_OTHER, false);
            }
            if (!allowPush.get(FormIDEnum.RECEIVABLE_OTHER) && !allowPush.get(FormIDEnum.RECEIVABLE)) {
                return false;
            }
        }
        return true;
    }

    private Integer getSize(List<String> list) {
        if (CollectionUtil.isNotEmpty(list)) {
            return list.size();
        } else {
            return 0;
        }
    }
}
