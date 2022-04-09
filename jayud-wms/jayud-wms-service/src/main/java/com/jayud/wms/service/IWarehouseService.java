package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.QueryWarehouseForm;
import com.jayud.wms.model.bo.WarehouseForm;
import com.jayud.wms.model.po.Warehouse;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库信息表 服务类
 *
 * @author jyd
 * @since 2021-12-14
 */
public interface IWarehouseService extends IService<Warehouse> {

        /**
         *  分页查询
         * @param queryWarehouseForm
         * @param pageNo
         * @param pageSize
         * @param req
         * @return
         */
        IPage<Warehouse> selectPage(QueryWarehouseForm queryWarehouseForm,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

        /**
         *  查询列表
         * @param warehouse
         * @return
         */
        List<Warehouse> selectList(QueryWarehouseForm warehouse);

        /**\
         * 根据名字查询
         * @param name
         * @return
         */
        Warehouse getWarehouseByName(String name);

        /**\
         * 根据编号查询
         * @param name
         * @return
         */
        Warehouse getWarehouseByCode(String name);

        /**\
         * 新增或者修改
         * @param warehouse
         * @return
         */
        BaseResult saveOrUpdateWarehouse(WarehouseForm warehouse);

        void deleteById(List<Long> ids);

        /**
         * 获取启用的仓库的集合
         * @return
         */
        List<Warehouse> getWarehouse();

        List<LinkedHashMap<String, Object>> queryWarehouseForExcel(Map<String, Object> paramMap);

        /**
         * @description 根据仓库数据查询仓库信息
         * @author  ciro
         * @date   2022/1/17 18:11
         * @param: warehouseId
         * @param: warehouseCode
         * @return: com.jayud.model.po.Warehouse
         **/
        Warehouse selectByWarehouseMsg(Long warehouseId,String warehouseCode);


        /**
         * @description 根据货主id查询仓库信息
         * @author  ciro
         * @date   2022/2/9 17:32
         * @param: owerId
         * @return: com.jyd.component.commons.result.Result
         **/
        BaseResult queryWarehouseByOwerId(Long owerId);

        /**
         * @description 根据用户查询仓库
         * @author  ciro
         * @date   2022/4/9 14:19
         * @param:
         * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.wms.model.po.Warehouse>>
         **/
        BaseResult<List<Warehouse>> getWarehouseByUserId();
}
