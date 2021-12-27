package com.jayud.tools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.DateUtils;
import com.jayud.tools.model.bo.AddFbaOrderTrackForm;
import com.jayud.tools.model.po.FbaOrder;
import com.jayud.tools.model.po.FbaOrderTrack;
import com.jayud.tools.mapper.FbaOrderTrackMapper;
import com.jayud.tools.service.IFbaOrderTrackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * fba订单轨迹表 服务实现类
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@Service
public class FbaOrderTrackServiceImpl extends ServiceImpl<FbaOrderTrackMapper, FbaOrderTrack> implements IFbaOrderTrackService {

    @Override
    public List<FbaOrderTrack> getFbaOrderTrackByOrderId(Integer id) {
        QueryWrapper<FbaOrderTrack> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FbaOrderTrack::getOrderId,id);
        queryWrapper.lambda().eq(FbaOrderTrack::getIsDelete,0);
        queryWrapper.lambda().orderByDesc(FbaOrderTrack::getTrajectoryTime);
        return this.list(queryWrapper);
    }

    @Override
    public void deleteById(List<Long> ids) {
        List<FbaOrderTrack> fbaOrders = new ArrayList<>();
        for (Long id : ids) {
            FbaOrderTrack fbaOrder = new FbaOrderTrack();
            fbaOrder.setId(id.intValue());
            fbaOrder.setIsDelete(1);
            fbaOrder.setUpdateTime(LocalDateTime.now());
            fbaOrder.setUpdateUser(UserOperator.getToken());
            fbaOrders.add(fbaOrder);
        }
        boolean result = this.updateBatchById(fbaOrders);
        if(result){
            log.warn("删除订单成功");
        }
    }

    @Override
    public void saveOrUpdateFbaOrderTrack(AddFbaOrderTrackForm addFbaOrderTrackForm) {
        List<FbaOrderTrack> fbaOrderTracks = new ArrayList<>();
        for (Integer orderId : addFbaOrderTrackForm.getOrderIds()) {
            FbaOrderTrack fbaOrderTrack = new FbaOrderTrack();
            fbaOrderTrack.setOrderId(orderId);
            fbaOrderTrack.setOperationInformation(addFbaOrderTrackForm.getOperationInformation());
            fbaOrderTrack.setRemark(addFbaOrderTrackForm.getRemark());
            fbaOrderTrack.setTrajectoryTime(DateUtils.str2LocalDateTime(addFbaOrderTrackForm.getTrajectoryTime(),"yyyy-MM-dd HH:mm:ss"));
            fbaOrderTrack.setCreateTime(LocalDateTime.now());
            fbaOrderTrack.setCreateUser(addFbaOrderTrackForm.getLoginUserName());
            fbaOrderTracks.add(fbaOrderTrack);
        }
        boolean result = this.saveBatch(fbaOrderTracks);
        if(result){
            log.warn("新增订单轨迹成功");
        }
    }
}
