package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryCustomsDataForm;
import com.jayud.mall.model.po.CustomsData;
import com.jayud.mall.model.vo.CustomsDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 报关资料表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Mapper
@Component
public interface CustomsDataMapper extends BaseMapper<CustomsData> {

    /**
     * 分页
     * @param page
     * @param form
     * @return
     */
    IPage<CustomsDataVO> findCustomsDataByPage(Page<CustomsDataVO> page,@Param("form") QueryCustomsDataForm form);
}
