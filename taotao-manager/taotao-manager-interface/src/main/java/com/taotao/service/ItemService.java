package com.taotao.service;

import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import common.pojo.EasyUIDataGrideResult;
import common.pojo.TaotaoResult;

public interface ItemService {
    public TbItem getItemById(long itemId) throws  Exception;
    public EasyUIDataGrideResult getItemList(int page,int rows)throws  Exception;
    public TaotaoResult addItem(TbItem tbItem,String desc)throws  Exception;
    public TbItemDesc getItemDescById(long itemId) throws  Exception;
}
