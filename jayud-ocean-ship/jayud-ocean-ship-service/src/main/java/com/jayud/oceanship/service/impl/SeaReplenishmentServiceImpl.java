package com.jayud.oceanship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.oceanship.bo.QuerySeaOrderForm;
import com.jayud.oceanship.feign.FileClient;
import com.jayud.oceanship.feign.OauthClient;
import com.jayud.oceanship.feign.OmsClient;
import com.jayud.oceanship.po.SeaReplenishment;
import com.jayud.oceanship.mapper.SeaReplenishmentMapper;
import com.jayud.oceanship.service.ISeaContainerInformationService;
import com.jayud.oceanship.service.ISeaReplenishmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oceanship.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 海运补料表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-18
 */
@Service
@Slf4j
public class SeaReplenishmentServiceImpl extends ServiceImpl<SeaReplenishmentMapper, SeaReplenishment> implements ISeaReplenishmentService {

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private ISeaContainerInformationService seaContainerInformationService;

    /**
     * 分页获取草稿提单数据
     *
     * @return
     */
    @Override
    public IPage<SeaReplenishmentFormVO> findBillByPage(QuerySeaOrderForm form) {
        if (StringUtils.isEmpty(form.getStatus())) { //订单列表
            form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                    , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
        } else {
            form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
        }

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<SeaOrderFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findBillByPage(page, form, legalIds);
    }

    @Override
    public SeaReplenishmentVO getSeaRepOrderDetails(Long orderId) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        Integer businessType = BusinessTypeEnum.HY.getCode();
        //海运订单信息
        SeaReplenishmentVO seaReplenishmentVO = this.baseMapper.getSeaRepOrderDetails(orderId);
        //获取截补料中的柜型数量以及货柜信息
        List<SeaContainerInformationVO> seaContainerInformations = seaContainerInformationService.getList(seaReplenishmentVO.getId());
        seaReplenishmentVO.setSeaContainerInformations(seaContainerInformations);

        //查询商品信息
        ApiResult<List<GoodsVO>> result1 = this.omsClient.getGoodsByBusOrders(Collections.singletonList(seaReplenishmentVO.getOrderNo()), businessType);
        if (result1.getCode() != HttpStatus.SC_OK) {
            log.warn("查询商品信息失败 seaOrderId={}", seaReplenishmentVO.getId());
        }
        seaReplenishmentVO.setGoodsForms(result1.getData());
        //查询地址信息
        ApiResult<List<OrderAddressVO>> resultOne1 = this.omsClient.getOrderAddressByBusOrders(Collections.singletonList(seaReplenishmentVO.getOrderNo()), businessType);
        if (resultOne1.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 seaOrderId={}", seaReplenishmentVO.getId());
        }
        //处理地址信息
        for (OrderAddressVO address : resultOne1.getData()) {
            address.getFile(prePath);
            seaReplenishmentVO.processingAddress(address);
        }

        return seaReplenishmentVO;
    }

    @Override
    public void deleteSeaReplenishment(Long orderId, String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sea_order_id", orderId);
        queryWrapper.like("sea_order_no", orderNo);
        this.baseMapper.delete(queryWrapper);
    }

    @Override
    public List<SeaReplenishment> getList(Long orderId, String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sea_order_id", orderId);
        queryWrapper.like("sea_order_no", orderNo);
        return this.baseMapper.selectList(queryWrapper);
    }

}
