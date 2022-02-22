package com.jayud.auth.model.bo;

import com.jayud.auth.model.po.SysTenant;
import com.jayud.auth.model.po.SysUrl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ciro
 * @date 2022/2/22 11:27
 * @description: 租户信息
 */
@Data
@ApiModel(value="多租户信息表对象", description="多租户信息表")
public class SysTenantForm extends SysTenant {

    @ApiModelProperty(value = "系统链接对象")
    List<SysUrl> sysUrlList;


}
