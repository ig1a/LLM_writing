package com.book.chat.dto;

import com.book.chat.entity.User;
import lombok.Data;

/**
 * 用户信息DTO
 */
@Data
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
}
