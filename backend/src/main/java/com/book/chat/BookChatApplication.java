package com.book.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用程序入口类
 */
@SpringBootApplication
@MapperScan("com.book.chat.mapper")  // 扫描MyBatis Mapper接口
public class BookChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookChatApplication.class, args);
    }
}
