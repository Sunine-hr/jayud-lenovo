package com.jayud.oms.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.order.model.vo.OmsOrderEntryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.oms.order.model.po.OmsOrderEntry;
import com.jayud.oms.order.mapper.OmsOrderEntryMapper;
import com.jayud.oms.order.service.IOmsOrderEntryService;
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
 * 订单管理-订单明细表 服务实现类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Slf4j
@Service
public class OmsOrderEntryServiceImpl extends ServiceImpl<OmsOrderEntryMapper, OmsOrderEntry> implements IOmsOrderEntryService {


    @Autowired
    private OmsOrderEntryMapper omsOrderEntryMapper;

    @Override
    public IPage<OmsOrderEntry> selectPage(OmsOrderEntry omsOrderEntry,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<OmsOrderEntry> page=new Page<OmsOrderEntry>(currentPage,pageSize);
        IPage<OmsOrderEntry> pageList= omsOrderEntryMapper.pageList(page, omsOrderEntry);
        return pageList;
    }

    @Override
    public List<OmsOrderEntry> selectList(OmsOrderEntry omsOrderEntry){
        return omsOrderEntryMapper.list(omsOrderEntry);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        omsOrderEntryMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        omsOrderEntryMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryOmsOrderEntryForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryOmsOrderEntryForExcel(paramMap);
    }

    @Override
    public List<OmsOrderEntryVO> getByOrderId(Long id) {
        QueryWrapper<OmsOrderEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OmsOrderEntry::getOmsOrderId,id);
        queryWrapper.lambda().eq(OmsOrderEntry::getIsDeleted,0);
        List<OmsOrderEntry> list = this.list(queryWrapper);
        List<OmsOrderEntryVO> omsOrderEntryVOS = ConvertUtil.convertList(list, OmsOrderEntryVO.class);
        return omsOrderEntryVOS;
    }

}
