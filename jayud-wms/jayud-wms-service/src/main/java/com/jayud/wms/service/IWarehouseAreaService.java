package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.QueryWarehouseAreaForm;
import com.jayud.wms.model.bo.WarehouseAreaForm;
import com.jayud.wms.model.po.WarehouseArea;
import com.jayud.wms.model.vo.WarehouseAreaVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库区信息 服务类
 *
 * @author jyd
 * @since 2021-12-14
 */
public interface IWarehouseAreaService extends IService<WarehouseArea> {

    /**
     *  分页查询
     * @param warehouseArea
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    IPage<WarehouseAreaVO> selectPage(QueryWarehouseAreaForm warehouseArea,
                                      Integer pageNo,
                                      Integer pageSize,
                                      HttpServletRequest req);

    /**
     *  查询列表
     * @param warehouseArea
     * @return
     */
    List<WarehouseAreaVO> selectList(WarehouseArea warehouseArea);

    /**\
     * 根据名字查询
     * @param name
     * @param warehouseId
     * @return
     */
    WarehouseArea getWarehouseAreaByName(String name, Long warehouseId);

    /**\
     * 根据编号查询
     * @param name
     * @param warehouseId
     * @return
     */
    WarehouseArea getWarehouseAreaByCode(String name, Long warehouseId);

    /**\
     * 新增或者修改
     * @param warehouseArea
     * @return
     */
    BaseResult saveOrUpdateWarehouseArea(WarehouseAreaForm warehouseArea);

    void deleteById(List<Long> ids);

    List<WarehouseArea> getWarehouseAreaByWarehouseId(Long id);

    List<WarehouseArea> getWarehouseAreaByWarehouseIdAndStatus(Long id);

    List<LinkedHashMap<String, Object>> queryWarehouseAreaForExcel(Map<String, Object> paramMap);
}
