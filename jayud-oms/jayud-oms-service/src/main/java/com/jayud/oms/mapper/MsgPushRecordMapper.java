package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryMsgPushRecordForm;
import com.jayud.oms.model.po.MsgPushRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.MsgPushRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 消息推送记录 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-08-03
 */
@Mapper
public interface MsgPushRecordMapper extends BaseMapper<MsgPushRecord> {

    IPage<MsgPushRecordVO> findByPage(@Param("page") Page<MsgPushRecord> page,
                                      @Param("form") QueryMsgPushRecordForm form);
}
