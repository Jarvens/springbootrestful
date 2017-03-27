package com.kunlun.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Map;

/**
 * Created by kunlun on 2017/3/24.
 */
@Controller
public class IndexController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Map<String, Object> model) {
        model.put("time", "这是我的第一个ftl页面");
        return "index";
    }
}
