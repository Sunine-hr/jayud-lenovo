package com.jayud.oms.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.oms.order.model.po.OmsOrder;
import com.jayud.oms.order.mapper.OmsOrderMapper;
import com.jayud.oms.order.service.IOmsOrderService;
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
 * 订单管理——订单主表 服务实现类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Slf4j
@Service
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements IOmsOrderService {


    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @Override
    public IPage<OmsOrder> selectPage(OmsOrder omsOrder,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<OmsOrder> page=new Page<OmsOrder>(currentPage,pageSize);
        IPage<OmsOrder> pageList= omsOrderMapper.pageList(page, omsOrder);
        return pageList;
    }

    @Override
    public List<OmsOrder> selectList(OmsOrder omsOrder){
        return omsOrderMapper.list(omsOrder);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        omsOrderMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        omsOrderMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryOmsOrderForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryOmsOrderForExcel(paramMap);
    }

}
