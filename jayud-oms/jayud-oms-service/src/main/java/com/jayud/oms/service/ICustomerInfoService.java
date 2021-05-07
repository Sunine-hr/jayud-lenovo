package com.jayud.oms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.AddCustomerInfoForm;
import com.jayud.oms.model.bo.QueryCusAccountForm;
import com.jayud.oms.model.bo.QueryCustomerInfoForm;
import com.jayud.oms.model.bo.QueryRelUnitInfoListForm;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.vo.CustAccountVO;
import com.jayud.oms.model.vo.CustomerInfoVO;
import com.jayud.oms.model.vo.InitComboxStrVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 客户信息
 */
public interface ICustomerInfoService extends IService<CustomerInfo> {

    /**
     * 客户列表分页查询
     *
     * @param from
     * @return
     */
    IPage<CustomerInfoVO> findCustomerInfoByPage(QueryCustomerInfoForm from);

    /**
     * 根据id获取客户信息
     *
     * @param id
     * @return
     */
    CustomerInfoVO getCustomerInfoById(Long id);

    /**
     * 根据条件获取客户
     *
     * @param param
     * @return
     */
    List<CustomerInfo> getCustomerInfoByCondition(List<Long> param);

    /**
     * 根据条件获取客户
     *
     * @param param
     * @return
     */
    List<CustomerInfo> findCustomerInfoByCondition(Map<String, Object> param);

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
     * @param form
     * @return
     */
    IPage<CustAccountVO> findCustAccountByPage(QueryCusAccountForm form);

    /**
     * 分页查询客户基本信息
     */
    IPage<CustomerInfoVO> findCustomerBasicsInfoByPage(QueryCustomerInfoForm form);

    /**
     * 做客户代码和客户名称唯一性校验
     *
     * @param idCode
     * @return
     */
    List<CustomerInfoVO> existCustomerInfo(String idCode);

    /**
     * 根据客户ID获取结算单位
     *
     * @param id
     * @return
     */
    List<CustomerInfoVO> relateUnitList(Long id);

    /**
     * 查询审核通过的关联客户,并且没有关联当前客户的
     *
     * @param form
     * @return
     */
    List<CustomerInfoVO> findRelateUnitList(QueryRelUnitInfoListForm form);

    /**
     * 导入客户信息
     *
     * @param file
     * @param response
     * @return
     */
    String importCustomerInfoExcel(HttpServletResponse response, MultipartFile file, String userName) throws Exception;

    /**
     * 下载错误文件
     *
     * @param response
     * @return
     */
    void insExcel(HttpServletResponse response, String userName) throws Exception;

    boolean checkMes(String userName);

    /**
     * 校验客户代码唯一性
     */
    public boolean exitCode(Long customerId, String idCode);

    /**
     * 初始化审核通过客户
     *
     * @return
     */
    List<InitComboxStrVO> initApprovedCustomer();

    void saveOrUpdateCustomerInfo(AddCustomerInfoForm form, CustomerInfo customerInfo);


    /**
     * 根据客户名称查询客户信息
     */
    public CustomerInfo getByName(String name);

    /**
     * 校验客户名称唯一性
     */
    public boolean exitName(Long customerId, String name);


    /**
     * 根据code查询客户信息
     */
    public CustomerInfo getByCode(String code);

    List<CustomerInfo> getByCustomerName(String customerName);
}
