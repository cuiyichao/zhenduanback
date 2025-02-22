package com.example.demo.controller;

import com.example.demo.service.UidToUtokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UidToUtokenController {

    @Autowired
    private UidToUtokenService uidToUtokenService;

    /**
     * 根据uid生成utoken的接口
     * @param uid 学员uid
     * @param tokenKey 加密密钥
     * @return utoken
     * @throws Exception 异常
     */
    @GetMapping("/convertUidToUtoken")
    public String convertUidToUtoken(@RequestParam String uid, @RequestParam String tokenKey) throws Exception {
        return uidToUtokenService.convertUidToUtoken(uid, tokenKey);
    }
}