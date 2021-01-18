package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.oms.mapper.WarehouseInfoMapper;
import com.jayud.oms.model.bo.QueryWarehouseInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.WarehouseInfo;
import com.jayud.oms.model.vo.WarehouseInfoVO;
import com.jayud.oms.service.IWarehouseInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 仓库信息表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Service
public class WarehouseInfoServiceImpl extends ServiceImpl<WarehouseInfoMapper, WarehouseInfo> implements IWarehouseInfoService {

    @Override
    public IPage<WarehouseInfoVO> findWarehouseInfoByPage(QueryWarehouseInfoForm form) {
        Page<WarehouseInfo> page = new Page<>(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("w.id"));
        return this.baseMapper.findWarehouseInfoByPage(page, form);
    }

    @Override
    public boolean checkUnique(WarehouseInfo warehouseInfo) {
        QueryWrapper<WarehouseInfo> condition = new QueryWrapper<>();
        if (warehouseInfo.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(WarehouseInfo::getId, warehouseInfo.getId())
                    .eq(WarehouseInfo::getWarehouseName, warehouseInfo.getWarehouseName()));
            int count = this.count(condition);
            //匹配到自己名称,不进行唯一校验
            if (count == 0) {
                condition = new QueryWrapper<>();
                condition.lambda().eq(WarehouseInfo::getWarehouseName, warehouseInfo.getWarehouseName());
                return this.count(condition) > 0;
            } else {
                return false;
            }
        } else {
            condition.lambda().eq(WarehouseInfo::getWarehouseName, warehouseInfo.getWarehouseName())
                    .or().eq(WarehouseInfo::getWarehouseCode, warehouseInfo.getWarehouseCode());
            return this.count(condition) > 0;
        }

    }

    @Override
    public boolean saveOrUpdateWarehouseInfo(WarehouseInfo warehouseInfo) {
        if (Objects.isNull(warehouseInfo.getId())) {
            warehouseInfo.setStatus(StatusEnum.ENABLE.getCode())
                    .setAuditStatus("1")
                    .setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken());
            return this.save(warehouseInfo);
        } else {
            warehouseInfo.setAuditStatus("1");
            return this.updateById(warehouseInfo);
        }
    }

    @Override
    public boolean enableOrDisableWarehouse(Long id) {
        //查询当前状态
        QueryWrapper<WarehouseInfo> condition = new QueryWrapper<>();
        condition.lambda().select(WarehouseInfo::getStatus).eq(WarehouseInfo::getId, id);
        WarehouseInfo tmp = this.baseMapper.selectOne(condition);

        String status = "1".equals(tmp.getStatus()) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();

        WarehouseInfo warehouseInfo = new WarehouseInfo().setId(id).setStatus(status);
        return this.updateById(warehouseInfo);
    }


}
