package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryOrderInfoForm;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.vo.InputOrderVO;
import com.jayud.oms.model.vo.OrderInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 主订单基础数据表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 分页查询未提交订单
     * @param page
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findOrderInfoByPage(Page page, @Param("form") QueryOrderInfoForm form);

    /**
     * 获取主订单信息
     * @param idValue
     * @return
     */
    InputOrderVO getMainOrderById(Long idValue);

}
