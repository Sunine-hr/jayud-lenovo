package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryProductBizForm;
import com.jayud.oms.model.po.ProductBiz;
import com.jayud.oms.model.vo.ProductBizVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ProductBizMapper extends BaseMapper<ProductBiz> {

    IPage<ProductBizVO> findProductBizByPage(Page page, @Param("form") QueryProductBizForm form);
}
