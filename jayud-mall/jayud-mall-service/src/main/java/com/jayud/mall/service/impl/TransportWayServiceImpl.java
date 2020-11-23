package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.TransportWayMapper;
import com.jayud.mall.model.bo.TransportWayForm;
import com.jayud.mall.model.po.TransportWay;
import com.jayud.mall.service.ITransportWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 运输方式 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class TransportWayServiceImpl extends ServiceImpl<TransportWayMapper, TransportWay> implements ITransportWayService {

    @Autowired
    TransportWayMapper transportWayMapper;


    @Override
    public List<TransportWay> findTransportWay(TransportWayForm form) {
        QueryWrapper<TransportWay> queryWrapper = new QueryWrapper<>();
        String idCode = form.getIdCode();
        String codeName = form.getCodeName();
        String status = form.getStatus();
        if(idCode != null && idCode != ""){
            queryWrapper.like("id_code", idCode);
        }
        if(codeName != null && codeName != ""){
            queryWrapper.like("code_name", codeName);
        }
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        List<TransportWay> list = transportWayMapper.selectList(queryWrapper);
        return list;
    }
}
