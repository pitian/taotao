package com.taotao.content.service.impl;

import com.sun.javafx.beans.annotations.Default;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import common.pojo.EasyUITreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) throws Exception {
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> tbContentCategoryList = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        List<EasyUITreeNode> easyUITreeNodes = new ArrayList<EasyUITreeNode>();
        if(null!=null){
            for(int i=0;i<tbContentCategoryList.size();i++){
                EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
                easyUITreeNode.setState(String.valueOf(tbContentCategoryList.get(i).getStatus()));
                easyUITreeNode.setText(tbContentCategoryList.get(i).getName());
                easyUITreeNode.setId(tbContentCategoryList.get(i).getId());
                easyUITreeNodes.add(easyUITreeNode);
            }
        }
        return easyUITreeNodes;
    }
}
