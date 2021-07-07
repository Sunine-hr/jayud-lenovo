package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.CustomsBaseServiceCost;
import com.jayud.mall.model.vo.CustomsBaseServiceCostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 海关基础资料-服务费用表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-08
 */
@Mapper
@Component
public interface CustomsBaseServiceCostMapper extends BaseMapper<CustomsBaseServiceCost> {

    /**
     * 查询
     * @param paraMap
        paraMap.put("type", 1);
        paraMap.put("customs_id", customsId);
     * @return
     */
    List<CustomsBaseServiceCostVO> findCustomsBaseServiceCostByParaMap(@Param("paraMap") Map<String, Object> paraMap);
}
