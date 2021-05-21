package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.FeeCopeWith;
import com.jayud.mall.model.vo.FeeCopeWithVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 提单(或柜子)对应费用明细 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-21
 */
@Mapper
@Component
public interface FeeCopeWithMapper extends BaseMapper<FeeCopeWith> {

    /**
     * 根据qie[提单id(ocean_bill id)/柜子id(ocean_counter id)]，查询费用信息
     * @param qie
     * @param businessType
     * @return
     */
    List<FeeCopeWithVO> findFeeCopeWithByQie(@Param("qie") Integer qie, @Param("businessType") Integer businessType);

}
