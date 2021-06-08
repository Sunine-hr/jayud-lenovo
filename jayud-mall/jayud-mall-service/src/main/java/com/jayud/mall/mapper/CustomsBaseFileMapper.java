package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.CustomsBaseFile;
import com.jayud.mall.model.vo.CustomsBaseFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询
     * @param paraMap
        paraMap.put("type", 1);
        paraMap.put("customs_id", customsId);
     * @return
     */
    List<CustomsBaseFileVO> findCustomsBaseFileByParaMap(@Param("paraMap") Map<String, Object> paraMap);
}
