package com.book.chat.dto;

import com.book.chat.entity.Book;

/**
 * 书籍信息DTO
 */
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
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
