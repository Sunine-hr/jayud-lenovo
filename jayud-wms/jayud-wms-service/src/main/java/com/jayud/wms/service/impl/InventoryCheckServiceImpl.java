package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.mapper.InventoryCheckMapper;
import com.jayud.wms.mapper.WarehouseLocationMapper;
import com.jayud.wms.mapper.WarehouseShelfMapper;
import com.jayud.wms.model.bo.*;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.enums.CheckDetailStatusEnum;
import com.jayud.wms.model.enums.CheckStatusEnum;
import com.jayud.wms.model.enums.ColorCodeEnum;
import com.jayud.wms.model.enums.ShelfMoveTaskEnum;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.*;
import com.jayud.wms.service.*;
import com.jayud.wms.utils.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存盘点表 服务实现类
 *
 * @author jyd
 * @since 2021-12-27
 */
@Service
public class InventoryCheckServiceImpl extends ServiceImpl<InventoryCheckMapper, InventoryCheck> implements IInventoryCheckService {


    @Autowired
    private InventoryCheckMapper inventoryCheckMapper;
    @Autowired
    private CodeUtils codeUtils;
    @Autowired
    private IInventoryCheckDetailService inventoryCheckDetailService;
    @Autowired
    private IInventoryDetailService inventoryDetailService;
    @Resource
    private AuthClient authClient;
    @Autowired
    private IWarehouseLocationService warehouseLocationService;
    @Autowired
    private IWorkbenchService workbenchService;
    @Autowired
    private IWarehouseShelfService warehouseShelfService;
    @Autowired
    private WarehouseLocationMapper warehouseLocationMapper;
    @Autowired
    private WarehouseShelfMapper warehouseShelfMapper;
    @Autowired
    private IShelfMoveTaskService shelfMoveTaskService;

    @Override
    public IPage<InventoryCheck> selectPage(InventoryCheck inventoryCheck,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<InventoryCheck> page=new Page<InventoryCheck>(pageNo, pageSize);
        IPage<InventoryCheck> pageList= inventoryCheckMapper.pageList(page, inventoryCheck);
        //查询明细里面的 数量
        List<InventoryCheck> records = pageList.getRecords();
        List<Long> inventoryCheckIdList = records.stream().map(r -> r.getId()).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(inventoryCheckIdList)){
            List<Map<String, Object>> detailCountList = inventoryCheckDetailService.queryDetailCountByInventoryCheckIds(inventoryCheckIdList);
            Map<Long, Map<String, Object>> detailCountMap = detailCountList.stream().collect(Collectors.toMap(d -> MapUtil.getLong(d, "inventoryCheckId"), d -> d));
            records.forEach(r -> {
                Long inventoryCheckId = r.getId();
                Map<String, Object> map = detailCountMap.get(inventoryCheckId);
                if(ObjectUtil.isNotEmpty(map)){
                    String inventoryCount = MapUtil.getStr(map, "inventoryCount");
                    String checkCount = MapUtil.getStr(map, "checkCount");
                    r.setInventoryCount(new BigDecimal(inventoryCount));
                    r.setCheckCount(new BigDecimal(checkCount));
                }else{
                    r.setInventoryCount(new BigDecimal("0"));
                    r.setCheckCount(new BigDecimal("0"));
                }
            });
        }
        return pageList;
    }

    @Override
    public List<InventoryCheck> selectList(InventoryCheck inventoryCheck){
        return inventoryCheckMapper.list(inventoryCheck);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryCheck saveOrUpdateInventoryCheck(InventoryCheck inventoryCheck) {
        Long id = inventoryCheck.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增 --> add 创建人、创建时间
            inventoryCheck.setCreateBy(CurrentUserUtil.getUsername());
            inventoryCheck.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<InventoryCheck> inventoryCheckQueryWrapper = new QueryWrapper<>();
            //inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getCode, inventoryCheck.getCode());
            //inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getIsDeleted, 0);
            //List<InventoryCheck> list = this.list(inventoryCheckQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        }else{
            //修改 --> update 更新人、更新时间
            inventoryCheck.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryCheck.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<InventoryCheck> inventoryCheckQueryWrapper = new QueryWrapper<>();
            //inventoryCheckQueryWrapper.lambda().ne(InventoryCheck::getId, id);
            //inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getCode, inventoryCheck.getCode());
            //inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getIsDeleted, 0);
            //List<InventoryCheck> list = this.list(inventoryCheckQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(inventoryCheck);
        return inventoryCheck;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delInventoryCheck(int id) {
        InventoryCheck inventoryCheck = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(inventoryCheck)){
            throw new IllegalArgumentException("库存盘点表不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        inventoryCheck.setUpdateBy(CurrentUserUtil.getUsername());
        inventoryCheck.setUpdateTime(new Date());
        inventoryCheck.setIsDeleted(true);
        this.saveOrUpdate(inventoryCheck);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryInventoryCheckForExcel(Map<String, Object> paramMap) {
        AuthUserDetail userDetail = CurrentUserUtil.getUserDetail();
        List<String> owerIdList = authClient.getOwerIdByUserId(String.valueOf(userDetail.getId()));
        paramMap.put("owerIdList",owerIdList);
        List<String> warehouseIdList = authClient.getWarehouseIdByUserId(String.valueOf(userDetail.getId()));
        paramMap.put("warehouseIdList",warehouseIdList);
        return this.baseMapper.queryInventoryCheckForExcel(paramMap);
    }

    /**
     * 生成盘点任务
     * 业务：
     *     1、保存库存盘点
     *         1.1、判断 货架是否存在，能否创建盘点单             惠州道科业务
     *         1.2、判断 工作台是否存在，是否要生成货架移动任务     惠州道科业务  调用AGV
     *     2、保存库存盘点明细
     *     3、冻结库位
     * @param bo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryCheckVO generateInventoryCheck(InventoryCheckForm bo) {
        String checkCode = codeUtils.getCodeByRule(CodeConStants.INVENTORY_CHECK_CODE);//库存盘点单号

        //System.out.println("[库存盘点单号] checkCode: " + checkCode);
        //库存盘点
        InventoryCheck inventoryCheck = ConvertUtil.convert(bo, InventoryCheck.class);

        //仓库
        Long warehouseId = inventoryCheck.getWarehouseId();
        String warehouseCode = inventoryCheck.getWarehouseCode();
        String warehouseName = inventoryCheck.getWarehouseName();
        //库区
        Long warehouseAreaId = bo.getWarehouseAreaId();
        String warehouseAreaCode = bo.getWarehouseAreaCode();
        String warehouseAreaName = bo.getWarehouseAreaName();
        //货架
//        Long shelfId = bo.getShelfId();
//        String shelfCode = bo.getShelfCode();
        //物料
        Long materialId = bo.getMaterialId();
        String materialCode = bo.getMaterialCode();
        String materialName = bo.getMaterialName();


        if(ObjectUtil.isEmpty(warehouseCode)){
            throw new IllegalArgumentException("仓库不能为空");
        }
        Integer checkType = inventoryCheck.getCheckType();
        if(ObjectUtil.isEmpty(checkType)){
            throw new IllegalArgumentException("盘点类型不能为空");
        }
        LocalDateTime checkStartTime = inventoryCheck.getCheckStartTime();
        if(ObjectUtil.isEmpty(checkStartTime)){
            throw new IllegalArgumentException("盘点开始时间不能为空");
        }
        LocalDateTime now = LocalDateTime.now();
        if(checkStartTime.isBefore(now)){
            throw new IllegalArgumentException("盘点开始时间，不得在系统当前时间之前");//因为 盘点时 无法出入库 需要冻结库位
        }
        LocalDateTime updateStartTime = inventoryCheck.getUpdateStartTime();//更新日期 : 库存信息修改的时间的时间区间
        LocalDateTime updateFinishTime = inventoryCheck.getUpdateFinishTime();
        if(ObjectUtil.isNotEmpty(updateStartTime) && ObjectUtil.isNotEmpty(updateFinishTime) && updateFinishTime.isBefore(updateStartTime)){
            throw new IllegalArgumentException("更新日期的完成时间，不得在更新开始时间之前");
        }
        inventoryCheck.setId(null);
        inventoryCheck.setCheckCode(checkCode);
        inventoryCheck.setCheckStatus(1);//生成是默认为1 盘点状态(1未盘点、2部分盘点、3已盘点)
        inventoryCheck.setCheckBy(CurrentUserUtil.getUsername());//盘点人
        inventoryCheck.setCreateBy(CurrentUserUtil.getUsername());
        inventoryCheck.setCreateTime(new Date());

        //1.1、判断 货架是否存在，能否创建盘点单
//        if(StrUtil.isNotEmpty(shelfCode)){
//            //若货架存在，则需要判断是否能创建盘点单
//            QueryWrapper<InventoryCheck> inventoryCheckQueryWrapper = new QueryWrapper<>();
//            inventoryCheckQueryWrapper.lambda().ne(InventoryCheck::getCheckStatus, CheckStatusEnum.CHECKSTATUS_3.getTypeCode());
//            inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getShelfCode, shelfCode);
//            inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getIsDeleted, 0);
//            List<InventoryCheck> inventoryChecks = inventoryCheckMapper.selectList(inventoryCheckQueryWrapper);
//            if(inventoryChecks.size() > 0){
//                throw new IllegalArgumentException("选择的货架，存在未完成的盘点单，不能创建盘点单");
//            }
//        }
        //1.2、判断 工作台是否存在，是否要生成货架移动任务
//        String workbenchCode = bo.getWorkbenchCode();
//        if(StrUtil.isNotEmpty(workbenchCode)){
//            QueryWrapper<Workbench> workbenchQueryWrapper = new QueryWrapper<>();
//            workbenchQueryWrapper.lambda().eq(Workbench::getIsDeleted, 0);
//            workbenchQueryWrapper.lambda().eq(Workbench::getCode, workbenchCode);
//            workbenchQueryWrapper.lambda().groupBy(Workbench::getCode);
//            Workbench workbench = workbenchService.getOne(workbenchQueryWrapper);
//            if(ObjectUtil.isEmpty(workbench)){
//                throw new IllegalArgumentException("工作台编码正确，不能创建盘点单");
//            }
//            Long workbenchId = workbench.getId();
            //根据 仓库id，库区id，货架id 查询货架，准备进行 货架移动操作
//            QueryWrapper<WarehouseShelf> warehouseShelfQueryWrapper = new QueryWrapper<>();
//            if(ObjectUtil.isNotEmpty(warehouseId)){
//                warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getWarehouseId, warehouseId);
//            }
//            if(ObjectUtil.isNotEmpty(warehouseAreaId)){
//                warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getWarehouseAreaId, warehouseAreaId);
//            }
//            if(ObjectUtil.isNotEmpty(shelfId)){
//                warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getId, shelfId);
//            }
//            warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getIsDeleted, 0);
//            List<WarehouseShelf> list = warehouseShelfService.list(warehouseShelfQueryWrapper);

            //自动生成对应的货架移动任务：MTC05("MTC05","货架至工作台-盘点")
//            CreateShelfMoveTaskForm shelfMoveTaskForm = new CreateShelfMoveTaskForm();
//            shelfMoveTaskForm.setMovementTypeCode(ShelfMoveTaskEnum.MTC05.getTypeCode());
//            shelfMoveTaskForm.setMovementTypeName(ShelfMoveTaskEnum.MTC05.getTypeDesc());
//            shelfMoveTaskForm.setWorkbenchId(workbenchId);
//            shelfMoveTaskForm.setWorkbenchCode(workbenchCode);
//            List<WarehouseShelf> warehouseShelfList = new ArrayList<>();
//            warehouseShelfList.addAll(list);
//            shelfMoveTaskForm.setWarehouseShelfList(warehouseShelfList);
            /**
             * 创建货架移库任务
             * @see com.jyd.bases.service.IShelfMoveTaskService#createShelfMoveTask(CreateShelfMoveTaskForm)
             */
//            shelfMoveTaskService.createShelfMoveTask(shelfMoveTaskForm);

//        }

        //1、保存库存盘点
        boolean b = this.saveOrUpdate(inventoryCheck);

        //根据盘点条件，查询库存明细，进行盘点
        //仓库、库区、物料、更新日期[开始 ~ 结束]
        Map<String, Object> paramMap = new HashMap<>();
        //仓库
        //paramMap.put("warehouseId", warehouseId);
        paramMap.put("warehouseCode", warehouseCode);
        //paramMap.put("warehouseName", warehouseName);
        //库区
        //paramMap.put("warehouseAreaId", warehouseAreaId);
        paramMap.put("warehouseAreaCode", warehouseAreaCode);
        //paramMap.put("warehouseAreaName", warehouseAreaName);
        //货架 -> 库位ids
        /*根据货架，查询货架下关联的所有库位，拿到库位ids，在去库存明细查询*/
//        QueryWrapper<WarehouseLocation> warehouseLocationQueryWrapper = new QueryWrapper<>();
//        warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getShelfId, shelfId);
//        warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getIsDeleted, 0);
//        List<WarehouseLocation> warehouseLocationList = warehouseLocationService.list(warehouseLocationQueryWrapper);
//        List<Long> warehouseLocationIds = warehouseLocationList.stream().map(k -> k.getId()).collect(Collectors.toList());
//        paramMap.put("warehouseLocationIds", warehouseLocationIds);

        //物料
        //paramMap.put("materialId", materialId);
        paramMap.put("materialCode", materialCode);
        //paramMap.put("materialCode", materialCode);
        //更新日期[开始 ~ 结束]
        paramMap.put("updateStartTime", updateStartTime);
        paramMap.put("updateFinishTime", updateFinishTime);

        List<InventoryDetail> inventoryDetails = inventoryDetailService.queryInventoryDetailForCheck(paramMap);

        //java8 stream：检查list集合中是否存在某个值
        //warehouseLocationStatus 库位状态(0未冻结 1已冻结)
//        boolean present = inventoryDetails.stream().filter(m -> m.getWarehouseLocationStatus().equals(1)).findAny().isPresent();
//        if(present){
//            throw new IllegalArgumentException("存在被冻结的库位，正在被其他盘点单盘点");
//        }

        List<InventoryCheckDetail> details = new ArrayList<>();
        if(CollUtil.isNotEmpty(inventoryDetails)){
            inventoryDetails.forEach(inventoryDetail -> {
                InventoryCheckDetail inventoryCheckDetail = ConvertUtil.convert(inventoryDetail, InventoryCheckDetail.class);
                inventoryCheckDetail.setId(null);
                inventoryCheckDetail.setInventoryCheckId(inventoryCheck.getId());//库存盘点ID
                inventoryCheckDetail.setInventoryDetailId(inventoryDetail.getId());//库存明细ID
                inventoryCheckDetail.setCheckStatus(1);//明细盘点状态(1未盘点、2已盘点、3已过账)
                inventoryCheckDetail.setInventoryCount(inventoryDetail.getExistingCount());//库存数量 ,1取 库存明细 现有量
                inventoryCheckDetail.setCheckCount(new BigDecimal("0"));//盘点数量
                inventoryCheckDetail.setCheckSurplusCount(new BigDecimal("0"));//盘盈数量
                inventoryCheckDetail.setCheckLossesCount(new BigDecimal("0"));//盘亏数量
                inventoryCheckDetail.setCreateBy(CurrentUserUtil.getUsername());
                inventoryCheckDetail.setCreateTime(new Date());
                inventoryCheckDetail.setUpdateBy(null);
                inventoryCheckDetail.setUpdateTime(null);

                details.add(inventoryCheckDetail);
            });
        }
        //2、保存库存盘点明细
        if(CollUtil.isNotEmpty(details)){
            inventoryCheckDetailService.saveOrUpdateBatch(details);
        }

        //3、冻结库位
        inventoryDetails.forEach(inventoryDetail -> {
            inventoryDetail.setWarehouseLocationStatus(1);//库位状态(0未冻结 1已冻结)
            inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryDetail.setUpdateTime(new Date());
        });
        if(CollUtil.isNotEmpty(inventoryDetails)){
            inventoryDetailService.saveOrUpdateBatch(inventoryDetails);
        }

        InventoryCheckVO vo = ConvertUtil.convert(inventoryCheck, InventoryCheckVO.class);
        return vo;
    }

    /**
     * 查看盘点报告
     * @param id
     * @return
     */
    @Override
    public InventoryCheckVO lookInventoryCheck(int id) {
        InventoryCheck inventoryCheck = this.getById(id);
        if(ObjectUtil.isEmpty(inventoryCheck)){
            throw new IllegalArgumentException("盘点单不存在");
        }
        Long inventoryCheckId = Long.valueOf(id);
        List<Long> inventoryCheckIds = Arrays.asList(inventoryCheckId);
        List<Map<String, Object>> detailCountList = inventoryCheckDetailService.queryDetailCountByInventoryCheckIds(inventoryCheckIds);
        Map<Long, Map<String, Object>> detailCountMap = detailCountList.stream().collect(Collectors.toMap(d -> MapUtil.getLong(d, "inventoryCheckId"), d -> d));
        Map<String, Object> map = detailCountMap.get(inventoryCheckId);
        if(ObjectUtil.isNotEmpty(map)){
            String inventoryCount = MapUtil.getStr(map, "inventoryCount");
            String checkCount = MapUtil.getStr(map, "checkCount");
            inventoryCheck.setInventoryCount(new BigDecimal(inventoryCount));
            inventoryCheck.setCheckCount(new BigDecimal(checkCount));
        }else{
            inventoryCheck.setInventoryCount(new BigDecimal("0"));
            inventoryCheck.setCheckCount(new BigDecimal("0"));
        }

        InventoryCheckVO vo = ConvertUtil.convert(inventoryCheck, InventoryCheckVO.class);
        QueryWrapper<InventoryCheckDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.lambda().eq(InventoryCheckDetail::getInventoryCheckId, id);
        detailQueryWrapper.lambda().eq(InventoryCheckDetail::getIsDeleted, 0);
        List<InventoryCheckDetail> details = inventoryCheckDetailService.list(detailQueryWrapper);
        vo.setDetails(details);
        return vo;
    }

    /**
     * 确认完成盘点
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmCompleteInventoryCheck(int id) {
        InventoryCheck inventoryCheck = this.getById(id);
        if(ObjectUtil.isEmpty(inventoryCheck)){
            throw new IllegalArgumentException("盘点单不存在");
        }
        Integer checkStatus = inventoryCheck.getCheckStatus();//盘点状态(1未盘点、2部分盘点、3已盘点)
        if(checkStatus == 3){
            throw new IllegalArgumentException("盘点单已全部盘点完成，不能重复操作");
        }
        InventoryCheckVO vo = ConvertUtil.convert(inventoryCheck, InventoryCheckVO.class);
        QueryWrapper<InventoryCheckDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.lambda().eq(InventoryCheckDetail::getInventoryCheckId, id);
        detailQueryWrapper.lambda().eq(InventoryCheckDetail::getIsDeleted, 0);
        List<InventoryCheckDetail> details = inventoryCheckDetailService.list(detailQueryWrapper);

        //checkStatus 明细盘点状态(1未盘点、2已盘点、3已过账)
        boolean present = details.stream().filter(m -> !m.getCheckStatus().equals(3)).findAny().isPresent();
        if(present){
            throw new IllegalArgumentException("存在没有过账的盘点明细，不能完成盘点");
        }
        inventoryCheck.setCheckStatus(3);//`check_status` '盘点状态(1未盘点、2部分盘点、3已盘点)'
        inventoryCheck.setCheckFinishTime(LocalDateTime.now());//盘点完成时间
        inventoryCheck.setUpdateBy(CurrentUserUtil.getUsername());
        inventoryCheck.setUpdateTime(new Date());
        //保存 库存盘点表
        this.saveOrUpdate(inventoryCheck);

        //取消库存明细 的 盘点冻结
        List<Long> inventoryDetailIdList = details.stream().map(InventoryCheckDetail::getInventoryDetailId).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(inventoryDetailIdList)){
            QueryWrapper<InventoryDetail> inventoryDetailQueryWrapper = new QueryWrapper<>();
            inventoryDetailQueryWrapper.lambda().in(InventoryDetail::getId, inventoryDetailIdList);
            List<InventoryDetail> inventoryDetailList = inventoryDetailService.list(inventoryDetailQueryWrapper);

            inventoryDetailList.forEach(inventoryDetail -> {
                inventoryDetail.setWarehouseLocationStatus(0);//库位状态-盘点(0未冻结 1已冻结)
                inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
                inventoryDetail.setUpdateTime(new Date());
            });
            if(CollUtil.isNotEmpty(inventoryDetailList)){
                inventoryDetailService.saveOrUpdateBatch(inventoryDetailList);
            }
        }
        return true;
    }

    /**
     * 分页查询数据Feign
     * @param inventoryCheck
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @Override
    public IPage<InventoryCheckAppVO> selectPageByFeign(InventoryCheck inventoryCheck, Integer pageNo, Integer pageSize, HttpServletRequest req) {
        AuthUserDetail userDetail = CurrentUserUtil.getUserDetail();
        List<String> owerIdList = authClient.getOwerIdByUserId(String.valueOf(userDetail.getId()));
        List<String> warehouseIdList = authClient.getWarehouseIdByUserId(String.valueOf(userDetail.getId()));
        inventoryCheck.setOwerIdList(owerIdList);
        inventoryCheck.setWarehouseIdList(warehouseIdList);
        //过滤 盘点明细 已全部过账的盘点单
        List<Long> notCheckIdList = inventoryCheckDetailService.queryNotCheckIdList();
        inventoryCheck.setNotCheckIdList(notCheckIdList);
        Page<InventoryCheckAppVO> page=new Page<InventoryCheckAppVO>(pageNo, pageSize);
        IPage<InventoryCheckAppVO> pageList= inventoryCheckMapper.selectPageByFeign(page, inventoryCheck);
        return pageList;
    }

    /**
     * 去盘点(盘点详情)
     * @param checkCode
     * @return
     */
    @Override
    public InventoryCheckAppVO queryInventoryCheckByCheckCode(String checkCode) {
        InventoryCheckAppVO vo = inventoryCheckMapper.queryInventoryCheckByCheckCode(checkCode);
        if(ObjectUtil.isEmpty(vo)){
            throw new IllegalArgumentException("盘点任务不存在");
        }
        Long inventoryCheckId = vo.getId();
        QueryWrapper<InventoryCheckDetail> inventoryCheckDetailQueryWrapper = new QueryWrapper<>();
        inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getIsDeleted, 0);
        inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getInventoryCheckId, inventoryCheckId);
        inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getCheckStatus, 1);
        inventoryCheckDetailQueryWrapper.lambda().groupBy(InventoryCheckDetail::getId);
        inventoryCheckDetailQueryWrapper.lambda().orderByAsc(InventoryCheckDetail::getId);
        List<InventoryCheckDetail> list = inventoryCheckDetailService.list(inventoryCheckDetailQueryWrapper);
        if(CollUtil.isNotEmpty(list)){
            InventoryCheckDetail inventoryCheckDetail = list.get(0);
            InventoryCheckDetailAppCompletedForm convert = ConvertUtil.convert(inventoryCheckDetail, InventoryCheckDetailAppCompletedForm.class);
            vo.setCompleted(convert);
        }else{
            vo.setCompleted(null);
        }
        return vo;
    }

    /**
     * 盘点确认
     * @param inventoryCheckAppVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryCheckAppVO inventoryCheckCompletedByApp(InventoryCheckAppVO inventoryCheckAppVO) {
        String checkCode = inventoryCheckAppVO.getCheckCode();
        InventoryCheckAppVO vo = inventoryCheckMapper.queryInventoryCheckByCheckCode(checkCode);
        if(ObjectUtil.isEmpty(vo)){
            throw new IllegalArgumentException("盘点任务不存在");
        }

        InventoryCheckDetailAppCompletedForm completed = inventoryCheckAppVO.getCompleted();
        Long id = completed.getId();
        InventoryCheckDetail inventoryCheckDetail = inventoryCheckDetailService.getById(id);
        if(ObjectUtil.isEmpty(inventoryCheckDetail)){
            throw new IllegalArgumentException("盘点任务明细不存在");
        }
        Integer checkStatus = inventoryCheckDetail.getCheckStatus();//明细盘点状态(1未盘点、2已盘点、3已过账)
        if(ObjectUtil.isEmpty(checkStatus) || checkStatus != 1){
            throw new IllegalArgumentException("明细盘点状态不正确");
        }

        String warehouseLocationCode = inventoryCheckDetail.getWarehouseLocationCode();//库位编号
        if(StrUtil.isEmpty(warehouseLocationCode)){
            throw new IllegalArgumentException("库位编号不能为空");
        }
        String containerCode = inventoryCheckDetail.getContainerCode();//容器号
        if(StrUtil.isEmpty(containerCode)){
            throw new IllegalArgumentException("容器号不能为空");
        }
        //BigDecimal checkCount = inventoryCheckDetail.getCheckCount();//盘点数量
        BigDecimal checkCount = completed.getCheckCount();//盘点数量 by app
        if(ObjectUtil.isEmpty(checkCount)){
            throw new IllegalArgumentException("盘点数量不能为空");
        }
        if(checkCount.compareTo(new BigDecimal("0")) < 0){
            throw new IllegalArgumentException("盘点数量不能小于0");
        }
        inventoryCheckDetail.setCheckCount(checkCount);

        /**  app验证  **/
        String actualWarehouseLocationCode = completed.getActualWarehouseLocationCode();//实际盘点库位
        String actualContainerCode = completed.getActualContainerCode();//实际盘点容器号
        //BigDecimal actualCheckCount = completed.getActualCheckCount();//实际盘点数量
        if(!warehouseLocationCode.equals(actualWarehouseLocationCode)){
            throw new IllegalArgumentException("盘点库位编号 和 实际盘点库位 不相等");
        }
        if(!containerCode.equals(actualContainerCode)){
            throw new IllegalArgumentException("盘点容器号 和 实际盘点容器号 不相等");
        }
        //if(checkCount.subtract(actualCheckCount).compareTo(new BigDecimal("0")) != 0){
        //    throw new IllegalArgumentException("盘点数量 和 实际盘点数量 不相等");
        //}

        Long inventoryCheckId = vo.getId();
        List<InventoryCheckDetail> details = new ArrayList<>();
        details.add(inventoryCheckDetail);

        // ==> 盘点任务完成
        InventoryCheckDetailPostForm form = new InventoryCheckDetailPostForm();
        form.setInventoryCheckId(inventoryCheckId);
        form.setDetails(details);
        boolean b = inventoryCheckDetailService.confirmPost(form);
        // ==> 获取盘点详情、获取下一条盘点详情
        InventoryCheckAppVO vo1 = this.queryInventoryCheckByCheckCode(checkCode);
        return vo1;
    }

    /**
     * 工作台盘点确认，货架+物料+数量(盘点数量)+库位编号
     * 业务：
     * 1、根据 工作台id+货架id，找到 没有盘点完成的 盘点主单（唯一一条盘点主单）
     * 2、根据 盘点主单id+物料编号+库位编号，找到盘点明细（唯一一条盘点明细）
     * 3、点击确认时，修改盘点明细的业务数据（数量、状态等）
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void workbenchCheckAffirm(WorkbenchCheckAffirmForm form) {
        /**
         * 验证和设值 工作台盘点确认
         */
        WorkbenchCheckAffirmForm workbenchCheckAffirmForm = verificationWorkbenchCheckAffirm(form);

        Long workbenchId = workbenchCheckAffirmForm.getWorkbenchId();
        Long shelfId = workbenchCheckAffirmForm.getShelfId();

        QueryWrapper<InventoryCheck> inventoryCheckQueryWrapper = new QueryWrapper<>();
        inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getIsDeleted, 0);
        inventoryCheckQueryWrapper.lambda().ne(InventoryCheck::getCheckStatus, CheckStatusEnum.CHECKSTATUS_3.getTypeCode());
        inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getWorkbenchId, workbenchId);
        inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getShelfId, shelfId);
        inventoryCheckQueryWrapper.lambda().groupBy(InventoryCheck::getWorkbenchId, InventoryCheck::getShelfId);
        //1、根据 工作台id+货架id，找到 没有盘点完成的 盘点主单（唯一一条盘点主单）
        InventoryCheck inventoryCheck = inventoryCheckMapper.selectOne(inventoryCheckQueryWrapper);
        if(ObjectUtil.isEmpty(inventoryCheck)){
            throw new IllegalArgumentException("盘点主单不存在");
        }

        Long inventoryCheckId = inventoryCheck.getId();
        String warehouseLocationCode = form.getWarehouseLocationCode();
        String materialCode = form.getMaterialCode();
        QueryWrapper<InventoryCheckDetail> inventoryCheckDetailQueryWrapper = new QueryWrapper<>();
        inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getIsDeleted, 0);
        inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getInventoryCheckId, inventoryCheckId);
        inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getWarehouseLocationCode, warehouseLocationCode);
        inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getMaterialCode, materialCode);
        inventoryCheckDetailQueryWrapper.lambda().groupBy(InventoryCheckDetail::getInventoryCheckId, InventoryCheckDetail::getWarehouseLocationCode, InventoryCheckDetail::getMaterialCode);
        //2、根据 盘点主单id+物料编号+库位编号，找到盘点明细（唯一一条盘点明细）
        InventoryCheckDetail inventoryCheckDetail = inventoryCheckDetailService.getOne(inventoryCheckDetailQueryWrapper);
        if(ObjectUtil.isEmpty(inventoryCheckDetail)){
            throw new IllegalArgumentException("根据条件，没有找到这条盘点明细");
        }
        Integer checkStatus = inventoryCheckDetail.getCheckStatus();
        if(checkStatus.equals(CheckDetailStatusEnum.CHECKSTATUS_3.getTypeCode())){
            throw new IllegalArgumentException("盘点明细已过账，不能盘点");
        }

        BigDecimal checkCount = workbenchCheckAffirmForm.getCheckCount();//盘点数量
        if(ObjectUtil.isEmpty(checkCount)){
            throw new IllegalArgumentException("盘点数量，不能为空");
        }
        BigDecimal inventoryCount = inventoryCheckDetail.getInventoryCount();//库存数量
        if(ObjectUtil.isEmpty(inventoryCount)){
            throw new IllegalArgumentException("库存数量，不能为空");
        }
        BigDecimal subtract = checkCount.subtract(inventoryCount);//盘点数量 - 库存数量
        if(subtract.compareTo(new BigDecimal("0"))  == 0){
            //正常
            inventoryCheckDetail.setCheckStatus(CheckDetailStatusEnum.CHECKSTATUS_2.getTypeCode());//明细盘点状态(1未盘点、2已盘点、3已过账)
            inventoryCheckDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryCheckDetail.setUpdateTime(new Date());
            inventoryCheckDetail.setCheckSurplusCount(new BigDecimal("0"));//盘盈数量
            inventoryCheckDetail.setCheckLossesCount(new BigDecimal("0"));//盘亏数量
        }else if(subtract.compareTo(new BigDecimal("0"))  > 0){
            //盘盈
            inventoryCheckDetail.setCheckStatus(CheckDetailStatusEnum.CHECKSTATUS_2.getTypeCode());//明细盘点状态(1未盘点、2已盘点、3已过账)
            inventoryCheckDetail.setCheckSurplusCount(subtract);//盘盈数量 = 盘点数量 - 库存数量
            inventoryCheckDetail.setCheckLossesCount(new BigDecimal("0"));//盘亏数量
            inventoryCheckDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryCheckDetail.setUpdateTime(new Date());
        }else if(subtract.compareTo(new BigDecimal("0"))  < 0){
            //盘亏
            inventoryCheckDetail.setCheckStatus(CheckDetailStatusEnum.CHECKSTATUS_2.getTypeCode());//明细盘点状态(1未盘点、2已盘点、3已过账)
            inventoryCheckDetail.setCheckSurplusCount(new BigDecimal("0"));//盘盈数量
            inventoryCheckDetail.setCheckLossesCount(subtract);//盘亏数量 = 盘点数量 - 库存数量，（因为 库存数量 > 盘点数量， 所以 这里是负数）
            inventoryCheckDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryCheckDetail.setUpdateTime(new Date());
        }
        //3、点击确认时，修改盘点明细的业务数据（数量、状态等）
        inventoryCheckDetailService.saveOrUpdate(inventoryCheckDetail);

    }

    /**
     * 工作台盘点，查询货架号对应的示意图和盘点任务
     * 业务：
     * 1、根据 工作台id+货架id，找到 没有盘点完成的 盘点主单（唯一一条盘点主单）
     * 2、根据 盘点主单id，找到所有盘点明细
     * 3、根据 货架id，找到所有库位
     * 4、根据 盘点明细库位，填空，渲染货架示意图颜色
     * @param form
     * @return
     */
    @Override
    public WorkbenchCheckVO workbenchCheckQueryByShelfCode(WorkbenchCheckAffirmForm form) {
        //验证和设值 工作台盘点，查询货架号对应的示意图和盘点任务
        WorkbenchCheckAffirmForm workbenchCheckAffirmForm = verificationWorkbenchCheckQueryByShelfCode(form);

        WorkbenchCheckVO workbenchCheckVO = ConvertUtil.convert(workbenchCheckAffirmForm, WorkbenchCheckVO.class);

        Long workbenchId = workbenchCheckAffirmForm.getWorkbenchId();
        Long shelfId = workbenchCheckAffirmForm.getShelfId();

        QueryWrapper<InventoryCheck> inventoryCheckQueryWrapper = new QueryWrapper<>();
        inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getIsDeleted, 0);
        inventoryCheckQueryWrapper.lambda().ne(InventoryCheck::getCheckStatus, CheckStatusEnum.CHECKSTATUS_3.getTypeCode());
        inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getWorkbenchId, workbenchId);
        inventoryCheckQueryWrapper.lambda().eq(InventoryCheck::getShelfId, shelfId);
        inventoryCheckQueryWrapper.lambda().groupBy(InventoryCheck::getWorkbenchId, InventoryCheck::getShelfId);
        //1、根据 工作台id+货架id，找到 没有盘点完成的 盘点主单（唯一一条盘点主单）
        InventoryCheck inventoryCheck = inventoryCheckMapper.selectOne(inventoryCheckQueryWrapper);
        if(ObjectUtil.isEmpty(inventoryCheck)){
            throw new IllegalArgumentException("盘点主单不存在");
        }

        Long inventoryCheckId = inventoryCheck.getId();

        QueryWrapper<InventoryCheckDetail> inventoryCheckDetailQueryWrapper = new QueryWrapper<>();
        inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getIsDeleted, 0);
        inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getInventoryCheckId, inventoryCheckId);
        //2、根据 盘点主单id，找到所有盘点明细
        List<InventoryCheckDetail> inventoryCheckDetails = inventoryCheckDetailService.list(inventoryCheckDetailQueryWrapper);
        workbenchCheckVO.setInventoryCheckDetails(inventoryCheckDetails);
        //list 分组 转map
        Map<String, List<InventoryCheckDetail>> inventoryCheckDetailMap = inventoryCheckDetails.stream().collect(Collectors.groupingBy(ic -> ic.getWarehouseLocationCode()));

        WarehouseShelf warehouseShelf = warehouseShelfMapper.selectById(shelfId);
        Map<String, Object> shelfMap = new HashMap<>();
        shelfMap.put("warehouseId", warehouseShelf.getWarehouseId());
        shelfMap.put("warehouseAreaId", warehouseShelf.getWarehouseAreaId());
        shelfMap.put("shelfId", warehouseShelf.getId());
        //3、根据 货架id，找到所有库位
        List<WarehouseLocation> warehouseLocationList = warehouseLocationMapper.selectLocationCapacityByShelfId(shelfMap);

        //4、根据 盘点明细库位，填空，渲染货架示意图颜色
        List<WarehouseShelfSketchMapVO> list = new ArrayList<>();
        for (int k=0; k<warehouseLocationList.size(); k++) {
            WarehouseLocation warehouseLocation = warehouseLocationList.get(k);
            Long warehouseLocationId = warehouseLocation.getId();
            String warehouseLocationCode = warehouseLocation.getCode();

            WarehouseShelfSketchMapVO sketchMapVO = new WarehouseShelfSketchMapVO();
            sketchMapVO.setWarehouseLocationCode(warehouseLocationCode);
            /*
            白色:不需要盘点的库位
            橙色:需要盘点的库位
            绿色: 已盘点完成的库位
            */
            //查看库位下，是否存在需要盘点的库位
            List<InventoryCheckDetail> inventoryCheckDetailList = inventoryCheckDetailMap.get(warehouseLocationCode);
            if(CollUtil.isEmpty(inventoryCheckDetailList)){
                //库位下不存在盘点明细                                   设置为白色
                sketchMapVO.setColorCode(ColorCodeEnum.WHITE.getTypeCode());
            }else{
                //判断是否 存在 未盘点的明细
                boolean exist = inventoryCheckDetailList.stream().filter(icd -> icd.getCheckStatus().equals(CheckDetailStatusEnum.CHECKSTATUS_1.getTypeCode())).findAny().isPresent();
                if (exist) {
                    //存在  CHECKSTATUS_1(1,"未盘点"), 的明细           设置颜色为 橙色
                    sketchMapVO.setColorCode(ColorCodeEnum.ORANGE.getTypeCode());
                }else{
                    //不存在 未盘点的 明细                              设置颜色为 绿色
                    sketchMapVO.setColorCode(ColorCodeEnum.GREEN.getTypeCode());
                }

                List<MaterialDetailVO> materialDetails = new ArrayList<>();
                inventoryCheckDetailList.forEach(detail -> {
                    MaterialDetailVO materialDetailVO = new MaterialDetailVO();
                    materialDetailVO.setMaterialId(detail.getMaterialId());
                    materialDetailVO.setMaterialCode(detail.getMaterialCode());
                    materialDetailVO.setMaterialName(detail.getMaterialName());
                    materialDetailVO.setCheckCount(detail.getCheckCount());//盘点数量
                    materialDetails.add(materialDetailVO);
                });
                sketchMapVO.setMaterialDetails(materialDetails);
            }
            list.add(sketchMapVO);
        }
        workbenchCheckVO.setShelfSketchMaps(list);
        return workbenchCheckVO;
    }

    /**
     * 工作台盘点，撤销盘点
     * 业务
     *     1、判断 盘点任务明细，是否存在
     *     2、判断 盘点任务明细，状态是否可以撤销盘点
     *     3、修改盘点明细的业务数据（数量、状态等）
     * @param inventoryCheckDetails
     */
    @Override
    public void workbenchCheckRevocation(List<InventoryCheckDetail> inventoryCheckDetails) {
        if(CollUtil.isEmpty(inventoryCheckDetails)){
            throw new IllegalArgumentException("盘点明细不能为空");
        }
        List<InventoryCheckDetail> details = new ArrayList<>();
        for (int i=0; i<inventoryCheckDetails.size(); i++){
            InventoryCheckDetail inventoryCheckDetail = inventoryCheckDetails.get(i);
            Long id = inventoryCheckDetail.getId();
            InventoryCheckDetail checkDetail = inventoryCheckDetailService.getById(id);
            if(ObjectUtil.isEmpty(checkDetail)){
                throw new IllegalArgumentException("盘点明细不存在");
            }
            Integer checkStatus = checkDetail.getCheckStatus();//明细盘点状态(1未盘点、2已盘点、3已过账)
            if(!checkStatus.equals(CheckDetailStatusEnum.CHECKSTATUS_2.getTypeCode())){
                throw new IllegalArgumentException("盘点明细，状态不正确，不能撤销盘点");
            }
            checkDetail.setCheckStatus(CheckDetailStatusEnum.CHECKSTATUS_1.getTypeCode());// 状态 从 2已盘点 改为 1未盘点
            checkDetail.setCheckCount(new BigDecimal("0"));//盘点数量 0
            checkDetail.setCheckSurplusCount(new BigDecimal("0"));//盘盈数量 0
            checkDetail.setCheckLossesCount(new BigDecimal("0"));//盘亏数量 0
            details.add(checkDetail);
        }
        if(CollUtil.isEmpty(details)){
            inventoryCheckDetailService.saveOrUpdateBatch(details);
        }
    }

    /**
     * 工作台盘点，货架回库
     * 业务：
     *     1、货架id+工作台id，直接进行货架回库(没有其他验证)
     * @param form
     */
    @Override
    public void workbenchCheckBackLibrary(WorkbenchCheckAffirmForm form) {
        String workbenchCode = CurrentUserUtil.getCurrrentUserWorkbenchCode();
        if(StrUtil.isEmpty(workbenchCode)){
            throw new IllegalArgumentException("用户不存在工作台");
        }
        QueryWrapper<Workbench> workbenchQueryWrapper = new QueryWrapper<>();
        workbenchQueryWrapper.lambda().eq(Workbench::getCode, workbenchCode);
        workbenchQueryWrapper.lambda().eq(Workbench::getIsDeleted, 0);
        workbenchQueryWrapper.lambda().groupBy(Workbench::getCode);
        Workbench workbench = workbenchService.getOne(workbenchQueryWrapper);
        if(ObjectUtil.isEmpty(workbench)){
            throw new IllegalArgumentException("工作台不存在");
        }
        Long warehouseId = workbench.getWarehouseId();
        Long workbench_warehouseId = workbench.getWarehouseId();

        String shelfCode = form.getShelfCode();
        if(StrUtil.isEmpty(shelfCode)){
            throw new IllegalArgumentException("货架号不能为空");
        }
        QueryWrapper<WarehouseShelf> warehouseShelfQueryWrapper = new QueryWrapper<>();
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getWarehouseId, warehouseId);
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getCode, shelfCode);
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getIsDeleted, 0);
        warehouseShelfQueryWrapper.lambda().groupBy(WarehouseShelf::getCode);
        WarehouseShelf warehouseShelf = warehouseShelfService.getOne(warehouseShelfQueryWrapper);
        if(ObjectUtil.isEmpty(warehouseShelf)){
            throw new IllegalArgumentException("货架不存在");
        }
        Long warehouseShelf_warehouseId = warehouseShelf.getWarehouseId();
        if(!workbench_warehouseId.equals(warehouseShelf_warehouseId)){
            throw new IllegalArgumentException("工作台，货架，的所属仓库id不一致");
        }

        Long workbenchId = workbench.getId();

        //4.1、点击货架回库后，根据收货信息自动生成对应的：货架移库任务（工作台至货架-上架）
        CreateShelfMoveTaskForm shelfMoveTaskForm = new CreateShelfMoveTaskForm();
        shelfMoveTaskForm.setMovementTypeCode(ShelfMoveTaskEnum.MTC06.getTypeCode());
        shelfMoveTaskForm.setMovementTypeName(ShelfMoveTaskEnum.MTC06.getTypeDesc());
        shelfMoveTaskForm.setWorkbenchId(workbenchId);
        shelfMoveTaskForm.setWorkbenchCode(workbenchCode);
        List<WarehouseShelf> warehouseShelfList = new ArrayList<>();
        warehouseShelfList.add(warehouseShelf);
        shelfMoveTaskForm.setWarehouseShelfList(warehouseShelfList);
        /**
         * 创建货架移库任务
         * @see com.jyd.bases.service.IShelfMoveTaskService#createShelfMoveTask(CreateShelfMoveTaskForm)
         */
        shelfMoveTaskService.createShelfMoveTask(shelfMoveTaskForm);
    }

    /**
     * 验证和设值 工作台盘点，查询货架号对应的示意图和盘点任务
     * @param form
     * @return
     */
    private WorkbenchCheckAffirmForm verificationWorkbenchCheckQueryByShelfCode(WorkbenchCheckAffirmForm form) {
        String workbenchCode = CurrentUserUtil.getCurrrentUserWorkbenchCode();
        if(StrUtil.isEmpty(workbenchCode)){
            throw new IllegalArgumentException("用户不存在工作台");
        }
        QueryWrapper<Workbench> workbenchQueryWrapper = new QueryWrapper<>();
        workbenchQueryWrapper.lambda().eq(Workbench::getCode, workbenchCode);
        workbenchQueryWrapper.lambda().eq(Workbench::getIsDeleted, 0);
        workbenchQueryWrapper.lambda().groupBy(Workbench::getCode);
        Workbench workbench = workbenchService.getOne(workbenchQueryWrapper);
        if(ObjectUtil.isEmpty(workbench)){
            throw new IllegalArgumentException("工作台不存在");
        }
        Long warehouseId = workbench.getWarehouseId();
        Long workbench_warehouseId = workbench.getWarehouseId();

        String shelfCode = form.getShelfCode();
        if(StrUtil.isEmpty(shelfCode)){
            throw new IllegalArgumentException("货架号不能为空");
        }
        QueryWrapper<WarehouseShelf> warehouseShelfQueryWrapper = new QueryWrapper<>();
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getWarehouseId, warehouseId);
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getCode, shelfCode);
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getIsDeleted, 0);
        warehouseShelfQueryWrapper.lambda().groupBy(WarehouseShelf::getCode);
        WarehouseShelf warehouseShelf = warehouseShelfService.getOne(warehouseShelfQueryWrapper);
        if(ObjectUtil.isEmpty(warehouseShelf)){
            throw new IllegalArgumentException("货架不存在");
        }
        Long warehouseShelf_warehouseId = warehouseShelf.getWarehouseId();
        if(!workbench_warehouseId.equals(warehouseShelf_warehouseId)){
            throw new IllegalArgumentException("工作台，货架，的所属仓库id不一致");
        }
        form.setWorkbenchId(workbench.getId());
        form.setWorkbenchCode(workbench.getCode());
        form.setShelfId(warehouseShelf.getId());
        form.setShelfCode(warehouseShelf.getCode());
        return form;
    }

    /**
     * 验证和设值 工作台盘点确认
     * @param form
     */
    private WorkbenchCheckAffirmForm verificationWorkbenchCheckAffirm(WorkbenchCheckAffirmForm form) {
        String workbenchCode = CurrentUserUtil.getCurrrentUserWorkbenchCode();
        if(StrUtil.isEmpty(workbenchCode)){
            throw new IllegalArgumentException("用户不存在工作台");
        }
        QueryWrapper<Workbench> workbenchQueryWrapper = new QueryWrapper<>();
        workbenchQueryWrapper.lambda().eq(Workbench::getCode, workbenchCode);
        workbenchQueryWrapper.lambda().eq(Workbench::getIsDeleted, 0);
        workbenchQueryWrapper.lambda().groupBy(Workbench::getCode);
        Workbench workbench = workbenchService.getOne(workbenchQueryWrapper);
        if(ObjectUtil.isEmpty(workbench)){
            throw new IllegalArgumentException("工作台不存在");
        }
        Long warehouseId = workbench.getWarehouseId();
        Long workbench_warehouseId = workbench.getWarehouseId();

        String shelfCode = form.getShelfCode();
        if(StrUtil.isEmpty(shelfCode)){
            throw new IllegalArgumentException("货架号不能为空");
        }
        QueryWrapper<WarehouseShelf> warehouseShelfQueryWrapper = new QueryWrapper<>();
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getWarehouseId, warehouseId);
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getCode, shelfCode);
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getIsDeleted, 0);
        warehouseShelfQueryWrapper.lambda().groupBy(WarehouseShelf::getCode);
        WarehouseShelf warehouseShelf = warehouseShelfService.getOne(warehouseShelfQueryWrapper);
        if(ObjectUtil.isEmpty(warehouseShelf)){
            throw new IllegalArgumentException("货架不存在");
        }
        Long warehouseShelf_warehouseId = warehouseShelf.getWarehouseId();

        String warehouseLocationCode = form.getWarehouseLocationCode();
        if(StrUtil.isEmpty(warehouseLocationCode)){
            throw new IllegalArgumentException("库位编号不能为空");
        }
        QueryWrapper<WarehouseLocation> warehouseLocationQueryWrapper = new QueryWrapper<>();
        warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getWarehouseId, warehouseId);
        warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getCode, warehouseLocationCode);
        warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getIsDeleted, 0);
        warehouseLocationQueryWrapper.lambda().groupBy(WarehouseLocation::getCode);
        WarehouseLocation warehouseLocation = warehouseLocationService.getOne(warehouseLocationQueryWrapper);
        if(ObjectUtil.isEmpty(warehouseLocation)){
            throw new IllegalArgumentException("库位不存在");
        }
        Long warehouseLocation_warehouseId = warehouseLocation.getWarehouseId();
        if(!workbench_warehouseId.equals(warehouseShelf_warehouseId)
                || !workbench_warehouseId.equals(warehouseLocation_warehouseId)
                || !warehouseShelf_warehouseId.equals(warehouseLocation_warehouseId)){
            throw new IllegalArgumentException("工作台，货架，库位的所属仓库id不一致");
        }

        String materialCode = form.getMaterialCode();
        if(StrUtil.isEmpty(materialCode)){
            throw new IllegalArgumentException("物料编号不能为空");
        }

        BigDecimal checkCount = form.getCheckCount();
        if(ObjectUtil.isEmpty(checkCount)){
            throw new IllegalArgumentException("盘点数量不能为空");
        }
        if(checkCount.compareTo(new BigDecimal("0")) < 0){
            throw new IllegalArgumentException("盘点数量不能为小于0");
        }
        form.setWorkbenchId(workbench.getId());
        form.setWorkbenchCode(workbench.getCode());
        form.setShelfId(warehouseShelf.getId());
        form.setShelfCode(warehouseShelf.getCode());

        return form;

    }

}
