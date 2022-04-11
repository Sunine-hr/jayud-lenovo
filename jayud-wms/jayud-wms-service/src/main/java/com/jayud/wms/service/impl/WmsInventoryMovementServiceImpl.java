package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.WmsInventoryMovementForm;
import com.jayud.wms.model.vo.WmsInventoryMovementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.model.po.WmsInventoryMovement;
import com.jayud.wms.mapper.WmsInventoryMovementMapper;
import com.jayud.wms.service.IWmsInventoryMovementService;
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
 * 库存移动订单表 服务实现类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Service
public class WmsInventoryMovementServiceImpl extends ServiceImpl<WmsInventoryMovementMapper, WmsInventoryMovement> implements IWmsInventoryMovementService {


    @Autowired
    private WmsInventoryMovementMapper wmsInventoryMovementMapper;

    @Override
    public IPage<WmsInventoryMovementVO> selectPage(WmsInventoryMovementForm wmsInventoryMovement,
                                                    Integer currentPage,
                                                    Integer pageSize,
                                                    HttpServletRequest req){

        Page<WmsInventoryMovementForm> page=new Page<WmsInventoryMovementForm>(currentPage,pageSize);
        IPage<WmsInventoryMovementVO> pageList= wmsInventoryMovementMapper.pageList(page, wmsInventoryMovement);
        return pageList;
    }

    @Override
    public List<WmsInventoryMovement> selectList(WmsInventoryMovement wmsInventoryMovement){
        return wmsInventoryMovementMapper.list(wmsInventoryMovement);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        wmsInventoryMovementMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        wmsInventoryMovementMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryWmsInventoryMovementForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsInventoryMovementForExcel(paramMap);
    }

}
