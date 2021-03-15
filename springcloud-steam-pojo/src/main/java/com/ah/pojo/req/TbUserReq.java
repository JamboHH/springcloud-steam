package com.ah.pojo.req;

import lombok.Data;

@Data
public class TbUserReq {
    private Integer id;
    private String userName;
    private String email;
    private String password;
    private String pic;
    private String code;
}
