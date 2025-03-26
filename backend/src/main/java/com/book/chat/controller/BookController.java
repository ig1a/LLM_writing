package com.book.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 书籍相关API控制器
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    /**
     * 根据ISBN获取书籍信息
     * 目前使用硬编码的测试数据，后续可改为数据库查询
     */
    @GetMapping("/isbn")
    public ResponseEntity<?> getBookByISBN(@RequestParam String isbn) {
        // 打印请求信息
        System.out.println("收到获取书籍信息请求，ISBN: " + isbn);
        
        // 模拟书籍数据 - 后续可改为数据库查询
        Map<String, Object> bookInfo = new HashMap<>();
        
        // 根据不同的ISBN返回不同的书籍信息
        switch (isbn) {
            case "9787536692930":
                // 三体
                bookInfo.put("id", "1001");
                bookInfo.put("title", "三体");
                bookInfo.put("author", "刘慈欣");
                bookInfo.put("publisher", "重庆出版社");
                bookInfo.put("isbn", isbn);
                bookInfo.put("cover", "https://img3.doubanio.com/view/subject/l/public/s2768378.jpg");
                bookInfo.put("description", "文化大革命如火如荼进行的同时，军方探寻外星文明的绝秘计划\"红岸工程\"取得了突破性进展。但在按下发射键的那一刻，历经劫难的叶文洁没有意识到，她彻底改变了人类的命运。地球文明向宇宙发出的第一声啼鸣，以太阳为中心，以光速向宇宙深处飞驰...");
                break;
                
            case "9787506365437":
                // 活着
                bookInfo.put("id", "1002");
                bookInfo.put("title", "活着");
                bookInfo.put("author", "余华");
                bookInfo.put("publisher", "作家出版社");
                bookInfo.put("isbn", isbn);
                bookInfo.put("cover", "https://img2.doubanio.com/view/subject/l/public/s29053580.jpg");
                bookInfo.put("description", "《活着》是余华的代表作，讲述了一个人一生的故事，这是一个历经世间沧桑和磨难老人的人生感言，是一幕演绎人生苦难经历的戏剧。小说的叙事者\"我\"在去乡下收集民间歌谣的途中，遇到了一个爱讲故事的老人富贵，听他讲述了自己坎坷的人生经历。");
                break;
                
            case "9787508647357":
                // 人类简史
                bookInfo.put("id", "1003");
                bookInfo.put("title", "人类简史: 从动物到上帝");
                bookInfo.put("author", "[以色列] 尤瓦尔·赫拉利");
                bookInfo.put("publisher", "中信出版社");
                bookInfo.put("isbn", isbn);
                bookInfo.put("cover", "https://img9.doubanio.com/view/subject/l/public/s27814883.jpg");
                bookInfo.put("description", "\"人类简史\"是以色列新锐历史学家的一部重磅作品。从十万年前有生命迹象开始到21世纪资本、科技交织的人类发展史。挑战长久以来形成的关于人类进化的常识，这本书带领读者穿越过去，重新了解人类近几万年的发展历程。");
                break;
                
            default:
                // 默认书籍
                bookInfo.put("id", "1000");
                bookInfo.put("title", "未找到该书籍");
                bookInfo.put("author", "未知");
                bookInfo.put("publisher", "未知");
                bookInfo.put("isbn", isbn);
                bookInfo.put("cover", "/images/default_book.png");
                bookInfo.put("description", "抱歉，我们无法找到该ISBN对应的书籍信息。请确认ISBN是否正确，或者尝试其他书籍。");
                break;
        }
        
        System.out.println("返回书籍信息: " + bookInfo.get("title"));
        return ResponseEntity.ok(bookInfo);
    }
}
