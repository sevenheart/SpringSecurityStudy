package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/product")
@Controller
public class ProductController {

    @RequestMapping(value = "/add")
    public String add(){
        return "product/add";
    }
    @RequestMapping(value = "/delete")
    public String delete(){
        return "/product/delete";
    }
    @RequestMapping(value = "/update")
    public String update(){
        return "product/update";
    }
    @RequestMapping(value = "/select")
    public String select(){
        return "product/select";
    }
}
