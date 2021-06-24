package com.jayud.mall.mapper;

import com.jayud.mall.model.po.CounterDocumentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * (提单)柜子对应-文件信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-24
 */
@Mapper
@Component
public interface CounterDocumentInfoMapper extends BaseMapper<CounterDocumentInfo> {

}
