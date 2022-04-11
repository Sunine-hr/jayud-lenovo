package com.jayud.wms.fegin;


import com.jayud.auth.model.po.SysDictItem;
import com.jayud.auth.model.po.SysUserToWarehouse;
import com.jayud.common.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * customs模块消费file模块的接口
 */
@Component
@FeignClient(name = "jayud-auth-web")
public interface AuthClient {


    @GetMapping(value = "/user/getOwerIdByUserId")
    List<String> getOwerIdByUserId(@RequestParam(name = "userId", required = false) String userId);


    @GetMapping(value = "/user/getWarehouseIdByUserId")
    List<String> getWarehouseIdByUserId(@RequestParam(name = "userId", required = false) String userId);


    /**
     * 自定义单号
     */
    @PostMapping(value = "/bNoRule/api/getOrderFeign")
    public BaseResult getOrderFeign(@RequestParam(name = "code") String code, @RequestParam(name = "date") Date date);

    /**
    * @description 获取字典项
    * @author  ciro
    * @date   2022/3/16 21:30:13
    * @param dictCode
    * @return: com.jayud.common.BaseResult<java.util.List<SysDictItem>>
    **/
    @GetMapping(value = "/sysDictItem/selectItemByDictCode")
    public BaseResult<List<SysDictItem>> selectItemByDictCode(@RequestParam("dictCode") String dictCode);


    /**
     * 自定义单号
     */
    @PostMapping(value = "/bNoRule/getOrder")
    public BaseResult<String> getOrder(@RequestParam(name = "code") String code, @RequestParam(name = "date") Date date);


    /**
     * 字段表查询
     */
    @PostMapping(value = "/sysDictItem/api/selectItemByDictCode")
    public BaseResult selectItemByDictCodeFeign(@RequestParam("dictCode") String dictCode);

    /**
     * @description 根据用户查询仓库
     * @author  ciro
     * @date   2022/4/9 14:21
     * @param: userId
     * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysUserToWarehouse>>
     **/
    @GetMapping(value = "/sysUserToWarehouse/selectList")
    public BaseResult<List<SysUserToWarehouse>> getWarehouseByUserId(@RequestParam("userId") Long userId);

    /**
     * @description 获取编码
     * @author  ciro
     * @date   2022/4/11 16:26
     * @param: code
     * @return: com.jayud.common.BaseResult
     **/
    @GetMapping(value = "/bNoRule/getOrder")
    public BaseResult getOrderNumber(@RequestParam(name = "code") String code);
}
