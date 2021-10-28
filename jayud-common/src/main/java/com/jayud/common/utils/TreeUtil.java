package com.jayud.common.utils;

import com.jayud.common.entity.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeUtil {

    /**
     * 递归创建树形结构
     */
    public static List<TreeNode> getTree(List<TreeNode> nodeList, Long parentId) {
        List<TreeNode> threeNodeList = new ArrayList<>();
        for (TreeNode entity : nodeList) {
            Long nodeId = entity.getId();
            Long nodeParentId = entity.getParentId();
            if (Objects.equals(parentId, nodeParentId)) {
                List<TreeNode> childList = getTree(nodeList, nodeId);
                if (childList.size() > 0) {
                    entity.setChildren(childList);
                }
                threeNodeList.add(entity);
            }
        }
        return threeNodeList;
    }
}
