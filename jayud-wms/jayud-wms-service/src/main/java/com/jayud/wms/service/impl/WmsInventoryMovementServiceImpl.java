package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.model.bo.InventoryMovementTaskForm;
import com.jayud.wms.model.bo.WmsInventoryMovementForm;
import com.jayud.wms.model.po.InventoryMovementTask;
import com.jayud.wms.model.po.Receipt;
import com.jayud.wms.model.vo.MaterialVO;
import com.jayud.wms.model.vo.ReceiptVO;
import com.jayud.wms.model.vo.WmsInventoryMovementVO;
import com.jayud.wms.service.IInventoryMovementTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.model.po.WmsInventoryMovement;
import com.jayud.wms.mapper.WmsInventoryMovementMapper;
import com.jayud.wms.service.IWmsInventoryMovementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 库存移动订单表 服务实现类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Service
public class WmsInventoryMovementServiceImpl extends ServiceImpl<WmsInventoryMovementMapper, WmsInventoryMovement> implements IWmsInventoryMovementService {


    @Autowired
    private WmsInventoryMovementMapper wmsInventoryMovementMapper;

    @Autowired
    private AuthClient authClient;

    @Autowired
    public IInventoryMovementTaskService inventoryMovementTaskService;

    @Override
    public IPage<WmsInventoryMovementVO> selectPage(WmsInventoryMovementForm wmsInventoryMovement,
                                                    Integer currentPage,
                                                    Integer pageSize,
                                                    HttpServletRequest req){

        Page<WmsInventoryMovementForm> page=new Page<WmsInventoryMovementForm>(currentPage,pageSize);
        IPage<WmsInventoryMovementVO> pageList= wmsInventoryMovementMapper.pageList(page, wmsInventoryMovement);
        return pageList;
    }

    @Override
    public List<WmsInventoryMovement> selectList(WmsInventoryMovement wmsInventoryMovement){
        return wmsInventoryMovementMapper.list(wmsInventoryMovement);
    }

    @Override
    public boolean saveOrUpdateWmsInventoryMovement(WmsInventoryMovementForm wmsInventoryMovementForm) {

        WmsInventoryMovement convert = ConvertUtil.convert(wmsInventoryMovementForm, WmsInventoryMovement.class);
        Date date = new Date();

        //修改
        if(wmsInventoryMovementForm.getId()!=null){





        }else {
            BaseResult baseResult = authClient.getOrderFeign("inventory_movement_main_code", new Date());
            HashMap data = (HashMap)baseResult.getResult();
            //创建
            convert.setMovementCode(data.get("order").toString());
            convert.setCreateBy(CurrentUserUtil.getUsername());
            convert.setCreateTime(date);
            //移库原因
            convert.setRemark(convert.getRemark());

        }
        //创建主订单

        this.updateById(convert);
        Long id = convert.getId();
        //创建移库任务明细
        List<InventoryMovementTaskForm> inventoryMovementTaskForms = wmsInventoryMovementForm.getInventoryMovementTaskForms();

        for (InventoryMovementTaskForm inventoryMovementTaskForm:inventoryMovementTaskForms){
            //移库主表id
            inventoryMovementTaskForm.setMovementCheckId(id);
            if(inventoryMovementTaskForm.getId()!=null){
                inventoryMovementTaskForm.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            } else {
                inventoryMovementTaskForm.setUpdateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            }
        }

        List<InventoryMovementTask> inventoryMovementTasks = ConvertUtil.convertList(inventoryMovementTaskForms, InventoryMovementTask.class);

        inventoryMovementTaskService.saveOrUpdateBatch(inventoryMovementTasks);
        return false;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        wmsInventoryMovementMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        wmsInventoryMovementMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryWmsInventoryMovementForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsInventoryMovementForExcel(paramMap);
    }

    @Override
    public WmsInventoryMovementVO getDetails(Long id) {

        WmsInventoryMovement byId = this.getById(id);
        WmsInventoryMovementVO convert = ConvertUtil.convert(byId, WmsInventoryMovementVO.class);

        InventoryMovementTask inventoryMovementTask = new InventoryMovementTask();
        inventoryMovementTask.setMovementCheckId(convert.getId());
        inventoryMovementTask.setIsDeleted(false);
        List<InventoryMovementTask> byCondition = this.inventoryMovementTaskService.getByCondition(inventoryMovementTask);

        List<InventoryMovementTaskForm> inventoryMovementTaskForms = ConvertUtil.convertList(byCondition, InventoryMovementTaskForm.class);

        convert.setInventoryMovementTaskForms(inventoryMovementTaskForms);

        return convert;
    }

}
