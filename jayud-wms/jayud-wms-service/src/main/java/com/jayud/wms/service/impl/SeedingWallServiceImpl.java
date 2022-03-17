package com.jayud.wms.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.SeedingWall;
import com.jayud.wms.mapper.SeedingWallMapper;
import com.jayud.wms.service.ISeedingWallService;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 播种墙信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Service
public class SeedingWallServiceImpl extends ServiceImpl<SeedingWallMapper, SeedingWall> implements ISeedingWallService {


    @Autowired
    private SeedingWallMapper seedingWallMapper;

    @Override
    public IPage<SeedingWall> selectPage(SeedingWall seedingWall,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SeedingWall> page=new Page<SeedingWall>(pageNo,pageSize);
        IPage<SeedingWall> pageList= seedingWallMapper.pageList(page, seedingWall);
        return pageList;
    }

    @Override
    public List<SeedingWall> selectList(SeedingWall seedingWall){
        return seedingWallMapper.list(seedingWall);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SeedingWall saveOrUpdateSeedingWall(SeedingWall seedingWall) {
        Long id = seedingWall.getId();

        Integer row = seedingWall.getRow();
        Integer column = seedingWall.getColumn();
        if(ObjectUtil.isEmpty(row) || ObjectUtil.isEmpty(column)){
            throw new IllegalArgumentException("行，列，不能为空");
        }

        if(ObjectUtil.isEmpty(id)){
            //新增
            seedingWall.setCreateBy(CurrentUserUtil.getUsername());
            seedingWall.setCreateTime(new Date());

            QueryWrapper<SeedingWall> seedingWallQueryWrapper = new QueryWrapper<>();
            seedingWallQueryWrapper.lambda().eq(SeedingWall::getCode, seedingWall.getCode());
            seedingWallQueryWrapper.lambda().eq(SeedingWall::getIsDeleted, 0);
            List<SeedingWall> list = this.list(seedingWallQueryWrapper);
            if(CollUtil.isNotEmpty(list)){
                throw new IllegalArgumentException("播种墙编号已存在，操作失败");
            }

        }else{
            //修改
            seedingWall.setUpdateBy(CurrentUserUtil.getUsername());
            seedingWall.setUpdateTime(new Date());

            QueryWrapper<SeedingWall> seedingWallQueryWrapper = new QueryWrapper<>();
            seedingWallQueryWrapper.lambda().ne(SeedingWall::getId, id);
            seedingWallQueryWrapper.lambda().eq(SeedingWall::getCode, seedingWall.getCode());
            seedingWallQueryWrapper.lambda().eq(SeedingWall::getIsDeleted, 0);
            List<SeedingWall> list = this.list(seedingWallQueryWrapper);
            if(CollUtil.isNotEmpty(list)){
                throw new IllegalArgumentException("播种墙编号已存在，操作失败");
            }
        }
        this.saveOrUpdate(seedingWall);
        return seedingWall;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delSeedingWall(int id) {
        SeedingWall seedingWall = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(seedingWall)){
            throw new IllegalArgumentException("播种墙不存在，无法删除");
        }
        //删除
        seedingWall.setUpdateBy(CurrentUserUtil.getUsername());
        seedingWall.setUpdateTime(new Date());
        seedingWall.setIsDeleted(true);
        this.saveOrUpdate(seedingWall);
    }

    @Override
    public List<LinkedHashMap<String, Object>> querySeedingWallForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.querySeedingWallForExcel(paramMap);
    }

}
