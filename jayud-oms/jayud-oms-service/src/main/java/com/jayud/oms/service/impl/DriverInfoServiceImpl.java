package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.oms.model.bo.QueryDriverInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.mapper.DriverInfoMapper;
import com.jayud.oms.model.vo.DriverInfoVO;
import com.jayud.oms.service.IDriverInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 供应商对应司机信息 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Service
public class DriverInfoServiceImpl extends ServiceImpl<DriverInfoMapper, DriverInfo> implements IDriverInfoService {

    /**
     * 分页查询司机信息
     *
     * @param form
     * @return
     */
    @Override
    public IPage<DriverInfoVO> findDriverInfoByPage(QueryDriverInfoForm form) {
        Page page = new Page(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findDriverInfoByPage(page, form);
    }

    /**
     * 新增编辑司机信息
     *
     * @param driverInfo
     * @return
     */
    @Override
    public boolean saveOrUpdateDriverInfo(DriverInfo driverInfo) {
        if (Objects.isNull(driverInfo.getId())) {
            driverInfo.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken()).setStatus(StatusEnum.ENABLE.getCode());
            return this.save(driverInfo);
        } else {
            driverInfo
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken());
            return this.updateById(driverInfo);
        }
    }

    @Override
    public boolean checkUnique(DriverInfo driverInfo) {
        QueryWrapper<DriverInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverInfo::getName, driverInfo.getName());
        return this.count(condition) > 0;
    }
}
