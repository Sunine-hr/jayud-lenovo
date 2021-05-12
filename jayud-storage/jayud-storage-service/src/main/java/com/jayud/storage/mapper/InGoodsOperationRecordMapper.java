package com.jayud.storage.mapper;

import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 入库商品操作记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Mapper
public interface InGoodsOperationRecordMapper extends BaseMapper<InGoodsOperationRecord> {

    List<InGoodsOperationRecord> getListByOrderId(@Param("id") Long id);
}
