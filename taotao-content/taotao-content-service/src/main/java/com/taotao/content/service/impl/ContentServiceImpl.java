package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbItem;
import common.pojo.EasyUIDataGrideResult;
import common.pojo.EasyUITreeNode;
import common.pojo.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService{
    @Autowired
    private TbContentMapper tbContentMapper;

    @Override
    public EasyUIDataGrideResult getContentList(Long categoryId,int page, int rows) throws Exception {
        PageHelper.startPage(page,rows);
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> tbContents = tbContentMapper.selectByExample(tbContentExample);
        PageInfo<TbItem> pageInfo = new PageInfo(tbContents);
        EasyUIDataGrideResult result = new EasyUIDataGrideResult();
        result.setRows(tbContents);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public TaotaoResult addContent(TbContent tbContent) throws Exception {
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        tbContentMapper.insert(tbContent);
        return TaotaoResult.ok();
    }
}
