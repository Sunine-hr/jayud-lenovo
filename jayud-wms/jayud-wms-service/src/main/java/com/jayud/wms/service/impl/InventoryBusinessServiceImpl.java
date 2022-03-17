package com.jayud.wms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.model.po.InventoryBusiness;
import com.jayud.wms.mapper.InventoryBusinessMapper;
import com.jayud.wms.service.IInventoryBusinessService;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存事务表 服务实现类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Service
public class InventoryBusinessServiceImpl extends ServiceImpl<InventoryBusinessMapper, InventoryBusiness> implements IInventoryBusinessService {


    @Autowired
    private InventoryBusinessMapper inventoryBusinessMapper;
    @Resource
    private AuthClient authClient;

    @Override
    public IPage<InventoryBusiness> selectPage(InventoryBusiness inventoryBusiness,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<InventoryBusiness> page=new Page<InventoryBusiness>(pageNo, pageSize);
        IPage<InventoryBusiness> pageList= inventoryBusinessMapper.pageList(page, inventoryBusiness);
        return pageList;
    }

    @Override
    public List<InventoryBusiness> selectList(InventoryBusiness inventoryBusiness){
        return inventoryBusinessMapper.list(inventoryBusiness);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryBusiness saveOrUpdateInventoryBusiness(InventoryBusiness inventoryBusiness) {
        Long id = inventoryBusiness.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增 --> add 创建人、创建时间
            inventoryBusiness.setCreateBy(CurrentUserUtil.getUsername());
            inventoryBusiness.setCreateTime(new Date());
        }else{
            //修改 --> update 更新人、更新时间
            inventoryBusiness.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryBusiness.setUpdateTime(new Date());
        }
        this.saveOrUpdate(inventoryBusiness);
        return inventoryBusiness;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delInventoryBusiness(int id) {
        InventoryBusiness inventoryBusiness = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(inventoryBusiness)){
            throw new IllegalArgumentException("库存事务表不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        inventoryBusiness.setUpdateBy(CurrentUserUtil.getUsername());
        inventoryBusiness.setUpdateTime(new Date());
        inventoryBusiness.setIsDeleted(true);
        this.saveOrUpdate(inventoryBusiness);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryInventoryBusinessForExcel(Map<String, Object> paramMap) {
        AuthUserDetail userDetail = CurrentUserUtil.getUserDetail();
        List<String> owerIdList = authClient.getOwerIdByUserId(String.valueOf(userDetail.getId()));
        paramMap.put("owerIdList",owerIdList);
        List<String> warehouseIdList = authClient.getWarehouseIdByUserId(String.valueOf(userDetail.getId()));
        paramMap.put("warehouseIdList",warehouseIdList);
        return this.baseMapper.queryInventoryBusinessForExcel(paramMap);
    }


}
