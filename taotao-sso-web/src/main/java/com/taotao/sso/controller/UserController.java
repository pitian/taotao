package com.taotao.sso.controller;

import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import common.pojo.TaotaoResult;
import common.utils.CookieUtils;
import common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkUserData(@PathVariable String param,@PathVariable Integer type)throws Exception{
       TaotaoResult result =  userService.checkUserData(param,type);
        return result;
    }

    @RequestMapping(value="/user/register", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser tbUser)throws Exception{
        TaotaoResult result = userService.register(tbUser);
        return result;
    }

    @RequestMapping("/user/login")
    @ResponseBody
    public TaotaoResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) throws Exception{
        TaotaoResult result = userService.login(username,password);
        //登录成功后写cookie
        if(result.getStatus().equals("200")){
            CookieUtils.setCookie(request,response,TOKEN_KEY,result.getData().toString());
        }
        return result;
    }
    /*@RequestMapping("/user/token/{token}")
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback)throws Exception{
        TaotaoResult result = userService.getUserByToken(token);
        //js跨域解决，js可以跨域加载js文件
        //判断是否为jsonp请求
        if(StringUtils.isNotBlank(callback)){
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }*/
    //jsonp的第二种方法，spring4.1以上版本使用
    @RequestMapping("/user/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callback)throws Exception{
        TaotaoResult result = userService.getUserByToken(token);
        //js跨域解决，js可以跨域加载js文件
        if(StringUtils.isNotBlank(callback)){
            MappingJacksonValue mappingJacksonValue =new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }
}
