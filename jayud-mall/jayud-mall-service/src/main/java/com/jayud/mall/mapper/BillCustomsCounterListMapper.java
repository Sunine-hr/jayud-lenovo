package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.bo.BillCustomsCounterListForm;
import com.jayud.mall.model.po.BillCustomsCounterList;
import com.jayud.mall.model.vo.BillCustomsCounterListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * （提单)报关、清关 关联 柜子清单 表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-07-06
 */
@Mapper
@Component
public interface BillCustomsCounterListMapper extends BaseMapper<BillCustomsCounterList> {

    /**
     * 查询已选的装柜清单列表
     * @param form
     * @return
     */
    List<BillCustomsCounterListVO> findBillCustomsCounterListByTypeAndCustomsId(@Param("form") BillCustomsCounterListForm form);
}
