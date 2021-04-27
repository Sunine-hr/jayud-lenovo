package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.BillClearanceInfoForm;
import com.jayud.mall.model.bo.ClearanceInfoCaseForm;
import com.jayud.mall.model.po.BillClearanceInfo;
import com.jayud.mall.model.po.ClearanceInfoCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.AccountReceivableVO;
import com.jayud.mall.model.vo.ClearanceInfoCaseVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 清关文件箱号 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface ClearanceInfoCaseMapper extends BaseMapper<ClearanceInfoCase> {
    /**
     * 集合数据
     * @param
     * @return
     */
    IPage<ClearanceInfoCaseVO> findClearanceInfoCaseByPage(Page<ClearanceInfoCase> page, @Param("form") ClearanceInfoCaseForm form);


    /**
     * 增加
     * @param clearanceInfoCase
     */
    void insertClearanceInfoCase(@Param("clearanceInfoCase") ClearanceInfoCaseForm clearanceInfoCase);

    /**
     * 修改
     * @param clearanceInfoCase
     */
    void updateClearanceInfoCase(@Param("clearanceInfoCase") ClearanceInfoCaseForm clearanceInfoCase);

    /**
     * 删除
     * @param id
     */
    void deleteClearanceInfoCase(@Param("id") Long id);
}
