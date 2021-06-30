package com.jayud.oceanship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oceanship.bo.QuerySeaBillForm;
import com.jayud.oceanship.po.SeaBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oceanship.vo.SeaBillFormVO;
import com.jayud.oceanship.vo.SeaOrderFormVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 提单信息表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-06-23
 */
public interface SeaBillMapper extends BaseMapper<SeaBill> {

    IPage<SeaBillFormVO> findBillByPage(@Param("page") Page<SeaOrderFormVO> page, @Param("form")QuerySeaBillForm form, @Param("legalIds")List<Long> legalIds);
}
