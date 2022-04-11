package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.CostGenre;
import com.jayud.auth.mapper.CostGenreMapper;
import com.jayud.auth.service.ICostGenreService;
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
 * 基础数据费用类型 服务实现类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Service
public class CostGenreServiceImpl extends ServiceImpl<CostGenreMapper, CostGenre> implements ICostGenreService {


    @Autowired
    private CostGenreMapper costGenreMapper;

    @Override
    public IPage<CostGenre> selectPage(CostGenre costGenre,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CostGenre> page=new Page<CostGenre>(currentPage,pageSize);
        IPage<CostGenre> pageList= costGenreMapper.pageList(page, costGenre);
        return pageList;
    }

    @Override
    public List<CostGenre> selectList(CostGenre costGenre){
        return costGenreMapper.list(costGenre);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        costGenreMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        costGenreMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCostGenreForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCostGenreForExcel(paramMap);
    }

}
