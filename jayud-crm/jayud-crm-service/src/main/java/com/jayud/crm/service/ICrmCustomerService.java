package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.crm.model.bo.*;
import com.jayud.crm.model.po.CrmCustomer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.crm.model.vo.CrmCustomerVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_基本信息(crm_customer) 服务类
 *
 * @author jayud
 * @since 2022-03-01
 */
public interface ICrmCustomerService extends IService<CrmCustomer> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: crmCustomerForm
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomer>
     **/
    IPage<CrmCustomerForm> selectPage(CrmCustomerForm crmCustomerForm,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-01
     * @param: crmCustomerForm
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomer>
     **/
    List<CrmCustomerForm> selectList(CrmCustomerForm crmCustomerForm);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-01
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-01
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCustomerForExcel(Map<String, Object> paramMap);


    /**
     * @description 保存用户数据
     * @author  ciro
     * @date   2022/3/2 9:17
     * @param: crmCustomerForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult saveCrmCustomer(CrmCustomerForm crmCustomerForm);


    /**
     * @description 根据id查询
     * @author  ciro
     * @date   2022/3/2 11:18
     * @param: id
     * @return: com.jayud.crm.model.bo.CrmCustomerForm
     **/
    CrmCustomerForm selectById(Long id);

    /**
     * @description 获取客户字典数据
     * @author  ciro
     * @date   2022/3/2 11:43
     * @param:
     * @return: com.jayud.crm.model.bo.CrmCodeFrom
     **/
    CrmCodeFrom getCrmCode();


    /**
     * @description 根据客户id查询用户类型
     * @author  ciro
     * @date   2022/3/3 11:10
     * @param: custId
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.bo.CrmCodeFrom>
     **/
    BaseResult<CrmCodeFrom> getBbusinessTypesByCustId(Long custId);

    /**
     * @description 获取下一个编码
     * @author  ciro
     * @date   2022/3/3 11:30
     * @param: code
     * @return: java.lang.String
     **/
    String getNextCode(String code);

    /**
     * @description 移入黑名单
     * @author  ciro
     * @date   2022/3/3 16:15
     * @param: ids
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult moveCustToRick(List<CrmCustomer> custList);

    /**
     * @description 转为供应商
     * @author  ciro
     * @date   2022/3/3 16:27
     * @param: custList
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult changeToSupplier(List<CrmCustomer> custList);

    /**
     * @description 放入公海
     * @author  ciro
     * @date   2022/3/3 16:34
     * @param: custList
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult changeToPublic(List<CrmCustomer> custList);

    /**
     * @description 获取业务集合名称
     * @author  ciro
     * @date   2022/3/3 18:34
     * @param: businessTypesList
     * @return: java.lang.String
     **/
    String changeBusinessType(List<String> businessTypesList);

    /**
    * @description 根据业务类型获取数据对象
    * @author  ciro
    * @date   2022/3/14 14:35:05
    * @return: java.util.Map<java.lang.String,java.lang.String>
    **/
    Map<String,String> getBusinessTypeData();

    /**
     * @description 领取客户
     * @author  ciro
     * @date   2022/3/4 16:47
     * @param: comCustomerForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult receiveCustomer(ComCustomerForm comCustomerForm);

    /**
     * @description 获取错误信息
     * @author  ciro
     * @date   2022/3/4 18:45
     * @param: comCustomerForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult getErrMsg(ComCustomerForm comCustomerForm);


    /**
     * @description 判断用户信息
     * @author  ciro
     * @date   2022/3/5 10:31
     * @param: crmCustomerCheckForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult checkCustomerMsg(CrmCustomerCheckForm crmCustomerCheckForm);

    /**
     * @description 根据id集合逻辑删除
     * @author  ciro
     * @date   2022/3/5 13:58
     * @param: deleteForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult logicDelByIds(DeleteForm deleteForm);

    /**
     * @description 更新企业负责人信息
     * @author  ciro
     * @date   2022/3/7 16:10
     * @param: idList
     * @param: managerUserId
     * @param: managerUserName
     * @return: void
     **/
    void updateManagerMsg(List<Long> idList,Long managerUserId,String managerUserName);

    /**
    * @description 根据id集合取消公海
    * @author  ciro
    * @date   2022/3/18 11:31:18
    * @param ids
    * @param managerUserId
    * @param managerUsername
    * @return: void
    **/
    void cnaleInpublicByIds(List<Long> ids,Long managerUserId,String managerUsername);

    /**
     * 根据条件获取客户信息
     * @param crmCustomerForm
     * @param currentPage
     * @param pageSize
     * @param req
     * @return
     */
    IPage selectCrmCustomerPage(CrmCustomerForm crmCustomerForm, Integer currentPage, Integer pageSize, HttpServletRequest req);

    List<CrmCustomerVO> selectCrmCustomerList(CrmCustomerForm crmCustomerForm);
}
