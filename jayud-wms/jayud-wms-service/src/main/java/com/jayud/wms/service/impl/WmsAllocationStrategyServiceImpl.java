package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.WmsAllocationStrategyMapper;
import com.jayud.wms.model.constant.AllocationStrategyConstant;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.dto.allocationStrategy.AllocationStrategyDTO;
import com.jayud.wms.model.dto.allocationStrategy.AllocationStrategyParamDTO;
import com.jayud.wms.model.enums.AllocationStrategyDetailTypeEnum;
import com.jayud.wms.model.enums.BatchAttributeFiledEnum;
import com.jayud.wms.model.enums.MaterialTurnoverModeEnum;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.WmsAllocationStrategyDetailVO;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.wms.service.*;
import com.jayud.wms.utils.CodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分配策略 服务实现类
 *
 * @author jyd
 * @since 2022-01-17
 */
@Service
public class WmsAllocationStrategyServiceImpl extends ServiceImpl<WmsAllocationStrategyMapper, WmsAllocationStrategy> implements IWmsAllocationStrategyService {

    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private IWmsAllocationStrategyDetailService wmsAllocationStrategyDetailService;
    @Autowired
    private IWmsOwerInfoService wmsOwerInfoService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IInventoryDetailService inventoryDetailService;

    @Autowired
    private WmsAllocationStrategyMapper wmsAllocationStrategyMapper;

    @Autowired
    private CodeUtils codeUtils;

    @Override
    public IPage<WmsAllocationStrategy> selectPage(WmsAllocationStrategy wmsAllocationStrategy,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsAllocationStrategy> page=new Page<WmsAllocationStrategy>(currentPage,pageSize);
        IPage<WmsAllocationStrategy> pageList= wmsAllocationStrategyMapper.pageList(page, wmsAllocationStrategy);
        return pageList;
    }

    @Override
    public List<WmsAllocationStrategy> selectList(WmsAllocationStrategy wmsAllocationStrategy){
        return wmsAllocationStrategyMapper.list(wmsAllocationStrategy);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsAllocationStrategyMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsAllocationStrategyForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsAllocationStrategyForExcel(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult saveStrategy(WmsAllocationStrategy wmsAllocationStrategy) {
        boolean isAdd = false;
        if (StringUtils.isBlank(wmsAllocationStrategy.getAllocationStrategyCode())){
            isAdd = true;
            wmsAllocationStrategy.setAllocationStrategyCode(codeUtils.getCodeByRule(CodeConStants.ALLOCATION_STRATEGY));
        }
        this.saveOrUpdate(wmsAllocationStrategy);
        if (isAdd){
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }else{
            return BaseResult.ok(SysTips.EDIT_SUCCESS);
        }
    }



    @Override
    public BaseResult<List<AllocationStrategyDTO>> initStrategy(String materialCode, String owerCode, String warehouseCode) {
        List<AllocationStrategyDTO> strategyList = new ArrayList<>();
        BaseResult<AllocationStrategyDTO> materialResult = initMaterialStrategy(materialCode);
        BaseResult<AllocationStrategyDTO> owerResult = initOwerStrategy(owerCode);
        BaseResult<AllocationStrategyDTO> warehouseResult = initWarehouseStrategy(warehouseCode);
        if (!materialResult.isSuccess()&&!owerResult.isSuccess()&&!warehouseResult.isSuccess()){
            return BaseResult.error();
        }
        if (materialResult.isSuccess()){
            strategyList.add(materialResult.getResult());
        }else {
            strategyList.add(new AllocationStrategyDTO());
        }
        if (owerResult.isSuccess()){
            strategyList.add(owerResult.getResult());
        }else {
            strategyList.add(new AllocationStrategyDTO());
        }
        if (warehouseResult.isSuccess()){
            strategyList.add(warehouseResult.getResult());
        }else {
            strategyList.add(new AllocationStrategyDTO());
        }
        return BaseResult.ok(strategyList);
    }

    @Override
    public List<InventoryDetail> getStrategyInventory(List<AllocationStrategyDTO> strategyList, InventoryDetail inventoryDetail) {
        List<InventoryDetail> inventoryDetailList = new ArrayList<>();
        boolean isEnd = false;
        if (CollUtil.isNotEmpty(strategyList)) {
            for (AllocationStrategyDTO allocationStrategyDTO : strategyList) {

//                if (isEnd){
//                    break;
//                }
//                if (CollUtil.isNotEmpty(allocationStrategyDTO.getOriginAscList())||CollUtil.isNotEmpty(allocationStrategyDTO.getOriginDescList())){
//                    inventoryDetailList = getAllocationDetailByStrategy(inventoryDetail,allocationStrategyDTO.getOriginDescList(),allocationStrategyDTO.getOriginAscList(),null);
//                    if (CollectionUtil.isNotEmpty(inventoryDetailList)) {
//                        break;
//                    }
//                }
//                if (CollUtil.isNotEmpty(allocationStrategyDTO.getParamList())) {
//                    for (AllocationStrategyParamDTO param : allocationStrategyDTO.getParamList()) {
//                        inventoryDetailList = getAllocationDetailByStrategy(inventoryDetail,param.getDescList(),param.getAscList(), param.getConditionList());
//                        if (CollectionUtil.isNotEmpty(inventoryDetailList)) {
//                            isEnd = true;
//                            break;
//                        }
//                    }
//                }

                List<String> descList = new ArrayList<>();
                List<String> ascList = new ArrayList<>();
                List<String> conditionList = new ArrayList<>();

                if (CollUtil.isNotEmpty(allocationStrategyDTO.getOriginDescList())){
                    descList.addAll(allocationStrategyDTO.getOriginDescList());
                }
                if (CollUtil.isNotEmpty(allocationStrategyDTO.getOriginAscList())){
                    ascList.addAll(allocationStrategyDTO.getOriginAscList());
                }
                if (CollUtil.isNotEmpty(allocationStrategyDTO.getConditionList())){
                    conditionList.addAll(allocationStrategyDTO.getConditionList());
                }
                boolean isSelect = false;
                if (CollUtil.isNotEmpty(allocationStrategyDTO.getParamList())) {
                    for (AllocationStrategyParamDTO param : allocationStrategyDTO.getParamList()) {
                        if (CollUtil.isNotEmpty(param.getDescList())){
                            descList.addAll(param.getDescList());
                            isSelect = true;
                        }
                        if (CollUtil.isNotEmpty(param.getAscList())){
                            ascList.addAll(param.getAscList());
                            isSelect = true;
                        }
                        if (CollUtil.isNotEmpty(param.getConditionList())){
                            conditionList.addAll(param.getConditionList());
                            isSelect = true;
                        }

                    }
                }
                if (isSelect){
                    inventoryDetailList = getAllocationDetailByStrategy(inventoryDetail,descList,ascList, conditionList);
                }
                if (CollUtil.isNotEmpty(inventoryDetailList)){
                    break;
                }
            }
        }
        return inventoryDetailList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult logicDel(Long id) {
        List<WmsAllocationStrategyDetailVO> detailList = wmsAllocationStrategyDetailService.selectByStrategyId(id);
        if (CollUtil.isNotEmpty(detailList)){
            return BaseResult.error(SysTips.EXIT_ALLCATION_STRATEGY_DETAIL);
        }
        this.baseMapper.logicDel(id,CurrentUserUtil.getUsername());
        wmsAllocationStrategyDetailService.delByStrategyId(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    @Override
    public BaseResult saveMsg(WmsAllocationStrategy wmsAllocationStrategy) {
        boolean isAdd = true;
        if (wmsAllocationStrategy.getId() != null){
            isAdd = false;
        }
        if (checkIsSameName(isAdd,wmsAllocationStrategy.getAllocationStrategyName(),wmsAllocationStrategy.getId())){
            return BaseResult.error(SysTips.EXIT_SAMENAME);
        }
        if (isAdd) {
            this.save(wmsAllocationStrategy);
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }else {
            this.updateById(wmsAllocationStrategy);
            return BaseResult.ok(SysTips.EDIT_SUCCESS);
        }
    }


    /**
     * @description 初始化物料分配策略
     * @author  ciro
     * @date   2022/1/18 14:16
     * @param: materialCode
     * @return: com.jyd.component.commons.result.Result<com.jayud.model.dto..allocationStrategy.AllocationStrategyDTO>
     **/
    private BaseResult<AllocationStrategyDTO> initMaterialStrategy(String materialCode){
        AllocationStrategyDTO allocationStrategyDTO = new AllocationStrategyDTO();
        allocationStrategyDTO.initOrigin();
        allocationStrategyDTO.setAllocationType(AllocationStrategyConstant.MATERIAL);
        WmsMaterialBasicInfoVO materialBasicInfoVO = wmsMaterialBasicInfoService.selectByCode(materialCode);
        int nullCount = 0;
        String nullErr = "";
        boolean notTurnover = true;
        if (materialBasicInfoVO != null) {
            if (materialBasicInfoVO.getTurnoverAttribute() != null && materialBasicInfoVO.getTurnoverMode() != null) {
                if (materialBasicInfoVO.getTurnoverMode().equals(MaterialTurnoverModeEnum.DESC.getType())) {
                    allocationStrategyDTO.getOriginAscList().add(BatchAttributeFiledEnum.getFiledValue(materialBasicInfoVO.getTurnoverAttribute()));
                } else if (materialBasicInfoVO.getTurnoverMode().equals(MaterialTurnoverModeEnum.ASC.getType())) {
                    allocationStrategyDTO.getOriginDescList().add(BatchAttributeFiledEnum.getFiledValue(materialBasicInfoVO.getTurnoverAttribute()));
                }
                allocationStrategyDTO.getConditionList().add("  "+BatchAttributeFiledEnum.getFiledValue(materialBasicInfoVO.getTurnoverAttribute())+" IS NOT NULL AND "+BatchAttributeFiledEnum.getFiledValue(materialBasicInfoVO.getTurnoverAttribute())+" != \"\" ");
                notTurnover = false;
            }
        }
        if (notTurnover){
            nullCount+=1;
            nullErr+="物料没有周转方式！";
        }
        if (materialBasicInfoVO.getAllocationStrategyId() != null){
            initStrategyParam(allocationStrategyDTO,materialBasicInfoVO.getAllocationStrategyId());
        }else {
            nullCount+=1;
            nullErr+="物料没有分配策略！";
        }
        if (nullCount == 2){
            return BaseResult.error(nullErr);
        }
        return BaseResult.ok(allocationStrategyDTO);
    }

    /**
     * @description 初始化货主分配策略
     * @author  ciro
     * @date   2022/1/18 14:19
     * @param: owerCode
     * @return: com.jyd.component.commons.result.Result<com.jayud.model.dto..allocationStrategy.AllocationStrategyDTO>
     **/
    private BaseResult<AllocationStrategyDTO> initOwerStrategy(String owerCode){
        AllocationStrategyDTO allocationStrategyDTO = new AllocationStrategyDTO();
        allocationStrategyDTO.initOrigin();
        allocationStrategyDTO.setAllocationType(AllocationStrategyConstant.OWER);
        WmsOwerInfo wmsOwerInfo = wmsOwerInfoService.selectByOwerMsg(null,owerCode);
        if (wmsOwerInfo.getAllocationStrategyId() != null){
            initStrategyParam(allocationStrategyDTO,wmsOwerInfo.getAllocationStrategyId());
        }else {
            return BaseResult.error("货主没有分配策略！");
        }
        return BaseResult.ok(allocationStrategyDTO);
    }

    /**
     * @description 初始化仓库分配策略
     * @author  ciro
     * @date   2022/1/18 14:19
     * @param: warehuoseCode
     * @return: com.jyd.component.commons.result.Result<com.jayud.model.dto..allocationStrategy.AllocationStrategyDTO>
     **/
    private BaseResult<AllocationStrategyDTO> initWarehouseStrategy(String warehuoseCode){
        AllocationStrategyDTO allocationStrategyDTO = new AllocationStrategyDTO();
        allocationStrategyDTO.initOrigin();
        allocationStrategyDTO.setAllocationType(AllocationStrategyConstant.WAREHOUSE);
        Warehouse warehouse = warehouseService.selectByWarehouseMsg(null,warehuoseCode);
        if (warehouse.getAllocationStrategyId() != null){
            initStrategyParam(allocationStrategyDTO,warehouse.getAllocationStrategyId());
        }else {
            return BaseResult.error("仓库没有分配策略！");
        }
        return BaseResult.ok(allocationStrategyDTO);
    }

    /**
     * @description 初始化分配策略数据
     * @author  ciro
     * @date   2022/1/18 11:15
     * @param: paramList
     * @param: strategyId
     * @return: void
     **/
    private void initStrategyParam(AllocationStrategyDTO allocationStrategyDTO,Long strategyId){
        List<AllocationStrategyParamDTO> paramList = new ArrayList<>();
        List<WmsAllocationStrategyDetail> detailList = wmsAllocationStrategyDetailService.selectByStrategyMsg(strategyId,null);
        if (CollUtil.isNotEmpty(detailList)){
            detailList.forEach(detail -> {
                AllocationStrategyParamDTO param = new AllocationStrategyParamDTO();
                param.initList();
                boolean isAdd = false;
                if (detail.getAllocationStrategyType().equals(AllocationStrategyDetailTypeEnum.LINE_SWQUENCE.getType())){
                    param.getAscList().add(AllocationStrategyConstant.LINE_SWQUENCE_PARAM);
                    param.getConditionList().add(AllocationStrategyConstant.LINE_SWQUENCE_CONDITION_NOT_NULL);
                    isAdd = true;
                }else if (detail.getAllocationStrategyType().equals(AllocationStrategyDetailTypeEnum.EMPTY_LOCATION.getType())){
                    param.getAscList().add(AllocationStrategyConstant.EMPTY_LOCATION_PARAM);
                    isAdd = true;
                }
                if (isAdd){
                    paramList.add(param);
                }
            });
        }
        allocationStrategyDTO.setParamList(paramList);
    }

    /**
     * @description 根据策略条件查询库存
     * @author  ciro
     * @date   2022/2/21 16:38
     * @param: inventoryDetail
     * @param: descList
     * @param: ascList
     * @param: conditionList
     * @return: java.util.List<com.jayud.model.po.InventoryDetail>
     **/
    private List<InventoryDetail> getAllocationDetailByStrategy(InventoryDetail inventoryDetail,List<String> descList,List<String> ascList,List<String> conditionList){
        if (CollUtil.isNotEmpty(descList)){
            inventoryDetail.setDescMsg(StringUtils.join(descList, StrUtil.C_COMMA));
        }
        if (CollUtil.isNotEmpty(ascList)) {
            inventoryDetail.setAscMsg(StringUtils.join(ascList, StrUtil.C_COMMA));
        }
        if (CollUtil.isNotEmpty(conditionList)){
            inventoryDetail.setConditionParam(" AND "+StringUtils.join(conditionList, " AND "));
        }
        List<InventoryDetail> inventoryDetailList = inventoryDetailService.selectList(inventoryDetail);
        return inventoryDetailList;
    }

    /**
     * @description 判断名称是否相同
     * @author  ciro
     * @date   2022/2/21 17:05
     * @param: isAdd    是都新增
     * @param: strategyName     名称
     * @param: id           主键id
     * @return: boolean
     **/
    private boolean checkIsSameName(boolean isAdd,String strategyName,Long id){
        WmsAllocationStrategy exitMsg = new WmsAllocationStrategy();
        exitMsg.setAllocationStrategyName(strategyName);
        List<WmsAllocationStrategy> strategyList = this.selectList(exitMsg);
        if (CollUtil.isNotEmpty(strategyList)){
            exitMsg = strategyList.get(0);
            if (isAdd){
                return true;
            }else {
                if (!exitMsg.getId().equals(id)){
                    return true;
                }
            }
        }
        return false;
    }

}
