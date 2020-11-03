package com.lhc.note;

import org.junit.Test;

import java.util.Optional;

/**
 * @program: sauron
 * @description:
 * @author: hjt
 * @create: 2019-07-29 11:46
 **/

public class OptionalTest {
    @Test
    public void test(){
        User user = new User();
        user.setAge(10);
        user.setName("z");
        String paramOpt = Optional.ofNullable(user).map(user1 -> user1.getName()).orElse("参数错误");

    }

    public static class User{
        private int age;
        private String name;
        private String password;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
