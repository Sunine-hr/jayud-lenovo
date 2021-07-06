package com.jayud.mall.service;

import com.jayud.mall.model.bo.BillCustomsCounterListForm;
import com.jayud.mall.model.po.BillCustomsCounterList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.BillCustomsCounterListVO;

import java.util.List;

/**
 * <p>
 * （提单)报关、清关 关联 柜子清单 表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-07-06
 */
public interface IBillCustomsCounterListService extends IService<BillCustomsCounterList> {

    /**
     * 查询已选的装柜清单列表
     * @param form
     * @return
     */
    List<BillCustomsCounterListVO> findBillCustomsCounterListByTypeAndCustomsId(BillCustomsCounterListForm form);
}
