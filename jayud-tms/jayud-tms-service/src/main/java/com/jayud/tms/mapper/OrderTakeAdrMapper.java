package com.jayud.tms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.tms.model.po.OrderTakeAdr;
import com.jayud.tms.model.vo.DriverOrderTakeAdrVO;
import com.jayud.tms.model.vo.InputOrderTakeAdrVO;
import com.jayud.tms.model.vo.OrderTakeAdrInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单对应收货地址 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderTakeAdrMapper extends BaseMapper<OrderTakeAdr> {

    /**
     * 获取提货信息
     *
     * @param orderNo
     * @return
     */
    public List<InputOrderTakeAdrVO> findTakeGoodsInfo(@Param("orderNo") String orderNo);

    /**
     * 根据订单编号查询司机送货/收货地址
     */
    List<DriverOrderTakeAdrVO> getDriverOrderTakeAdr(@Param("list") List<String> orderNoList
            , @Param("oprType") Integer oprType);



    /**
     *  批量查询所有地址详细内容
     */
    List<OrderTakeAdrInfoVO> getOrderTakeAdrInfos(@Param("list") List<String> orderNoList
            , @Param("oprType") Integer oprType);

    /**
     *
     * @param months
     * @param format
     * @return
     */
    List<OrderTakeAdr> getByTakeTime(@Param("months") List<String> months,@Param("format") String format);
}
