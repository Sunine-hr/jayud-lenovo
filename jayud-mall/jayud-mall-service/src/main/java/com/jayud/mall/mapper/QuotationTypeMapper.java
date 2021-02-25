package com.jayud.mall.mapper;

import com.jayud.mall.model.po.QuotationType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 报价类型表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-08
 */
@Mapper
@Component
public interface QuotationTypeMapper extends BaseMapper<QuotationType> {

    /**
     * 根据code，查询订柜尺寸（报价类型名称）
     * @param code
     * @return
     */
    QuotationType findQuotationTypeByCode(@Param("code") String code);

}
