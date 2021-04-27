package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.CustomsInfoCaseForm;
import com.jayud.mall.model.po.CustomsInfoCase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.CustomsInfoCaseVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 报关文件箱号 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface ICustomsInfoCaseService extends IService<CustomsInfoCase> {
    /**
     * 集合数据
     * @param
     * @return
     */
    IPage<CustomsInfoCaseVO> findCustomsInfoCasePage(@Param("form") CustomsInfoCaseForm form);


    /**
     * 增加
     * @param customsInfoCase
     */
    void insertCustomsInfoCase(@Param("customsInfoCase") CustomsInfoCaseForm customsInfoCase);

    /**
     * 修改
     * @param customsInfoCase
     */
    void updateCustomsInfoCase(@Param("customsInfoCase") CustomsInfoCaseForm customsInfoCase);

    /**
     * 删除
     * @param id
     */
    void deleteCustomsInfoCase(@Param("id") Long id);
}
