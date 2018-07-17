package com.taotao.sso.service.impl;

import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserService;
import common.pojo.TaotaoResult;
import common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${USER_SESSION}")
    private String  USER_SESSION;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    @Override
    public TaotaoResult checkUserData(String data, Integer type) throws Exception {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if(type==1){
            criteria.andUsernameEqualTo(data);
        }else if(type==2){
            criteria.andPhoneEqualTo(data);
        }else if(type==3){
            criteria.andEmailEqualTo(data);
        }else{
            TaotaoResult.build(400,"参数中包含非法数据");
        }

        List<TbUser> list =  tbUserMapper.selectByExample(example);
        if(null!=list&&list.size()>0){
            return TaotaoResult.ok(false);
        }
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult register(TbUser tbUser) throws Exception {
        //检查数据有效性
        if(StringUtils.isBlank(tbUser.getUsername())){
            TaotaoResult.build(400,"用户名不能为空");
        }
        //判断用户性是否重复
        TaotaoResult taotaoResult = this.checkUserData(tbUser.getUsername(),1);
        if(!(boolean)taotaoResult.getData()){
            TaotaoResult.build(400,"用户名重复");
        }
        //判断密码是否为空
        if(StringUtils.isBlank(tbUser.getPassword())){
            TaotaoResult.build(400,"密码不能为空");
        }
        if(StringUtils.isNotBlank(tbUser.getPhone())){
            //是否重复校验
            taotaoResult = checkUserData(tbUser.getPhone(),2);
            if(!(boolean)taotaoResult.getData()){
                TaotaoResult.build(400,"手机号重复");
            }
        }

        if(StringUtils.isNotBlank(tbUser.getEmail())){
            //是否重复校验
            taotaoResult = checkUserData(tbUser.getEmail(),3);
            if(!(boolean)taotaoResult.getData()){
                TaotaoResult.build(400,"email重复");
            }
        }
        //补全POJO属性
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        //密码要进行MD5加密
       String md5Pass =  DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
       tbUser.setPassword(md5Pass);
       tbUserMapper.insert(tbUser);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult login(String username, String password) throws Exception {
        TbUserExample example =  new TbUserExample();
        TbUserExample.Criteria criteria  = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if(null==list||list.size()==0){
            return TaotaoResult.build(400,"用户名或者密码错误");
        }
        TbUser user = list.get(0);
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            TaotaoResult.build(400,"用户名或者密码错误");
        }
        //生成token,写入redis
        String token = UUID.randomUUID().toString();
        user.setPassword(null);
        jedisClient.set(USER_SESSION + ":"+ token, JsonUtils.objectToJson(user));
        jedisClient.expire(USER_SESSION + ":" + token,SESSION_EXPIRE);
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) throws Exception {
        String json =jedisClient.get(USER_SESSION + ":"+ token);
        if(StringUtils.isBlank(json)){
            return TaotaoResult.build(400, "用户登录已经过期");
        }
        //重置Session的过期时间
        jedisClient.expire(USER_SESSION + ":"+ token,SESSION_EXPIRE);
        return TaotaoResult.ok(JsonUtils.jsonToPojo(json,TbUser.class));
    }
}
