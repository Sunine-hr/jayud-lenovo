package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.AddSupplierInfoForm;
import com.jayud.oms.model.bo.QueryAuditSupplierInfoForm;
import com.jayud.oms.model.bo.QuerySupplierInfoForm;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.vo.SupplierInfoVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供应商信息 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
public interface ISupplierInfoService extends IService<SupplierInfo> {

    /**
     * 列表分页查询
     *
     * @param form
     * @return
     */
    IPage<SupplierInfoVO> findSupplierInfoByPage(QuerySupplierInfoForm form);

    /**
     * 新增编辑供应商
     *
     * @param form
     * @return
     */
    boolean saveOrUpdateSupplierInfo(AddSupplierInfoForm form);

    /**
     * 分页查询供应商审核信息
     */
    IPage<SupplierInfoVO> findAuditSupplierInfoByPage(QueryAuditSupplierInfoForm form);

    /**
     * 获取启用审核通过供应商
     */
    List<SupplierInfo> getApprovedSupplier(List<Long> supplierIds);

    /**
     * 获取启用审核通过供应商
     */
    List<SupplierInfo> getApprovedSupplier(String... fields);

    /**
     * 校验唯一性
     *
     * @return
     */
    boolean checkUnique(SupplierInfo supplierInfo);

    /**
     * 导入供应商信息
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
     * 获取通过审核的供应商列表
     *
     * @param
     * @return
     */
    List<SupplierInfo> findSupplierInfoByCondition();

    /**
     * 根据供应商名称查询供应商信息
     */
    SupplierInfo getByName(String name);

    boolean exitName(Long id, String supplierChName);

    List<SupplierInfo> existSupplierInfo(String supplierCode);

    //判断代码是否存在
    boolean exitCode(Long id, String supplierCode);

    /**
     * 根据状态查询待处理数
     *
     * @param status
     * @param legalIds
     * @return
     */
    public Integer getNumByStatus(String status, List<Long> legalIds);


    List<SupplierInfo> getByCondition(SupplierInfo supplierInfo);

    /**
     * 查询供应商及其车辆tree
     * @return 供应商及其车辆tree
     */
    List<Map<String, Object>> getSupplierVehicleTree();

    /**
     * 查询供应商
     * @return 供应商list
     */
    List<Map<String, Object>> getList();

    /**
     * 查询订单tree
     * @param orderTypeCode 单据类型
     * @param pickUpTimeStart 提货时间Start
     * @param pickUpTimeEnd 提货时间End
     * @param orderNo 订单号
     * @return 订单tree
     */
    List<Map<String, Object>> getOrderTree(String orderTypeCode, String pickUpTimeStart, String pickUpTimeEnd, String orderNo);
}
