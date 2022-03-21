package com.jayud.auth.feign;


import com.jayud.auth.model.bo.CheckForm;
import com.jayud.auth.model.dto.SysUserDTO;
import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.model.po.SysUser;
import com.jayud.common.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * customs模块消费file模块的接口
 */
@Component
@FeignClient(name = "jayud-crm-web")
public interface CrmClient {

    /**
     * @description 根据用户id集合查询客户id
     * @author  ciro
     * @date   2022/3/21 11:16
     * @param: userIds
     * @return: com.jayud.common.BaseResult<java.util.List<java.lang.Long>>
     **/
    @PostMapping("/crmCustomerManager/selectCustIdListByUserIds")
    public BaseResult<List<Long>> selectCustIdListByUserIds(@RequestParam("userIds") List<Long> userIds);

}
