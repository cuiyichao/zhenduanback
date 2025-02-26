package com.example.demo.controller;

import com.example.demo.service.UidToUtokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UidToUtokenController {

    private static final Logger logger = LoggerFactory.getLogger(UidToUtokenController.class);

    @Autowired
    private UidToUtokenService uidToUtokenService;

    /**
     * 根据uid生成utoken的接口
     * @param uid 学员uid
     * @param env 环境信息
     * @return utoken
     * @throws Exception 异常
     */
    @GetMapping("/convertUidToUtoken")
    public String convertUidToUtoken(@RequestParam String uid, @RequestParam String env) throws Exception {
        logger.info("Received uid: {}, env: {}", uid, env);
        String tokenKey;
        if ("0".equals(env)) {
            // 测试环境的 tokenKey
            tokenKey = "1234567890abcdef";
        } else {
            // 线上环境的 tokenKey
            tokenKey = "tal-chengjiukehu";
        }
        return uidToUtokenService.convertUidToUtoken(uid, tokenKey);
    }
}
