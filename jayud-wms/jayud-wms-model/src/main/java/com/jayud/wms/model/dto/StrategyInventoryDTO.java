package com.jayud.wms.model.dto;

import com.jayud.wms.model.po.InventoryDetail;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ciro
 * @date 2022/1/17 16:39
 * @description:
 */
@Data
public class StrategyInventoryDTO extends InventoryDetail {

    private List<String> originDescList;

    private List<String> originAscList;

    /**
     * 降序
     */
    private List<String> descList;

    /**
     * 升序
     */
    private List<String> ascList;

    public void initOrigin(){
        this.originDescList = new ArrayList<>();
        this.originAscList = new ArrayList<>();
    }

    public void setOrigin(){
        this.descList = this.originDescList;
        this.ascList = this.originAscList;
    }


}
