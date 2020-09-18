package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.mapper.OrderInfoMapper;
import com.jayud.oms.model.bo.InputOrderForm;
import com.jayud.oms.model.bo.QueryOrderInfoForm;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.vo.NoSubmitOrderVO;
import com.jayud.oms.service.IOrderInfoService;
import com.jayud.oms.service.IProductBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 主订单基础数据表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    IProductBizService productBizService;

    @Override
    public String oprMainOrder(InputOrderForm form) {
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);
        if(form != null && form.getOrderNo() != null){//修改
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_1.getCode()));
            orderInfo.setUpTime(LocalDateTime.now());
            orderInfo.setUpUser(getLoginUser());
        }else {//新增
            //生成主订单号
            String orderNo = StringUtils.loadNum("MAIN",12);
            while (true){
                if(!isExistOrder(orderNo)){//重复
                    orderNo = StringUtils.loadNum("MAIN",12);
                }else {
                    break;
                }
            }
            orderInfo.setOrderNo(orderNo);
            orderInfo.setCreateTime(LocalDateTime.now());
            orderInfo.setCreatedUser(getLoginUser());
            if("preSubmit".equals(form.getCmd())) {
                orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_2.getCode()));
            }else if("submit".equals(form.getCmd())){
                orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_1.getCode()));
            }
        }
        saveOrUpdate(orderInfo);
        return orderInfo.getOrderNo();
    }

    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        List<OrderInfo> orderInfoList = baseMapper.selectList(queryWrapper);
        if(orderInfoList == null || orderInfoList.size() == 0){
            return true;
        }
        return false;
    }

    @Override
    public IPage<NoSubmitOrderVO> noSubmitOrderByPage(QueryOrderInfoForm form) {
        //定义分页参数
        Page<NoSubmitOrderVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("oi.id"));
        IPage<NoSubmitOrderVO> pageInfo = baseMapper.noSubmitOrderByPage(page, form);
        return pageInfo;
    }


    /**
     * 获取当前登录用户
     * @return
     */
    private String getLoginUser(){
        String loginUser = redisUtils.get("loginUser",100);
        return loginUser;
    }
}
