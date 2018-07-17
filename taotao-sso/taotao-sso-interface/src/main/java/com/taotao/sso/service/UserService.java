package com.taotao.sso.service;

import com.taotao.pojo.TbUser;
import common.pojo.TaotaoResult;

public interface UserService {
    TaotaoResult checkUserData(String data,Integer type)throws Exception;

    TaotaoResult register(TbUser tbUser) throws Exception;

    TaotaoResult login(String username,String password) throws Exception;

    TaotaoResult getUserByToken(String token)throws Exception;

}
