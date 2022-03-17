package com.jayud.wms.service.impl;

import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthClient authClient;

    @Override
    public List<LinkedHashMap<String, Object>> queryDictByDictType(String dictCode) {
        BaseResult<List<SysDictItem>> dictResult = authClient.selectItemByDictCode(dictCode);
        List<LinkedHashMap<String, Object>> returnList = new ArrayList<>();
        dictResult.getResult().forEach(dict -> {
            LinkedHashMap<String, Object> maps = new LinkedHashMap<>();
            maps.put("name",dict.getItemText());
            maps.put("value",dict.getItemValue());
            returnList.add(maps);
        });
        return returnList;
    }
}
