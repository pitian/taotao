package com.taotao.service;

import common.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    public List<EasyUITreeNode> getItemCatList(long parentId) throws  Exception;
}
