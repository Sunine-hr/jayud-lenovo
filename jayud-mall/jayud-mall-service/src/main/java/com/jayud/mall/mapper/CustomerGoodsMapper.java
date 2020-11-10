package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryCustomerGoodsForm;
import com.jayud.mall.model.po.CustomerGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.CustomerGoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 客户商品表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Mapper
@Component
public interface CustomerGoodsMapper extends BaseMapper<CustomerGoods> {

    /**
     * 分页
     * @param page
     * @param form
     * @return
     */
    IPage<CustomerGoodsVO> findCustomerGoodsByPage(Page<CustomerGoodsVO> page, QueryCustomerGoodsForm form);
}
