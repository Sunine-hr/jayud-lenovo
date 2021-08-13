package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.po.AppletOrderAddr;
import com.jayud.oms.model.po.AppletOrderRecord;
import com.jayud.oms.mapper.AppletOrderRecordMapper;
import com.jayud.oms.model.vo.AppletOrderAddrVO;
import com.jayud.oms.model.vo.AppletOrderRecordVO;
import com.jayud.oms.model.vo.DriverOrderTakeAdrVO;
import com.jayud.oms.model.vo.DriverOrderTransportVO;
import com.jayud.oms.service.IAppletOrderAddrService;
import com.jayud.oms.service.IAppletOrderRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序订单记录 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-12
 */
@Service
public class AppletOrderRecordServiceImpl extends ServiceImpl<AppletOrderRecordMapper, AppletOrderRecord> implements IAppletOrderRecordService {

    @Autowired
    private IAppletOrderAddrService appletOrderAddrService;


    @Override
    public List<AppletOrderRecord> getByCondition(AppletOrderRecord appletOrderRecord) {
        return this.baseMapper.selectList(new QueryWrapper<>(appletOrderRecord));
    }

    @Override
    public List<DriverOrderTransportVO> getConvertPendingRecord() {
        List<AppletOrderRecord> appletOrderRecords = this.baseMapper.selectList(new QueryWrapper<>(new AppletOrderRecord().setRecordStatus(1)));
        if (CollectionUtils.isEmpty(appletOrderRecords)) {
            return new ArrayList<>();
        }
        List<Long> appletOrderRecordIds = appletOrderRecords.stream().map(e -> e.getId()).collect(Collectors.toList());
        QueryWrapper<AppletOrderAddr> condition = new QueryWrapper<>();
        condition.lambda().in(AppletOrderAddr::getAppletOrderRecordId, appletOrderRecordIds);
        List<AppletOrderAddr> appletOrderAddrs = this.appletOrderAddrService.getBaseMapper().selectList(condition);

        List<DriverOrderTransportVO> driverOrderTransportVOS = ConvertUtil.convertList(appletOrderRecords, DriverOrderTransportVO.class);
        List<DriverOrderTakeAdrVO> driverOrderTakeAdrVOS = ConvertUtil.convertList(appletOrderAddrs, DriverOrderTakeAdrVO.class);
        driverOrderTransportVOS.forEach(e -> {
            e.groupAddr(driverOrderTakeAdrVOS);
        });
        driverOrderTransportVOS.forEach(DriverOrderTransportVO::assemblyAddr);
        return driverOrderTransportVOS;
    }
}
