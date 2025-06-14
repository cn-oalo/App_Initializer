package cn.oalo.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 注册请求参数
 */
@Data
@ApiModel(value = "注册请求参数", description = "注册请求参数")
public class UserRegisterDTO {
    
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true, example = "zhangsan")
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,16}$", message = "用户名必须为4-16位字母、数字或下划线")
    private String username;
    
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_!@#$%^&*]{6,20}$", message = "密码必须为6-20位字母、数字或特殊字符")
    private String password;
    
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", required = true, example = "张三")
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;
    
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", example = "zhangsan@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;
} 