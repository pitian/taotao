package com.taotao.content.service;

import com.taotao.pojo.TbContentCategory;
import common.pojo.EasyUITreeNode;
import common.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {
    public List<EasyUITreeNode> getContentCategoryList(long parentId) throws Exception;
    public TaotaoResult addContentCategory(Long parentId,String name) throws Exception;
    public TaotaoResult updateaContentCategory(Long id,String name)throws Exception;
    public TaotaoResult deleteContentCategory(Long id) throws Exception;
}
