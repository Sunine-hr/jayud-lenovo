package com.jayud.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建分页信息
 */
public class PaginationBuilder {

	private PaginationBuilder() {

	}

	public static Map<String, Object> buildResult(List<LinkedHashMap<String, Object>> resultList, long total, Integer currentPage, Integer pageSize) {
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("list", resultList);
		LinkedHashMap<String, Long> paginationMap = new LinkedHashMap<>();
		paginationMap.put("total", total);
		paginationMap.put("pageSize", (long) pageSize);
		paginationMap.put("current", (long) currentPage);
		resultMap.put("pagination", paginationMap);
		return resultMap;
	}

	public static Map<String, Object> buildObjectResult(List<?> resultList, long total, Integer currentPage, Integer pageSize) {
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("list", resultList);
		LinkedHashMap<String, Long> paginationMap = new LinkedHashMap<>();
		paginationMap.put("total", total);
		paginationMap.put("pageSize", (long) pageSize);
		paginationMap.put("current", (long) currentPage);
		resultMap.put("pagination", paginationMap);
		return resultMap;
	}

	/**
	 * @description mybatis-plus下page转ListPageRuslt
	 * @author  ciro
	 * @date   2021/12/15 17:47
	 * @param: page
	 * @return: com.jyd.component.commons.result.ListPageRuslt
	 **/
	public static ListPageRuslt buildPageResult(IPage page){
		ListPageRuslt listPageRuslt = new ListPageRuslt();
		listPageRuslt.setRecords(page.getRecords());
		listPageRuslt.setSize(page.getSize());
		listPageRuslt.setTotal(page.getTotal());
		listPageRuslt.setCurrent(page.getCurrent());
		listPageRuslt.setPages(page.getPages());
		return listPageRuslt;
	}



}
