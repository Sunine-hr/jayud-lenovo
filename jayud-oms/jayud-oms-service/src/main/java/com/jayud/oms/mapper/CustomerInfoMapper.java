package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryCustomerInfoForm;
import com.jayud.oms.model.bo.QueryRelUnitInfoListForm;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.vo.CustomerInfoVO;
import com.jayud.oms.model.bo.QueryCusAccountForm;
import com.jayud.oms.model.vo.CustAccountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface CustomerInfoMapper extends BaseMapper<CustomerInfo> {

    /**
     * 获取客户列表分页
     *
     * @param page
     * @param form
     * @return
     */
    IPage<CustomerInfoVO> findCustomerInfoByPage(Page page, @Param("form") QueryCustomerInfoForm form);

    /**
     * 查看客户详情
     *
     * @param id
     * @return
     */
    CustomerInfoVO getCustomerInfoById(Long id);

    /**
     * 获取客户账号
     *
     * @param param
     * @return
     */
    CustAccountVO getCustAccountByCondition(Map<String, Object> param);

    /**
     * 客户账户列表分页查询
     *
     * @param page
     * @param form
     * @return
     */
    IPage<CustAccountVO> findCustAccountByPage(Page page, @Param("form") QueryCusAccountForm form);

    /**
     * 分页查询客户基础数据
     */
    IPage<CustomerInfoVO> findCustomerBasicsInfoByPage(Page page, @Param("form") QueryCustomerInfoForm form);

    /**
     * 做客户代码和客户名称唯一性校验
     * @param idCode
     * @param name
     * @return
     */
    List<CustomerInfoVO> existCustomerInfo(@Param("idCode") String idCode,@Param("name") String name);

    /**
     * 根据客户ID获取结算单位
     * @param id
     * @return
     */
    List<CustomerInfoVO> relateUnitList(@Param("id") Long id);

    /**
     * 查询审核通过的关联客户,并且没有关联当前客户的
     * @param form
     * @return
     */
    List<CustomerInfoVO> findRelateUnitList(@Param("form") QueryRelUnitInfoListForm form);
}
