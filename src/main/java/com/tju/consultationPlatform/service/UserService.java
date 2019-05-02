package com.tju.consultationPlatform.service;

import com.tju.consultationPlatform.domain.User;
import com.tju.consultationPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user)throws Exception {
        // 检查用户名是否注册，一般在前端验证的时候处理，因为注册不存在高并发的情况，这里再加一层查询是不影响性能的
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new Exception("用户名已存在");
        }
        User newuser = userRepository.save(user);
        if(newuser==null){
            throw new Exception("注册失败，请再试一次，谢谢");
        }else {
            return newuser;
        }

        // 注册成功后选择发送邮件激活。现在一般都是短信验证码
    }

    public User getUserById(String id) {
        return userRepository.findByUserId(id);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public boolean updateAvatar(String userId,String url)throws Exception{
        try {
            userRepository.updateAvatarUrl(userId,url);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("头像上传出错");
        }

    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    //用户登录验证
    public User userAuth(String userName, String userPassword) throws Exception{

            User user = userRepository.findByUsername(userName);
            if (user != null) {
                if (user.getPassword().equals(userPassword)) {
                    user.setUserIsOnline(1);
                    return user;
                } else
                    throw new Exception("用户名或者密码错误");
            } else {
                throw new Exception("用户不存在");
            }
    }
}
