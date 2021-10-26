package com.jayud.oms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.QueryContractInfoForm;
import com.jayud.oms.model.po.ContractInfo;
import com.jayud.oms.model.vo.ContractInfoVO;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据条件查询合同信息
     * @param param
     * @return
     */
    List<ContractInfo> findContractByCondition(Map<String,Object> param);


    List<ContractInfo> getByCondition(ContractInfo contractInfo);
}
