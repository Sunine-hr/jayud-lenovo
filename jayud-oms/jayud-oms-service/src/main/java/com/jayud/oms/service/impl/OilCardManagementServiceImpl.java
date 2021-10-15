package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.mapper.OilCardManagementMapper;
import com.jayud.oms.model.bo.AddOilCardManagementForm;
import com.jayud.oms.model.bo.QueryOilCardManagementForm;
import com.jayud.oms.model.enums.OilCardRechargeTypeEnum;
import com.jayud.oms.model.enums.OilCardStatusEnum;
import com.jayud.oms.model.enums.OilCardTypeEnum;
import com.jayud.oms.model.po.OilCardManagement;
import com.jayud.oms.model.vo.OilCardManagementVO;
import com.jayud.oms.service.IDriverInfoService;
import com.jayud.oms.service.IOilCardManagementService;
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
            return new Page<>();
        }
        Map<Long, String> driverInfoMap = this.driverInfoService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
        Map<Long, String> vehicleInfoMap = this.vehicleInfoService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getPlateNumber()));
        records.forEach(e -> {
            e.setDriverName(driverInfoMap.get(e.getDriverId()))
                    .setVehicleName(vehicleInfoMap.get(e.getVehicleId()))
                    .setOilTypeDesc(OilCardTypeEnum.getDesc(e.getOilType()))
                    .setOilStatusDesc(OilCardStatusEnum.getDesc(e.getOilStatus()))
                    .setRechargeTypeDesc(OilCardRechargeTypeEnum.getDesc(e.getRechargeType()));
        });

        return iPage;
    }

    /**
     * 更改启用/禁用状态
     *
     * @param id
     * @return
     */
    @Override
    public boolean enableOrDisable(Long id) {
        //查询当前状态
        QueryWrapper<OilCardManagement> condition = new QueryWrapper<>();
        condition.lambda().select(OilCardManagement::getStatus).eq(OilCardManagement::getId, id);
        OilCardManagement tmp = this.baseMapper.selectOne(condition);

        Integer status = 1 == tmp.getStatus() ? StatusEnum.DISABLE.getCode() : StatusEnum.ENABLE.getCode();

        OilCardManagement update = new OilCardManagement().setId(id).setStatus(status)
                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());

        return this.updateById(update);
    }

    @Override
    public boolean exitByOilCardNum(String oilCardNum) {
        QueryWrapper<OilCardManagement> condition = new QueryWrapper<>();
        condition.lambda().eq(OilCardManagement::getOilCardNum, oilCardNum);
        return this.count(condition) > 0;
    }
}
