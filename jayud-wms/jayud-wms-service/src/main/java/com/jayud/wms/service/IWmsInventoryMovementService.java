package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.wms.model.bo.WmsInventoryMovementForm;
import com.jayud.wms.model.po.WmsInventoryMovement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.vo.WmsInventoryMovementVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存移动订单表 服务类
 *
 * @author jayud
 * @since 2022-04-11
 */
public interface IWmsInventoryMovementService extends IService<WmsInventoryMovement> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: wmsInventoryMovement
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsInventoryMovement>
     **/
    IPage<WmsInventoryMovementVO> selectPage(WmsInventoryMovementForm wmsInventoryMovement,
                                             Integer currentPage,
                                             Integer pageSize,
                                             HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-11
     * @param: wmsInventoryMovement
     * @param: req
     * @return: java.util.List<com.jayud.wms.model.po.WmsInventoryMovement>
     **/
    List<WmsInventoryMovement> selectList(WmsInventoryMovement wmsInventoryMovement);


   boolean saveOrUpdateWmsInventoryMovement(WmsInventoryMovementForm wmsInventoryMovement);
    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-04-11
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-04-11
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryWmsInventoryMovementForExcel(Map<String, Object> paramMap);


    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    WmsInventoryMovementVO getDetails(Long id);


}
