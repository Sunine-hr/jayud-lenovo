package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.model.po.WmsReceiptAppend;
import com.jayud.wms.mapper.WmsReceiptAppendMapper;
import com.jayud.wms.service.IWmsReceiptAppendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货单附加服务表 服务实现类
 *
 * @author jayud
 * @since 2022-03-31
 */
@Slf4j
@Service
public class WmsReceiptAppendServiceImpl extends ServiceImpl<WmsReceiptAppendMapper, WmsReceiptAppend> implements IWmsReceiptAppendService {


    @Autowired
    private WmsReceiptAppendMapper wmsReceiptAppendMapper;

    @Override
    public IPage<WmsReceiptAppend> selectPage(WmsReceiptAppend wmsReceiptAppend,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsReceiptAppend> page=new Page<WmsReceiptAppend>(currentPage,pageSize);
        IPage<WmsReceiptAppend> pageList= wmsReceiptAppendMapper.pageList(page, wmsReceiptAppend);
        return pageList;
    }

    @Override
    public List<WmsReceiptAppend> selectList(WmsReceiptAppend wmsReceiptAppend){
        return wmsReceiptAppendMapper.list(wmsReceiptAppend);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        wmsReceiptAppendMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        wmsReceiptAppendMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryWmsReceiptAppendForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsReceiptAppendForExcel(paramMap);
    }

}
