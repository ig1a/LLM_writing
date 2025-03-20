package com.book.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("t_user")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;            // 用户ID
    
    private String username;    // 用户名
    
    private String password;    // 密码(加密存储)
    
    private String nickname;    // 昵称
    
    private String avatar;      // 头像URL
    
    private String openId;      // 微信OpenID
    
    private Boolean enabled;    // 是否启用
    
    private LocalDateTime createTime;  // 创建时间
    
    private LocalDateTime updateTime;  // 更新时间
}
