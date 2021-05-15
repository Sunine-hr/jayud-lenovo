package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryShipmentForm;
import com.jayud.mall.model.po.Shipment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.ShipmentVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 南京新智慧-运单装货信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-09
 */
public interface IShipmentService extends IService<Shipment> {

    /**
     * 同步新智慧订单
     * @param shipmentVO
     * @return
     */
    ShipmentVO saveShipment(ShipmentVO shipmentVO);

    /**
     * 分页查询南京新智慧订单装货信息
     * @param form
     * @return
     */
    IPage<ShipmentVO> findShipmentByPage(QueryShipmentForm form);

    /**
     * 根据运单号，查询运单装箱信息
     * @param shipment_id 运单号
     * @return
     */
    CommonResult<ShipmentVO> findShipmentById(String shipment_id);

    /**
     * 根据新智慧运单生成南京订单(订单、订单商品、订单箱号)
     * @param shipment_id 运单表
     * @return
     */
    CommonResult<ShipmentVO> createOrderByShipment(String shipment_id);

    /**
     * 导入Excel的数据
     * @param file
     * @return
     */
    List<List<Object>> importExcelByNewWisdom(MultipartFile file);
}
