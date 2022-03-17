package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.StrategyConf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略配置 Mapper 接口
 *
 * @author jyd
 * @since 2022-01-13
 */
@Mapper
public interface StrategyConfMapper extends BaseMapper<StrategyConf> {
    /**
    *   分页查询
    */
    IPage<StrategyConf> pageList(@Param("page") Page<StrategyConf> page, @Param("strategyConf") StrategyConf strategyConf);

    /**
    *   列表查询
    */
    List<StrategyConf> list(@Param("strategyConf") StrategyConf strategyConf);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryStrategyConfForExcel(Map<String, Object> paramMap);
}
