package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.DriverOrderInfoMapper;
import com.jayud.oms.model.enums.DriverFeedbackStatusEnum;
import com.jayud.oms.model.enums.DriverOrderStatusEnum;
import com.jayud.oms.model.po.DriverOrderInfo;
import com.jayud.oms.service.IDriverOrderInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 司机接单信息(微信小程序) 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-10
 */
@Service
public class DriverOrderInfoServiceImpl extends ServiceImpl<DriverOrderInfoMapper, DriverOrderInfo> implements IDriverOrderInfoService {

    /**
     * 查询司机接单信息
     */
    @Override
    public List<DriverOrderInfo> getDriverOrderInfoByStatus(Long driverId, String status, Long jockeyId) {
        QueryWrapper<DriverOrderInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverOrderInfo::getDriverId, driverId);
        if (jockeyId != null) {
            condition.lambda().or().eq(DriverOrderInfo::getJockeyId, jockeyId);
        }
        if (status != null) {
            condition.lambda().eq(DriverOrderInfo::getStatus, status);
        }

        return this.baseMapper.selectList(condition);
    }


    @Override
    public boolean saveOrUpdateDriverOrder(DriverOrderInfo driverOrderInfo) {
        if (driverOrderInfo.getId() == null) {
            driverOrderInfo.setTime(LocalDateTime.now());
            return this.save(driverOrderInfo);
        } else {
            return this.updateById(driverOrderInfo);
        }
    }

    @Override
    public DriverOrderInfo getByOrderId(Long orderId) {
        QueryWrapper<DriverOrderInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverOrderInfo::getOrderId, orderId);
        return this.baseMapper.selectOne(condition);
    }

    /**
     * 修改司机接单状态
     */
    @Override
    public boolean updateStatus(Long orderId, String status) {
        QueryWrapper<DriverOrderInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverOrderInfo::getOrderId, orderId);
        return this.update(new DriverOrderInfo().setStatus(status), condition);
    }

    /**
     * 订单信息是否存在
     */
    @Override
    public boolean isExistOrder(Long orderId) {
        QueryWrapper<DriverOrderInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverOrderInfo::getOrderId, orderId);
        return this.count(condition) > 0;
    }

    @Override
    public void synchronizeTmsStatus(Map<String, Object> processNode, Long orderId) {
        //查询流程节点是否完成
        if (DriverFeedbackStatusEnum.FOUR.getCode().equals(processNode.get("id"))
                && !Boolean.parseBoolean(processNode.get("isEdit").toString())) {
            //同步状态
            DriverOrderInfo driverOrderInfo = this.getByOrderId(orderId);
            if (!DriverOrderStatusEnum.FINISHED.getCode().equals(driverOrderInfo.getStatus())) {
                this.saveOrUpdateDriverOrder(new DriverOrderInfo().setId(driverOrderInfo.getId())
                        .setStatus(DriverOrderStatusEnum.FINISHED.getCode()));

            }
        }
    }
}
