package com.example.demo.service;

import com.example.demo.util.PhpApiEncodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UidToUtokenService {

    private static final Logger logger = LoggerFactory.getLogger(UidToUtokenService.class);

    /**
     * 根据 uid 和 tokenKey 生成 utoken
     * @param uid 学员 uid
     * @param tokenKey 令牌密钥
     * @return utoken
     */
    public String convertUidToUtoken(String uid, String tokenKey) {
        try {
            System.out.println("Received uid: env: "+uid+":"+ tokenKey);
            String utoken = PhpApiEncodeUtil.encoded(uid, tokenKey);
            logger.info("生成的 utoken: {}", utoken);
            System.out.println("生成的 utoken: {}"+ utoken);
            return utoken;
        } catch (Exception e) {
            System.out.println("生成 utoken 时发生异常，uid: {}, tokenKey: {}"+uid+":"+ tokenKey+":"+ e);
            System.out.println(e.toString());
            logger.error("生成 utoken 时发生异常，uid: {}, tokenKey: {}", uid, tokenKey, e);
            return null;
        }
    }
}
