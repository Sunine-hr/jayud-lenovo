package com.jayud.mall.mapper;

import com.jayud.mall.model.po.LegalPerson;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 法人表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Mapper
@Component
public interface LegalPersonMapper extends BaseMapper<LegalPerson> {

}
