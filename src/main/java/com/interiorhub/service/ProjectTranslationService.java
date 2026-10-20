
package com.interiorhub.service;

import com.interiorhub.entity.Project;
import com.interiorhub.entity.ProjectTranslation;
import com.interiorhub.exception.ProjectNotFoundException;
import com.interiorhub.repository.ProjectRepository;
import com.interiorhub.repository.ProjectTranslationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.interiorhub.dto.ProjectTranslationResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectTranslationService {

    private static final Set<String> SUPPORTED_LANGUAGES =
            Set.of("en", "ru", "kk");

    private final ProjectRepository projectRepository;
    private final ProjectTranslationRepository translationRepository;

    public ProjectTranslationService(
            ProjectRepository projectRepository,
            ProjectTranslationRepository translationRepository
    ) {
        this.projectRepository = projectRepository;
        this.translationRepository = translationRepository;
    }

    public List<ProjectTranslationResponse> getTranslations(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }

        return translationRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<ProjectTranslationResponse> getTranslation(
            Long projectId,
            String language
    ) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }

        validateLanguage(language);

        return translationRepository
                .findByProjectIdAndLanguage(projectId, language)
                .map(this::toResponse);
    }

    @Transactional
    public ProjectTranslationResponse createTranslation(
            Long projectId,
            String language,
            String title,
            String description,
            String style
    ) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        validateLanguage(language);

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (translationRepository
                .findByProjectIdAndLanguage(projectId, language)
                .isPresent()) {
            throw new IllegalArgumentException(
                    "Translation already exists for language: " + language
            );
        }

        ProjectTranslation translation = new ProjectTranslation();
        translation.setProject(project);
        translation.setLanguage(language);
        translation.setTitle(title);
        translation.setDescription(description);
        translation.setStyle(style);

        ProjectTranslation saved = translationRepository.save(translation);

        return toResponse(saved);
    }


    @Transactional
    public ProjectTranslationResponse updateTranslation(
            Long projectId,
            String language,
            String title,
            String description,
            String style
    ) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }

        validateLanguage(language);

        ProjectTranslation translation = translationRepository
                .findByProjectIdAndLanguage(projectId, language)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Translation not found for language: " + language
                ));

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        translation.setTitle(title);
        translation.setDescription(description);
        translation.setStyle(style);

        ProjectTranslation saved = translationRepository.save(translation);

        return toResponse(saved);
    }

    private void validateLanguage(String language) {
        if (language == null || !SUPPORTED_LANGUAGES.contains(language)) {
            throw new IllegalArgumentException(
                    "Language must be en, ru, or kk"
            );
        }
    }

    private ProjectTranslationResponse toResponse(
            ProjectTranslation translation
    ) {
        ProjectTranslationResponse response =
                new ProjectTranslationResponse();

        response.setId(translation.getId());
        response.setProjectId(translation.getProject().getId());
        response.setLanguage(translation.getLanguage());
        response.setTitle(translation.getTitle());
        response.setDescription(translation.getDescription());
        response.setStyle(translation.getStyle());

        return response;
    }
}
