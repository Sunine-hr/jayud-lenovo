package com.jayud.oceanship.mapper;

import com.jayud.oceanship.po.SeaContainerInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 货柜信息表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-03-25
 */
@Mapper
public interface SeaContainerInformationMapper extends BaseMapper<SeaContainerInformation> {

    int deleteSeaContainerInfo(@Param("orderNo") List<String> orderNo);
}
