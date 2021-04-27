package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.ClearanceInfoCaseForm;
import com.jayud.mall.model.po.ClearanceInfoCase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.ClearanceInfoCaseVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 清关文件箱号 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface IClearanceInfoCaseService extends IService<ClearanceInfoCase> {
    /**
     * 集合数据
     * @param
     * @return
     */
    IPage<ClearanceInfoCaseVO> findClearanceInfoCaseByPage(@Param("form") ClearanceInfoCaseForm form);


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
