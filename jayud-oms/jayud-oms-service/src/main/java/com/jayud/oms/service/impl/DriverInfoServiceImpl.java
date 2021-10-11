package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.mapper.DriverInfoMapper;
import com.jayud.oms.model.bo.QueryDriverInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.model.vo.DriverInfoLinkVO;
import com.jayud.oms.model.vo.DriverInfoVO;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.model.vo.VehicleInfoVO;
import com.jayud.oms.service.IDriverInfoService;
import com.jayud.oms.service.IVehicleInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    @Autowired
    IVehicleInfoService vehicleInfoService;

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

    /**
     * TODO 车辆管理关联关系更改,小程序需要更改
     *
     * @param driverId
     * @return
     */
    @Override
    public DriverInfoLinkVO getDriverInfoLink(Long driverId) {
        DriverInfo driverInfo = getById(driverId);
        if (driverInfo != null) {
            DriverInfoLinkVO driverInfoLinkVO = new DriverInfoLinkVO();
            driverInfoLinkVO.setDriverName(driverInfo.getName());
            driverInfoLinkVO.setDriverPhone(driverInfo.getPhone());
            //获取中港车
            List<VehicleInfoVO> tmp = new ArrayList<>();
            List<VehicleInfoVO> vehicleInfoVOs = vehicleInfoService.findTmsVehicle();
            for (VehicleInfoVO vehicleInfoVO : vehicleInfoVOs) {
                if (vehicleInfoVO.getDriverIds() != null && vehicleInfoVO.getDriverIds().contains(driverInfo.getId())) {
                    tmp.add(vehicleInfoVO);
                }
            }
            driverInfoLinkVO.setVehicleInfoVOList(tmp);
            return driverInfoLinkVO;
        }
        return new DriverInfoLinkVO();
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

    /**
     * 查询启用的司机
     */
    @Override
    public List<DriverInfo> getEnableDriverInfo(String driverName) {
        QueryWrapper<DriverInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverInfo::getStatus, StatusEnum.ENABLE.getCode());
        if (!StringUtils.isEmpty(driverName)) {
            condition.lambda().like(DriverInfo::getName, driverName);
        }
        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据司机id集合拼装司机名称
     */
    @Override
    public String assemblyDriverName(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        Collection<DriverInfo> driverInfos = this.listByIds(ids);
        StringBuilder sb = new StringBuilder();
        driverInfos.forEach(e -> sb.append(e.getName()).append(","));
        return sb.toString();
    }

    /**
     * 下拉有效司机
     */
    @Override
    public List<InitComboxStrVO> initEffectiveDriver() {
        QueryWrapper<DriverInfo> condition = new QueryWrapper<>();
        //TODO 还有缺少有效时间条件,等司机管理追加字段
        condition.lambda().eq(DriverInfo::getStatus, StatusEnum.ENABLE.getCode());
        List<DriverInfo> driverInfos = this.baseMapper.selectList(condition);
        List<InitComboxStrVO> list = new ArrayList<>();
        driverInfos.forEach(e -> {
            InitComboxStrVO tmp = new InitComboxStrVO();
            tmp.setId(e.getId());
            tmp.setName(e.getName());
            list.add(tmp);
        });
        return list;
    }


}
