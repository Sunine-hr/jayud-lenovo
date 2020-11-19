package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.TemplateCopeReceivable;
import com.jayud.mall.model.vo.TemplateCopeReceivableVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 报价对应应收费用明细 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface TemplateCopeReceivableMapper extends BaseMapper<TemplateCopeReceivable> {

    /**
     * 根据报价模板id，查询应收费用明细（海运）
     * @param qie
     * @return
     */
    List<TemplateCopeReceivableVO> findTemplateCopeReceivableByQie(Integer qie);
}
