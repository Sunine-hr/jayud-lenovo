package com.jayud.wms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsOwerToWarehouseRelation;

/**
 * 货主信息 服务类
 *
 * @author jyd
 * @since 2021-12-13
 */
public interface IWmsOwerToWarehouseRelationService extends IService<WmsOwerToWarehouseRelation> {

        /**
         *  分页查询
         * @param wmsOwerInfo
         * @param pageNo
         * @param pageSize
         * @param req
         * @return
         */
//        IPage<WmsOwerInfo> selectPage(WmsOwerInfo wmsOwerInfo,
//                                    Integer pageNo,
//                                    Integer pageSize,
//                                    HttpServletRequest req);

        /**
         *  查询列表
         * @param wmsOwerInfo
         * @return
         */
//        List<WmsOwerInfo> selectList(WmsOwerInfo wmsOwerInfo);

        /**\
         * 新增或者修改
         * @param wmsOwerInfoForm
         * @return
         */
//        boolean saveOrUpdateWmsCustomerInfo(WmsOwerInfoForm wmsOwerInfoForm);



}
