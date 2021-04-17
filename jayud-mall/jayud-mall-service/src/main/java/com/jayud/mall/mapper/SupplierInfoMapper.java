package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QuerySupplierInfoForm;
import com.jayud.mall.model.po.SupplierInfo;
import com.jayud.mall.model.vo.SupplierInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 供应商信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Mapper
@Component
public interface SupplierInfoMapper extends BaseMapper<SupplierInfo> {

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<SupplierInfoVO> findSupplierInfoByPage(Page<SupplierInfoVO> page, @Param("form") QuerySupplierInfoForm form);
}
