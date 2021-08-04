package com.jayud.oms.mapper;

import com.jayud.oms.model.bo.QueryMsgPushListCondition;
import com.jayud.oms.model.po.MsgPushList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.po.MsgPushListInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 消息推送列表 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
public interface MsgPushListMapper extends BaseMapper<MsgPushList> {

    List<MsgPushListInfoVO> getDetailsList(@Param("condition") QueryMsgPushListCondition condition);
}
