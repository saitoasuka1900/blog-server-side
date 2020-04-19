package com.example.demo.controller;

import java.util.List;

import com.example.demo.util.JwtUtil;
import com.example.demo.util.ResultFactory;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.User;
import com.example.demo.util.result.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.*;

@CrossOrigin("http://localhost:8080")
@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/api/Register", method = RequestMethod.POST)
    @ResponseBody
    public Result userRegister (@RequestBody User user) {
        user.setAnnounce("");
        List<User> users = userRepository.findByUsername(user.getUsername());
        JSONObject data = new JSONObject();
        if (users.size() > 0) {
            return ResultFactory.buildFailedResult("用户名重复", data);
        }
        user.setRnd();
        user.setIcode("");
        userRepository.save(user);
        log.info(user.toString() + " saved to the repo");
        String token = JwtUtil.createToken(user);
        log.info(token);
        data.put("token", token);
        data.put("rnd", user.getRnd());
        return ResultFactory.buildSuccessResult(data);
    }

    @RequestMapping(value = "/api/Login", method = RequestMethod.POST)
    public @ResponseBody Result userLogin (
            @RequestBody User user1, Model model) {
        List<User> users = userRepository.findByUsername(user1.getUsername());
        JSONObject data = new JSONObject();
        if (users.size() == 0) {
            log.warn("attempting to log in with the non-existed account");
            return ResultFactory.buildFailedResult("用户不存在", data);
        }
        else {
            User user = users.get(0);
            if (user.getPassword().equals(user1.getPassword())) {
                // 如果密码与用户名配对成功:
                model.addAttribute("name", user1.getUsername());
                log.info(user.toString()+ " logged in");
                String token = JwtUtil.createToken(user);
                data.put("token", token);
                user.setRnd();
                userRepository.save(user);
                data.put("rnd", user.getRnd());
                return ResultFactory.buildSuccessResult(data);
            } else {
                // 如果密码与用户名不匹配:
                model.addAttribute("name", "logging failed");
                log.warn(user.toString()+ " failed to log in");
                return ResultFactory.buildFailedResult("用户或密码错误", data);
            }
        }
    }

    @RequestMapping(value = "/api/manage/getUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result getUserInfo(@RequestAttribute("username") String username) {
        List<User> users = userRepository.findByUsername(username);
        JSONObject data = new JSONObject();
        User user = users.get(0);
        data.put("nickname", user.getNickname());
        data.put("username", user.getUsername());
        data.put("E_mail", user.getEmail());
        data.put("announce", user.getAnnounce());
        return ResultFactory.buildSuccessResult(data);
    }

    boolean verify(User user, User checker) {
        return user.getRnd().equals(checker.getRnd());
    }

    @RequestMapping(value = "/api/manage/submitNickName", method = RequestMethod.POST)
    @ResponseBody
    public Result submitNickName(@RequestBody User user, @RequestAttribute("username") String username) {
        List<User> users = userRepository.findByUsername(username);
        if (!verify(user, users.get(0)))
            return ResultFactory.buildFailedResult("验证失败", new JSONObject());
        String nickname = user.getNickname();
        user = users.get(0);
        user.setNickname(nickname);
        user.setRnd();
        userRepository.save(user);
        JSONObject data = new JSONObject();
        data.put("rnd", user.getRnd());
        data.put("nickname", user.getNickname());
        return ResultFactory.buildSuccessResult(data);
    }

    @RequestMapping(value = "/api/manage/submitAnnounce", method = RequestMethod.POST)
    @ResponseBody
    public Result submitAnnounce(@RequestBody User user, @RequestAttribute("username") String username) {
        List<User> users = userRepository.findByUsername(username);
        if (!verify(user, users.get(0)))
            return ResultFactory.buildFailedResult("验证失败", new JSONObject());
        String announce = user.getAnnounce();
        user = users.get(0);
        user.setAnnounce(announce);
        user.setRnd();
        userRepository.save(user);
        JSONObject data = new JSONObject();
        data.put("rnd", user.getRnd());
        data.put("announce", user.getAnnounce());
        return ResultFactory.buildSuccessResult(data);
    }
}
