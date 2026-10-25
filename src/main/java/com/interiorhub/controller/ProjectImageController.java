
package com.interiorhub.controller;

import com.interiorhub.dto.ProjectImageRequest;
import com.interiorhub.dto.ProjectImageResponse;
import com.interiorhub.service.ProjectImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.interiorhub.service.ImageStorageService;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/images")
public class ProjectImageController {

    private final ProjectImageService imageService;
    private final ImageStorageService imageStorageService;

    public ProjectImageController(
            ProjectImageService imageService,
            ImageStorageService imageStorageService) {
        this.imageService = imageService;
        this.imageStorageService = imageStorageService;
    }



    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectImageResponse uploadImage(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "displayOrder", required = false)
            Integer displayOrder,
            @RequestParam(value = "title", required = false)
            String title) {

        String imageUrl = imageStorageService.store(file);

        return imageService.addImage(
                projectId,
                imageUrl,
                displayOrder,
                title
        );
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectImageResponse addImage(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectImageRequest request) {

        return imageService.addImage(
                projectId,
                request.getImageUrl(),
                request.getDisplayOrder(),
                request.getTitle()
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

    @PatchMapping("/{imageId}/title")
    public ProjectImageResponse updateImageTitle(
            @PathVariable Long projectId,
            @PathVariable Long imageId,
            @RequestBody java.util.Map<String, String> request) {

        return imageService.updateImageTitle(
                projectId,
                imageId,
                request.get("title")
        );
    }


}
