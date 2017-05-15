package com.oureda.thunder.pobooks.Data;

/**
 * Created by thunder on 17-5-15.
 */

public class User
{
    public Data data;
    private String email;
    private String password;
    public Status status;
    private String verify;

    public String getEmail()
    {
        return this.email;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getVerify()
    {
        return this.verify;
    }

    public void setEmail(String paramString)
    {
        this.email = paramString;
    }

    public void setPassword(String paramString)
    {
        this.password = paramString;
    }

    public void setVerify(String paramString)
    {
        this.verify = paramString;
    }

    public class Data
    {
        private String avatar;
        private String favourite;
        private String fullintroduction;
        private String nickname;
        private String receive_address;
        private boolean sex;

        public Data() {}

        public String getAvatar()
        {
            return this.avatar;
        }

        public String getFavourite()
        {
            return this.favourite;
        }

        public String getFullintroduction()
        {
            return this.fullintroduction;
        }

        public String getNickname()
        {
            return this.nickname;
        }

        public String getReceive_address()
        {
            return this.receive_address;
        }

        public boolean isSex()
        {
            return this.sex;
        }
    }
    public class Status
    {
        int code;
        String msg;

        public Status() {}

        public int getCode()
        {
            return this.code;
        }

        public String getMessage()
        {
            return this.msg;
        }
    }
}
