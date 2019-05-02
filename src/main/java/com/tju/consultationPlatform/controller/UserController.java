package com.tju.consultationPlatform.controller;

import com.tju.consultationPlatform.domain.JsonResult;
import com.tju.consultationPlatform.domain.User;
import com.tju.consultationPlatform.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@CrossOrigin
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${web.base.path}")
    private String basePath;
    private static Cookie cookie;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/SignOn", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult Login(@RequestBody Map<String,String> map,HttpServletRequest request,
                            HttpSession httpSession, HttpServletResponse response) {
        try {
            logger.info(map.get("username")+"  "+map.get("password"));
            User user = userService.userAuth(map.get("username"),map.get("password"));
            httpSession.setAttribute("currentUser", user); //把用户数据保存在session域对象中
            logger.info("login session:"+httpSession.getId());
            logger.info("login session:"+request.getSession().getId());
            response.addCookie(new Cookie("JSSESIONID",httpSession.getId()));
            //response.addCookie(cookie);
            logger.info("currentUser:"+httpSession.getAttribute("currentUser"));
            logger.info("httpSessionId:" + httpSession.getId());
            return new JsonResult(1,"success",user);
        }catch (Exception e){
           return new JsonResult(-1,e.getMessage(),null);
        }
    }

    @RequestMapping(value = "/SignOnWithCookie", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult LoginWithCookie(HttpSession httpSession) {
        try {
            User user = (User) httpSession.getAttribute("currentUser");
            logger.info("获取session的当前ID:" + user.getUserId());
            if (user == null) {
                return new JsonResult(0, "登陆失败", null);
            } else {

                return new JsonResult(1, "登陆成功", user);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return new JsonResult(0, "登陆失败", null);

    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult Register(@RequestBody User user) {
        try {
            return new JsonResult(1, "注册成功", userService.registerUser(user));
        }catch (Exception e){
            return new JsonResult(-1, e.getMessage(), null);
        }
    }



       /**
         * 用户上传头像接口，上传头像图片保存至本地
         * 同时将用户信息中头像地址修改成相对地址
         * @return JSONResult对象
         */




}
