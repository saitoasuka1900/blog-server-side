package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Label;
import com.example.demo.entity.User;
import com.example.demo.repository.LabelRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.ResultFactory;
import com.example.demo.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:8080")
@Controller
public class LabelController {
    private static final Logger log = LoggerFactory.getLogger(LabelController.class);

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;

    private final int name_length_limit = 5;

    public static class PostData {
        private Integer id;
        private String name;
        private String belong;
        private String rnd;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBelong() {
            return belong;
        }

        public void setBelong(String belong) {
            this.belong = belong;
        }

        public String getRnd() {
            return rnd;
        }

        public void setRnd(String rnd) {
            this.rnd = rnd;
        }
    }

    private boolean checkRnd(String username, String rnd) {
        List<User> users = userRepository.findByUsername(username);
        if (users.size() == 0)
            return false;
        return users.get(0).getRnd().equals(rnd);
    }

    private String setRnd(String username) {
        User user = userRepository.findByUsername(username).get(0);
        user.setRnd();
        userRepository.save(user);
        return user.getRnd();
    }

    @RequestMapping(value = "/api/manage/label/add", method = RequestMethod.POST)
    @ResponseBody
    private Result Add(@RequestBody PostData pData, @RequestAttribute("username") String username){
        JSONObject data = new JSONObject();
        if (checkRnd(username, pData.getRnd()) == false)
            return ResultFactory.buildAuthorizedFailedResult(data);
        if (pData.getName().length() > name_length_limit)
            return ResultFactory.buildFailedResult("标签名过长", data);
        List<Label> labels = labelRepository.findByNameAndBelong(pData.getName(), pData.getBelong());
        if (labels.size() > 0)
            return ResultFactory.buildFailedResult("标签名重复", data);
        Label label = new Label();
        label.setName(pData.getName());
        label.setCount(0);
        label.setBelong(pData.getBelong());
        labelRepository.save(label);
        label = labelRepository.findByNameAndBelong(label.getName(), label.getBelong()).get(0);
        data.put("id", label.getId());
        data.put("name", label.getName());
        data.put("count", label.getCount());
        data.put("rnd", setRnd(username));
        return ResultFactory.buildSuccessResult(data);
    }

    @RequestMapping(value = "/api/manage/label/del", method = RequestMethod.POST)
    @ResponseBody
    private Result Del(@RequestBody PostData pData, @RequestAttribute("username") String username){
        JSONObject data = new JSONObject();
        if (checkRnd(username, pData.getRnd()) == false)
            return ResultFactory.buildAuthorizedFailedResult(data);
        Optional<Label> labels = labelRepository.findById(pData.getId());
        if (labels.isPresent() == false)
            return ResultFactory.buildFailedResult("标签不存在", data);
        labelRepository.deleteById(pData.getId());
        data.put("rnd", setRnd(username));
        return ResultFactory.buildSuccessResult(data);
    }

    @RequestMapping(value = "/api/manage/label/edit", method = RequestMethod.POST)
    @ResponseBody
    private Result Edit(@RequestBody PostData pData, @RequestAttribute("username") String username){
        JSONObject data = new JSONObject();
        if (checkRnd(username, pData.getRnd()) == false)
            return ResultFactory.buildAuthorizedFailedResult(data);
        if (labelRepository.findByNameAndBelong(pData.getName(), pData.getBelong()).size() > 0)
            return ResultFactory.buildFailedResult("标签名已存在", data);
        if (pData.getName().length() > name_length_limit)
            return ResultFactory.buildFailedResult("标签名过长", data);
        Optional<Label> labels = labelRepository.findById(pData.getId());
        if (labels.isPresent() == false)
            return ResultFactory.buildFailedResult("标签不存在", data);
        Label label = new Label();
        label.setId(labels.get().getId());
        label.setName(pData.getName());
        label.setBelong(labels.get().getBelong());
        label.setCount(labels.get().getCount());
        labelRepository.save(label);
        log.info(label.toString());
        data.put("label_name", label.getName());
        data.put("rnd", setRnd(username));
        return ResultFactory.buildSuccessResult(data);
    }

    @RequestMapping(value = "/api/manage/label/get", method = RequestMethod.POST)
    @ResponseBody
    private Result Get(@RequestBody PostData pData) {
        log.info("find " + pData.getBelong());
        JSONObject data = new JSONObject();
        List<Label> Labels = labelRepository.findByBelong(pData.getBelong());
        JSONArray labels = new JSONArray();
        for (int i = 0; i < Labels.size(); ++i) {
            JSONObject label = new JSONObject();
            label.put("id", Labels.get(i).getId());
            label.put("name", Labels.get(i).getName());
            label.put("count", Labels.get(i).getCount());
            labels.add(label);
        }
        data.put("label_info", labels);
        return ResultFactory.buildSuccessResult(data);
    }
}
