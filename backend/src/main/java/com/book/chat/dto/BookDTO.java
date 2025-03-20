package com.book.chat.dto;

import com.book.chat.entity.Book;
import lombok.Data;

/**
 * 书籍信息DTO
 */
@Data
public class BookDTO {
    
    private Long id;           // 书籍ID
    
    private String title;      // 书名
    
    private String author;     // 作者
    
    private String publisher;  // 出版社
    
    private String isbn;       // ISBN编码
    
    private String cover;      // 封面图片URL
    
    private String description; // 描述
    
    /**
     * 从Book实体转换为BookDTO
     * @param book 书籍实体
     * @return 书籍DTO
     */
    public static BookDTO fromEntity(Book book) {
        if (book == null) {
            return null;
        }
        
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublisher(book.getPublisher());
        dto.setIsbn(book.getIsbn());
        dto.setCover(book.getCover());
        dto.setDescription(book.getDescription());
        
        return dto;
    }
}
