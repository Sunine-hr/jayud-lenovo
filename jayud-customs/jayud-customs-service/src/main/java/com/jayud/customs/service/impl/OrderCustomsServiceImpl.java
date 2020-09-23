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
import com.jayud.customs.feign.OmsClient;
import com.jayud.customs.mapper.OrderCustomsMapper;
import com.jayud.customs.model.bo.*;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.*;
import com.jayud.customs.service.IOrderCustomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public boolean oprOrderCustoms(InputOrderCustomsForm form) {
        try {
            InputOrderForm inputOrderForm = ConvertUtil.convert(form, InputOrderForm.class);
            List<OprOrderLogForm> oprOrderLogForms = new ArrayList<>();
            //处理主订单
            //保存主订单数据,返回主订单号,暂存和提交
            ApiResult apiResult = omsClient.oprMainOrder(inputOrderForm);
            //调用服务失败
            if(apiResult.getCode() != 200 || apiResult.getData() == null){
                return false;
            }

            //暂存或提交只是主订单的状态不一样，具体更新还是保存还是根据主键区别
            OrderCustoms customs = ConvertUtil.convert(form, OrderCustoms.class);
            //子订单数据初始化处理
            //设置子订单号/报关抬头/结算单位/附件
            List<OrderCustoms> orderCustomsList = new ArrayList<>();
            List<InputSubOrderCustomsForm> subOrderCustomsForms = form.getSubOrders();
            for (InputSubOrderCustomsForm subOrder : subOrderCustomsForms) {
                customs.setOrderNo(subOrder.getOrderNo());
                customs.setTitle(subOrder.getTitle());
                customs.setUnitCode(subOrder.getUnitCode());
                customs.setUnitAccount(subOrder.getUnitAccount());
                customs.setDescription(subOrder.getDescription());
                customs.setMainOrderNo(String.valueOf(apiResult.getData()));
                customs.setStatus(OrderStatusEnum.CUSTOMS_C_0.getCode());
                if (subOrder .getSubOrderId() != null) {
                    customs.setUpdatedTime(LocalDateTime.now());
                    customs.setUpdatedUser(getLoginUser());
                } else {
                    customs.setCreatedUser(getLoginUser());
                }
                orderCustomsList.add(customs);

                //记录操作日志
                OprOrderLogForm oprOrderLogForm = new OprOrderLogForm();
                oprOrderLogForm.setMainOrderNo(String.valueOf(apiResult.getData()));
                oprOrderLogForm.setOrderNo(subOrder.getOrderNo());
                oprOrderLogForm.setRemarks("纯报关-创建订单");
                oprOrderLogForm.setOptUname(getLoginUser());
                oprOrderLogForms.add(oprOrderLogForm);
            }
            saveOrUpdateBatch(orderCustomsList);
            //记录订单日志
            omsClient.saveOprOrderLog(oprOrderLogForms);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public InputOrderCustomsVO editOrderCustomsView(Long id) {
        String prePath = String.valueOf(omsClient.getBaseUrl().getData());
        //1.查询主订单信息
        InputOrderVO inputOrderVO = omsClient.getMainOrderById(id).getData();
        InputOrderCustomsVO inputOrderCustomsVO = ConvertUtil.convert(inputOrderVO,InputOrderCustomsVO.class);
        //2.查询子订单信息,根据主订单
        if(inputOrderCustomsVO != null && inputOrderCustomsVO.getOrderNo() != null){
            Map<String, Object> param = new HashMap<>();
            param.put("main_order_no",inputOrderCustomsVO.getOrderNo());
            List<OrderCustomsVO> orderCustomsVOS = findOrderCustomsByCondition(param);
            if(orderCustomsVOS != null && orderCustomsVOS.size() > 0){
                OrderCustomsVO orderCustomsVO = orderCustomsVOS.get(0);
                //设置纯报关头部分
                inputOrderCustomsVO.setPortCode(orderCustomsVO.getPortCode());
                inputOrderCustomsVO.setGoodsType(orderCustomsVO.getGoodsType());
                inputOrderCustomsVO.setCntrNo(orderCustomsVO.getCntrNo());
                inputOrderCustomsVO.setCntrPic(""+orderCustomsVO.getCntrPic());
                inputOrderCustomsVO.setEncode(orderCustomsVO.getEncode());
                //处理子订单部分
                List<InputSubOrderCustomsVO> subOrderCustomsVOS = new ArrayList<>();
                for (OrderCustomsVO orderCustoms : orderCustomsVOS) {
                    InputSubOrderCustomsVO subOrderCustomsVO = new InputSubOrderCustomsVO();
                    subOrderCustomsVO.setSubOrderId(orderCustoms.getSubOrderId());
                    subOrderCustomsVO.setOrderNo(orderCustoms.getOrderNo());
                    subOrderCustomsVO.setTitle(orderCustoms.getTitle());
                    subOrderCustomsVO.setUnitCode(orderCustoms.getUnitCode());
                    //处理子订单附件信息
                    String fileStr = orderCustoms.getFileStr();
                    List<FileView> fileViews = new ArrayList<>();
                    if(fileStr != null && !"".equals(fileStr)){
                        String[] fileList = fileStr.split(",");
                        for(String str : fileList){
                            int index = str.lastIndexOf("/");
                            FileView fileView = new FileView();
                            fileView.setRelativePath(str);
                            fileView.setFileName(str.substring(index + 1, str.length()));
                            fileView.setAbsolutePath(prePath + str);
                            fileViews.add(fileView);
                        }
                    }
                    subOrderCustomsVO.setFileViews(fileViews);
                }
                inputOrderCustomsVO.setSubOrders(subOrderCustomsVOS);
            }
        }
        return inputOrderCustomsVO;
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
        page.addOrder(OrderItem.asc("oc.id"));
        IPage<CustomsOrderInfoVO> pageInfo = baseMapper.findCustomsOrderByPage(page, form);
        //处理附件
        List<CustomsOrderInfoVO> customsOrderInfoVOS = pageInfo.getRecords();
        String prePath = omsClient.getBaseUrl().getData().toString();
        for (CustomsOrderInfoVO customsOrder : customsOrderInfoVOS) {
            //处理子订单附件信息
            String fileStr = customsOrder.getFileStr();
            List<FileView> fileViews = new ArrayList<>();
            if(fileStr != null && !"".equals(fileStr)){
                String[] fileList = fileStr.split(",");
                for(String str : fileList){
                    int index = str.lastIndexOf("/");
                    FileView fileView = new FileView();
                    fileView.setRelativePath(str);
                    fileView.setFileName(str.substring(index + 1, str.length()));
                    fileView.setAbsolutePath(prePath + str);
                    fileViews.add(fileView);
                }
            }
            customsOrder.setFileViews(fileViews);
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
