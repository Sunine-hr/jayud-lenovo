package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.model.bo.QueryCustomerInfoForm;
import com.jayud.model.po.CustomerInfo;
import com.jayud.model.vo.CustomerInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface CustomerInfoMapper extends BaseMapper<CustomerInfo> {

    /**
     * 获取客户列表分页
     * @param page
     * @param form
     * @return
     */
    IPage<CustomerInfoVO> findCustomerInfoByPage(Page page, @Param("form") QueryCustomerInfoForm form);

    /**
     * 查看客户详情
     * @param form
     * @return
     */
    CustomerInfo getCustomerInfoById(QueryCustomerInfoForm form);
}
