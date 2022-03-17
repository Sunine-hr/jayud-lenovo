package com.jayud.wms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.WmsWaveToMaterial;
import com.jayud.wms.mapper.WmsWaveToMaterialMapper;
import com.jayud.wms.service.IWmsWaveToMaterialService;
import com.jayud.wms.model.vo.QueryScanInformationVO;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoToMaterialVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 波次单-物料信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-27
 */
@Service
public class WmsWaveToMaterialServiceImpl extends ServiceImpl<WmsWaveToMaterialMapper, WmsWaveToMaterial> implements IWmsWaveToMaterialService {


    @Autowired
    private WmsWaveToMaterialMapper wmsWaveToMaterialMapper;

    @Override
    public IPage<WmsWaveToMaterial> selectPage(WmsWaveToMaterial wmsWaveToMaterial,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsWaveToMaterial> page=new Page<WmsWaveToMaterial>(currentPage,pageSize);
        IPage<WmsWaveToMaterial> pageList= wmsWaveToMaterialMapper.pageList(page, wmsWaveToMaterial);
        return pageList;
    }

    @Override
    public List<WmsWaveToMaterial> selectList(WmsWaveToMaterial wmsWaveToMaterial){
        return wmsWaveToMaterialMapper.list(wmsWaveToMaterial);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsWaveToMaterialMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsWaveToMaterialForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsWaveToMaterialForExcel(paramMap);
    }

    @Override
    public List<QueryScanInformationVO> queryScanInformation(String orderNumber, String materialCode) {
        return this.baseMapper.queryScanInformation(orderNumber, materialCode);
    }

    @Override
    public List<WmsWaveToMaterial> getWmsWaveToMaterialByWaveNumber(String wareNumber) {
        QueryWrapper<WmsWaveToMaterial> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(WmsWaveToMaterial::getWaveNumber,wareNumber);
        queryWrapper.lambda().ne(WmsWaveToMaterial::getStatusType,4);
        queryWrapper.lambda().ne(WmsWaveToMaterial::getIsDeleted,0);
        return this.list(queryWrapper);
    }

    @Override
    public void addWaveToMaterial(List<WmsOutboundOrderInfoToMaterialVO> materialVOList, String waveOrderNumber) {
        List<WmsWaveToMaterial> materialList = new ArrayList<>();
        Map<String,List<WmsOutboundOrderInfoToMaterialVO>> listMap = new HashMap<>(16);
        //筛选出相同物料
        materialVOList.forEach(material -> {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(material);
            String keys = jsonObject.getString("materialCode")+"*"+
                    jsonObject.getString("batchCode")+"*"+
                    jsonObject.getString("materialProductionDate")+"*"+
                    jsonObject.getString("customField1")+"*"+
                    jsonObject.getString("customField2")+"*"+
                    jsonObject.getString("customField3")+"*";
            if (!listMap.containsKey(keys)){
                listMap.put(keys,new ArrayList<WmsOutboundOrderInfoToMaterialVO>());
            }
            List<WmsOutboundOrderInfoToMaterialVO> materialLists = listMap.get(keys);
            materialLists.add(material);
        });
        //物料集合
        listMap.forEach((k,v) ->{
            if (!v.isEmpty()) {
                BigDecimal accounts = new BigDecimal(0);
                for (int i=0;i<v.size();i++){
                    accounts = accounts.add(v.get(i).getRequirementAccount());
                }
                WmsWaveToMaterial wave = new WmsWaveToMaterial();
                BeanUtils.copyProperties(v.get(0), wave);
                wave.setId(null);
                wave.clearCreate();
                wave.clearUpdate();
                wave.setWaveNumber(waveOrderNumber);
                wave.setRequirementAccount(accounts);
                materialList.add(wave);
            }
        });
        this.saveBatch(materialList);
    }

}
