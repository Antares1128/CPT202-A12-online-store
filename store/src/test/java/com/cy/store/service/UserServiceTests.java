package com.cy.store.service;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
// @RunWith(SpringRunner.class)Annotation is a test launcher that can load Springboot test annotations

@SpringBootTest
@RunWith(SpringRunner.class)

public class UserServiceTests {
    @Autowired
    private IUserService userService;

    @Test
    public void reg() {
        try {
            User user = new User();
            user.setUsername("BaoXingYu03");
            user.setPassword("123");

            userService.reg(user);
            System.out.println("OK");
        } catch (ServiceException e) {
            //Get the object of the class, get the name of the class
            System.out.println(e.getClass().getSimpleName());
            //Get the specific description information of the exception
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void login() {
        User user = userService.login("123","123456");
        System.out.println(user);
    }



    @Test
    public void changePassword() {
        try {
        Integer uid = 24;
        String username = "lower";
        String oldPassword = "888888";
        String newPassword = "888888";
        userService.changePassword(uid, username, oldPassword, newPassword);
        System.out.println("Password reset complete!");
        } catch (ServiceException e) {
        System.out.println("Password change failed!" + e.getClass().getSimpleName());
        System.out.println(e.getMessage());
        }
    }

    @Test
    public void getByUid() {
        System.err.println(userService.getByUid(13));

    }

    @Test
    public void changeInfo() {
        try {
            Integer uid = 13;
            String username = "data admin";
            User user = new User();
            user.setPhone("15512328888");
            user.setEmail("admin03@cy.cn");
            user.setGender(2);
            userService.changeInfo(uid, username, user);
            System.out.println("OK.");
        } catch (ServiceException e) {
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
        }

    }

    @Test
    public void changeAvatar(){
        userService.changeAvatar(31, "/upload/test.png","小米");
    }

}
