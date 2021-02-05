package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryAuditSupplierInfoForm;
import com.jayud.oms.model.bo.QuerySupplierInfoForm;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.vo.SupplierInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 供应商信息 Mapper 接口
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
@Mapper
public interface SupplierInfoMapper extends BaseMapper<SupplierInfo> {

//    IPage<SupplierInfoVO> findSupplierInfoByPage(Page page, @Param(value = "form") QuerySupplierInfoForm form);

    IPage<SupplierInfoVO> findAuditSupplierInfoByPage(Page page, @Param(value = "form") QueryAuditSupplierInfoForm form);

    IPage<SupplierInfoVO> findSupplierInfoByPage(Page page, @Param(value = "form") QuerySupplierInfoForm form, @Param("legalIds") List<Long> legalIds);

    List<SupplierInfo> getApprovedSupplier(@Param("supplierStatus") String supplierStatus,
                                           @Param("auditStatus") String auditStatus,
                                           @Param("tablesDesc") String tablesDesc,
                                           @Param("supplierIds") List<Long> supplierIds);
}
