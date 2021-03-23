package com.jayud.customs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.customs.model.bo.InputOrderCustomsForm;
import com.jayud.customs.model.bo.QueryCustomsOrderInfoForm;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.CustomsOrderInfoVO;
import com.jayud.customs.model.vo.InputOrderCustomsVO;
import com.jayud.customs.model.vo.OrderCustomsVO;
import com.jayud.customs.model.vo.StatisticsDataNumberVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报关业务订单表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderCustomsService extends IService<OrderCustoms> {

    /**
     * 报关子订单是否存在
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);

    /**
     *暂存/提交纯报关订单
     * @param form
     * @return
     */
    public boolean oprOrderCustoms(InputOrderCustomsForm form);

    /**
     * 获取子订单
     * @param param
     * @return
     */
    public List<OrderCustomsVO> findOrderCustomsByCondition(Map<String,Object> param);

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<CustomsOrderInfoVO> findCustomsOrderByPage(QueryCustomsOrderInfoForm form);

    /**
     * 获取订单详情
     * @param mainOrderNo
     * @return
     */
    public InputOrderCustomsVO getOrderCustomsDetail(String mainOrderNo);

    /**
     * 报关各个菜单列表数据量统计
     * @return
     */
    StatisticsDataNumberVO statisticsDataNumber();


    /**
     * 根据主订单集合查询所有报关信息
     */
    List<OrderCustoms> getCustomsOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos);

    List<OrderCustoms> getOrderCustomsByStatus(List<String> statuses);

    /**
     * 查询菜单待处理订单数
     * @param status
     * @param legalIds
     * @return
     */
    Integer getNumByStatus(String status, List<Long> legalIds);
}
