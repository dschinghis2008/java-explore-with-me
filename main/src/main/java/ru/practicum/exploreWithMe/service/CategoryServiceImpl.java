package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.exception.InvalidDataException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.repository.CategoryRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category add(Category category) {
        log.info("---===>>>CATG_SERV added category /{}/", category);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Category category) {
        Category categUpd = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        categUpd.setName(category.getName());
        log.info("---===>>>CATG_SERV updated category=/{}/", category);
        return categUpd;
    }

    @Override
    public Category getById(long id) {
        log.info("---===>>>CATG_SERV get category id=/{}/", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Category> getAll(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return categoryRepository.getAll(from, size, pageable).getContent();
    }

    @Override
    @Transactional
    public void remove(long id) {
        if (categoryRepository.findById(id).isPresent()) {
            log.info("deleted category id=/{}/", id);
            categoryRepository.deleteById(id);
        } else {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }

    }
}
