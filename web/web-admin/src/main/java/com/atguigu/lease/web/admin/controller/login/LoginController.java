package com.atguigu.lease.web.admin.controller.login;


import com.atguigu.lease.common.context.LoginUserContext;
import com.atguigu.lease.common.result.Result;
import com.atguigu.lease.model.entity.SystemUser;
import com.atguigu.lease.web.admin.service.LoginService;
import com.atguigu.lease.web.admin.vo.login.CaptchaVo;
import com.atguigu.lease.web.admin.vo.login.LoginVo;
import com.atguigu.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "后台管理系统登录管理")
@RestController
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private LoginService service;

    @Operation(summary = "获取图形验证码")
    @GetMapping("login/captcha")
    public Result<CaptchaVo> getCaptcha() {
        CaptchaVo captchaVo = service.getCaptcha();
        return Result.ok(captchaVo);
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String token = service.login(loginVo);
        return Result.ok(token);
    }

    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
    public Result<SystemUserInfoVo> info() {
        SystemUserInfoVo user = service.getSystemUserInfoById(LoginUserContext.getLoginUser().getUserId());
        return Result.ok(user);
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @Operation(summary = "获取今日土味情话")
    @PostMapping("tuweiJoke")
    @ResponseBody
    public Map tuweiJoke(@RequestBody SystemUser systemuser) {
        Map resMap = new HashMap();
        resMap.put("code", 2);
        resMap.put("content", "你知道我最喜欢喝什么吗？ 奶茶？ 不，是呵护你");
        System.out.println("===> systemuser is " + systemuser);
        System.out.println("===> tuweiJoke 执行！");
        return resMap;
    }

//    @CrossOrigin(origins = "http://localhost:5000")
//    @Operation(summary = "获取今日土味情话")
//    @GetMapping("tuweiJoke")
//    @ResponseBody
//    public Map tuweiJoke(@RequestParam String username, @RequestParam String phone) {
//        Map resMap = new HashMap();
//        resMap.put("code", 2);
//        resMap.put("content", "你知道我最喜欢喝什么吗？ 奶茶？ 不，是呵护你");
//        System.out.println("===> username is " + username);
//        System.out.println("===> phone is " + phone);
//        System.out.println("===> tuweiJoke 执行！");
//        return resMap;
//    }
}