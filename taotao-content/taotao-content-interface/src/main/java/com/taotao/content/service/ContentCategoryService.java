package com.taotao.content.service;

import common.pojo.EasyUITreeNode;

import java.util.List;

public interface ContentCategoryService {
    public List<EasyUITreeNode> getContentCategoryList(long parentId) throws Exception;
}
