package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户与客户权限关联表实体类
 */
@Data
public class SysUserOwerPermission extends SysBaseEntity {


	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 货主id
	 */
	private String owerId;
	/**
	 *租户编码
	 */
	private String tenantCode;


	/**
	 * 是否选中
	 */
	@TableField(exist = false)
	private Boolean isSelected;

	@ApiModelProperty(value = "是否删除")
	@TableLogic
	private Boolean isDeleted;
}
