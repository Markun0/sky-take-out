package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
@ApiModel(description = "C端用户登录")
public class UserLoginDTO implements Serializable {

    @ApiModelProperty(value = "code")
    private String code;

}
