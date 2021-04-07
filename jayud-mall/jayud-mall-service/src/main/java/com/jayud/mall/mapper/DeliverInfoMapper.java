package com.jayud.mall.mapper;

import com.jayud.mall.model.po.DeliverInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.DeliverInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 送货信息表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-06
 */
@Mapper
@Component
public interface DeliverInfoMapper extends BaseMapper<DeliverInfo> {

    /**
     * 根据transportId，查询送货地址
     * @param transportId 运输id
     * @return
     */
    List<DeliverInfoVO> findDeliverInfoByTransportId(@Param("transportId") Long transportId);
}
