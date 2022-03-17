package com.jayud.wms.model.dto.allocationStrategy;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ciro
 * @date 2022/1/18 10:53
 * @description: 分配策略DTO
 */
@Data
public class AllocationStrategyDTO {

    /**
     * 分配类型
     */
    private String allocationType;

    /**
     * 参数
     */
    private List<AllocationStrategyParamDTO> paramList;

    /**
     * 初始降序对象
     */
    private List<String> originDescList;

    /**
     * 初始升序对象
     */
    private List<String> originAscList;

    /**
     *  条件集合
     */
    private List<String> conditionList;

    public void initOrigin(){
        this.originDescList = new ArrayList<>();
        this.originAscList = new ArrayList<>();
        this.conditionList = new ArrayList<>();
    }


}
