package com.jayud.oms.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.order.model.po.OmsOrderEntry;
import com.jayud.oms.order.model.vo.OmsOrderEntryVO;
import com.jayud.oms.order.model.vo.OmsOrderFollowVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.oms.order.model.po.OmsOrderFollow;
import com.jayud.oms.order.mapper.OmsOrderFollowMapper;
import com.jayud.oms.order.service.IOmsOrderFollowService;
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
 * 订单状态跟进表 服务实现类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Slf4j
@Service
public class OmsOrderFollowServiceImpl extends ServiceImpl<OmsOrderFollowMapper, OmsOrderFollow> implements IOmsOrderFollowService {


    @Autowired
    private OmsOrderFollowMapper omsOrderFollowMapper;

    @Override
    public IPage<OmsOrderFollow> selectPage(OmsOrderFollow omsOrderFollow,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<OmsOrderFollow> page=new Page<OmsOrderFollow>(currentPage,pageSize);
        IPage<OmsOrderFollow> pageList= omsOrderFollowMapper.pageList(page, omsOrderFollow);
        return pageList;
    }

    @Override
    public List<OmsOrderFollow> selectList(OmsOrderFollow omsOrderFollow){
        return omsOrderFollowMapper.list(omsOrderFollow);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        omsOrderFollowMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        omsOrderFollowMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryOmsOrderFollowForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryOmsOrderFollowForExcel(paramMap);
    }

    @Override
    public List<OmsOrderFollowVO> getByOrderId(Long id) {
        QueryWrapper<OmsOrderFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OmsOrderFollow::getOmsOrderId,id);
        queryWrapper.lambda().eq(OmsOrderFollow::getIsDeleted,0);
        List<OmsOrderFollow> list = this.list(queryWrapper);
        List<OmsOrderFollowVO> omsOrderFollowVOS = ConvertUtil.convertList(list, OmsOrderFollowVO.class);
        return omsOrderFollowVOS;
    }

}
