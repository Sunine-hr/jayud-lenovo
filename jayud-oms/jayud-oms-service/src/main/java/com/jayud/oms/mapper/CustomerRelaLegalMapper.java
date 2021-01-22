package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.po.CustomerRelaLegal;
import com.jayud.oms.model.vo.LegalEntityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-12-03
 */
@Mapper
public interface CustomerRelaLegalMapper extends BaseMapper<CustomerRelaLegal> {

    /**
     * 根据客户ID获取相关联的法人主体
     * @param id
     * @param auditStatus
     * @return
     */
    List<LegalEntityVO> findLegalByCustomerId(@Param("id") Long id, Long auditStatus);
}
