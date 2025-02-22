package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.util.Base64;
import java.util.Random;

@Slf4j
public class PhpApiEncodeUtil {
	/**
	 * TOKEN的过期时间
	 */
	private final static long U_TOKEN_EXPIRE = 31536000L;

	private final static int CONSTANT_LEN = 30;

	/**
	 * Description：根据uid生成token并返回
	 *
	 * @param uid：学员uid
	 * @return：老入学诊断的token
	 * @throws Exception
	 */
	public static String encoded(String uid, String tokenKey) throws Exception {
		/** 第一步：组加密json串 **/
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("uid", uid);
		jsonObject.put("expire", String.valueOf(U_TOKEN_EXPIRE));

		/** 第二步：生成随机向量 **/
		byte[] iv = new byte[16];
		Random random = new Random();
		random.nextBytes(iv);

		/** 第三步：初始化加密对象Cipher **/
		byte[] key = tokenKey.getBytes();
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), ivParameterSpec);

		/** 第四步：进行加密 **/
		/** 特殊处理：由于PHP和Java加解密有不同之处，需要进行特殊处理 **/
		int varLength =  CONSTANT_LEN + uid.length();
		String encryptValue = new String(Base64.getEncoder().encode(cipher.doFinal(("s:" + varLength + ":\"" + jsonObject.toString() + "\";").getBytes())));

		/** 第五步：再次组装json串（带向量） **/
		JSONObject sourceData = new JSONObject();
		sourceData.put("iv", new String(Base64.getEncoder().encode(iv)));
		sourceData.put("value", encryptValue);

		/** 第六步：对json串进行Base64转码并转为String对象返回 **/
		return new String(Base64.getEncoder().encode(sourceData.toString().getBytes()));
	}

	/**
	 * Description：对utoken进行解密并返回json字符串
	 *
	 * @param encodeString
	 * @return
	 * @throws Exception
	 */
	public static String decode(String encodeString, String tokenKey) throws Exception {
		log.info("tokenKey is "+ tokenKey);
		/** 第一步：对整个串进行Base64解码,解码之后是json串 **/
		String decodeString = new String(Base64.getDecoder().decode(encodeString.getBytes()));

		/** 第二步：从json串中获取加密想想 **/
		JSONObject jsonObject = JSONObject.fromObject(decodeString);
		/** 需要对json串中的向量进行Base64解码 **/
		byte[] ivBytes = Base64.getDecoder().decode(jsonObject.getString("iv").getBytes());

		/** 第二步：初始化解密对象 **/
		byte[] key = tokenKey.getBytes();
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
		AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
		params.init(ivParameterSpec);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, params);

		/** 第三步：获取解码的内容并进行解密 **/
		byte[] content = Base64.getDecoder().decode(jsonObject.getString("value").getBytes());
		String rawContent = new String(cipher.doFinal(content));
		int startLocation = rawContent.indexOf("{");
		int lastLocation = rawContent.lastIndexOf("}");

		return rawContent.substring(startLocation, lastLocation+1);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(encoded("1021357870", "tal-chengjiukehu"));
	}
}