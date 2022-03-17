package com.jayud.wms.model.constant;

/**
 * @author ciro
 * @date 2022/1/18 10:58
 * @description:
 */
public interface AllocationStrategyConstant {

    String MATERIAL = "material";

    String OWER = "ower";

    String WAREHOUSE = "warehouse";

    String EMPTY_LOCATION_PARAM = "(id.existing_count-id.allocation_count-id.picking_count)";

    String LINE_SWQUENCE_PARAM = "wl.route_sorting";

    String LINE_SWQUENCE_CONDITION_NOT_NULL = "wl.route_sorting IS NOT NULL";

}
