package com.ah.pojo;


import lombok.Data;

import java.io.Serializable;

@Data
public class GameOrder{

    private Integer id;

    private String orderid;

    private String gamename;

    private String pic;

    private String price;

    private Integer num;

    private String status;

    private Integer uid;

    private Integer gid;

}
