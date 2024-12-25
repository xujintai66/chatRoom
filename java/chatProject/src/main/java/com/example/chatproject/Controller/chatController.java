package com.example.chatproject.Controller;

import com.example.chatproject.entity.Message;
import com.example.chatproject.entity.register;
import com.example.chatproject.entity.user;
import com.example.chatproject.utils.GlobalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.chatproject.Mapper.chatMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173","http://43.139.233.117"})
public class chatController {
    @Autowired
    private chatMapper chatMapper;


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody register r1) {
        Map<String, Object> response = new HashMap<>();
        try {
            chatMapper.InsertUser(r1);
            response.put("code", HttpStatus.OK.value());
            response.put("message", "注册成功");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            // 如果捕获到主键重复（DuplicateKeyException）异常，说明账号已存在
            response.put("code", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "账号已存在");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "注册出现未知错误");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> query(@RequestBody user u1) {
        String account = u1.getAccount();
        String passWord = u1.getPassWord();
        Map<String, Object> response = new HashMap<>();
        register u2 = chatMapper.find(account, passWord);
        if (u2!= null) {
            response.put("code", HttpStatus.OK.value());
            response.put("message", "登录成功");
            response.put("users", u2);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("code", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "账号或密码错误");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/history")
    public List<String> getHis(){
        return GlobalData.getHistory();
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody register u1) {
        String username = u1.getUsername();
        String passWord = u1.getPassWord();
        String account = u1.getAccount();
        Map<String, Object> response = new HashMap<>();
        try {
            chatMapper.UpdateUser(passWord,username,account);
            response.put("code", HttpStatus.OK.value());
            response.put("message", "更改成功");
            System.out.println(111);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            response.put("code", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "昵称已存在");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "注册出现未知错误");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
