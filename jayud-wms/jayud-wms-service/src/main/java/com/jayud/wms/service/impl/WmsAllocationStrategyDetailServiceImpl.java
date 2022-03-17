package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.WmsAllocationStrategyDetailMapper;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.po.WmsAllocationStrategyDetail;
import com.jayud.wms.model.vo.WmsAllocationStrategyDetailVO;
import com.jayud.wms.service.IWmsAllocationStrategyDetailService;
import com.jayud.wms.utils.CodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分配策略详情 服务实现类
 *
 * @author jyd
 * @since 2022-01-17
 */
@Service
public class WmsAllocationStrategyDetailServiceImpl extends ServiceImpl<WmsAllocationStrategyDetailMapper, WmsAllocationStrategyDetail> implements IWmsAllocationStrategyDetailService {


    @Autowired
    private WmsAllocationStrategyDetailMapper wmsAllocationStrategyDetailMapper;

    @Autowired
    private CodeUtils codeUtils;


    @Override
    public IPage<WmsAllocationStrategyDetailVO> selectPage(WmsAllocationStrategyDetailVO wmsAllocationStrategyDetailVO,
                                                           Integer currentPage,
                                                           Integer pageSize,
                                                           HttpServletRequest req){

        Page<WmsAllocationStrategyDetailVO> page=new Page<WmsAllocationStrategyDetailVO>(currentPage,pageSize);
        IPage<WmsAllocationStrategyDetailVO> pageList= wmsAllocationStrategyDetailMapper.pageList(page, wmsAllocationStrategyDetailVO);
        return pageList;
    }

    @Override
    public List<WmsAllocationStrategyDetailVO> selectList(WmsAllocationStrategyDetailVO wmsAllocationStrategyDetailVO){
        return wmsAllocationStrategyDetailMapper.list(wmsAllocationStrategyDetailVO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsAllocationStrategyDetailMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsAllocationStrategyDetailForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsAllocationStrategyDetailForExcel(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult saveStrategyDetail(WmsAllocationStrategyDetail wmsAllocationStrategyDetail) {
        boolean isAdd = false;
        if (StringUtils.isBlank(wmsAllocationStrategyDetail.getAllocationStrategyCode())){
            return BaseResult.error(SysTips.EMPTY_ALLOCATION_STRATEGY_CODE);
        }
        if (wmsAllocationStrategyDetail.getAllocationStrategyCode() == null){
            return BaseResult.error(SysTips.EMPTY_ALLOCATION_STRATEGY_ID);
        }
        if (StringUtils.isBlank(wmsAllocationStrategyDetail.getAllocationStrategyDetailCode())){
            isAdd = true;
            wmsAllocationStrategyDetail.setAllocationStrategyDetailCode(codeUtils.getCodeByRule(CodeConStants.ALLOCATION_STRATEGY_DETAIL));
            wmsAllocationStrategyDetail.setSorts(getNextSorts(wmsAllocationStrategyDetail.getAllocationStrategyCode()));
        }
        if (checkSame(isAdd,wmsAllocationStrategyDetail.getAllocationStrategyType(),wmsAllocationStrategyDetail.getAllocationStrategyCode(),wmsAllocationStrategyDetail.getAllocationStrategyDetailCode())){
            return BaseResult.error(SysTips.THE_SAME_ALLOCATION_STRATEGY_DETAIL_ERROR);
        }
        this.saveOrUpdate(wmsAllocationStrategyDetail);
        if (isAdd){
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }else{
            return BaseResult.ok(SysTips.EDIT_SUCCESS);
        }
    }

    @Override
    public List<WmsAllocationStrategyDetail> selectByStrategyMsg(Long strategyId, String strategyCode) {
        if (strategyId == null && StringUtils.isBlank(strategyCode)){
            return null;
        }
        LambdaQueryWrapper<WmsAllocationStrategyDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (strategyId != null) {
            lambdaQueryWrapper.eq(WmsAllocationStrategyDetail::getAllocationStrategyId, strategyId);
        }
        if (StringUtils.isNotBlank(strategyCode)) {
            lambdaQueryWrapper.eq(WmsAllocationStrategyDetail::getAllocationStrategyCode, strategyCode);
        }
        lambdaQueryWrapper.orderByAsc(WmsAllocationStrategyDetail::getSorts);
        List<WmsAllocationStrategyDetail> detailList = this.list(lambdaQueryWrapper);
        return detailList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult delByStrategyId(long strategyId) {
        wmsAllocationStrategyDetailMapper.delByStrategyId(strategyId,CurrentUserUtil.getUsername());
        return null;
    }

    @Override
    public List<WmsAllocationStrategyDetailVO> selectByStrategyId(Long strategyId) {
        WmsAllocationStrategyDetailVO detail = new WmsAllocationStrategyDetailVO();
        detail.setAllocationStrategyId(strategyId);
        return this.selectList(detail);
    }

    /**
     * @description     判断是否相同
     * @author  ciro
     * @date   2022/1/17 14:12
     * @param: isAdd
     * @param: allocationStrategyType   详情类型
     * @param: strategyCode             分配策略编号
     * @param: strategyDetailCode       分配策略详情编号
     * @return: boolean
     **/
    private boolean checkSame(boolean isAdd ,int allocationStrategyType,String strategyCode,String strategyDetailCode){
        LambdaQueryWrapper<WmsAllocationStrategyDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsAllocationStrategyDetail::getAllocationStrategyCode,strategyCode);
        lambdaQueryWrapper.eq(WmsAllocationStrategyDetail::getAllocationStrategyType,allocationStrategyType);
        WmsAllocationStrategyDetail wmsAllocationStrategyDetail = getOne(lambdaQueryWrapper);
        if (isAdd){
            if (wmsAllocationStrategyDetail != null){
                return true;
            }
        }else {
            if (wmsAllocationStrategyDetail == null){
                return false;
            }else if (!wmsAllocationStrategyDetail.getAllocationStrategyDetailCode().equals(strategyDetailCode)){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    /**
     * @description 获取下一个排序
     * @author  ciro
     * @date   2022/1/20 11:24
     * @param: strategyCode
     * @return: int
     **/
    private int getNextSorts(String strategyCode){
        WmsAllocationStrategyDetailVO wmsAllocationStrategyDetailVO = new WmsAllocationStrategyDetailVO();
        wmsAllocationStrategyDetailVO.setAllocationStrategyCode(strategyCode);
        List<WmsAllocationStrategyDetailVO> list = selectList(wmsAllocationStrategyDetailVO);
        if (CollUtil.isEmpty(list)){
            return 1;
        }
        return list.get(list.size()-1).getSorts()+1;
    }


}
