package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryOfferInfoForm;
import com.jayud.mall.model.po.OfferInfo;
import com.jayud.mall.model.vo.OfferInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 报价管理 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-05
 */
@Mapper
@Component
public interface OfferInfoMapper extends BaseMapper<OfferInfo> {

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OfferInfoVO> findOfferInfoByPage(Page<OfferInfoVO> page, QueryOfferInfoForm form);

    /**
     * 查询报价信息
     * @param id
     * @return
     */
    OfferInfoVO selectOfferInfoVO(Long id);
}
