package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.BillCustomsInfoForm;
import com.jayud.mall.model.bo.ClearanceInfoCaseForm;
import com.jayud.mall.model.bo.CustomsInfoCaseForm;
import com.jayud.mall.model.po.ClearanceInfoCase;
import com.jayud.mall.model.po.CustomsInfoCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.BillCustomsInfoVO;
import com.jayud.mall.model.vo.ClearanceInfoCaseVO;
import com.jayud.mall.model.vo.CustomsInfoCaseVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 报关文件箱号 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface CustomsInfoCaseMapper extends BaseMapper<CustomsInfoCase> {
    /**
     * 集合数据
     * @param
     * @return
     */
    IPage<BillCustomsInfoVO> findCustomsInfoCasePage(@Param("form") CustomsInfoCaseForm form);


    /**
     * 增加
     * @param customsInfoCase
     */
    void insertCustomsInfoCase(@Param("customsInfoCase") CustomsInfoCaseForm customsInfoCase);

    /**
     * 修改
     * @param customsInfoCases
     */
    void updateCustomsInfoCase(@Param("customsInfoCase") CustomsInfoCaseForm customsInfoCase);

    /**
     * 删除
     * @param id
     */
    void deleteCustomsInfoCase(@Param("id") Long id);
}
