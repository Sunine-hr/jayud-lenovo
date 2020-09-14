package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.model.bo.QueryContractInfoForm;
import com.jayud.model.po.ContractInfo;
import com.jayud.model.vo.ContractInfoVO;
import com.jayud.oms.mapper.ContractInfoMapper;
import com.jayud.oms.service.IContractInfoService;
import org.springframework.stereotype.Service;


@Service
public class ContractInfoServiceImpl extends ServiceImpl<ContractInfoMapper, ContractInfo> implements IContractInfoService {


    @Override
    public IPage<ContractInfoVO> findContractInfoByPage(QueryContractInfoForm form) {
        //定义分页参数
        Page<ContractInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("ci.id"));
        IPage<ContractInfoVO> pageInfo = baseMapper.findContractInfoByPage(page, form);
        return pageInfo;
    }

    @Override
    public ContractInfoVO getContractInfoById(Long id) {
        return  baseMapper.getContractInfoById(id);
    }


}
