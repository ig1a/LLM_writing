package com.book.chat.dto;

import com.book.chat.entity.User;

/**
 * 用户信息DTO
 */
public class UserDTO {
    
    private Long id;         // 用户ID
    
    private String username;  // 用户名
    
    private String nickname;  // 昵称
    
    private String avatar;    // 头像URL
    
    /**
     * 从User实体转换为UserDTO
     * @param user 用户实体
     * @return 用户DTO
     */
    public static UserDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setAvatar(user.getAvatar());
        
        return dto;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
