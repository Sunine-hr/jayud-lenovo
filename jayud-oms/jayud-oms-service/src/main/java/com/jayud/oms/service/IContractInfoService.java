package com.jayud.oms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.bo.QueryContractInfoForm;
import com.jayud.model.po.ContractInfo;
import com.jayud.model.vo.ContractInfoVO;

/**
 * 合同信息
 */
public interface IContractInfoService extends IService<ContractInfo> {

    /**
     * 列表分页查询
     * @param from
     * @return
     */
    IPage<ContractInfoVO>  findContractInfoByPage(QueryContractInfoForm from);

    /**
     * 根据id获取合同信息
     * @param id
     * @return
     */
    ContractInfoVO getContractInfoById(Long id);



}