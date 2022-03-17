package com.jayud.wms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.WmsOutboundSeedingRecord;
import com.jayud.wms.mapper.WmsOutboundSeedingRecordMapper;
import com.jayud.wms.service.IWmsOutboundSeedingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库播种记录 服务实现类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Service
public class WmsOutboundSeedingRecordServiceImpl extends ServiceImpl<WmsOutboundSeedingRecordMapper, WmsOutboundSeedingRecord> implements IWmsOutboundSeedingRecordService {


    @Autowired
    private WmsOutboundSeedingRecordMapper wmsOutboundSeedingRecordMapper;

    @Override
    public IPage<WmsOutboundSeedingRecord> selectPage(WmsOutboundSeedingRecord wmsOutboundSeedingRecord,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsOutboundSeedingRecord> page=new Page<WmsOutboundSeedingRecord>(currentPage,pageSize);
        IPage<WmsOutboundSeedingRecord> pageList= wmsOutboundSeedingRecordMapper.pageList(page, wmsOutboundSeedingRecord);
        return pageList;
    }

    @Override
    public List<WmsOutboundSeedingRecord> selectList(WmsOutboundSeedingRecord wmsOutboundSeedingRecord){
        return wmsOutboundSeedingRecordMapper.list(wmsOutboundSeedingRecord);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsOutboundSeedingRecordMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundSeedingRecordForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundSeedingRecordForExcel(paramMap);
    }

}
