package com.jayud.tools.mapper;

import com.jayud.tools.model.po.SensitiveCommodity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 敏感品名表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Mapper
@Component
public interface SensitiveCommodityMapper extends BaseMapper<SensitiveCommodity> {

    /**
     * 查询敏感品名list
     * @return
     */
    List<SensitiveCommodity> getSensitiveCommodityList();
}
