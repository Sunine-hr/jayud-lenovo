package com.jayud.scm.mapper;

import com.jayud.scm.model.po.HgBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.SingleWindowData;
import com.jayud.scm.model.vo.YunBaoGuanData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 报关单主表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Mapper
public interface HgBillMapper extends BaseMapper<HgBill> {

    List<SingleWindowData> getSingleWindowData(@Param("billNo") String billNo);

    List<YunBaoGuanData> getYunBaoGuanData(@Param("id")Integer id);
}
