package com.jayud.wms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.WmsWaveToOutboundInfo;
import com.jayud.wms.mapper.WmsWaveToOutboundInfoMapper;
import com.jayud.wms.service.IWmsWaveToOutboundInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 波次-出库单关系 服务实现类
 *
 * @author jyd
 * @since 2021-12-27
 */
@Service
public class WmsWaveToOutboundInfoServiceImpl extends ServiceImpl<WmsWaveToOutboundInfoMapper, WmsWaveToOutboundInfo> implements IWmsWaveToOutboundInfoService {


    @Autowired
    private WmsWaveToOutboundInfoMapper wmsWaveToOutboundInfoMapper;

    @Override
    public IPage<WmsWaveToOutboundInfo> selectPage(WmsWaveToOutboundInfo wmsWaveToOutboundInfo,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsWaveToOutboundInfo> page=new Page<WmsWaveToOutboundInfo>(currentPage,pageSize);
        IPage<WmsWaveToOutboundInfo> pageList= wmsWaveToOutboundInfoMapper.pageList(page, wmsWaveToOutboundInfo);
        return pageList;
    }

    @Override
    public List<WmsWaveToOutboundInfo> selectList(WmsWaveToOutboundInfo wmsWaveToOutboundInfo){
        return wmsWaveToOutboundInfoMapper.list(wmsWaveToOutboundInfo);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsWaveToOutboundInfoMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsWaveToOutboundInfoForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsWaveToOutboundInfoForExcel(paramMap);
    }

}
