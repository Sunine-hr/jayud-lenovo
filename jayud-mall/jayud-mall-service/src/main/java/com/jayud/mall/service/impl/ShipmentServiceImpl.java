package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.ShipmentMapper;
import com.jayud.mall.model.po.Shipment;
import com.jayud.mall.model.vo.ShipmentVO;
import com.jayud.mall.service.IShipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>
 * 南京新智慧-运单装货信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-09
 */
@Slf4j
@Service
public class ShipmentServiceImpl extends ServiceImpl<ShipmentMapper, Shipment> implements IShipmentService {

    @Autowired
    ShipmentMapper shipmentMapper;

    @Override
    public ShipmentVO saveShipment(ShipmentVO shipmentVO) {
        String shipment_id = shipmentVO.getShipment_id();
        ShipmentVO shipment = shipmentMapper.findShipment(shipment_id);
        if(ObjectUtil.isEmpty(shipment)){
            //新增插入
            log.info("新增插入:{}", shipmentVO);
        }else{
            //更新修改
            log.info("更新修改:{}", shipmentVO);
        }
        Shipment s = ConvertUtil.convert(shipmentVO, Shipment.class);
        if(ObjectUtil.isNotEmpty(shipmentVO.getPicking_time())){
            long l = shipmentVO.getPicking_time()*1000L;//秒转为毫秒
            LocalDateTime ldt = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
            s.setPicking_time(ldt);
        }
        if(ObjectUtil.isNotEmpty(shipmentVO.getRates_time())){
            long l = shipmentVO.getRates_time()*1000L;//秒转为毫秒
            LocalDateTime ldt = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
            s.setRates_time(ldt);
        }
        if(ObjectUtil.isNotEmpty(shipmentVO.getCreat_time())){
            long l = shipmentVO.getCreat_time()*1000L;//秒转为毫秒
            LocalDateTime ldt = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
            s.setCreat_time(ldt);
        }
        s.setShipmentJson(JSONUtil.toJsonStr(shipmentVO));
        this.saveOrUpdate(s);
        return shipmentVO;
    }
}
