package com.taotao.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;


public class FreeMarker {
    @Test
    public void testFreeMarker()throws Exception{
        //1.创建一个模板文件
        //2.创建一个 Configuration 对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //3.设置模板所在的路径

        configuration.setDirectoryForTemplateLoading(new File("F:/taotao/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
        //4.设置模板的字符集
        configuration.setDefaultEncoding("utf-8");
        //5.使用 Configuration 对象加载一个模板文件,需要指定模板文件的文件名
        Template template = configuration.getTemplate("student.ftl");
        //6.创建一个数据集 可以是POJO，也可以是Map 推荐使用Map
        Map data = new HashMap<>();
        data.put("hello","hello freemarker");
        Student student =new Student(1,"pitian",21,"南京");
        data.put("student",student);

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"pitian",21,"南京"));
        studentList.add(new Student(2,"pitian2",22,"南京"));
        studentList.add(new Student(3,"pitian3",23,"南京"));
        studentList.add(new Student(4,"pitian4",24,"南京"));
        studentList.add(new Student(5,"pitian5",25,"南京"));
        studentList.add(new Student(6,"pitian6",26,"南京"));
        studentList.add(new Student(6,"pitian7",27,"南京"));
        data.put("stuList",studentList);
//日期类型的处理
        data.put("date",new Date());
        data.put("val","123");

        //7.创建一个Writer对象指定输出文件的路径及文件名
        Writer out = new FileWriter(new File("F:/PITIAN/freemarker/student.html"));
        //8.使用模板对象的process 方法输出文件
        template.process(data,out);
        //9.关闭流
        out.close();

    }
}
