package com.jayud.oceanship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oceanship.bo.QuerySeaOrderForm;
import com.jayud.oceanship.po.SeaOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oceanship.vo.SeaOrderFormVO;
import com.jayud.oceanship.vo.SeaOrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 海运订单表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
public interface SeaOrderMapper extends BaseMapper<SeaOrder> {

    /**
     * 根据订单id获取订单信息
     * @param id
     * @return
     */
    SeaOrderVO getSeaOrder(@Param("id") Long id);

    /**
     * 分页获取订单列表
     * @param page
     * @param form
     * @param legalIds
     * @return
     */
    IPage<SeaOrderFormVO> findByPage(Page<SeaOrder> page,@Param("form") QuerySeaOrderForm form,@Param("legalIds") List<Long> legalIds);
}
