package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddFleetManagementForm;
import com.jayud.oms.model.bo.QueryFleetManagementForm;
import com.jayud.oms.model.po.FleetManagement;
import com.jayud.oms.mapper.FleetManagementMapper;
import com.jayud.oms.model.po.MaintenanceManagement;
import com.jayud.oms.model.po.OilCardManagement;
import com.jayud.oms.model.vo.FleetManagementVO;
import com.jayud.oms.service.IDriverInfoService;
import com.jayud.oms.service.IFleetManagementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 车队管理 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-10-13
 */
@Service
public class FleetManagementServiceImpl extends ServiceImpl<FleetManagementMapper, FleetManagement> implements IFleetManagementService {

    @Autowired
    private IDriverInfoService driverInfoService;

    @Override
    public void saveOrUpdate(AddFleetManagementForm form) {
        //TODO 按规则生成车队编号
        FleetManagement tmp = ConvertUtil.convert(form, FleetManagement.class);
        if (tmp.getId() == null) {
            tmp.setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
        } else {
            tmp.setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(tmp);
    }

    @Override
    public IPage<FleetManagementVO> findByPage(QueryFleetManagementForm form) {
        Page<FleetManagement> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<FleetManagementVO> iPage = this.baseMapper.findByPage(page, form);
        Map<Long, String> driverMap = this.driverInfoService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
        iPage.getRecords().stream().forEach(e -> {
            e.setDriverName(driverMap.get(e.getDriverId()));
        });
        return iPage;
    }

    @Override
    public boolean enableOrDisable(Long id) {
        //查询当前状态
        QueryWrapper<FleetManagement> condition = new QueryWrapper<>();
        condition.lambda().select(FleetManagement::getStatus).eq(FleetManagement::getId, id);
        FleetManagement tmp = this.baseMapper.selectOne(condition);

        Integer status = 1 == tmp.getStatus() ? StatusEnum.DISABLE.getCode():StatusEnum.ENABLE.getCode();

        FleetManagement update = new FleetManagement().setId(id).setStatus(status)
                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());

        return this.updateById(update);
    }
}
