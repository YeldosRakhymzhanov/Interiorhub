
package com.interiorhub.repository;

import com.interiorhub.entity.ProjectTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectTranslationRepository
        extends JpaRepository<ProjectTranslation, Long> {

    List<ProjectTranslation> findByProjectId(Long projectId);

    Optional<ProjectTranslation> findByProjectIdAndLanguage(
            Long projectId,
            String language
    );
    void deleteByProjectId(Long projectId);
}
