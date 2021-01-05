package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OrderConfMapper;
import com.jayud.mall.model.bo.OrderConfForm;
import com.jayud.mall.model.bo.QueryOrderConfForm;
import com.jayud.mall.model.po.OceanConfDetail;
import com.jayud.mall.model.po.OrderConf;
import com.jayud.mall.model.vo.OceanCounterVO;
import com.jayud.mall.model.vo.OfferInfoVO;
import com.jayud.mall.model.vo.OrderConfVO;
import com.jayud.mall.service.IOceanConfDetailService;
import com.jayud.mall.service.IOrderConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 配载单 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class OrderConfServiceImpl extends ServiceImpl<OrderConfMapper, OrderConf> implements IOrderConfService {

    @Autowired
    OrderConfMapper orderConfMapper;

    @Autowired
    IOceanConfDetailService oceanConfDetailService;

    @Override
    public IPage<OrderConfVO> findOrderConfByPage(QueryOrderConfForm form) {
        //定义分页参数
        Page<OrderConfVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<OrderConfVO> pageInfo = orderConfMapper.findOrderConfByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderConf(OrderConfForm form) {
        OrderConf orderConf = ConvertUtil.convert(form, OrderConf.class);
        this.saveOrUpdate(orderConf);

        //配载单id
        Long orderId = orderConf.getId();

        //先删除
        QueryWrapper<OceanConfDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        oceanConfDetailService.remove(queryWrapper);

        List<OceanConfDetail> oceanConfDetailList = form.getOceanConfDetailList();
        oceanConfDetailList.forEach(oceanConfDetail -> {
            oceanConfDetail.setOrderId(orderId);
        });
        //再保存
        oceanConfDetailService.saveOrUpdateBatch(oceanConfDetailList);
    }

    @Override
    public CommonResult<OrderConfVO> lookOrderConf(Long id) {
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(id);

        Long orderId = orderConfVO.getId();
        //报价信息list
        List<OfferInfoVO> offerInfoVOList = orderConfMapper.findOfferInfoVOByOrderId(orderId);
        orderConfVO.setOfferInfoVOList(offerInfoVOList);

        /*
        * 提单
        * 1个提单对应1(N)个柜子
        * 1个柜子对应N个运单
        * 1个运单对应N个箱号
        */
        //提单-柜号信息list
        List<OceanCounterVO> oceanCounterVOList = orderConfMapper.findOceanCounterVOByOrderId(orderId);

//        oceanCounterVOList.forEach( oceanCounterVO -> {
//            //1个柜子对应N个运单
//            Long oceanCounterId = oceanCounterVO.getId();
//            QueryWrapper<OceanWaybill> queryWrapperOceanWaybill = new QueryWrapper<>();
//            queryWrapperOceanWaybill.eq("ocean_counter_id", oceanCounterId);
//            List<OceanWaybill> oceanWaybillList = oceanWaybillMapper.selectList(queryWrapperOceanWaybill);
//            List<OceanWaybillVO> oceanWaybillVOList = ConvertUtil.convertList(oceanWaybillList, OceanWaybillVO.class);
//
//
//            oceanWaybillVOList.forEach(oceanWaybillVO -> {
//                //1个运单对应N个箱号
//                Long oceanWaybillId = oceanWaybillVO.getId();
//                QueryWrapper<OceanWaybillCaseRelation> queryWrapperOceanWaybillCaseRelation = new QueryWrapper<>();
//                List<OceanWaybillCaseRelationVO> xhxxList =
//                        oceanWaybillCaseRelationMapper.findXhxxByOceanWaybillId(oceanWaybillId);//根据运单id，查询箱号信息list
//                oceanWaybillVO.setOceanWaybillCaseRelationVOList(xhxxList);
//            });
//            oceanCounterVO.setOceanWaybillVOList(oceanWaybillVOList);
//        });

        orderConfVO.setOceanCounterVOList(oceanCounterVOList);
        return CommonResult.success(orderConfVO);
    }


}
