package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.enums.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.CostType;
import com.jayud.auth.mapper.CostTypeMapper;
import com.jayud.auth.service.ICostTypeService;
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
 * 费用类别 服务实现类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Service
public class CostTypeServiceImpl extends ServiceImpl<CostTypeMapper, CostType> implements ICostTypeService {


    @Autowired
    private CostTypeMapper costTypeMapper;

    @Override
    public IPage<CostType> selectPage(CostType costType,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CostType> page=new Page<CostType>(currentPage,pageSize);
        IPage<CostType> pageList= costTypeMapper.pageList(page, costType);
        return pageList;
    }

    @Override
    public List<CostType> selectList(CostType costType){
        return costTypeMapper.list(costType);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        costTypeMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        costTypeMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCostTypeForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCostTypeForExcel(paramMap);
    }

    /**
     * 获取启用费用类别
     */
    @Override
    public List<CostType> getEnableCostType() {
        QueryWrapper<CostType> condition = new QueryWrapper<>();
        condition.lambda().eq(CostType::getStatus, StatusEnum.ENABLE.getCode());
        return this.baseMapper.selectList(condition);
    }

}
