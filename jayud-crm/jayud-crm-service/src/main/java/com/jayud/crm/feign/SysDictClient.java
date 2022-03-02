package com.jayud.crm.feign;

import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ciro
 * @date 2022/3/2 11:27
 * @description: 字典查询
 */
@Component

@FeignClient(name = "jayud-auth-web" )
public interface SysDictClient {


    @GetMapping(value = "/sysDictItem/selectItemByDictCode")
    public BaseResult<List<SysDictItem>> selectItemByDictCode(@RequestParam("dictCode") String dictCode);

}
