package ebook6.features.review;


import java.util.UUID;

public class ReviewRequest {
    private UUID userId;
    private UUID ebookId;
    private String content;
    private int rating;
    
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public UUID getEbookId() {
        return ebookId;
    }
    
    public void setEbookId(UUID ebookId) {
        this.ebookId = ebookId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
}
