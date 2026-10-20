
package com.interiorhub.service;

import com.interiorhub.dto.ProjectImageResponse;
import com.interiorhub.entity.Project;
import com.interiorhub.entity.ProjectImage;
import com.interiorhub.exception.ProjectNotFoundException;
import com.interiorhub.repository.ProjectImageRepository;
import com.interiorhub.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectImageService {

    private final ProjectImageRepository imageRepository;
    private final ProjectRepository projectRepository;

    public ProjectImageService(
            ProjectImageRepository imageRepository,
            ProjectRepository projectRepository) {
        this.imageRepository = imageRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public ProjectImageResponse addImage(
            Long projectId, String imageUrl, Integer displayOrder) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        ProjectImage image = new ProjectImage();
        image.setProject(project);
        image.setImageUrl(imageUrl);

        if (displayOrder == null) {
            displayOrder = imageRepository
                    .findByProjectIdOrderByDisplayOrderAsc(projectId)
                    .size() + 1;
        }

        image.setDisplayOrder(displayOrder);

        ProjectImage savedImage = imageRepository.save(image);
        return toResponse(savedImage);
    }

    @Transactional(readOnly = true)
    public List<ProjectImageResponse> getImages(Long projectId) {

        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }

        return imageRepository
                .findByProjectIdOrderByDisplayOrderAsc(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional
    public void deleteImage(Long projectId, Long imageId) {
        ProjectImage image = imageRepository.findById(imageId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Image not found: " + imageId));

        if (!image.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException(
                    "Image does not belong to project: " + projectId);
        }

        imageRepository.delete(image);
    }


    @Transactional
    public ProjectImageResponse updateImageOrder(
            Long projectId, Long imageId, Integer displayOrder) {

        if (displayOrder == null || displayOrder < 1) {
            throw new IllegalArgumentException(
                    "Display order must be at least 1");
        }

        ProjectImage image = imageRepository.findById(imageId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Image not found: " + imageId));

        if (!image.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException(
                    "Image does not belong to project: " + projectId);
        }

        image.setDisplayOrder(displayOrder);

        ProjectImage updatedImage = imageRepository.save(image);

        return toResponse(updatedImage);
    }

    private ProjectImageResponse toResponse(ProjectImage image) {
        ProjectImageResponse response = new ProjectImageResponse();
        response.setId(image.getId());
        response.setProjectId(image.getProject().getId());
        response.setImageUrl(image.getImageUrl());
        response.setDisplayOrder(image.getDisplayOrder());
        return response;
    }
}
