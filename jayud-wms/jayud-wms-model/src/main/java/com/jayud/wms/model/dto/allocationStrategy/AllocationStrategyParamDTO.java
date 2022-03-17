package com.jayud.wms.model.dto.allocationStrategy;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ciro
 * @date 2022/1/18 10:50
 * @description: 分配策略参数DTO
 */
@Data
public class AllocationStrategyParamDTO {

    /**
     * 降序集合
     */
    private List<String> descList;

    /**
     * 升序集合
     */
    private List<String> ascList;

    /**
     *  条件集合
     */
    private List<String> conditionList;

    public void initList(){
        this.descList = new ArrayList<>();
        this.ascList = new ArrayList<>();
        this.conditionList = new ArrayList<>();
    }

}
