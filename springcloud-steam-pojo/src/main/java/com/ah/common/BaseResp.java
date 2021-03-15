package com.ah.common;

import lombok.Data;

@Data
public class BaseResp {
    private Integer code;
    private Object data;
    private String message;
    private Long total;
}
