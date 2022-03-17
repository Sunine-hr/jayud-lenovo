package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.StringUtils;
import com.jayud.wms.model.bo.IncomingSeedingForm;
import com.jayud.wms.model.bo.QueryIncomingSeedingForm;
import com.jayud.wms.model.po.IncomingSeeding;
import com.jayud.wms.model.po.ShelfOrder;
import com.jayud.wms.model.po.SowingResults;
import com.jayud.wms.model.enums.MaterialStatusEnum;
import com.jayud.wms.model.enums.ShelfOrderStatusEnum;
import com.jayud.wms.model.enums.SowingResultsStatusEnum;
import com.jayud.wms.mapper.IncomingSeedingMapper;
import com.jayud.wms.service.IContainerService;
import com.jayud.wms.service.IIncomingSeedingService;
import com.jayud.wms.service.IShelfOrderService;
import com.jayud.wms.service.ISowingResultsService;
import com.jayud.wms.model.vo.IncomingSeedingVO;
import com.jayud.wms.model.vo.MaterialVO;
import com.jayud.wms.model.vo.ReceiptVO;

import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 入库播种 服务实现类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Service
public class IncomingSeedingServiceImpl extends ServiceImpl<IncomingSeedingMapper, IncomingSeeding> implements IIncomingSeedingService {


    @Autowired
    private IncomingSeedingMapper incomingSeedingMapper;
    @Autowired
    private ISowingResultsService sowingResultsService;
    @Autowired
    private IContainerService containerService;
    @Autowired
    private IShelfOrderService shelfOrderService;

    @Override
    public IPage<IncomingSeeding> selectPage(IncomingSeeding incomingSeeding,
                                             Integer pageNo,
                                             Integer pageSize,
                                             HttpServletRequest req) {

        Page<IncomingSeeding> page = new Page<IncomingSeeding>(pageNo, pageSize);
        IPage<IncomingSeeding> pageList = incomingSeedingMapper.pageList(page, incomingSeeding);
        return pageList;
    }

    @Override
    public List<IncomingSeeding> selectList(IncomingSeeding incomingSeeding) {
        return incomingSeedingMapper.list(incomingSeeding);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IncomingSeeding saveOrUpdateIncomingSeeding(IncomingSeeding incomingSeeding) {
        Long id = incomingSeeding.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            incomingSeeding.setCreateBy(CurrentUserUtil.getUsername());
            incomingSeeding.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<IncomingSeeding> incomingSeedingQueryWrapper = new QueryWrapper<>();
            //incomingSeedingQueryWrapper.lambda().eq(IncomingSeeding::getCode, incomingSeeding.getCode());
            //incomingSeedingQueryWrapper.lambda().eq(IncomingSeeding::getIsDeleted, 0);
            //List<IncomingSeeding> list = this.list(incomingSeedingQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        } else {
            //修改 --> update 更新人、更新时间
            incomingSeeding.setUpdateBy(CurrentUserUtil.getUsername());
            incomingSeeding.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<IncomingSeeding> incomingSeedingQueryWrapper = new QueryWrapper<>();
            //incomingSeedingQueryWrapper.lambda().ne(IncomingSeeding::getId, id);
            //incomingSeedingQueryWrapper.lambda().eq(IncomingSeeding::getCode, incomingSeeding.getCode());
            //incomingSeedingQueryWrapper.lambda().eq(IncomingSeeding::getIsDeleted, 0);
            //List<IncomingSeeding> list = this.list(incomingSeedingQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(incomingSeeding);
        return incomingSeeding;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delIncomingSeeding(int id) {
        IncomingSeeding incomingSeeding = this.baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(incomingSeeding)) {
            throw new IllegalArgumentException("入库播种不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        incomingSeeding.setUpdateBy(CurrentUserUtil.getUsername());
        incomingSeeding.setUpdateTime(new Date());
        incomingSeeding.setIsDeleted(true);
        this.saveOrUpdate(incomingSeeding);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryIncomingSeedingForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryIncomingSeedingForExcel(paramMap);
    }

    @Override
    public void doWarehousingSeeding(ReceiptVO receiptDetails) {
        List<MaterialVO> materialList = receiptDetails.getMaterialForms().stream().filter(e -> e.getActualNum() > 0.0).collect(Collectors.toList());
        Date date = new Date();
        List<IncomingSeeding> list = new ArrayList<>();
        for (MaterialVO materialVO : materialList) {
            if (MaterialStatusEnum.FOUR.getCode().equals(materialVO.getStatus())) {
                continue;
            }
            IncomingSeeding seeding = ConvertUtil.convert(materialVO, IncomingSeeding.class);
            seeding.setId(null).setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date).setUpdateBy(null).setUpdateTime(null);
            seeding.setMaterialId(materialVO.getId()).setOrderId(receiptDetails.getId()).setOrderNum(receiptDetails.getReceiptNum())
                    .setReceiptNoticeNum(receiptDetails.getReceiptNoticeNum()).setNum(materialVO.getActualNum()).setAllocatedQuantity(0.0);
            list.add(seeding);
        }
        if (list.size() > 0) {
            this.saveOrUpdateBatch(list);
        }

    }

    @Override
    public IncomingSeedingVO getDetails(QueryIncomingSeedingForm form) {
        IncomingSeedingVO incomingSeeding = new IncomingSeedingVO();
        if (!StringUtils.isEmpty(form.getContainerNum()) && !StringUtils.isEmpty(form.getMaterialCode())) {
            List<IncomingSeeding> incomingSeedings = this.baseMapper.selectList(new QueryWrapper<>(new IncomingSeeding().setContainerNum(form.getContainerNum()).setMaterialCode(form.getMaterialCode())));
            if (incomingSeedings.size() == 0) {
                throw new ServiceException("不存在该物料");
            }
            incomingSeeding = ConvertUtil.convert(incomingSeedings.get(0), IncomingSeedingVO.class);

        } else {
            List<IncomingSeeding> incomingSeedings = this.baseMapper.selectList(new QueryWrapper<>(new IncomingSeeding().setContainerNum(form.getContainerNum())));
            incomingSeedings = incomingSeedings.stream().filter(e -> !e.getNum().equals(e.getAllocatedQuantity())).collect(Collectors.toList());
            if (incomingSeedings.size() > 1) {
                return incomingSeeding;
            }
            if (incomingSeedings.size() == 0) {
                throw new ServiceException("不存在该物料");
            }
            incomingSeeding = ConvertUtil.convert(incomingSeedings.get(0), IncomingSeedingVO.class);
        }

        incomingSeeding.setNumDesc(incomingSeeding.getNum() + "/" + (BigDecimal.valueOf(incomingSeeding.getNum()).subtract(BigDecimal.valueOf(incomingSeeding.getAllocatedQuantity()))).toPlainString());

        List<SowingResults> sowingResults = sowingResultsService.getByCondition(new SowingResults().setIsDeleted(false).setMaterialCode(incomingSeeding.getMaterialCode())
                .setOldContainerNum(incomingSeeding.getContainerNum()));

        List<SowingResults> sowingResultsList = sowingResults.stream().filter(e -> SowingResultsStatusEnum.ONE.getCode().equals(e.getStatus()) ||
                SowingResultsStatusEnum.TWO.getCode().equals(e.getStatus())).collect(Collectors.toList());
        if (sowingResultsList.size() > 0) {
            SowingResults tmp = sowingResultsList.get(0);
            incomingSeeding.setNewContainerNum(tmp.getNewContainerNum()).setNewContainerName(tmp.getNewContainerName()).setSeedingPositionNum(tmp.getSeedingPositionNum());
        }

        return incomingSeeding;
    }

    @Override
    public void confirmReplacement(IncomingSeedingForm form) {

        //同一个新容器只能播种到相同库位
        verifyingContainerReplacement(form);

        //获取当前工作台编号
        //根据工作台编号获取播种墙
        //校验该工作台是否有权限用这个播种墙位置

        //校验该位置是否被占用

        //完成上架任务,清空播种位,把播种结果对应的播种位改成完成上架

        List<SowingResults> sowingResults = this.sowingResultsService.getByCondition(new SowingResults().setSeedingPositionNum(form.getSeedingPositionNum()).setIsDeleted(false));
        Map<String, SowingResults> sowingResultsMap = sowingResults.stream().filter(e -> SowingResultsStatusEnum.TWO.getCode().equals(e.getStatus())
                || SowingResultsStatusEnum.THREE.getCode().equals(e.getStatus())).collect(Collectors.toMap(e -> e.getSeedingPositionNum() + "~" + e.getOldContainerNum() + "~" + e.getMaterialCode(), e -> e));

        SowingResults tmp = sowingResultsMap.get(form.getSeedingPositionNum() + "~" + form.getContainerNum() + "~" + form.getMaterialCode());

        if (tmp != null) {
            SowingResults update = new SowingResults().setSowingQuantity(tmp.getSowingQuantity() + form.getNewNum());
            update.setOldNum(form.getNum() - form.getAllocatedQuantity()).setQuantityAfterSowing(update.getOldNum() - update.getSowingQuantity()).setNewQuantityAfterSowing(update.getSowingQuantity());
//            update.setId(SowingResultsStatusEnum.FOUR.getCode().equals(tmp.getStatus()) ? null : tmp.getId()).setUpdateTime(new Date()).setUpdateBy(CurrentUserUtil.getUsername());
            update.setId(tmp.getId());
            this.sowingResultsService.saveOrUpdate(update);
        } else {
            if (!CollectionUtil.isEmpty(sowingResultsMap)) {
                throw new ServiceException("该播种位已被占用");
            }

            SowingResults add = new SowingResults();
            add.setStatus(SowingResultsStatusEnum.TWO.getCode()).setSeedingPositionNum(form.getSeedingPositionNum()).setMaterialCode(form.getMaterialCode())
                    .setMaterialName(form.getMaterialName()).setMaterialId(form.getMaterialId())
                    .setOldContainerNum(form.getContainerNum())
                    .setOldNum(form.getNum() - form.getAllocatedQuantity()).setSowingQuantity(form.getNewNum()).setQuantityAfterSowing(add.getOldNum() - form.getNewNum())
                    .setUnit(form.getUnit()).setNewContainerNum(form.getNewContainerNum()).setNewContainerName(form.getNewContainerName())
                    .setIncomingSeedingId(form.getId())
                    .setNewQuantityAfterSowing(form.getNewNum()).setReceiptNoticeNum(form.getReceiptNoticeNum()).setOrderId(form.getOrderId()).setOrderNum(form.getOrderNum())
                    .setBatchNum(form.getBatchNum()).setProductionDate(form.getProductionDate()).setColumnOne(form.getColumnOne())
                    .setColumnTwo(form.getColumnTwo()).setColumnThree(form.getColumnThree()).setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(new Date());

            this.sowingResultsService.save(add);
        }

        //更换播种已分配数量
        IncomingSeeding incomingSeeding = new IncomingSeeding();
        incomingSeeding.setId(form.getId()).setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(new Date());
        incomingSeeding.setAllocatedQuantity(form.getAllocatedQuantity() + form.getNewNum());
        this.updateById(incomingSeeding);

    }

    /**
     * 确认上架
     */
    @Override
    public void confirmShelf() {
        List<SowingResults> sowingResults = this.sowingResultsService.getByCondition(new SowingResults().setIsDeleted(false).setStatus(SowingResultsStatusEnum.TWO.getCode()));
        if (sowingResults.size() == 0) {
            throw new ServiceException("没有已更换的数据");
        }
        List<SowingResults> tmps = new ArrayList<>();
        List<ShelfOrder> list = new ArrayList<>();
        Date date = new Date();
        for (SowingResults sowingResult : sowingResults) {
            ShelfOrder shelfOrder = new ShelfOrder();
            shelfOrder.setOrderId(sowingResult.getOrderId()).setOrderNum(sowingResult.getOrderNum()).setReceiptNoticeNum(sowingResult.getReceiptNoticeNum())
                    .setMaterialId(sowingResult.getMaterialId()).setMaterialCode(sowingResult.getMaterialCode())
                    .setNum(sowingResult.getNewQuantityAfterSowing()).setUnit(sowingResult.getUnit())
                    .setContainerNum(sowingResult.getNewContainerNum()).setIsDeleted(false)
                    .setBatchNum(sowingResult.getBatchNum())
                    .setProductionDate(sowingResult.getProductionDate())
                    .setColumnOne(sowingResult.getColumnOne())
                    .setColumnTwo(sowingResult.getColumnTwo())
                    .setColumnThree(sowingResult.getColumnThree())
                    .setSeedingPositionNum(sowingResult.getSeedingPositionNum())

                    .setStatus(ShelfOrderStatusEnum.ONE.getCode())
                    .setIsPutShelf(false).setCreateBy(CurrentUserUtil.getUsername())
                    .setCreateTime(new Date());
            SowingResults tmp = new SowingResults();
            tmp.setId(sowingResult.getId()).setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);
            tmp.setStatus(SowingResultsStatusEnum.THREE.getCode());
            list.add(shelfOrder);
            tmps.add(tmp);
        }
        this.shelfOrderService.saveBatch(list);
        this.sowingResultsService.updateBatchById(tmps);
    }


    /**
     * 容器更换已经存在的必须更换到相应的库位
     *
     * @param form
     */
    @Override
    public void verifyingContainerReplacement(IncomingSeedingForm form) {

        //查询对应容器的
        QueryWrapper<SowingResults> condition = new QueryWrapper<>();
        condition.lambda().eq(SowingResults::getMaterialCode, form.getMaterialCode())//物料编号
                .eq(SowingResults::getNewContainerNum, form.getNewContainerNum()) //新容器号
                .eq(SowingResults::getOldContainerNum, form.getContainerNum())//原容器号
                .eq(SowingResults::getOrderNum, form.getOrderNum())//收货单号
                .notIn(SowingResults::getStatus, 3,4)//状态 3:确认上架,4:完成上架
                .ne(SowingResults::getIsDeleted, 1);//状态   1撤销
        SowingResults one = sowingResultsService.getOne(condition);

        if (one != null) {
            //传进来的播种位
            String seedingPositionNum = form.getSeedingPositionNum();
            //查询到已经播种的播种位
            String seedingPositionNum1 = one.getSeedingPositionNum();
            //传过来的播种位和当前播种位相同 则可以通过   不同则提示错误
            if (!seedingPositionNum.equals(seedingPositionNum1)) {
                throw new ServiceException("该容器已经播种到" + one.getSeedingPositionNum() + "库位");
            }
        }


    }

}
