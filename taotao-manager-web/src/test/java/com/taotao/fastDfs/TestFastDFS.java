package com.taotao.fastDfs;

import org.csource.fastdfs.*;
import org.junit.Test;

public class TestFastDFS {
    @Test
    public void uploadFile() throws Exception{
        //1.项目工程中添加jar包.中央仓库没有fastdfs的jar包.可以导入项目 fastdfs-cliet
        //2.创建配置文件。Tracker服务器的地址
        //3.加载配置文件
        ClientGlobal.init("F:/taotao/taotao-manager-web/src/main/resources/resource/client.conf");
        //4.创建一个TrackerClient 对象
        TrackerClient trackerClient = new TrackerClient();
        //5.获得TrackerServer 对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //6.创建一个storageServer 的引用
        StorageServer storageServer = null;
        //7.创建一个storageClient 对象。需要trackerServer,StorageServer参数
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
        //8.时候storageClient 来上传文件
        String [] strings = storageClient.upload_file("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg","jpg",null);
        for (String string: strings) {
            System.out.println(string);
        }
    }
}
