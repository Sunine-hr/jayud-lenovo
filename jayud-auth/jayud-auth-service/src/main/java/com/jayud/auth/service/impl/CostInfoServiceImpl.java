package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.enums.StatusEnum;
import com.jayud.auth.model.po.CostType;
import com.jayud.auth.service.ICostTypeService;
import com.jayud.common.entity.InitComboxVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.CostInfo;
import com.jayud.auth.mapper.CostInfoMapper;
import com.jayud.auth.service.ICostInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 费用名描述 服务实现类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Service
public class CostInfoServiceImpl extends ServiceImpl<CostInfoMapper, CostInfo> implements ICostInfoService {


    @Autowired
    private CostInfoMapper costInfoMapper;

    @Autowired
    private ICostTypeService costTypeService;

    @Override
    public IPage<CostInfo> selectPage(CostInfo costInfo,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CostInfo> page=new Page<CostInfo>(currentPage,pageSize);
        IPage<CostInfo> pageList= costInfoMapper.pageList(page, costInfo);
        return pageList;
    }

    @Override
    public List<CostInfo> selectList(CostInfo costInfo){
        return costInfoMapper.list(costInfo);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        costInfoMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        costInfoMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCostInfoForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCostInfoForExcel(paramMap);
    }

    @Override
    public List<CostInfo> findCostInfo() {
        return baseMapper.selectList(null);
    }

    @Override
    public Map<String, List<InitComboxVO>> initCostTypeByCostInfoCode() {
        List<CostInfo> costInfos = this.getCostInfoByStatus(StatusEnum.ENABLE.getCode());
        Map<Long, CostType> costTypeMap = this.costTypeService.getEnableCostType().stream().collect(Collectors.toMap(CostType::getId, e -> e));
        Map<String, List<InitComboxVO>> map = new HashMap<>();
        for (CostInfo costInfo : costInfos) {
            String[] split = costInfo.getCids().split(",");
            List<InitComboxVO> costTypeComboxs = new ArrayList<>();
            for (String s : split) {
                if (StringUtils.isEmpty(s)) continue;
                CostType costType = costTypeMap.get(Long.valueOf(s));
                if (costType == null) continue;
                InitComboxVO initComboxVO = new InitComboxVO();
                initComboxVO.setName(costType.getCodeName());
                initComboxVO.setId(costType.getId());
                costTypeComboxs.add(initComboxVO);
            }
            map.put(costInfo.getIdCode(), costTypeComboxs);
        }
        return map;
    }

    @Override
    public List<CostInfo> getCostInfoByStatus(String status) {
        QueryWrapper<CostInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(CostInfo::getStatus, status);
        return this.baseMapper.selectList(condition);
    }
}
