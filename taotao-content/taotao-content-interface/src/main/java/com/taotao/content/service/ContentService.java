package com.taotao.content.service;

import com.taotao.pojo.TbContent;
import common.pojo.EasyUIDataGrideResult;
import common.pojo.TaotaoResult;

import java.util.List;

public interface ContentService {
    public EasyUIDataGrideResult getContentList(Long categoryId,int page, int rows)throws Exception;
    public TaotaoResult addContent(TbContent tbContent) throws Exception;
}
