package com.filmrental.controller;

import com.filmrental.model.dto.LanguageDTO;
import com.filmrental.model.entity.Language;
import com.filmrental.repository.LanguageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageController.class);

    @Autowired
    private LanguageRepository languageRepository;

    @GetMapping("/all")
    public ResponseEntity<PageResponse<LanguageDTO>> getAllLanguages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            LOGGER.info("Fetching languages with page: {}, size: {}", page, size);
            Pageable pageable = PageRequest.of(page, size);
            Page<Language> languages = languageRepository.findAll(pageable);
            PageResponse<LanguageDTO> response = new PageResponse<>();
            response.setContent(languages.getContent().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList()));
            response.setTotalPages(languages.getTotalPages());
            LOGGER.info("Found {} languages", response.getContent().size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error("Error fetching languages", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private LanguageDTO toDto(Language language) {
        LanguageDTO dto = new LanguageDTO();
        dto.setLanguageId(language.getLanguageId());
        dto.setName(language.getName());
        return dto;
    }

    public static class PageResponse<T> {
        private List<T> content;
        private int totalPages;

        public List<T> getContent() { return content; }
        public void setContent(List<T> content) { this.content = content; }
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    }
}