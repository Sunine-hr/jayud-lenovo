package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.TemplateFile;
import com.jayud.mall.model.vo.TemplateFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 模板对应模块信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface TemplateFileMapper extends BaseMapper<TemplateFile> {

    /**
     * 根据`报价模板id`，查询`文件信息`
     * @param qie
     * @return
     */
    List<TemplateFileVO> findTemplateFileByQie(@Param("qie") Long qie);
}
