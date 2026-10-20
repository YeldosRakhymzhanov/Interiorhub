
package com.interiorhub.controller;

import com.interiorhub.dto.ProjectTranslationRequest;
import com.interiorhub.dto.ProjectTranslationResponse;
import com.interiorhub.service.ProjectTranslationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/translations")
public class ProjectTranslationController {

    private final ProjectTranslationService translationService;

    public ProjectTranslationController(
            ProjectTranslationService translationService
    ) {
        this.translationService = translationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectTranslationResponse createTranslation(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectTranslationRequest request
    ) {
        return translationService.createTranslation(
                projectId,
                request.getLanguage(),
                request.getTitle(),
                request.getDescription(),
                request.getStyle()
        );
    }


    @PutMapping("/{language}")
    public ProjectTranslationResponse updateTranslation(
            @PathVariable Long projectId,
            @PathVariable String language,
            @Valid @RequestBody ProjectTranslationRequest request
    ) {
        if (!language.equals(request.getLanguage())) {
            throw new IllegalArgumentException(
                    "Language in URL must match language in request body"
            );
        }

        return translationService.updateTranslation(
                projectId,
                language,
                request.getTitle(),
                request.getDescription(),
                request.getStyle()
        );
    }

    @GetMapping
    public List<ProjectTranslationResponse> getTranslations(
            @PathVariable Long projectId
    ) {
        return translationService.getTranslations(projectId);
    }

    @GetMapping("/{language}")
    public ProjectTranslationResponse getTranslation(
            @PathVariable Long projectId,
            @PathVariable String language
    ) {
        return translationService.getTranslation(projectId, language)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Translation not found for language: " + language
                ));
    }
}
