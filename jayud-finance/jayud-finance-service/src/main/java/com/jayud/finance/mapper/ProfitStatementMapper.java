package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.finance.bo.QueryProfitStatementForm;
import com.jayud.finance.po.ProfitStatement;
import com.jayud.finance.vo.ProfitStatementBasicData;
import com.jayud.finance.vo.ProfitStatementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 利润报表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2021-07-13
 */
public interface ProfitStatementMapper extends BaseMapper<ProfitStatement> {
    public List<ProfitStatementBasicData> getBasicDataOfProfitStatement();

    List<ProfitStatementVO> list(@Param("form") QueryProfitStatementForm form);
}
