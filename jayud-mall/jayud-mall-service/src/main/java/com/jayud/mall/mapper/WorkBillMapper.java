package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryWorkBillForm;
import com.jayud.mall.model.po.WorkBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.WorkBillVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 提单工单表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-30
 */
@Mapper
@Component
public interface WorkBillMapper extends BaseMapper<WorkBill> {

    /**
     * 订单工单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<WorkBillVO> findWorkBillByPage(Page<WorkBillVO> page, @Param("form") QueryWorkBillForm form);

    /**
     * 根据id，查询工单
     * @param id
     * @return
     */
    WorkBillVO findWorkBillById(@Param("id") Long id);
}
