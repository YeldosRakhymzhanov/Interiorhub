
package com.interiorhub.controller;

import com.interiorhub.dto.ProjectImageRequest;
import com.interiorhub.dto.ProjectImageResponse;
import com.interiorhub.service.ProjectImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/images")
public class ProjectImageController {

    private final ProjectImageService imageService;

    public ProjectImageController(ProjectImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectImageResponse addImage(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectImageRequest request) {

        return imageService.addImage(
                projectId,
                request.getImageUrl(),
                request.getDisplayOrder()
        );
    }

    @GetMapping
    public List<ProjectImageResponse> getImages(
            @PathVariable Long projectId) {

        return imageService.getImages(projectId);
    }


    @DeleteMapping("/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(
            @PathVariable Long projectId,
            @PathVariable Long imageId) {

        imageService.deleteImage(projectId, imageId);
    }


    @PatchMapping("/{imageId}/order")
    public ProjectImageResponse updateImageOrder(
            @PathVariable Long projectId,
            @PathVariable Long imageId,
            @RequestBody java.util.Map<String, Integer> request) {

        return imageService.updateImageOrder(
                projectId,
                imageId,
                request.get("displayOrder")
        );
    }

}
