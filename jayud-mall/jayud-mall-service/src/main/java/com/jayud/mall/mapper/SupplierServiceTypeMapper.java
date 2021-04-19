package com.jayud.mall.mapper;

import com.jayud.mall.model.po.SupplierServiceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.SupplierServiceTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 供应商服务类型 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-24
 */
@Mapper
@Component
public interface SupplierServiceTypeMapper extends BaseMapper<SupplierServiceType> {

    /**
     * 根据供应商id，查询供应商对应服务类型
     * @param infoId
     * @return
     */
    List<SupplierServiceTypeVO> findSupplierServiceTypeByInfoId(@Param("infoId") Long infoId);
}
