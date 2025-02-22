package com.example.demo.service;

import com.example.demo.util.PhpApiEncodeUtil;
import org.springframework.stereotype.Service;

@Service
public class UidToUtokenService {
    /**
     * 根据uid生成utoken
     * @param uid 学员uid
     * @param tokenKey 加密密钥
     * @return utoken
     * @throws Exception 异常
     */
    public String convertUidToUtoken(String uid, String tokenKey) throws Exception {
        return PhpApiEncodeUtil.encoded(uid, tokenKey);
    }
}