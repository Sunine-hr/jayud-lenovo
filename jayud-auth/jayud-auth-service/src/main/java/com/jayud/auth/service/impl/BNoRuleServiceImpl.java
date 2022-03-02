package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.BNoRule;
import com.jayud.auth.mapper.BNoRuleMapper;
import com.jayud.auth.service.IBNoRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 编号规则表 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class BNoRuleServiceImpl extends ServiceImpl<BNoRuleMapper, BNoRule> implements IBNoRuleService {


    @Autowired
    private BNoRuleMapper bNoRuleMapper;

    @Override
    public IPage<BNoRule> selectPage(BNoRule bNoRule,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<BNoRule> page=new Page<BNoRule>(currentPage,pageSize);
        IPage<BNoRule> pageList= bNoRuleMapper.pageList(page, bNoRule);
        return pageList;
    }

    @Override
    public List<BNoRule> selectList(BNoRule bNoRule){
        return bNoRuleMapper.list(bNoRule);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        bNoRuleMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        bNoRuleMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public String getOrder(String code,  Date  date) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("i_code",code);
        map.put("i_date",date);
        this.baseMapper.getOrderNo(map);
        return (String)map.get("o_no");
    }

}
