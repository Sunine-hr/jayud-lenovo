package com.jayud.oceanship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oceanship.bo.QuerySeaOrderForm;
import com.jayud.oceanship.po.SeaReplenishment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oceanship.vo.SeaOrderFormVO;
import com.jayud.oceanship.vo.SeaReplenishmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 海运补料表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-03-18
 */
@Mapper
public interface SeaReplenishmentMapper extends BaseMapper<SeaReplenishment> {

    IPage<SeaReplenishmentVO> findBillByPage(Page<SeaOrderFormVO> page, @Param("form") QuerySeaOrderForm form, @Param("legalIds")List<Long> legalIds);
}
