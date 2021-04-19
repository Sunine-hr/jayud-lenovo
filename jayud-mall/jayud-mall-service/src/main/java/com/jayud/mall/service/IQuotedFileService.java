package com.jayud.mall.service;

import com.jayud.mall.model.bo.QuotedFileForm;
import com.jayud.mall.model.po.QuotedFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.QuotedFileReturnVO;

import java.util.List;

/**
 * <p>
 * 报价对应的文件表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface IQuotedFileService extends IService<QuotedFile> {

    /**
     * 查询报价对应的文件表list
     * @param form
     * @return
     */
    List<QuotedFile> findQuotedFile(QuotedFileForm form);

    /**
     * 报价模板使用模板文件
     * @param form
     * @return
     */
    List<QuotedFileReturnVO> findQuotedFileBy(QuotedFileForm form);
}
