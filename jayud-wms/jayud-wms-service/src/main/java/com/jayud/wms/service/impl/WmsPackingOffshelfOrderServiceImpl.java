package com.jayud.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.WmsPackingOffshelfOrder;
import com.jayud.wms.model.po.WmsPackingOffshelfTask;
import com.jayud.wms.model.enums.OffshelfTaskStatusEnum;
import com.jayud.wms.mapper.WmsPackingOffshelfOrderMapper;
import com.jayud.wms.service.IWmsPackingOffshelfOrderService;
import com.jayud.wms.service.IWmsPackingOffshelfTaskService;
import com.jayud.wms.model.vo.OutboundOrderNumberVO;
import com.jayud.common.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 拣货下架单 服务实现类
 *
 * @author jyd
 * @since 2022-01-06
 */
@Service
public class WmsPackingOffshelfOrderServiceImpl extends ServiceImpl<WmsPackingOffshelfOrderMapper, WmsPackingOffshelfOrder> implements IWmsPackingOffshelfOrderService {

    @Autowired
    private IWmsPackingOffshelfTaskService wmsPackingOffshelfTaskService;

    @Autowired
    private WmsPackingOffshelfOrderMapper wmsPackingOffshelfOrderMapper;

    @Override
    public IPage<WmsPackingOffshelfOrder> selectPage(WmsPackingOffshelfOrder wmsPackingOffshelfOrder,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsPackingOffshelfOrder> page=new Page<WmsPackingOffshelfOrder>(currentPage,pageSize);
        IPage<WmsPackingOffshelfOrder> pageList= wmsPackingOffshelfOrderMapper.pageList(page, wmsPackingOffshelfOrder);
        return pageList;
    }

    @Override
    public List<WmsPackingOffshelfOrder> selectList(WmsPackingOffshelfOrder wmsPackingOffshelfOrder){
        return wmsPackingOffshelfOrderMapper.list(wmsPackingOffshelfOrder);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsPackingOffshelfOrderMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsPackingOffshelfOrderForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsPackingOffshelfOrderForExcel(paramMap);
    }

    @Override
    public BaseResult saveOrder(OutboundOrderNumberVO outboundOrderNumberVO) {
        List<WmsPackingOffshelfOrder> saveList = new ArrayList<>();
        List<WmsPackingOffshelfOrder> updateList = new ArrayList<>();
        for (String orderNumber : outboundOrderNumberVO.getOrderNumberList()){
            boolean isAdd = true;
            WmsPackingOffshelfOrder order = getOrderMsg(orderNumber);
            if (order != null){
                isAdd = false;
                order.setPackingOffshelfNumber(orderNumber);
            }else {
                order = new WmsPackingOffshelfOrder();
            }
            getPackingCount(order);
            if (isAdd){
                this.save(order);
                saveList.add(order);
            }else {
                this.updateById(order);
                updateList.add(order);
            }
        }
        return BaseResult.ok();
    }

    /**
     * @description 根据编码查询数据
     * @author  ciro
     * @date   2022/1/6 15:29
     * @param: orderNumber
     * @return: com.jayud.model.po.WmsPackingOffshelfOrder
     **/
    private WmsPackingOffshelfOrder getOrderMsg(String orderNumber){
        LambdaQueryWrapper<WmsPackingOffshelfOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsPackingOffshelfOrder::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsPackingOffshelfOrder::getPackingOffshelfNumber,orderNumber);
        WmsPackingOffshelfOrder order = this.getOne(lambdaQueryWrapper);
        return order;
    }

    /**
     * @description 获取下架单状态数量
     * @author  ciro
     * @date   2022/1/6 15:38
     * @param: orderNumber
     * @return: com.jayud.model.vo.WmsPackingOffshelfVO
     **/
    private void getPackingCount(WmsPackingOffshelfOrder order){
        WmsPackingOffshelfTask task = new WmsPackingOffshelfTask();
        task.setPackingOffshelfNumber(order.getPackingOffshelfNumber());
        int allCount = 0;
        int unfinishCount = 0;
        int finishCount = 0;
        List<WmsPackingOffshelfTask> taskList = wmsPackingOffshelfTaskService.selectList(task);
        allCount = taskList.size();
        for (WmsPackingOffshelfTask tasks : taskList){
            if (tasks.getStatus().equals(OffshelfTaskStatusEnum.FINISH_PACKING.getStatus())){
                finishCount += 1;
            }else {
                unfinishCount += 1;
            }
            order.setOrderNumber(tasks.getOrderNumber());
            order.setWaveNumber(tasks.getWaveNumber());
        }
        order.setPackingOffshelfNumber(taskList.get(0).getPackingOffshelfNumber());
        order.setOrderNumber(taskList.get(0).getOrderNumber());
        order.setWaveNumber(taskList.get(0).getWaveNumber());
        order.setAllDetailCount(allCount);
        order.setFinishDetailCount(finishCount);
        order.setUnfinishDetailCount(unfinishCount);
    }

}
