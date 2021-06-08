package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.CustomsBaseFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 海关基础资料-文件表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-08
 */
@Mapper
@Component
public interface CustomsBaseFileMapper extends BaseMapper<CustomsBaseFile> {

}
