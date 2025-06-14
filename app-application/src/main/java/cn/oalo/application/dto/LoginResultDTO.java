package cn.oalo.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "登录结果", description = "登录结果")
public class LoginResultDTO {
    
    /**
     * 访问令牌
     */
    @ApiModelProperty(value = "访问令牌")
    private String token;
    
    /**
     * 令牌类型
     */
    @ApiModelProperty(value = "令牌类型")
    private String tokenType;
    
    /**
     * 过期时间（秒）
     */
    @ApiModelProperty(value = "过期时间（秒）")
    private Long expiresIn;
    
    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private UserDTO userInfo;
} 