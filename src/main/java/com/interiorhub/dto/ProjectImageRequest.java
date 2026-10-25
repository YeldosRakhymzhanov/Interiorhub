
package com.interiorhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProjectImageRequest {

    @NotBlank(message = "Image URL is required")
    @Size(max = 1000, message = "Image URL must not exceed 1000 characters")
    private String imageUrl;

    private Integer displayOrder;
    @Size(max = 200, message = "Image title must not exceed 200 characters")
    private String title;

    public ProjectImageRequest() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
