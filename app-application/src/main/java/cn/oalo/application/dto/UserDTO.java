package cn.oalo.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户DTO
 */
@Data
@ApiModel(value = "用户信息", description = "用户信息")
public class UserDTO {
    
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long id;
    
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;
    
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;
    
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
    
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态：0-禁用，1-启用")
    private Integer status;
    
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
} 