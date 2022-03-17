package com.jayud.wms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.AddWmsOutboundSeedingForm;
import com.jayud.wms.model.po.WmsOutboundSeeding;
import com.jayud.wms.model.po.WmsPackingOffshelfTask;
import com.jayud.wms.mapper.WmsOutboundSeedingMapper;
import com.jayud.wms.service.IWmsOutboundSeedingService;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 出库播种 服务实现类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Service
public class WmsOutboundSeedingServiceImpl extends ServiceImpl<WmsOutboundSeedingMapper, WmsOutboundSeeding> implements IWmsOutboundSeedingService {


    @Autowired
    private WmsOutboundSeedingMapper wmsOutboundSeedingMapper;

    @Override
    public IPage<WmsOutboundSeeding> selectPage(WmsOutboundSeeding wmsOutboundSeeding,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsOutboundSeeding> page=new Page<WmsOutboundSeeding>(currentPage,pageSize);
        IPage<WmsOutboundSeeding> pageList= wmsOutboundSeedingMapper.pageList(page, wmsOutboundSeeding);
        return pageList;
    }

    @Override
    public List<WmsOutboundSeeding> selectList(WmsOutboundSeeding wmsOutboundSeeding){
        return wmsOutboundSeedingMapper.list(wmsOutboundSeeding);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsOutboundSeedingMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundSeedingForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundSeedingForExcel(paramMap);
    }

    @Override
    public void createPackingToSeeding(List<WmsPackingOffshelfTask> taskList) {
        List<WmsOutboundSeeding> seedingList = new ArrayList<>();
        taskList.forEach(task -> {
            seedingList.add(initSeeding(task));
        });
        this.saveBatch(seedingList);
    }


    /**
     * @description 格式化出库播种数据
     * @author  ciro
     * @date   2021/12/25 11:30
     * @param: task
     * @return: com.jayud.model.po.WmsOutboundSeeding
     **/
    private WmsOutboundSeeding initSeeding(WmsPackingOffshelfTask task){
        WmsOutboundSeeding seeding = new WmsOutboundSeeding();
        seeding.setSowingId(task.getDeliverySowingId());
        seeding.setSowingCode(task.getDeliverySowingCode());
        seeding.setSowingName(task.getDeliverySowingName());
        seeding.setMaterialId(task.getMaterialId());
        seeding.setMaterialCode(task.getMaterialCode());
        seeding.setMaterialName(task.getMaterialName());
        seeding.setOldContainerId(task.getContainerId());
        seeding.setOldContainerCode(task.getContainerCode());
        seeding.setOldContainerName(task.getContainerName());
        seeding.setOldAccount(task.getRealOffshelfAccount().intValue());
        seeding.setOldSeedingAccount(0);
        seeding.setOldRealSeedingAccount(task.getRealOffshelfAccount().intValue());
        if (StringUtils.isNotBlank(task.getWaveNumber())){
            seeding.setWaveNumber(task.getWaveNumber());
        }
        if (StringUtils.isNotBlank(task.getOrderNumber())){
            seeding.setOrderNumber(task.getOrderNumber());
            seeding.setNoticeNumber(task.getNoticOrderNumber());
        }
        return seeding;
    }

    @Override
    public boolean saveOrUpdateWmsOutboundSeeding(List<AddWmsOutboundSeedingForm> form) {
        List<WmsOutboundSeeding> wmsOutboundSeedings = ConvertUtil.convertList(form, WmsOutboundSeeding.class);
        for (WmsOutboundSeeding wmsOutboundSeeding : wmsOutboundSeedings) {
            wmsOutboundSeeding.setStatus(1);
            if(wmsOutboundSeeding.getId() != null){
                wmsOutboundSeeding.setUpdateTime(new Date());
                wmsOutboundSeeding.setUpdateBy(CurrentUserUtil.getUsername());
            }else{
                wmsOutboundSeeding.setCreateTime(new Date());
                wmsOutboundSeeding.setCreateBy(CurrentUserUtil.getUsername());
            }
        }
        boolean results = this.saveOrUpdateBatch(wmsOutboundSeedings);
        if(results){
            log.warn("确认更换成功");

        }
        return results;
    }



}
