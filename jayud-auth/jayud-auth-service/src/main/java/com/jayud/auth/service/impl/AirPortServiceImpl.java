package com.jayud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.AirPort;
import com.jayud.auth.mapper.AirPortMapper;
import com.jayud.auth.service.IAirPortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 空运港口表 服务实现类
 *
 * @author jayud
 * @since 2022-03-24
 */
@Slf4j
@Service
public class AirPortServiceImpl extends ServiceImpl<AirPortMapper, AirPort> implements IAirPortService {


    @Autowired
    private AirPortMapper airPortMapper;

    @Override
    public IPage<AirPort> selectPage(AirPort airPort,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<AirPort> page=new Page<AirPort>(currentPage,pageSize);
        IPage<AirPort> pageList= airPortMapper.pageList(page, airPort);
        return pageList;
    }

    @Override
    public List<AirPort> selectList(AirPort airPort){
        return airPortMapper.list(airPort);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        airPortMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        airPortMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryAirPortForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryAirPortForExcel(paramMap);
    }

    @Override
    public AirPort isCodeExistence(String code) {
        QueryWrapper<AirPort> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AirPort::getCode,code);
        queryWrapper.lambda().eq(AirPort::getStatus,0);
        return this.getOne(queryWrapper);
    }

    @Override
    public AirPort isNameExistence(String name) {
        QueryWrapper<AirPort> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AirPort::getName,name);
        queryWrapper.lambda().eq(AirPort::getStatus,0);
        return this.getOne(queryWrapper);
    }

    @Override
    public void saveAirPort(AirPort airPort) {
        if(null == airPort.getId()){
            airPort.setCreateBy(CurrentUserUtil.getUsername());
            airPort.setCreateTime(new Date());
            airPort.setStatus(1);
        }else{
            airPort.setUpdateBy(CurrentUserUtil.getUsername());
            airPort.setUpdateTime(new Date());
        }

        this.saveOrUpdate(airPort);
    }

}
