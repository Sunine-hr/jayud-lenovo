package com.jayud.mall.service;

import com.jayud.mall.model.bo.OrderInfoQueryForm;
import com.jayud.mall.model.po.CounterListInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.CounterCaseInfoExcelVO;
import com.jayud.mall.model.vo.CounterCaseInfoVO;
import com.jayud.mall.model.vo.CounterListInfoVO;
import com.jayud.mall.model.vo.OrderInfoVO;

import java.util.List;

/**
 * <p>
 * 柜子清单信息表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-28
 */
public interface ICounterListInfoService extends IService<CounterListInfo> {

    /**
     * 根据id，查询柜子清单信息表
     * @param id
     * @return
     */
    CounterListInfoVO findCounterListInfoById(Long id);

    /**
     * 根据b_id，查询柜子箱号信息
     * @param b_id
     * @return
     */
    List<CounterCaseInfoVO> findCounterCaseInfo(Long b_id);

    /**
     * 导出清单-柜子清单箱子
     * @param b_id
     * @return
     */
    List<CounterCaseInfoExcelVO> findCounterCaseInfoBybid(Long b_id);

    /**
     * 查询-未选择的订单(柜子清单-绑定订单)
     * @param form
     * @return
     */
    List<OrderInfoVO> findUnselectedOrderInfo(OrderInfoQueryForm form);

    /**
     * 查询-已选择的订单(柜子清单-绑定订单)
     * @param form
     * @return
     */
    List<OrderInfoVO> findSelectedOrderInfo(OrderInfoQueryForm form);
}
