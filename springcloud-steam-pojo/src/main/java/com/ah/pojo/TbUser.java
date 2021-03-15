package com.ah.pojo;

import lombok.Data;


@Data
public class TbUser {
    private Integer id;
    private String userName;
    private String email;
    private String password;
    private String pic;
}
