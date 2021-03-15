package com.ah.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class GameDetails {

    private Integer id;

    private String video1;

    private String video2;

    private String pic1;

    private String pic2;

    private String pic3;

    private String pic4;

    private String pic5;

    private Integer gid;

    @JsonFormat(pattern = "yyyy年MM月dd日")
    @DateTimeFormat(pattern = "yyyy年MM月dd日")
    private Date time;

    private String developer;

}
