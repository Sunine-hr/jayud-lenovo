package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.bo.QuotedFileForm;
import com.jayud.mall.model.po.QuotedFile;
import com.jayud.mall.model.vo.QuotedFileReturnVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 报价对应的文件表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface QuotedFileMapper extends BaseMapper<QuotedFile> {

    /**
     * 报价模板使用模板文件
     * @param form
     * @return
     */
    List<QuotedFileReturnVO> findQuotedFileBy(@Param("form") QuotedFileForm form);
}
