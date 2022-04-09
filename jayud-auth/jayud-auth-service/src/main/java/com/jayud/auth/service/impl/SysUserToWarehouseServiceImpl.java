package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.feign.WmsClient;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.ListUtils;
import com.jayud.wms.model.po.Warehouse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysUserToWarehouse;
import com.jayud.auth.mapper.SysUserToWarehouseMapper;
import com.jayud.auth.service.ISysUserToWarehouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户与仓库关联表 服务实现类
 *
 * @author jayud
 * @since 2022-04-08
 */
@Slf4j
@Service
public class SysUserToWarehouseServiceImpl extends ServiceImpl<SysUserToWarehouseMapper, SysUserToWarehouse> implements ISysUserToWarehouseService {


    @Autowired
    private SysUserToWarehouseMapper sysUserToWarehouseMapper;

    @Autowired
    private WmsClient wmsClient;

    @Override
    public IPage<SysUserToWarehouse> selectPage(SysUserToWarehouse sysUserToWarehouse,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysUserToWarehouse> page=new Page<SysUserToWarehouse>(currentPage,pageSize);
        IPage<SysUserToWarehouse> pageList= sysUserToWarehouseMapper.pageList(page, sysUserToWarehouse);
        return pageList;
    }

    @Override
    public List<SysUserToWarehouse> selectList(SysUserToWarehouse sysUserToWarehouse){
        return sysUserToWarehouseMapper.list(sysUserToWarehouse);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysUserToWarehouseMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysUserToWarehouseMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> querySysUserToWarehouseForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.querySysUserToWarehouseForExcel(paramMap);
    }

    @Override
    public BaseResult saveWarehouse(Long userId, List<SysUserToWarehouse> warehouseList) {
        //上次关联数据
        LambdaQueryWrapper<SysUserToWarehouse> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserToWarehouse::getIsDeleted,false);
        lambdaQueryWrapper.eq(SysUserToWarehouse::getUserId,userId);
        List<SysUserToWarehouse> lastWarehouseList = this.list(lambdaQueryWrapper);

        //id为空新增
        List<SysUserToWarehouse> addList = new ArrayList<>();
        List<String> thisIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(warehouseList)){
            addList = warehouseList.stream().filter(x->x.getId()==null).collect(Collectors.toList());
            thisIdList = warehouseList.stream().filter(x->x.getId()!=null).map(x->String.valueOf(x.getId())).collect(Collectors.toList());
        }
        //删除id集合
        List<String> delIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(lastWarehouseList)){
            List<String> lastIdList = lastWarehouseList.stream().map(x->String.valueOf(x.getId())).collect(Collectors.toList());
            delIdList = ListUtils.getDiff(thisIdList,lastIdList);
        }
        if (CollUtil.isNotEmpty(addList)){
            addList.forEach(msg->{
                msg.setUserId(userId);
            });
            this.saveBatch(addList);
        }
        if (CollUtil.isNotEmpty(delIdList)){
            sysUserToWarehouseMapper.logicDelByIds(delIdList,CurrentUserUtil.getUsername());
        }
        return BaseResult.ok();
    }

    @Override
    public List<SysUserToWarehouse> getWarehouseMsg() {
        List<SysUserToWarehouse> warehouseList = new ArrayList<>();
        BaseResult<List<Warehouse>> warehouseResult = wmsClient.selectWarehouseList(CurrentUserUtil.getUserTenantCode(),1);
        if (CollUtil.isNotEmpty(warehouseResult.getResult())){
            warehouseResult.getResult().forEach(warehouse -> {
                SysUserToWarehouse sysUserToWarehouse = new SysUserToWarehouse();
                sysUserToWarehouse.setWarehouseId(warehouse.getId());
                sysUserToWarehouse.setWarehouseCode(warehouse.getCode());
                sysUserToWarehouse.setWarehouseName(warehouse.getName());
                warehouseList.add(sysUserToWarehouse);
            });
        }
        return warehouseList;
    }

}
