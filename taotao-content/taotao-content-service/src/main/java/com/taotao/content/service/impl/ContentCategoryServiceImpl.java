package com.taotao.content.service.impl;

import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import common.pojo.EasyUITreeNode;
import common.pojo.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
        for (TbContentCategory tbContentCategory : tbContentCategoryList) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            //添加到结果列表
            easyUITreeNodes.add(node);
        }
        return easyUITreeNodes;
    }

    public TaotaoResult addContentCategory(Long parentId,String name) throws Exception{
        //1.创建pojo对象
        TbContentCategory tbContentCategory = new TbContentCategory();

        tbContentCategory.setName(name);
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        tbContentCategoryMapper.insert(tbContentCategory);
        //判断父节点的状态
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            //如果父节点为叶子节点应该改为父节点
            parent.setIsParent(true);
            //更新父节点
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        return TaotaoResult.ok(tbContentCategory);
    }

    @Override
    public TaotaoResult updateaContentCategory(Long id, String name) throws Exception {
        TbContentCategory tbContentCategory  = tbContentCategoryMapper.selectByPrimaryKey(id);
        tbContentCategory.setName(name);
        tbContentCategory.setUpdated(new Date());
        tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContentCategory(Long id) throws Exception {
        //如果是父节点，递归删除
        TbContentCategory tbContentCategory  = tbContentCategoryMapper.selectByPrimaryKey(id);
        if(null!=tbContentCategory){
            //TODO 递归待实现
            if(tbContentCategory.getIsParent()){
                //父节点，递归删除
                TbContentCategoryExample example = new TbContentCategoryExample();
                TbContentCategoryExample.Criteria criteria = example.createCriteria();
                criteria.andParentIdEqualTo(id);
                List<TbContentCategory> tbContentCategoryList = tbContentCategoryMapper.selectByExample(example);
                for(TbContentCategory contentCategory:tbContentCategoryList){
                    if(contentCategory.getIsParent()){
                        deleteContentCategory(contentCategory.getId());
                    }
                    tbContentCategoryMapper.deleteByPrimaryKey(contentCategory.getId());
                }
            }else {
                tbContentCategoryMapper.deleteByPrimaryKey(id);
            }
        }
        return TaotaoResult.ok();
    }
}
