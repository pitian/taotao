package com.taotao.search.controller;

import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 搜索服务controller
 */
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;
    @RequestMapping("/search")
    public String  search(@RequestParam("q") String queryString , @RequestParam(defaultValue = "1") Integer page,Model model){
        //调用服务,执行查询
        try {
            //get乱码解决
            queryString = new String(queryString.getBytes("iso8859-1"),"utf-8");
            SearchResult result = searchService.search(queryString,page,SEARCH_RESULT_ROWS);
            //把结果传递给页面
            model.addAttribute("query",queryString);
            model.addAttribute("totalPages",result.getTotalPage());
            model.addAttribute("itemList",result.getSearchItemList());
            model.addAttribute("page",page);
        }catch (Exception e){
            e.printStackTrace();
        }

        return  "search";
    }

}
