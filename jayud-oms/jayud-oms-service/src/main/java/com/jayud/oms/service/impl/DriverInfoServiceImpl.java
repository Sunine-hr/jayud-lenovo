package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.oms.mapper.DriverInfoMapper;
import com.jayud.oms.model.bo.QueryDriverInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.model.vo.DriverInfoLinkVO;
import com.jayud.oms.model.vo.DriverInfoVO;
import com.jayud.oms.service.IDriverInfoService;
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
        //密码MD5加密
//        if (StringUtils.isNotEmpty(driverInfo.getPassword())) {
//            driverInfo.setPassword(MD5.encode(driverInfo.getPassword()));
//        }

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
        if (driverInfo.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(DriverInfo::getId, driverInfo.getId())
                    .eq(DriverInfo::getName, driverInfo.getName()));
            int count = this.count(condition);
            if (count > 0) {
                //匹配到自己名称,不进行唯一校验
                return false;
            }
        }
        condition = new QueryWrapper<>();
        condition.lambda().eq(DriverInfo::getName, driverInfo.getName());
        return this.count(condition) > 0;
    }

    @Override
    public boolean enableOrDisableDriver(Long id) {
        //查询当前状态
        QueryWrapper<DriverInfo> condition = new QueryWrapper<>();
        condition.lambda().select(DriverInfo::getStatus).eq(DriverInfo::getId, id);
        DriverInfo tmp = this.baseMapper.selectOne(condition);

        String status = StatusEnum.ENABLE.getCode().equals(tmp.getStatus()) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();

        DriverInfo driverInfo = new DriverInfo().setId(id).setStatus(status);
        return this.updateById(driverInfo);
    }

    @Override
    public DriverInfoLinkVO getDriverInfoLink(Long driverId) {
        return baseMapper.getDriverInfoLink(driverId);
    }


    /**
     * 根据司机大陆手机查询用户
     *
     * @param phone
     * @return
     */
    @Override
    public DriverInfo getByPhone(String phone) {
        QueryWrapper<DriverInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverInfo::getPhone, phone);
        return this.baseMapper.selectOne(condition);
    }

}
