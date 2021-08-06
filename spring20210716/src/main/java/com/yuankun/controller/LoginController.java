package com.yuankun.controller;

import com.google.code.kaptcha.Producer;
import com.yuankun.conf.CaptchaConf;
import com.yuankun.pojo.User;
import com.yuankun.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {
    @Autowired
    private Producer producer;
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/captcha")
    public Result ProduceCaptcha() throws IOException {
        String key= UUID.randomUUID().toString();
        String code= producer.createText();
        /*key="12345";
        code="12345";*/
        //System.out.println("---------------"+code);
        //redisTemplate.opsForValue().set(key,code,120L);
        redisTemplate.opsForValue().set(key,code,120, TimeUnit.SECONDS);
        BufferedImage image=producer.createImage(code);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",byteArrayOutputStream);

        BASE64Encoder base64Encoder=new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String Base64Image=str+base64Encoder.encode(byteArrayOutputStream.toByteArray());
        Map<String,Object> map=new HashMap<>();
        map.put("key",key);
        map.put("Base64Image",Base64Image);
        Result result=new Result();
        result.setStateCode(200);
        result.setObject(map);
        result.setMsg("生成成功");
        return result;

    }
}
