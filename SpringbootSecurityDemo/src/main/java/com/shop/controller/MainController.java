package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/index")
    public String indexPage(){
        return  "index";
    }

    @RequestMapping("/403")
    //403错误页处理
    public String forbidden(){
        return "403";
    }

}
