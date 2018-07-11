package com.taotao.item.listener;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class MyMessageListener implements MessageListener {
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${HTML_PATH_OUT}")
    private String HTML_PATH_OUT;
    @Override
    public void onMessage(Message message) {
        try{
            TextMessage textMessage = (TextMessage) message;
            String strId = textMessage.getText();
            long itemId = Long.parseLong(strId);
            TbItem tbItem = itemService.getItemById(itemId);
            Item item =new Item(tbItem);
            TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            Map data = new HashMap();
            data.put("item",item);
            data.put("itemDesc",tbItemDesc);
            Writer out = new FileWriter(new File(HTML_PATH_OUT + strId + ".html"));
            template.process(data,out);
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
