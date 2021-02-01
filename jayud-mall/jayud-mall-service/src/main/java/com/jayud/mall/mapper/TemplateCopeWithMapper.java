package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.TemplateCopeWith;
import com.jayud.mall.model.vo.TemplateCopeWithVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 报价对应应付费用明细 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface TemplateCopeWithMapper extends BaseMapper<TemplateCopeWith> {

    /**
     * 根据报价模板id（qie），查询应付费用信息
     * @param qie
     * @return
     */
    List<TemplateCopeWithVO> selectListByQie(@Param("qie") Long qie);
}
