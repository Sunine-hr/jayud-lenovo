package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ListUtils;
import com.jayud.wms.mapper.WmsMaterialBarCodeMapper;
import com.jayud.wms.model.po.WmsMaterialBarCode;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.wms.service.IWmsMaterialBarCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料-条码 服务实现类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Service
public class WmsMaterialBarCodeServiceImpl extends ServiceImpl<WmsMaterialBarCodeMapper, WmsMaterialBarCode> implements IWmsMaterialBarCodeService {


    @Autowired
    private WmsMaterialBarCodeMapper wmsMaterialBarCodeMapper;

    @Override
    public IPage<WmsMaterialBarCode> selectPage(WmsMaterialBarCode wmsMaterialBarCode,
                                        HttpServletRequest req){

        Page<WmsMaterialBarCode> page=new Page<WmsMaterialBarCode>(wmsMaterialBarCode.getCurrentPage(),wmsMaterialBarCode.getPageSize());
        IPage<WmsMaterialBarCode> pageList= wmsMaterialBarCodeMapper.pageList(page, wmsMaterialBarCode);
        return pageList;
    }

    @Override
    public List<WmsMaterialBarCode> selectList(WmsMaterialBarCode wmsMaterialBarCode){
        return wmsMaterialBarCodeMapper.list(wmsMaterialBarCode);
    }

    @Override
    public WmsMaterialBasicInfoVO selectByMaterialId(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        List<WmsMaterialBarCode> list = selectWrapperaByMaterialId(wmsMaterialBasicInfoVO.getId());
        wmsMaterialBasicInfoVO.setThisBarCodeSelect(list);
        wmsMaterialBasicInfoVO.setLastBarCodeSelect(list);
        return wmsMaterialBasicInfoVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult add(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        List<String> thisBarCodeSelect = getBarCodeList(wmsMaterialBasicInfoVO.getThisBarCodeSelect());
        List<String> lastBarCodeSelect = getBarCodeList(wmsMaterialBasicInfoVO.getLastBarCodeSelect());
        List<String> addCodeList = ListUtils.getDiff(lastBarCodeSelect,thisBarCodeSelect);
        List<String> delCodeList = ListUtils.getDiff(thisBarCodeSelect,lastBarCodeSelect);
        if (addCodeList != null){
            List<WmsMaterialBarCode> list = new ArrayList<>();
            addCodeList.forEach(code -> {
                WmsMaterialBarCode wmsMaterialBarCode = new WmsMaterialBarCode();
                wmsMaterialBarCode.setMaterialBasicInfoId(wmsMaterialBasicInfoVO.getId());
                wmsMaterialBarCode.setBarCode(code);
                list.add(wmsMaterialBarCode);
            });
            if (!list.isEmpty()) {
                this.saveBatch(list);
            }
        }
        if (delCodeList != null){
            if (!delCodeList.isEmpty()) {
                wmsMaterialBarCodeMapper.delByMaterialIdAndCode(wmsMaterialBasicInfoVO.getId(), delCodeList, CurrentUserUtil.getUsername());
            }
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByMaterialId(long materialId) {
        wmsMaterialBarCodeMapper.delByMaterialIdAndCode(materialId,null, CurrentUserUtil.getUsername());
    }

    /**
     * @description 根据物料id查询编码
     * @author  ciro
     * @date   2021/12/16 16:08
     * @param: materialId
     * @return: java.util.List<com.jayud.model.po.WmsMaterialBarCode>
     **/
    private List<WmsMaterialBarCode> selectWrapperaByMaterialId(long materialId){
        LambdaQueryWrapper<WmsMaterialBarCode> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(WmsMaterialBarCode::getMaterialBasicInfoId,materialId);
        lambdaQueryWrapper.eq(WmsMaterialBarCode::getIsDeleted,false);
        List<WmsMaterialBarCode> list = wmsMaterialBarCodeMapper.selectList(lambdaQueryWrapper);
        return list;
    }


    /**
     * @description 获取编码
     * @author  ciro
     * @date   2021/12/16 17:06
     * @param: codeList
     * @return: java.util.List<java.lang.String>
     **/
    private List<String> getBarCodeList(List<WmsMaterialBarCode> codeList){
        if (codeList == null){
            return new ArrayList<String>();
        }
        List<String> codeValueList = codeList.stream().map(WmsMaterialBarCode::getBarCode).distinct().collect(Collectors.toList());
        return codeValueList;
    }



}
