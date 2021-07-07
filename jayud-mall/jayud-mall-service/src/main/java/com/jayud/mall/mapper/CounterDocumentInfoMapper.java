package com.jayud.mall.mapper;

import com.jayud.mall.model.po.CounterDocumentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.CounterDocumentInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * (提单)柜子对应-文件信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-24
 */
@Mapper
@Component
public interface CounterDocumentInfoMapper extends BaseMapper<CounterDocumentInfo> {

    /**
     * 根据id，查询文件
     * @param id 柜子文件id(counter_document_info id)
     * @return
     */
    CounterDocumentInfoVO findCounterDocumentInfoById(@Param("id") Long id);

    /**
     * 根据柜子id，查询文件
     * @param counterId
     * @return
     */
    List<CounterDocumentInfoVO> findCounterDocumentInfoByCounterId(@Param("counterId") Long counterId);
}
