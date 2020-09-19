package com.jayud.oms.mapper;

import com.jayud.oms.model.po.CurrencyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.CurrencyInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 币种 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-19
 */
@Mapper
public interface CurrencyInfoMapper extends BaseMapper<CurrencyInfo> {

    List<CurrencyInfoVO> findCurrencyInfo();
}
