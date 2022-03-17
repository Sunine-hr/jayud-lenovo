package com.jayud.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 递归列表工具类
 */
public class RecursiveListUtils {

	private static final String PARENTID = "parentId";
	private static List<LinkedHashMap<String, Object>> totalList;

	private RecursiveListUtils() {

	}

	private static List<LinkedHashMap<String, Object>> getTotalList() {
		return totalList;
	}

	private static void setTotalList(List<LinkedHashMap<String, Object>> totalList) {
		RecursiveListUtils.totalList = totalList;
	}

	/**
	 * 获取根节点列表
	 *
	 * @param totalList 原始列表
	 * @return
	 */
	public static List<LinkedHashMap<String, Object>> queryRecursiveList(List<LinkedHashMap<String, Object>> totalList) {
		setTotalList(totalList);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> parentTotalList = totalList.stream().filter(map -> Long.valueOf(String.valueOf(map.get(PARENTID))) == 0L)
				.collect(Collectors.toList());
		for (int i = 0; i < parentTotalList.size(); i++) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			for (Entry<String, Object> entry : parentTotalList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("id")) {
					List<LinkedHashMap<String, Object>> data = queryRecursiveChildrenList(String.valueOf(value));
					if (!data.isEmpty()) {
						map.put("children", data);
					}
				}
				map.put(key, value);
			}
			resultList.add(map);
		}
		return resultList;
	}

	/**
	 * 根据ID获取递归子列表
	 *
	 * @param id 列表ID
	 * @return
	 */
	private static List<LinkedHashMap<String, Object>> queryRecursiveChildrenList(String id) {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> childrenTotalList = getTotalList().stream().filter(map -> id.equals(String.valueOf(map.get(PARENTID))))
				.collect(Collectors.toList());
		for (int i = 0; i < childrenTotalList.size(); i++) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			for (Entry<String, Object> entry : childrenTotalList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("id")) {
					List<LinkedHashMap<String, Object>> data = queryRecursiveChildrenList(String.valueOf(value));
					if (data != null && !data.isEmpty()) {
						map.put("children", data);
					}
				}
				map.put(key, value);
			}
			resultList.add(map);
		}
		return resultList;
	}

	/**
	 * 获取菜单的根节点列表
	 *
	 * @param totalList 原始列表
	 * @return
	 */
	public static List<LinkedHashMap<String, Object>> queryMenuRecursiveList(List<LinkedHashMap<String, Object>> totalList) {
		setTotalList(totalList);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> parentTotalList = totalList.stream().filter(map -> Long.valueOf(String.valueOf(map.get(PARENTID))) == 0L)
				.collect(Collectors.toList());
		for (int i = 0; i < parentTotalList.size(); i++) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			for (Entry<String, Object> entry : parentTotalList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("id")) {
					List<LinkedHashMap<String, Object>> data = queryMenuRecursiveChildrenList(String.valueOf(value));
					if (data != null && !data.isEmpty()) {
						map.put("routes", data);
					}
				}
				map.put(key, value);
			}
			resultList.add(map);
		}
		return resultList;
	}

	/**
	 * 根据ID获取菜单的递归子列表
	 *
	 * @param id 列表ID
	 * @return
	 */
	private static List<LinkedHashMap<String, Object>> queryMenuRecursiveChildrenList(String id) { // NOSONAR
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> childrenTotalList = getTotalList().stream().filter(map -> id.equals(String.valueOf(map.get(PARENTID))))
				.collect(Collectors.toList());
		for (int i = 0; i < childrenTotalList.size(); i++) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			for (Entry<String, Object> entry : childrenTotalList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("id")) {
					List<LinkedHashMap<String, Object>> data = queryMenuRecursiveChildrenList(String.valueOf(value));
					if (data != null && !data.isEmpty()) {
						map.put("routes", data);
					} else {
						map.put("exact", true);
					}
				}
				if (key.equals("path") && (String.valueOf(value).indexOf("http") != -1)) {
					map.put("target", "_blank");
				}
				map.put(key, value);
			}
			resultList.add(map);
		}
		return resultList;
	}

	/**
	 * 获取菜单和按钮的根节点列表
	 *
	 * @param totalList 原始列表
	 * @return
	 */
	public static List<LinkedHashMap<String, Object>> queryMenuButtonRecursiveList(List<LinkedHashMap<String, Object>> totalList,
			Map<String, String> menuButtonMap, Map<String, String> buttonDictMap) {
		setTotalList(totalList);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> parentTotalList = totalList.stream().filter(map -> Long.valueOf(String.valueOf(map.get(PARENTID))) == 0L)
				.collect(Collectors.toList());
		for (int i = 0; i < parentTotalList.size(); i++) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			for (Entry<String, Object> entry : parentTotalList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("id")) {
					List<LinkedHashMap<String, Object>> data = queryMenuButtonRecursiveChildrenList(String.valueOf(value), menuButtonMap, buttonDictMap);
					if (!data.isEmpty()) {
						map.put("children", data);
					}
				}
				map.put(key, value);
			}
			resultList.add(map);
		}
		return resultList;
	}

	/**
	 * 根据ID获取菜单和按钮的递归子列表
	 *
	 * @param id 列表ID
	 * @return
	 */
	private static List<LinkedHashMap<String, Object>> queryMenuButtonRecursiveChildrenList(String id, Map<String, String> menuButtonMap,
			Map<String, String> buttonDictMap) {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> childrenTotalList = getTotalList().stream().filter(map -> id.equals(String.valueOf(map.get(PARENTID))))
				.collect(Collectors.toList());
		for (int i = 0; i < childrenTotalList.size(); i++) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			String menuCode = null;
			for (Entry<String, Object> entry : childrenTotalList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("menuCode")) {
					menuCode = value.toString();
				}
				if (key.equals("id")) {
					List<LinkedHashMap<String, Object>> data = queryMenuButtonRecursiveChildrenList(String.valueOf(value), menuButtonMap, buttonDictMap);
					if (data != null && !data.isEmpty()) {
						map.put("children", data);
					} else {
						List<LinkedHashMap<String, Object>> buttonData = new ArrayList<>();

						if (menuButtonMap.get(menuCode) != null) {
							String[] buttonArray = menuButtonMap.get(menuCode).split(",");
							if (buttonArray != null) {
								for (int m = 0; m < buttonArray.length; m++) {
									LinkedHashMap<String, Object> buttonMap = new LinkedHashMap<>();
									buttonMap.put("key", menuCode + ":" + buttonArray[m]);
									buttonMap.put("title", buttonDictMap.get(buttonArray[m]));
									buttonData.add(buttonMap);
								}
							}
						}

						map.put("children", buttonData);
					}
				}
				map.put(key, value);
			}
			resultList.add(map);
		}
		return resultList;
	}

	/**
	 * 获取机构用户的根节点列表
	 *
	 * @param totalList 原始列表
	 * @return
	 */
	public static List<LinkedHashMap<String, Object>> queryOrgUserRecursiveList(List<LinkedHashMap<String, Object>> totalList, Map<Long, String> orgUserMap,
			Map<String, String> usernameNicknameMap) {
		setTotalList(totalList);
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> parentTotalList = totalList.stream().filter(map -> Long.valueOf(String.valueOf(map.get("pid"))) == 0L)
				.collect(Collectors.toList());
		for (int i = 0; i < parentTotalList.size(); i++) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			for (Entry<String, Object> entry : parentTotalList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("id")) {
					List<LinkedHashMap<String, Object>> data = queryOrgUserRecursiveChildrenList(String.valueOf(value), orgUserMap, usernameNicknameMap);
					if (!data.isEmpty()) {
						map.put("children", data);
					} else {
						map.put("children", Collections.emptyList());
					}
				}
				map.put(key, value);
			}
			resultList.add(map);
		}
		return resultList;
	}

	/**
	 * 根据ID获取机构用户的递归子列表
	 *
	 * @param id 列表ID
	 * @return
	 */
	private static List<LinkedHashMap<String, Object>> queryOrgUserRecursiveChildrenList(String id, Map<Long, String> orgUserMap,
			Map<String, String> usernameNicknameMap) {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> childrenTotalList = getTotalList().stream().filter(map -> id.equals(String.valueOf(map.get("pid"))))
				.collect(Collectors.toList());
		for (int i = 0; i < childrenTotalList.size(); i++) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			Long orgId = null;
			for (Entry<String, Object> entry : childrenTotalList.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("id")) {
					orgId = Long.valueOf(value.toString());
					List<LinkedHashMap<String, Object>> data = queryOrgUserRecursiveChildrenList(String.valueOf(value), orgUserMap, usernameNicknameMap);
					if (data != null && !data.isEmpty()) {
						if (StringUtils.isNotBlank(orgUserMap.get(orgId))) {
							String[] userArray = orgUserMap.get(orgId).split(",");
							if (userArray != null) {
								for (int m = 0; m < userArray.length; m++) {
									LinkedHashMap<String, Object> userMap = new LinkedHashMap<>();
									userMap.put("id", userArray[m]);
									userMap.put("key", userArray[m]);
									userMap.put("label", usernameNicknameMap.get(userArray[m]));
									userMap.put("children", Collections.emptyList());
									userMap.put("pid", orgId.toString());
									data.add(userMap);
								}
							}
						}

						map.put("children", data);
					} else {
						List<LinkedHashMap<String, Object>> userData = new ArrayList<>();
						if (StringUtils.isNotBlank(orgUserMap.get(orgId))) {
							String[] userArray = orgUserMap.get(orgId).split(",");
							if (userArray != null) {
								for (int m = 0; m < userArray.length; m++) {
									LinkedHashMap<String, Object> userMap = new LinkedHashMap<>();
									userMap.put("id", userArray[m]);
									userMap.put("key", userArray[m]);
									userMap.put("label", usernameNicknameMap.get(userArray[m]));
									userMap.put("children", Collections.emptyList());
									userMap.put("pid", orgId.toString());
									userData.add(userMap);
								}
							}
						}

						map.put("children", userData);
					}
				}
				map.put(key, value);
			}
			resultList.add(map);
		}
		return resultList;
	}

}
