package com.jayud.wms.service;

import java.util.LinkedHashMap;
import java.util.List;

public interface AuthService {

    /**
    * @description 根据字典编码获取数据
    * @author  ciro
    * @date   2022/3/16 21:33:58
    * @param dictCode
    * @return: List<LinkedHashMap<String,Object>>
    **/
    List<LinkedHashMap<String, Object>> queryDictByDictType(String dictCode);
    
}
