package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.TemplateFileForm;
import com.jayud.mall.model.bo.TemplateFileOrderForm;
import com.jayud.mall.model.po.TemplateFile;
import com.jayud.mall.model.vo.TemplateFileVO;

import java.util.List;

/**
 * <p>
 * 模板对应模块信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface ITemplateFileService extends IService<TemplateFile> {

    /**
     * 查询模板对应模块信息list
     * @param form
     * @return
     */
    List<TemplateFile> findTemplateFile(TemplateFileForm form);

    List<TemplateFileVO> findTemplateFileByOrder(TemplateFileOrderForm form);

}
