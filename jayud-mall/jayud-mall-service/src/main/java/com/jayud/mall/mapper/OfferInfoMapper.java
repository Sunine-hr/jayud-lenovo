package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryOfferInfoFareForm;
import com.jayud.mall.model.bo.QueryOfferInfoForm;
import com.jayud.mall.model.po.OfferInfo;
import com.jayud.mall.model.vo.OfferInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

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
    IPage<OfferInfoVO> findOfferInfoByPage(Page<OfferInfoVO> page, @Param("form") QueryOfferInfoForm form);

    /**
     * 查询报价信息
     * @param id
     * @return
     */
    OfferInfoVO selectOfferInfoVO(@Param("id") Long id);

    /**
     * 分页查询报价(运价)
     * @param page
     * @param form
     * @return
     */
    IPage<OfferInfoVO> findOfferInfoFareByPage(Page<OfferInfoVO> page, @Param("form") QueryOfferInfoFareForm form);

    /**
     * 查看运价详情
     * @param id
     * @return
     */
    OfferInfoVO lookOfferInfoFare(@Param("id") Long id);

    /**
     * 客户首页(最新报价Top4)
     * @param form
     * @return
     */
    List<OfferInfoVO> findOfferInfoFareTop4(@Param("form") QueryOfferInfoFareForm form);

    /**
     * 分页查询报价(给配载查询报价使用)
     * @param page
     * @param form
     * @return
     */
    IPage<OfferInfoVO> findOfferInfoPageByConf(Page<OfferInfoVO> page, @Param("form") QueryOfferInfoForm form);
}
