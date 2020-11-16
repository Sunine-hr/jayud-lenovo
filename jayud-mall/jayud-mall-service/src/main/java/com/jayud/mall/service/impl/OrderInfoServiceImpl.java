package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OrderClearanceFileMapper;
import com.jayud.mall.mapper.OrderCustomsFileMapper;
import com.jayud.mall.mapper.OrderInfoMapper;
import com.jayud.mall.model.bo.QueryOrderInfoForm;
import com.jayud.mall.model.po.OrderClearanceFile;
import com.jayud.mall.model.po.OrderCustomsFile;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.vo.OrderClearanceFileVO;
import com.jayud.mall.model.vo.OrderCustomsFileVO;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.service.IOrderClearanceFileService;
import com.jayud.mall.service.IOrderCustomsFileService;
import com.jayud.mall.service.IOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 产品订单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderCustomsFileMapper orderCustomsFileMapper;

    @Autowired
    OrderClearanceFileMapper orderClearanceFileMapper;

    @Autowired
    IOrderCustomsFileService orderCustomsFileService;

    @Autowired
    IOrderClearanceFileService orderClearanceFileService;


    @Override
    public IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form) {
        //定义分页参数
        Page<OrderInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<OrderInfoVO> pageInfo = orderInfoMapper.findOrderInfoByPage(page, form);
        return pageInfo;
    }

    @Override
    public CommonResult<OrderInfoVO> lookOrderInfoFile(Long id) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoFile(id);
        Long orderId = orderInfoVO.getId();//订单Id

        //订单对应报关文件list
        QueryWrapper<OrderCustomsFile> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("order_id", orderId);
        List<OrderCustomsFile> orderCustomsFiles = orderCustomsFileMapper.selectList(queryWrapper1);
        List<OrderCustomsFileVO> orderCustomsFileVOList =
                ConvertUtil.convertList(orderCustomsFiles, OrderCustomsFileVO.class);
        orderInfoVO.setOrderCustomsFileVOList(orderCustomsFileVOList);

        //订单对应清关文件list
        QueryWrapper<OrderClearanceFile> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("order_id", orderId);
        List<OrderClearanceFile> orderClearanceFiles = orderClearanceFileMapper.selectList(queryWrapper2);
        List<OrderClearanceFileVO> orderClearanceFileVOList =
                ConvertUtil.convertList(orderClearanceFiles, OrderClearanceFileVO.class);
        orderInfoVO.setOrderClearanceFileVOList(orderClearanceFileVOList);
        return CommonResult.success(orderInfoVO);
    }

    @Override
    public CommonResult<OrderCustomsFileVO> passOrderCustomsFile(Long id) {
        OrderCustomsFile orderCustomsFile = orderCustomsFileMapper.selectById(id);
        orderCustomsFile.setAuditStatus(1);//审核状态(0审核不通过  1审核通过)
        orderCustomsFileService.saveOrUpdate(orderCustomsFile);
        OrderCustomsFileVO orderCustomsFileVO = ConvertUtil.convert(orderCustomsFile, OrderCustomsFileVO.class);
        return CommonResult.success(orderCustomsFileVO);
    }

    @Override
    public CommonResult<OrderClearanceFileVO> passOrderClearanceFile(Long id) {
        OrderClearanceFile orderClearanceFile = orderClearanceFileService.getById(id);
        orderClearanceFile.setAuditStatus(1);//审核状态(0审核不通过  1审核通过)
        orderClearanceFileService.saveOrUpdate(orderClearanceFile);
        OrderClearanceFileVO orderClearanceFileVO = ConvertUtil.convert(orderClearanceFile, OrderClearanceFileVO.class);
        return CommonResult.success(orderClearanceFileVO);
    }

    @Override
    public CommonResult<OrderCustomsFileVO> onPassCustomsFile(Long id) {
        OrderCustomsFile orderCustomsFile = orderCustomsFileMapper.selectById(id);
        orderCustomsFile.setAuditStatus(0);//审核状态(0审核不通过  1审核通过)
        orderCustomsFileService.saveOrUpdate(orderCustomsFile);
        OrderCustomsFileVO orderCustomsFileVO = ConvertUtil.convert(orderCustomsFile, OrderCustomsFileVO.class);
        return CommonResult.success(orderCustomsFileVO);
    }

    @Override
    public CommonResult<OrderClearanceFileVO> onPassOrderClearanceFile(Long id) {
        OrderClearanceFile orderClearanceFile = orderClearanceFileService.getById(id);
        orderClearanceFile.setAuditStatus(0);//审核状态(0审核不通过  1审核通过)
        orderClearanceFileService.saveOrUpdate(orderClearanceFile);
        OrderClearanceFileVO orderClearanceFileVO = ConvertUtil.convert(orderClearanceFile, OrderClearanceFileVO.class);
        return CommonResult.success(orderClearanceFileVO);
    }
}
