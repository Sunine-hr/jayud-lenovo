package com.jayud.oms.order.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.order.model.po.OmsOrderFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.order.model.vo.OmsOrderFollowVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单状态跟进表 服务类
 *
 * @author jayud
 * @since 2022-03-23
 */
public interface IOmsOrderFollowService extends IService<OmsOrderFollow> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrderFollow
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.oms.order.model.po.OmsOrderFollow>
     **/
    IPage<OmsOrderFollow> selectPage(OmsOrderFollow omsOrderFollow,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrderFollow
     * @param: req
     * @return: java.util.List<com.jayud.oms.order.model.po.OmsOrderFollow>
     **/
    List<OmsOrderFollow> selectList(OmsOrderFollow omsOrderFollow);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-23
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-23
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryOmsOrderFollowForExcel(Map<String, Object> paramMap);

    /**
     * 根据订单id获取订单状态记录
     * @param id
     * @return
     */
    List<OmsOrderFollowVO> getByOrderId(Long id);
}
