package com.book.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 */
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
    
    // 手动添加getter和setter方法，解决Lombok可能未生效的问题
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
