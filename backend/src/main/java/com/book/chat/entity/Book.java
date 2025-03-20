package com.book.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 书籍实体类
 */
@Data
@TableName("t_book")
public class Book {
    
    @TableId(type = IdType.AUTO)
    private Long id;            // 书籍ID
    
    private String title;       // 书名
    
    private String author;      // 作者
    
    private String publisher;   // 出版社
    
    private String isbn;        // ISBN编码
    
    private String cover;       // 封面图片URL
    
    private String description; // 描述
    
    private LocalDateTime createTime;  // 创建时间
    
    private LocalDateTime updateTime;  // 更新时间
}
