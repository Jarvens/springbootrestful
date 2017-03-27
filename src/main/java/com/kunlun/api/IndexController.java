package com.kunlun.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kunlun on 2017/3/24.
 */
@RestController
public class IndexController {
    @ApiOperation(value = "index测试",notes = "哈哈哈")
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(){
        return "Hello World";
    }
}
