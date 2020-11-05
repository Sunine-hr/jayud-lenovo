package com.jayud.tools.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.tools.model.bo.QuerySensitiveCommodityForm;
import com.jayud.tools.model.po.SensitiveCommodity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.tools.model.vo.SensitiveCommodityVO;
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
    List<SensitiveCommodity> getSensitiveCommodityList(String name);

    /**
     * 分页查询敏感品名
     * @param page
     * @param form
     * @return
     */
    IPage<SensitiveCommodityVO> findSensitiveCommodityByPage(Page<SensitiveCommodityVO> page, QuerySensitiveCommodityForm form);
}
