package com.jayud.mall.mapper;

import com.jayud.mall.model.po.CurrencyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.CurrencyInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 币种 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Mapper
@Component
public interface CurrencyInfoMapper extends BaseMapper<CurrencyInfo> {

    /**
     * 查询所有币种
     * @return
     */
    List<CurrencyInfoVO> allCurrencyInfo();
}
