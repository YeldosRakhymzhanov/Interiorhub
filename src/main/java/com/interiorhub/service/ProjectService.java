package com.interiorhub.service;

import com.interiorhub.entity.Project;
import com.interiorhub.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import com.interiorhub.dto.ProjectRequest;
import com.interiorhub.dto.ProjectResponse;
import com.interiorhub.exception.ProjectNotFoundException;
import com.interiorhub.repository.ProjectTranslationRepository;
import com.interiorhub.repository.ProjectImageRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectTranslationRepository projectTranslationRepository;
    private final ProjectImageRepository projectImageRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            ProjectTranslationRepository projectTranslationRepository,
            ProjectImageRepository projectImageRepository
    ) {
        this.projectRepository = projectRepository;
        this.projectTranslationRepository = projectTranslationRepository;
        this.projectImageRepository = projectImageRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public ProjectResponse createProject(ProjectRequest request) {

        Project project = new Project();

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setArea(request.getArea());
        project.setStyle(request.getStyle());
        project.setCoverImage(request.getCoverImage());

        Project savedProject = projectRepository.save(project);

        ProjectResponse response = new ProjectResponse();

        response.setId(savedProject.getId());
        response.setTitle(savedProject.getTitle());
        response.setDescription(savedProject.getDescription());
        response.setArea(savedProject.getArea());
        response.setStyle(savedProject.getStyle());
        response.setCoverImage(savedProject.getCoverImage());
        response.setCreatedAt(savedProject.getCreatedAt());

        return response;
    }
    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        ProjectResponse response = new ProjectResponse();

        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setArea(project.getArea());
        response.setStyle(project.getStyle());
        response.setCoverImage(project.getCoverImage());
        response.setCreatedAt(project.getCreatedAt());

        return response;
    }

    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setArea(request.getArea());
        project.setStyle(request.getStyle());
        project.setCoverImage(request.getCoverImage());

        Project updatedProject = projectRepository.save(project);

        ProjectResponse response = new ProjectResponse();
        response.setId(updatedProject.getId());
        response.setTitle(updatedProject.getTitle());
        response.setDescription(updatedProject.getDescription());
        response.setArea(updatedProject.getArea());
        response.setStyle(updatedProject.getStyle());
        response.setCoverImage(updatedProject.getCoverImage());
        response.setCreatedAt(updatedProject.getCreatedAt());

        return response;
    }


    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        projectTranslationRepository.deleteByProjectId(id);
        projectImageRepository.deleteByProjectId(id);

        projectRepository.delete(project);
    }


}