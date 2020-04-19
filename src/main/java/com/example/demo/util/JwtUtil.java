package com.example.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
设置当前类为一个bean对象
 */
@Component
public class JwtUtil {
    @Autowired
    private UserRepository userRepositoryTmp;
    private static UserRepository userRepository;
    /*
    设为static的时候使用autowired，注入的属性为null，无法使用
    需要把非static的autowired赋值给设为static的成员

    @PostConstruct注解作用：是Java EE 5引入的注解，
    Spring允许开发者在受管Bean中使用它。
    当DI容器实例化当前受管Bean时，@PostConstruct注解的方法会被自动触发，
    从而完成一些初始化工作。
     */
    @PostConstruct
    public void init() {
        userRepository = userRepositoryTmp;
    }
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    /*
    token密钥
     */
    private static final String text = "my_secret";
    private static String secret = "my_secret";
    /*
    过期时间:设置为一个月
     */
    private static final Long limit_time = 1000 * 60 * 60 * 24 * 30L;
    /*
    创建token
     */
    public static String createToken(User user) {
        Date nowDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + limit_time);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        /*
        使用用户密码作为密钥
         */
        secret = user.getPassword() + text;
        String token = JWT.create()
                .withHeader(map)
                /*
                用户的基本信息放入claims中
                 */
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("nickname", user.getNickname())
                .withExpiresAt(expireDate)
                .withIssuedAt(nowDate)
                .sign(Algorithm.HMAC256(secret));
        return user.getUsername() + ':' + token;
    }
    public static int getUsernamePos(String token) {
        int pos = 0;
        for (int i = 0; i < token.length(); ++i) {
            if (token.charAt(i) == ':') {
                pos = i;
                break;
            }
        }
        return pos;
    }

    /*
    解析并校验token
     */
    public static Map<String, Claim> verifyToken(String token) {
        int pos = getUsernamePos(token);
        List<User> users = userRepository.findByUsername(token.substring(0, pos));
        if (users.size() == 0) {
            logger.warn("token对应用户不存在");
            return null;
        }

        String password = users.get(0).getPassword();
        token = token.substring(pos + 1);
        DecodedJWT jwt = null;
        try {
            secret = password + text;
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            jwt = verifier.verify(token);
        }
        catch (Exception e) {
            // token超时（The Token has expired on Wed Apr 。。。。）
            // 以及其他错误都会抛出
            logger.warn(e.getMessage());
            logger.warn("token异常");
            return null;
        }
        return jwt.getClaims();
    }
}
