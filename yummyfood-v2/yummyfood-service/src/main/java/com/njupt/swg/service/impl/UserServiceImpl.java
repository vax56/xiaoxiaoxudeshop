package com.njupt.swg.service.impl;

import com.njupt.swg.enums.Sex;
import com.njupt.swg.mapper.UsersMapper;
import com.njupt.swg.pojo.Users;
import com.njupt.swg.service.IUserService;
import com.njupt.swg.utils.DateUtil;
import com.njupt.swg.utils.MD5Utils;
import com.njupt.swg.bo.UserBO;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @Author swg.
 * @Date 2020/4/19 17:09
 * @CONTACT 317758022@qq.com
 * @DESC
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;

    @Override
    public boolean queryUsernameIsExist(String username) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        Users resUser = usersMapper.selectOneByExample(example);
        return resUser != null;
    }

    @Override
    public Users registNewUser(UserBO userBO) {
        Users user = new Users();
        user.setId(sid.nextShort());
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            log.error("创建用户-生成密码失败",e.getMessage());
        }
        user.setNickname(userBO.getUsername());
//        默认头像
        user.setFace("http://bloghello.oursnail.cn/avatar.png");
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        log.info("注册用的数据为：{}",user);
        usersMapper.insertSelective(user);
        return user;
    }
}
