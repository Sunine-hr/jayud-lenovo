package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QuerySupplierServeForm;
import com.jayud.mall.model.po.SupplierServe;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.SupplierServeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 供应商服务 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Mapper
@Component
public interface SupplierServeMapper extends BaseMapper<SupplierServe> {

    /**
     * 分页
     * @param page
     * @param form
     * @return
     */
    IPage<SupplierServeVO> findSupplierServeByPage(Page<SupplierServeVO> page, @Param("form") QuerySupplierServeForm form);

    List<SupplierServeVO> findSupplierSerCostInfoById(@Param("supplierInfoId") Long supplierInfoId);

    SupplierServeVO findSupplierServeById(@Param("id") Long id);
}
