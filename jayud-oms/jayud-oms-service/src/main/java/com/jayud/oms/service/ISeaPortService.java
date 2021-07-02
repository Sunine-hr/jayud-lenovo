package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddSeaPortForm;
import com.jayud.oms.model.bo.QuerySeaPortForm;
import com.jayud.oms.model.po.SeaPort;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.SeaPortVO;

import java.util.List;

/**
 * <p>
 * 船港口地址表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-06-30
 */
public interface ISeaPortService extends IService<SeaPort> {

    IPage<SeaPortVO> findByPage(QuerySeaPortForm form);

    SeaPort isCodeExistence(String code);

    SeaPort isNameExistence(String name);

    boolean saveOrUpdateSeaPort(AddSeaPortForm form);

    boolean deleteSeaPort(Long id);

    boolean saveOrUpdateBatchSeaPort(List<AddSeaPortForm> list);

}
