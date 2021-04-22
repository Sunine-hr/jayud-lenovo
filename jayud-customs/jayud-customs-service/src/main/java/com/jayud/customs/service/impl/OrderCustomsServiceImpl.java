package com.jayud.customs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderOprCmdEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.feign.FileClient;
import com.jayud.customs.feign.OauthClient;
import com.jayud.customs.feign.OmsClient;
import com.jayud.customs.mapper.OrderCustomsMapper;
import com.jayud.customs.model.bo.*;
import com.jayud.customs.model.enums.BGOrderStatusEnum;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.*;
import com.jayud.customs.service.IOrderCustomsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报关业务订单表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderCustomsServiceImpl extends ServiceImpl<OrderCustomsMapper, OrderCustoms> implements IOrderCustomsService {

    @Autowired
    OmsClient omsClient;

    @Autowired
    FileClient fileClient;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    OauthClient oauthClient;

    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", orderNo);
        List<OrderCustoms> orderCustomsList = baseMapper.selectList(queryWrapper);
        if (orderCustomsList == null || orderCustomsList.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean oprOrderCustoms(InputOrderCustomsForm form) {
        try {
            //暂存或提交只是主订单的状态不一样，子订单的操作每次先根据主订单号清空子订单
//            QueryWrapper<OrderCustoms> queryWrapper = new QueryWrapper<OrderCustoms>();
//            queryWrapper.eq("main_order_no", form.getMainOrderNo());
//            remove(queryWrapper);
            for (InputSubOrderCustomsForm subOrder : form.getSubOrders()) {
                if (subOrder.getSubOrderId() != null) {
                    removeById(subOrder.getSubOrderId());
                }
            }
            //子订单数据初始化处理
            //设置子订单号/报关抬头/结算单位/附件
            List<OrderCustoms> orderCustomsList = new ArrayList<>();
            List<InputSubOrderCustomsForm> subOrderCustomsForms = form.getSubOrders();
            for (InputSubOrderCustomsForm subOrder : subOrderCustomsForms) {
                OrderCustoms customs = ConvertUtil.convert(form, OrderCustoms.class);
                customs.setDescription(subOrder.getDescription());
                customs.setDescName(subOrder.getDescName());
                customs.setOrderNo(subOrder.getOrderNo());
                customs.setTitle(subOrder.getTitle());
                customs.setIsTitle(subOrder.getIsTitle());
                customs.setUnitCode(subOrder.getUnitCode());
                customs.setMainOrderNo(form.getMainOrderNo());
                customs.setStatus(OrderStatusEnum.CUSTOMS_C_0.getCode());
                customs.setCreatedUser(form.getLoginUser());
                customs.setCntrPic(StringUtils.getFileStr(form.getCntrPics()));
                customs.setCntrPicName(StringUtils.getFileNameStr(form.getCntrPics()));
                customs.setDescription(StringUtils.getFileStr(subOrder.getFileViews()));
                customs.setDescName(StringUtils.getFileNameStr(subOrder.getFileViews()));
                customs.setEncodePic(StringUtils.getFileStr(form.getEncodePics()));
                customs.setEncodePicName(StringUtils.getFileNameStr(form.getEncodePics()));
                customs.setAirTransportPic(StringUtils.getFileStr(form.getAirTransportPics()));
                customs.setAirTransPicName(StringUtils.getFileNameStr(form.getAirTransportPics()));
                customs.setSeaTransportPic(StringUtils.getFileStr(form.getSeaTransportPics()));
                customs.setSeaTransPicName(StringUtils.getFileNameStr(form.getSeaTransportPics()));
                orderCustomsList.add(customs);
            }
            if (!orderCustomsList.isEmpty()) {
                saveOrUpdateBatch(orderCustomsList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<OrderCustomsVO> findOrderCustomsByCondition(Map<String, Object> param) {
        return baseMapper.findOrderCustomsByCondition(param);
    }

    @Override
    public IPage<CustomsOrderInfoVO> findCustomsOrderByPage(QueryCustomsOrderInfoForm form) {

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        //定义分页参数
        Page<CustomsOrderInfoVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("oc.id"));
        IPage<CustomsOrderInfoVO> pageInfo = baseMapper.findCustomsOrderByPage(page, form, legalIds);
        //处理附件
        List<CustomsOrderInfoVO> customsOrderInfoVOS = pageInfo.getRecords();
        String prePath = fileClient.getBaseUrl().getData().toString();
        for (CustomsOrderInfoVO customsOrder : customsOrderInfoVOS) {
            customsOrder.setGoodsTypeDesc(customsOrder.getGoodsType());
            //处理子订单附件信息
            String fileStr = customsOrder.getFileStr();
            String fileNameStr = customsOrder.getFileNameStr();
            customsOrder.setFileViews(StringUtils.getFileViews(fileStr, fileNameStr, prePath));
        }
        return pageInfo;
    }


    @Override
    public InputOrderCustomsVO getOrderCustomsDetail(String mainOrderNo) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        InputOrderCustomsVO inputOrderCustomsVO = new InputOrderCustomsVO();
        Map<String, Object> param = new HashMap<>();
        param.put("main_order_no", mainOrderNo);
        List<OrderCustomsVO> orderCustomsVOS = findOrderCustomsByCondition(param);
        if (orderCustomsVOS != null && orderCustomsVOS.size() > 0) {
            OrderCustomsVO orderCustomsVO = orderCustomsVOS.get(0);
            //设置纯报关头部分
            inputOrderCustomsVO.setId(orderCustomsVO.getSubOrderId());
            inputOrderCustomsVO.setPortCode(orderCustomsVO.getPortCode());
            inputOrderCustomsVO.setPortName(orderCustomsVO.getPortName());
            inputOrderCustomsVO.setGoodsType(orderCustomsVO.getGoodsType());
            inputOrderCustomsVO.setCntrNo(orderCustomsVO.getCntrNo());
            inputOrderCustomsVO.setCntrPics(StringUtils.getFileViews(orderCustomsVO.getCntrPic(), orderCustomsVO.getCntrPicName(), prePath));
            inputOrderCustomsVO.setEncode(orderCustomsVO.getEncode());
            inputOrderCustomsVO.setEncodePics(StringUtils.getFileViews(orderCustomsVO.getEncodePic(), orderCustomsVO.getEncodePicName(), prePath));
            inputOrderCustomsVO.setIsAgencyTax(orderCustomsVO.getIsAgencyTax());
            inputOrderCustomsVO.setSeaTransportNo(orderCustomsVO.getSeaTransportNo());
            inputOrderCustomsVO.setSeaTransportPics(StringUtils.getFileViews(orderCustomsVO.getSeaTransportPic(), orderCustomsVO.getSeaTransPicName(), prePath));
            inputOrderCustomsVO.setAirTransportNo(orderCustomsVO.getAirTransportNo());
            inputOrderCustomsVO.setAirTransportPics(StringUtils.getFileViews(orderCustomsVO.getAirTransportPic(), orderCustomsVO.getAirTransPicName(), prePath));
            inputOrderCustomsVO.setLegalName(orderCustomsVO.getLegalName());
            inputOrderCustomsVO.setLegalEntityId(orderCustomsVO.getLegalEntityId());
            inputOrderCustomsVO.setBizModel(orderCustomsVO.getBizModel());
            inputOrderCustomsVO.setSupervisionMode(orderCustomsVO.getSupervisionMode());
            //为了控制驳回编辑子订单之间互不影响,报关中驳回时所有子订单都应驳回
            inputOrderCustomsVO.setSubCustomsStatus(orderCustomsVO.getStatus());
            //处理子订单部分
            List<InputSubOrderCustomsVO> subOrderCustomsVOS = new ArrayList<>();
            for (OrderCustomsVO orderCustoms : orderCustomsVOS) {
                InputSubOrderCustomsVO subOrderCustomsVO = new InputSubOrderCustomsVO();
                subOrderCustomsVO.setSubOrderId(orderCustoms.getSubOrderId());
                subOrderCustomsVO.setOrderNo(orderCustoms.getOrderNo());
                subOrderCustomsVO.setTitle(orderCustoms.getTitle());
                subOrderCustomsVO.setIsTitle(orderCustoms.getIsTitle());
                subOrderCustomsVO.setUnitCode(orderCustoms.getUnitCode());
                orderCustoms.setStatusDesc(orderCustoms.getStatus());
                subOrderCustomsVO.setStatusDesc(orderCustoms.getStatusDesc());
                subOrderCustomsVO.setEntrustNo(orderCustoms.getEntrustNo());
                subOrderCustomsVO.setSupervisionMode(orderCustoms.getSupervisionMode());
                //处理子订单附件信息
                String fileStr = orderCustoms.getFileStr();
                String fileNameStr = orderCustoms.getFileNameStr();
                subOrderCustomsVO.setFileViews(StringUtils.getFileViews(fileStr, fileNameStr, prePath));
                subOrderCustomsVOS.add(subOrderCustomsVO);
            }
            inputOrderCustomsVO.setSubOrders(subOrderCustomsVOS);
            inputOrderCustomsVO.setNumber(String.valueOf(subOrderCustomsVOS.size()));

        }
        return inputOrderCustomsVO;
    }

    @Override
    public StatisticsDataNumberVO statisticsDataNumber() {
        return baseMapper.statisticsDataNumber();
    }

    /**
     * 根据主订单集合查询所有报关信息
     */
    @Override
    public List<OrderCustoms> getCustomsOrderByMainOrderNos(List<String> mainOrderNos) {
        QueryWrapper<OrderCustoms> condition = new QueryWrapper<>();
        condition.lambda().in(OrderCustoms::getMainOrderNo, mainOrderNos);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 获取特定状态下的报关订单
     *
     * @param statuses
     * @return
     */
    @Override
    public List<OrderCustoms> getOrderCustomsByStatus(List<String> statuses) {
        return this.baseMapper.getOrderCustomsByStatus(statuses);
    }

    /**
     * 查询菜单待处理订单数
     *
     * @param status
     * @param legalIds
     * @return
     */
    @Override
    public Integer getNumByStatus(String status, List<Long> legalIds) {
        List<String> mainOrderNos = this.baseMapper.getMainOrderNoByStatus(status, legalIds);
        if (CollectionUtils.isEmpty(mainOrderNos)) {
            return 0;
        }
        return this.omsClient.getFilterOrderStatus(mainOrderNos, 1).getData();
    }

    @Override
    public OrderCustoms getOrderCustomsByOrderNo(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", orderNo);

        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean updateProcessStatus(OrderCustoms orderCustoms) {
        OprStatusForm form = new OprStatusForm();
        //获取主订单id
        Long mainOrderId = omsClient.getIdByOrderNo(orderCustoms.getMainOrderNo()).getData();
        //保存操作节点
        form.setMainOrderId(mainOrderId);
        form.setOrderId(orderCustoms.getId());
        form.setStatus(orderCustoms.getStatus());
        form.setStatusName(OrderStatusEnum.getEnums(orderCustoms.getStatus()).getDesc());
        form.setBusinessType(BusinessTypeEnum.BG.getCode());
        form.setOperatorTime(LocalDateTime.now().toString());
        omsClient.saveOprStatus(form);

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditStatus(orderCustoms.getStatus());
        auditInfoForm.setAuditTypeDesc(BGOrderStatusEnum.getDesc1(orderCustoms.getStatus()));
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_CUSTOMS);
        auditInfoForm.setAuditUser(form.getOperatorUser());
        auditInfoForm.setFileViews(form.getFileViewList());
        omsClient.saveAuditInfo(auditInfoForm);

        boolean b = this.saveOrUpdate(orderCustoms);
        return b;
    }
}
