package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OceanBillMapper;
import com.jayud.mall.mapper.OceanCounterMapper;
import com.jayud.mall.model.bo.OceanBillForm;
import com.jayud.mall.model.bo.OceanCounterForm;
import com.jayud.mall.model.bo.QueryOceanBillForm;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.model.vo.BillTaskRelevanceVO;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.model.vo.OceanCounterVO;
import com.jayud.mall.service.IBillTaskRelevanceService;
import com.jayud.mall.service.IOceanBillService;
import com.jayud.mall.service.IOceanCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 提单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OceanBillServiceImpl extends ServiceImpl<OceanBillMapper, OceanBill> implements IOceanBillService {

    @Autowired
    OceanBillMapper oceanBillMapper;

    @Autowired
    OceanCounterMapper oceanCounterMapper;

    @Autowired
    IOceanCounterService oceanCounterService;

    @Autowired
    IBillTaskRelevanceService billTaskRelevanceService;

    @Override
    public IPage<OceanBillVO> findOceanBillByPage(QueryOceanBillForm form) {
        //定义分页参数
        Page<OceanBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<OceanBillVO> pageInfo = oceanBillMapper.findOceanBillByPage(page, form);

        //设置柜号list
        List<OceanBillVO> records = pageInfo.getRecords();
        records.forEach(oceanBillVO -> {
            Long obId = oceanBillVO.getId();
            QueryWrapper<OceanCounter> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ob_id", obId);
            List<OceanCounter> oceanCounters = oceanCounterMapper.selectList(queryWrapper);
            List<OceanCounterVO> oceanCounterVOList = ConvertUtil.convertList(oceanCounters, OceanCounterVO.class);
            oceanBillVO.setOceanCounterVOList(oceanCounterVOList);
        });
        return pageInfo;
    }

    /**
     * <p>保存提单信息</p>
     * <p>1个提单对应1个柜子</p>
     * <p>1个柜子对应N个运单(订单)</p>
     * <p>1个运单对应N个箱号</p>
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<OceanBillVO> saveOceanBill(OceanBillForm form) {
        //1.保存提单
        OceanBill oceanBill = ConvertUtil.convert(form, OceanBill.class);
        this.saveOrUpdate(oceanBill);
        Long obId = oceanBill.getId();//提单id
        List<OceanCounterForm> oceanCounterForms = form.getOceanCounterForms();
        List<OceanCounter> oceanCounterList = new ArrayList<>();
        oceanCounterForms.forEach(oceanCounterForm -> {
            OceanCounter oceanCounter = ConvertUtil.convert(oceanCounterForm, OceanCounter.class);
            oceanCounter.setObId(obId);
            oceanCounterList.add(oceanCounter);
        });
        //先删除
        QueryWrapper<OceanCounter> oceanCounterQueryWrapper = new QueryWrapper<>();
        oceanCounterQueryWrapper.eq("ob_id", obId);
        oceanCounterService.remove(oceanCounterQueryWrapper);
        //2.保存提单对应的柜子
        oceanCounterService.saveOrUpdateBatch(oceanCounterList);
        OceanBillVO oceanBillVO = ConvertUtil.convert(oceanBill, OceanBillVO.class);

        //3.保存提单关联任务
        List<BillTaskRelevanceVO> billTaskRelevanceVOS =
                billTaskRelevanceService.savebillTaskRelevance(oceanBill);

        return CommonResult.success(oceanBillVO);
    }

    /**
     * <p>查看提单</p>
     * <p>1个提单对应1(N)个柜子</p>
     * <p>1个柜子对应N个运单</p>
     * <p>1个运单对应N个箱号</p>
     * @param id 提单id
     * @return
     */
    @Override
    public CommonResult<OceanBillVO> lookOceanBill(Long id) {
        //提单信息
        OceanBill oceanBill = oceanBillMapper.selectById(id);
        OceanBillVO oceanBillVO = ConvertUtil.convert(oceanBill, OceanBillVO.class);

        //1个提单对应1(N)个柜子
        Long obId = oceanBillVO.getId();
        QueryWrapper<OceanCounter> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","cntr_no","cabinet_code","volume","cost","cid","`status`","ob_id","create_time");
        queryWrapper.eq("ob_id", obId);
        List<OceanCounter> oceanCounterList = oceanCounterMapper.selectList(queryWrapper);
        List<OceanCounterVO> oceanCounterVOList = ConvertUtil.convertList(oceanCounterList, OceanCounterVO.class);

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
        oceanBillVO.setOceanCounterVOList(oceanCounterVOList);
        return CommonResult.success(oceanBillVO);
    }
}
