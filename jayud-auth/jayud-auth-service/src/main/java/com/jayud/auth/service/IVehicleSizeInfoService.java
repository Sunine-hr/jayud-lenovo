package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.VehicleSizeInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆尺寸信息 服务类
 *
 * @author jayud
 * @since 2022-04-11
 */
public interface IVehicleSizeInfoService extends IService<VehicleSizeInfo> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: vehicleSizeInfo
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.VehicleSizeInfo>
     **/
    IPage<VehicleSizeInfo> selectPage(VehicleSizeInfo vehicleSizeInfo,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-11
     * @param: vehicleSizeInfo
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.VehicleSizeInfo>
     **/
    List<VehicleSizeInfo> selectList(VehicleSizeInfo vehicleSizeInfo);



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
    List<LinkedHashMap<String, Object>> queryVehicleSizeInfoForExcel(Map<String, Object> paramMap);

    /**
     * 获取车型
     * @return
     */
    List<VehicleSizeInfo> findVehicleSize();

}
