package com.jayud.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TreeNode {

    private Long id;

    private Long parentId;

    private String value;

    private String label;

    private List<TreeNode> children;


}
