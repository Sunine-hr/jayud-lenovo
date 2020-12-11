package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.bo.ReceivableCostForm;
import com.jayud.mall.model.po.ReceivableCost;
import com.jayud.mall.model.vo.ReceivableCostReturnVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 应收/应付费用名称 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface ReceivableCostMapper extends BaseMapper<ReceivableCost> {

    /**
     * 报价模板使用应付费用信息
     * @param form
     * @return
     */
    List<ReceivableCostReturnVO> findReceivableCostBy(@Param("form") ReceivableCostForm form);
}
