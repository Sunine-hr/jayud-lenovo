package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddOilCardManagementForm;
import com.jayud.oms.model.bo.QueryOilCardManagementForm;
import com.jayud.oms.model.enums.OilCardRechargeTypeEnum;
import com.jayud.oms.model.enums.OilCardStatusEnum;
import com.jayud.oms.model.enums.OilCardTypeEnum;
import com.jayud.oms.model.po.DictType;
import com.jayud.oms.model.po.OilCardManagement;
import com.jayud.oms.mapper.OilCardManagementMapper;
import com.jayud.oms.model.vo.OilCardManagementVO;
import com.jayud.oms.model.vo.VehicleInfoVO;
import com.jayud.oms.service.IDriverInfoService;
import com.jayud.oms.service.IDriverOrderInfoService;
import com.jayud.oms.service.IOilCardManagementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.service.IVehicleInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 油卡管理 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-10-11
 */
@Service
public class OilCardManagementServiceImpl extends ServiceImpl<OilCardManagementMapper, OilCardManagement> implements IOilCardManagementService {

    @Autowired
    private IVehicleInfoService vehicleInfoService;
    @Autowired
    private IDriverInfoService driverInfoService;

    @Override
    public void saveOrUpdate(AddOilCardManagementForm form) {
        OilCardManagement oilCardManagement = ConvertUtil.convert(form, OilCardManagement.class);
        if (oilCardManagement.getId() == null) {
            oilCardManagement.setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
        } else {
            oilCardManagement.setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(oilCardManagement);
    }

    @Override
    public IPage<OilCardManagementVO> findByPage(QueryOilCardManagementForm form) {
        Page<OilCardManagementVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<OilCardManagementVO> iPage = this.baseMapper.findByPage(page, form);
        List<OilCardManagementVO> records = iPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return null;
        }
        Map<Long, String> driverInfoMap = this.driverInfoService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
        Map<Long, String> vehicleInfoMap = this.vehicleInfoService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getPlateNumber()));
        records.forEach(e -> {
            e.setDriverName(driverInfoMap.get(e.getDriverId()))
                    .setVehicleName(vehicleInfoMap.get(e.getVehicleId()))
                    .setOilTypeDesc(OilCardTypeEnum.getCode(e.getOilType()))
                    .setOilStatusDesc(OilCardStatusEnum.getDesc(e.getOilStatus()))
                    .setRechargeTypeDesc(OilCardRechargeTypeEnum.getDesc(e.getRechargeType()));
        });

        return iPage;
    }
}
