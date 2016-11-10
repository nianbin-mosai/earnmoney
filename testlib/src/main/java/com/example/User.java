package com.example;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年11月10日 15:49
 * 邮箱：nianbin@mosainet.com
 */
public class User {
    public int id;
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
