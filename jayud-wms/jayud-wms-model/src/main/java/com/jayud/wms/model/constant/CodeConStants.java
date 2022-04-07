package com.jayud.wms.model.constant;

/**
 * @author ciro
 * @date 2021/12/22 14:54
 * @description: 编码信息常量
 */
public interface CodeConStants {

    /**
     * 出库通知单
     */
    String OUTBOUND_NOTICE_ORDER_NUMBER = "outbound_notice_order_number";

    /**
     * 出库单
     */
    String OUTBOUND_ORDER_NUMBER = "outbound_order_number";

    /**
     * 拣货下架单
     */
    String PACKING_OFF_SHELF = "packing_off_shelf";

    /**
     * 库存盘点单号
     */
    String INVENTORY_CHECK_CODE = "inventory_check_code";
    /**
     * 波次单号
     */
    String WAVE_ORDER = "wave_order";

    /**
     * 拣货下架单明细号
     */
    String PACKING_OFF_SHELF_DETAIL = "packing_off_shelf_detail";

    /**
     * 库存移库主任务号
     */
    String INVENTORY_MOVEMENT_MAIN_CODE = "inventory_movement_main_code";

    /**
     * 库存移库明细任务号
     */
    String INVENTORY_MOVEMENT_DETAIL_CODE = "inventory_movement_detail_code";

    /**
     * 库存事务编号
     */
    String INVENTORY_BUSINESS_CODE = "inventory_business_code";

    /**
     * 分配策略X
     */
    String ALLOCATION_STRATEGY = "allocation_strategy";

    /**
     * 分配策略详情
     */
    String ALLOCATION_STRATEGY_DETAIL = "allocation_strategy_detail";

    /**
     * 货架移动任务号
     */
    String SHELF_MOVE_TASK_MAIN_CODE = "shelf_move_task_main_code";

    /**
     * 货架移动任务明细号
     */
    String SHELF_MOVE_TASK_MX_CODE = "shelf_move_task_mx_code";

}
