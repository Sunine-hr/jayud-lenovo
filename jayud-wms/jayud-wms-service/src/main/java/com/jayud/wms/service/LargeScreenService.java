package com.jayud.wms.service;

import com.jayud.common.BaseResult;
import com.jayud.wms.model.bo.LargeScreen.WarehouseForm;
import com.jayud.wms.model.vo.LargeScreen.OrderCountVO;
import com.jayud.wms.model.vo.LargeScreen.OrderMsgVO;
import com.jayud.wms.model.vo.LargeScreen.WareLocationUseStatusVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ciro
 * @date 2022/4/11 10:22
 * @description: 大屏看板
 */

public interface LargeScreenService {

    /**
     * @description 获取库位使用状态
     * @author  ciro
     * @date   2022/4/11 10:55
     * @param: warehouseForm
     * @return: com.jayud.wms.model.vo.LargeScreen.WareLocationUseStatusVO
     **/
    WareLocationUseStatusVO getWarehouseLocationUserStatus(WarehouseForm warehouseForm);


    /**
     * @description 获取完成订单数量
     * @author  ciro
     * @date   2022/4/11 11:06
     * @param: warehouseForm
     * @return: com.jayud.wms.model.vo.LargeScreen.OrderCountVO
     **/
    OrderCountVO getFinishOrderCount(WarehouseForm warehouseForm);


    /**
     * @description 获取未完成订单数据
     * @author  ciro
     * @date   2022/4/11 15:14
     * @param: warehouseForm
     * @return: java.util.List<com.jayud.wms.model.vo.LargeScreen.OrderMsgVO>
     **/
    OrderCountVO getUnfinsihOrderMsg(WarehouseForm warehouseForm);


}
