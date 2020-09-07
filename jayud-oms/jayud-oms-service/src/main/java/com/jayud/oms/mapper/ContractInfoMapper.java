package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.model.bo.QueryContractInfoForm;
import com.jayud.model.po.ContractInfo;
import com.jayud.model.vo.ContractInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ContractInfoMapper extends BaseMapper<ContractInfo> {

    /**
     * 获取合同列表分页
     * @param page
     * @param form
     * @return
     */
    IPage<ContractInfoVO> findContractInfoByPage(Page page, @Param("form") QueryContractInfoForm form);

    /**
     * 查看合同详情
     * @param form
     * @return
     */
    ContractInfo getContractInfoById(QueryContractInfoForm form);
}
