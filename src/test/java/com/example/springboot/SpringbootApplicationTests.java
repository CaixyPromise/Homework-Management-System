package com.example.springboot;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

//@SpringBootTest
class SpringbootApplicationTests {

    @Test
    void contextLoads() throws Exception {
        String res = HttpUtil.get("https://c.m.163.com/ug/api/wuhan/app/data/list-total?t=" + System.currentTimeMillis());
        JSON resJsonObj = JSONUtil.parse(res);
//        System.out.println(resJsonObj);
        JSONObject today = (JSONObject) JSONUtil.getByPath(resJsonObj, "data.chinaTotal.today");
        JSONObject total = (JSONObject) JSONUtil.getByPath(resJsonObj, "data.chinaTotal.total");
        System.out.println(today);
        System.out.println(total);
    }

}
