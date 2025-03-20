package com.book.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 */
@Data
@TableName("t_chat_message")
public class ChatMessage {
    
    @TableId(type = IdType.AUTO)
    private Long id;         // 消息ID
    
    private Long userId;     // 用户ID
    
    private Long bookId;     // 书籍ID
    
    private String content;  // 消息内容
    
    private String type;     // 消息类型: user-用户消息, ai-AI回复
    
    private LocalDateTime createTime;  // 创建时间
    
    // 消息类型常量
    public static final String TYPE_USER = "user";
    public static final String TYPE_AI = "ai";
}
