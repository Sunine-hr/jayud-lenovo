package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryCostInfoForm;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.vo.CostInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 费用名描述 Mapper 接口
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@Mapper
public interface CostInfoMapper extends BaseMapper<CostInfo> {
    IPage<CostInfoVO> findCostInfoByPage(Page page, @Param("form") QueryCostInfoForm form, @Param("cids") List<String> cids);
}
