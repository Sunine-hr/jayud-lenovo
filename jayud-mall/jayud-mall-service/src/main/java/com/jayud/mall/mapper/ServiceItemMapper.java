package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.bo.ServiceItemForm;
import com.jayud.mall.model.po.ServiceItem;
import com.jayud.mall.model.vo.ServiceItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 基础表-服务项目表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
@Mapper
@Component
public interface ServiceItemMapper extends BaseMapper<ServiceItem> {

    /**
     * 查询 服务项目 list
     * @param form
     * @return
     */
    List<ServiceItemVO> findServiceItem(@Param("form") ServiceItemForm form);
}
