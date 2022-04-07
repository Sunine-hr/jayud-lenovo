package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.utils.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToService;
import com.jayud.wms.mapper.WmsOutboundOrderInfoToServiceMapper;
import com.jayud.wms.service.IWmsOutboundOrderInfoToServiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 出库单-附加服务 服务实现类
 *
 * @author jayud
 * @since 2022-04-06
 */
@Slf4j
@Service
public class WmsOutboundOrderInfoToServiceServiceImpl extends ServiceImpl<WmsOutboundOrderInfoToServiceMapper, WmsOutboundOrderInfoToService> implements IWmsOutboundOrderInfoToServiceService {


    @Autowired
    private WmsOutboundOrderInfoToServiceMapper wmsOutboundOrderInfoToServiceMapper;

    @Override
    public IPage<WmsOutboundOrderInfoToService> selectPage(WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsOutboundOrderInfoToService> page=new Page<WmsOutboundOrderInfoToService>(currentPage,pageSize);
        IPage<WmsOutboundOrderInfoToService> pageList= wmsOutboundOrderInfoToServiceMapper.pageList(page, wmsOutboundOrderInfoToService);
        return pageList;
    }

    @Override
    public List<WmsOutboundOrderInfoToService> selectList(WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService){
        return wmsOutboundOrderInfoToServiceMapper.list(wmsOutboundOrderInfoToService);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        wmsOutboundOrderInfoToServiceMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        wmsOutboundOrderInfoToServiceMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoToServiceForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundOrderInfoToServiceForExcel(paramMap);
    }

    @Override
    public void saveService(String orderNumber, List<WmsOutboundOrderInfoToService> serviceList) {
        if (CollUtil.isNotEmpty(serviceList)){
            LambdaQueryWrapper<WmsOutboundOrderInfoToService> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(WmsOutboundOrderInfoToService::getIsDeleted,false);
            lambdaQueryWrapper.eq(WmsOutboundOrderInfoToService::getOrderNumber,orderNumber);
            List<WmsOutboundOrderInfoToService> lastServiceList = this.list(lambdaQueryWrapper);
            if (CollUtil.isNotEmpty(lastServiceList)){
                List<String> lastIdList = lastServiceList.stream().map(x->String.valueOf(x.getId())).collect(Collectors.toList());
                List<String> thisIdList = serviceList.stream().filter(x->x.getId()!=null).map(x->String.valueOf(x.getId())).collect(Collectors.toList());
                List<WmsOutboundOrderInfoToService> updateList = serviceList.stream().filter(x->x.getId()!=null).collect(Collectors.toList());
                List<WmsOutboundOrderInfoToService> saveList = serviceList.stream().filter(x->x.getId()==null).collect(Collectors.toList());
                //删除
                List<String> delLists = ListUtils.getDiff(thisIdList,lastIdList);
                if (CollUtil.isNotEmpty(delLists)){
                    this.baseMapper.logicDelByIds(delLists,CurrentUserUtil.getUsername());
                }
                if (CollUtil.isNotEmpty(updateList)){
                    this.updateBatchById(updateList);
                }
                if (CollUtil.isNotEmpty(saveList)){
                    this.saveBatch(saveList);
                }
            }else {
                serviceList.forEach(servece -> {
                    servece.setOrderNumber(orderNumber);
                });
                this.saveBatch(serviceList);
            }
        }
    }

}
