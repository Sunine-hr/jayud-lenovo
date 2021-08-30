package com.jayud.customs.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.customs.model.bo.QueryCustomsDeclarationFiling;
import com.jayud.customs.model.po.CustomsDeclarationFiling;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.customs.model.vo.CustomsDeclarationFilingVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 报关归档 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-08-24
 */
public interface CustomsDeclarationFilingMapper extends BaseMapper<CustomsDeclarationFiling> {

    Integer getSerialNumber(@Param("filingDate") String filingDate,
                            @Param("bizModel") Integer bizModel,
                            @Param("imAndExType") Integer imAndExType);

    IPage<CustomsDeclarationFilingVO> findByPage(@Param("page") Page<CustomsDeclarationFilingVO> page,
                                                 @Param("form") QueryCustomsDeclarationFiling form);
}
