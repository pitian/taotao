package com.taotao.controller;

import com.taotao.utils.FastDFSClient;
import common.utils.JsonUtils;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {

    //获取配置文件里的值
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String picUpload(MultipartFile uploadFile) {
        try{
            String originalFilename = uploadFile.getOriginalFilename();//获得原路径名称
            String  extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);//截取后缀名
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/client.conf");
            String path = fastDFSClient.uploadFile(uploadFile.getBytes(),extName);//group1/xx/xx/xxxxxxx
            String url =IMAGE_SERVER_URL + path;
            Map result = new HashMap();
            result.put("error",0);
            result.put("url",url);
            return JsonUtils.objectToJson(result);
        }catch (Exception e){
            e.printStackTrace();
            Map result = new HashMap();
            result.put("error",1);
            result.put("message","图片上传失败");
            return JsonUtils.objectToJson(result);
        }
    }
}
