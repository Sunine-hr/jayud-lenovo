package com.jayud.customs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.feign.FileClient;
import com.jayud.customs.feign.OmsClient;
import com.jayud.customs.mapper.OrderCustomsMapper;
import com.jayud.customs.model.bo.*;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.*;
import com.jayud.customs.service.IOrderCustomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        List<OrderCustoms> orderCustomsList = baseMapper.selectList(queryWrapper);
        if(orderCustomsList == null || orderCustomsList.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean oprOrderCustoms(InputOrderForm form) {
        try {
            InputOrderCustomsForm inputOrderCustomsForm = form.getOrderCustomsForm();
            InputMainOrderForm inputMainOrderForm = form.getOrderForm();
            //处理主订单
            //保存主订单数据,返回主订单号,暂存和提交
            inputMainOrderForm.setCmd(form.getCmd());
            inputMainOrderForm.setClassCode(OrderStatusEnum.CBG.getCode());
            ApiResult apiResult = omsClient.oprMainOrder(inputMainOrderForm);
            String mainOrderNo = String.valueOf(apiResult.getData());
            //根据主订单号获取主订单ID
            Long mainOrderId = omsClient.getIdByOrderNo(mainOrderNo).getData();
            if(mainOrderId == null){
                return false;
            }
            //调用服务失败
            if(apiResult.getCode() != 200 || apiResult.getData() == null){
                return false;
            }
            //暂存或提交只是主订单的状态不一样，子订单的操作每次先根据主订单号清空子订单
            QueryWrapper<OrderCustoms> queryWrapper = new QueryWrapper<OrderCustoms>();
            queryWrapper.eq("main_order_no",mainOrderNo);
            remove(queryWrapper);
            //子订单数据初始化处理
            //设置子订单号/报关抬头/结算单位/附件
            List<OrderCustoms> orderCustomsList = new ArrayList<>();
            List<InputSubOrderCustomsForm> subOrderCustomsForms = inputOrderCustomsForm.getSubOrders();
            for (InputSubOrderCustomsForm subOrder : subOrderCustomsForms) {
                OrderCustoms customs = ConvertUtil.convert(inputOrderCustomsForm, OrderCustoms.class);
                customs.setDescription(subOrder.getDescription());
                customs.setDescName(subOrder.getDescName());
                customs.setOrderNo(subOrder.getOrderNo());
                customs.setTitle(subOrder.getTitle());
                customs.setIsTitle(subOrder.getIsTitle());
                customs.setUnitCode(subOrder.getUnitCode());
                customs.setMainOrderNo(String.valueOf(apiResult.getData()));
                customs.setStatus(OrderStatusEnum.CUSTOMS_C_0.getCode());
                customs.setCreatedUser(getLoginUser());
                orderCustomsList.add(customs);
            }
            if(!orderCustomsList.isEmpty()) {
                saveOrUpdateBatch(orderCustomsList);
            }

            //记录操作状态
            if("submit".equals(form.getCmd())){
                OprStatusForm oprStatusForm = new OprStatusForm();
                oprStatusForm.setStatus(OrderStatusEnum.MAIN_PROCESS_1.getCode());
                oprStatusForm.setStatusName(OrderStatusEnum.MAIN_PROCESS_1.getDesc());
                oprStatusForm.setMainOrderId(mainOrderId);
                omsClient.saveOprStatus(oprStatusForm);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public InputOrderVO editOrderCustomsView(Long id) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        InputOrderVO inputOrderVO = new InputOrderVO();
        //1.查询主订单信息
        InputMainOrderVO inputMainOrderVO = omsClient.getMainOrderById(id).getData();
        inputOrderVO.setOrderForm(inputMainOrderVO);
        //2.查询子订单信息,根据主订单
        InputOrderCustomsVO inputOrderCustomsVO = new InputOrderCustomsVO();
        if(inputMainOrderVO != null && inputMainOrderVO.getOrderNo() != null){
            Map<String, Object> param = new HashMap<>();
            param.put("main_order_no",inputMainOrderVO.getOrderNo());
            List<OrderCustomsVO> orderCustomsVOS = findOrderCustomsByCondition(param);
            if(orderCustomsVOS != null && orderCustomsVOS.size() > 0){
                OrderCustomsVO orderCustomsVO = orderCustomsVOS.get(0);
                //设置纯报关头部分
                inputOrderCustomsVO.setPortCode(orderCustomsVO.getPortCode());
                inputOrderCustomsVO.setPortName(orderCustomsVO.getPortName());
                inputOrderCustomsVO.setGoodsType(orderCustomsVO.getGoodsType());
                inputOrderCustomsVO.setCntrNo(orderCustomsVO.getCntrNo());
                inputOrderCustomsVO.setCntrPics(StringUtils.getFileViews(orderCustomsVO.getCntrPic(),orderCustomsVO.getCntrPicName(),prePath));
                inputOrderCustomsVO.setEncode(orderCustomsVO.getEncode());
                inputOrderCustomsVO.setEncodePics(StringUtils.getFileViews(orderCustomsVO.getEncodePic(),orderCustomsVO.getEncodePicName(),prePath));
                inputOrderCustomsVO.setIsAgencyTax(orderCustomsVO.getIsAgencyTax());
                inputOrderCustomsVO.setSeaTransportNo(orderCustomsVO.getSeaTransportNo());
                inputOrderCustomsVO.setSeaTransportPics(StringUtils.getFileViews(orderCustomsVO.getSeaTransportPic(),orderCustomsVO.getSeaTransPicName(),prePath));
                inputOrderCustomsVO.setAirTransportNo(orderCustomsVO.getAirTransportNo());
                inputOrderCustomsVO.setAirTransportPics(StringUtils.getFileViews(orderCustomsVO.getAirTransportPic(),orderCustomsVO.getAirTransPicName(),prePath));
                inputOrderCustomsVO.setLegalName(orderCustomsVO.getLegalName());
                inputOrderCustomsVO.setBizModel(orderCustomsVO.getBizModel());
                //处理子订单部分
                List<InputSubOrderCustomsVO> subOrderCustomsVOS = new ArrayList<>();
                for (OrderCustomsVO orderCustoms : orderCustomsVOS) {
                    InputSubOrderCustomsVO subOrderCustomsVO = new InputSubOrderCustomsVO();
                    subOrderCustomsVO.setSubOrderId(orderCustoms.getSubOrderId());
                    subOrderCustomsVO.setOrderNo(orderCustoms.getOrderNo());
                    subOrderCustomsVO.setTitle(orderCustoms.getTitle());
                    subOrderCustomsVO.setIsTitle(orderCustoms.getIsTitle());
                    subOrderCustomsVO.setUnitCode(orderCustoms.getUnitCode());
                    //处理子订单附件信息
                    String fileStr = orderCustoms.getFileStr();
                    String fileNameStr = orderCustoms.getFileNameStr();
                    subOrderCustomsVO.setFileViews(StringUtils.getFileViews(fileStr,fileNameStr,prePath));
                    subOrderCustomsVOS.add(subOrderCustomsVO);
                }
                inputOrderCustomsVO.setSubOrders(subOrderCustomsVOS);
                inputOrderCustomsVO.setNumber(String.valueOf(subOrderCustomsVOS.size()));
                inputOrderVO.setOrderCustomsForm(inputOrderCustomsVO);
            }
        }
        return inputOrderVO;
    }

    @Override
    public List<OrderCustomsVO> findOrderCustomsByCondition(Map<String, Object> param) {
        return baseMapper.findOrderCustomsByCondition(param);
    }

    @Override
    public IPage<CustomsOrderInfoVO> findCustomsOrderByPage(QueryCustomsOrderInfoForm form) {
        //定义分页参数
        Page<CustomsOrderInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("oc.id"));
        IPage<CustomsOrderInfoVO> pageInfo = baseMapper.findCustomsOrderByPage(page, form);
        //处理附件
        List<CustomsOrderInfoVO> customsOrderInfoVOS = pageInfo.getRecords();
        String prePath = fileClient.getBaseUrl().getData().toString();
        for (CustomsOrderInfoVO customsOrder : customsOrderInfoVOS) {
            //处理子订单附件信息
            String fileStr = customsOrder.getFileStr();
            String fileNameStr = customsOrder.getFileNameStr();
            customsOrder.setFileViews(StringUtils.getFileViews(fileStr,fileNameStr,prePath));
        }
        return pageInfo;
    }


    /**
     * 获取当前登录用户
     * @return
     */
    @Override
    public String getLoginUser(){
        String loginUser = redisUtils.get("loginUser",100);
        return loginUser;
    }
}
