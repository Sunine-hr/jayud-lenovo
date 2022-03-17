package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WarehouseProcessConf;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 仓库流程配置 服务类
 *
 * @author jyd
 * @since 2021-12-14
 */
public interface IWarehouseProcessConfService extends IService<WarehouseProcessConf> {

        /**
         *  分页查询
         * @param warehouseProcessConf
         * @param pageNo
         * @param pageSize
         * @param req
         * @return
         */
        IPage<WarehouseProcessConf> selectPage(WarehouseProcessConf warehouseProcessConf,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

        /**
         *  查询列表
         * @param warehouseProcessConf
         * @return
         */
        List<WarehouseProcessConf> selectList(WarehouseProcessConf warehouseProcessConf);

    List<WarehouseProcessConf> getByCondition(WarehouseProcessConf warehouseProcessConf);

    void initConfig(Long warehouseId);

}
