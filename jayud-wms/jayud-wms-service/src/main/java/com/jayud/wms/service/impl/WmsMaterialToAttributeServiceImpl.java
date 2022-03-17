package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.WmsMaterialToAttribute;
import com.jayud.wms.mapper.WmsMaterialToAttributeMapper;
import com.jayud.wms.service.IWmsMaterialToAttributeService;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料管理-批属性配置 服务实现类
 *
 * @author jyd
 * @since 2022-01-12
 */
@Service
public class WmsMaterialToAttributeServiceImpl extends ServiceImpl<WmsMaterialToAttributeMapper, WmsMaterialToAttribute> implements IWmsMaterialToAttributeService {


    @Autowired
    private WmsMaterialToAttributeMapper wmsMaterialToAttributeMapper;

    @Override
    public IPage<WmsMaterialToAttribute> selectPage(WmsMaterialToAttribute wmsMaterialToAttribute,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsMaterialToAttribute> page=new Page<WmsMaterialToAttribute>(currentPage,pageSize);
        IPage<WmsMaterialToAttribute> pageList= wmsMaterialToAttributeMapper.pageList(page, wmsMaterialToAttribute);
        return pageList;
    }

    @Override
    public List<WmsMaterialToAttribute> selectList(WmsMaterialToAttribute wmsMaterialToAttribute){
        return wmsMaterialToAttributeMapper.list(wmsMaterialToAttribute);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsMaterialToAttributeMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsMaterialToAttributeForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsMaterialToAttributeForExcel(paramMap);
    }

    @Override
    public WmsMaterialBasicInfoVO selectByMaterialId(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        WmsMaterialToAttribute wmsMaterialToAttribute = new WmsMaterialToAttribute();
        wmsMaterialToAttribute.setMaterialBasicInfoId(wmsMaterialBasicInfoVO.getId());
        List<WmsMaterialToAttribute> attributeList = selectList(wmsMaterialToAttribute);
        wmsMaterialBasicInfoVO.setAttributeList(attributeList);
        return wmsMaterialBasicInfoVO;
    }

    @Override
    public BaseResult add(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        boolean isAdd = wmsMaterialBasicInfoVO.getIsAdd();
        boolean isNotNulls = true;
        if (CollectionUtil.isEmpty(wmsMaterialBasicInfoVO.getAttributeList())){
            isAdd = true;
            isNotNulls = false;
            wmsMaterialBasicInfoVO.setAttributeList(initAttribute(wmsMaterialBasicInfoVO.getId(),wmsMaterialBasicInfoVO.getMaterialCode()));
        }
        if (isNotNulls) {
            setMaterialMsg(wmsMaterialBasicInfoVO.getAttributeList(), wmsMaterialBasicInfoVO.getId(), wmsMaterialBasicInfoVO.getMaterialCode());
        }
        if (isAdd){
            this.saveBatch(wmsMaterialBasicInfoVO.getAttributeList());
        }else {
            this.updateBatchById(wmsMaterialBasicInfoVO.getAttributeList());
        }
        return BaseResult.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByMaterialId(long materialId) {
        wmsMaterialToAttributeMapper.delByMaterialId(materialId, CurrentUserUtil.getUsername());
    }

    @Override
    public List<WmsMaterialToAttribute> initAttribute() {
        List<WmsMaterialToAttribute> list = new ArrayList<>();
        WmsMaterialToAttribute wmsMaterialToAttribute1 = new WmsMaterialToAttribute();
        wmsMaterialToAttribute1.setAttributeType(1);
        wmsMaterialToAttribute1.setAttributeNanme("批次号");
        wmsMaterialToAttribute1.setAttributeType_text("批属性1");
        list.add(wmsMaterialToAttribute1);
        WmsMaterialToAttribute wmsMaterialToAttribute2 = new WmsMaterialToAttribute();
        wmsMaterialToAttribute2.setAttributeType(2);
        wmsMaterialToAttribute2.setAttributeNanme("批次号");
        wmsMaterialToAttribute2.setAttributeType_text("批属性2");
        list.add(wmsMaterialToAttribute2);
        WmsMaterialToAttribute wmsMaterialToAttribute3 = new WmsMaterialToAttribute();
        wmsMaterialToAttribute3.setAttributeType(3);
        wmsMaterialToAttribute3.setAttributeNanme("批次号");
        wmsMaterialToAttribute3.setAttributeType_text("批属性3");
        list.add(wmsMaterialToAttribute3);
        WmsMaterialToAttribute wmsMaterialToAttribute4 = new WmsMaterialToAttribute();
        wmsMaterialToAttribute4.setAttributeType(4);
        wmsMaterialToAttribute4.setAttributeNanme("批次号");
        wmsMaterialToAttribute4.setAttributeType_text("批属性4");
        list.add(wmsMaterialToAttribute4);
        WmsMaterialToAttribute wmsMaterialToAttribute5 = new WmsMaterialToAttribute();
        wmsMaterialToAttribute5.setAttributeType(5);
        wmsMaterialToAttribute5.setAttributeNanme("批次号");
        wmsMaterialToAttribute5.setAttributeType_text("批属性5");
        list.add(wmsMaterialToAttribute5);
        return list;
    }


    /**
     * @description 初始化批属性
     * @author  ciro
     * @date   2022/1/12 10:18
     * @param: materialId
     * @param: materialCode
     * @return: java.util.List<com.jayud.model.po.WmsMaterialToAttribute>
     **/
    private List<WmsMaterialToAttribute> initAttribute(long materialId,String materialCode){
        List<WmsMaterialToAttribute> list = new ArrayList<>();
        for (int i=0;i<4;i++){
            WmsMaterialToAttribute attribute = new WmsMaterialToAttribute();
            attribute.setMaterialBasicInfoId(materialId);
            attribute.setMaterialCode(materialCode);
            attribute.setAttributeType(i+1);
            list.add(attribute);
        }
        return list;
    }

    /**
     * @description 设置物料信息
     * @author  ciro
     * @date   2022/1/12 10:53
     * @param: attributeList
     * @param: materialId
     * @param: materialCode
     * @return: java.util.List<com.jayud.model.po.WmsMaterialToAttribute>
     **/
    private List<WmsMaterialToAttribute> setMaterialMsg(List<WmsMaterialToAttribute> attributeList,long materialId,String materialCode){
        attributeList.forEach(attribute -> {
            attribute.setMaterialBasicInfoId(materialId);
            attribute.setMaterialCode(materialCode);
        });
        return attributeList;
    }

}
