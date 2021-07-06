package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.TemplateCopeReceivable;
import com.jayud.mall.model.vo.TemplateCopeReceivableVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
     * 根据报价模板id，查询应收费用明细（海运费）
     * @param qie
     * @return
     */
    List<TemplateCopeReceivableVO> findTemplateCopeReceivableOceanFeeByQie(@Param("qie") Integer qie);

    /**
     * 根据报价模板id，查询应收费用明细（内陆费）
     * @param qie
     * @return
     */
    List<TemplateCopeReceivableVO> findTemplateCopeReceivableInlandFeeListByQie(@Param("qie") Integer qie);

    /**
     * 根据报价模板id，查询应收费用明细（其他费用）
     * @param qie
     * @return
     */
    List<TemplateCopeReceivableVO> findTemplateCopeReceivableOtherFeeListByQie(@Param("qie") Integer qie);

    /**
     * 根据报价模板id，查询应收费用明细
     * @param qie
     * @return
     */
    List<TemplateCopeReceivableVO> findTemplateCopeReceivableByQie(@Param("qie") Integer qie);

    /**
     * 根据 报价id 和 规格代码 ，查询应收费用
     * @param qie
     * @param specification_code
     * @return
     */
    TemplateCopeReceivableVO findTemplateCopeReceivableByQieAndSpecificationCode(@Param("qie") Integer qie, @Param("specification_code") String specification_code);
}
