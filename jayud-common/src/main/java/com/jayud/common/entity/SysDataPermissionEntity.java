package com.jayud.common.entity;

import lombok.Data;

import java.util.List;

/**
 * @author ciro
 * @date 2021/12/8 11:31
 * @description:    数据权限
 */
@Data
public class SysDataPermissionEntity {

    /**
     * 负责、所属部门
     */
    private List<String> orgIds;

    /**
     * 是否负责人
     */
    private Boolean isCharge;

    /**
     * 负责货主id集合
     */
    private List<String> owerIdList;

    /**
     * 负责仓库id集合
     */
    private List<String> warehouseIdList;


}
