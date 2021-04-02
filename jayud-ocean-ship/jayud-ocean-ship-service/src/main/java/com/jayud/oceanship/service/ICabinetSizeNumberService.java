package com.jayud.oceanship.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oceanship.po.CabinetSizeNumber;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oceanship.vo.CabinetSizeNumberVO;

import java.util.List;

/**
 * <p>
 * 柜型数量表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-17
 */
public interface ICabinetSizeNumberService extends IService<CabinetSizeNumber> {

    List<CabinetSizeNumberVO> getList(Long orderId);

    void deleteCabinet(QueryWrapper queryWrapper);
}
