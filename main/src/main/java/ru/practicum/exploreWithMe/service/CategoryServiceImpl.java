package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.exception.InvalidDataException;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.repository.CategoryRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category add(Category category) {
        log.info("---===>>>CATG_SERV added category /{}/", category);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        if (category.getName().isBlank()) {
            throw new InvalidDataException(HttpStatus.BAD_REQUEST);
        }
        log.info("---===>>>CATG_SERV updated category=/{}/", category);
        return categoryRepository.save(category);
    }

    @Override
    public Category getById(long id) {
        log.info("---===>>>CATG_SERV get category id=/{}/", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void remove(long id) {
        if (categoryRepository.findById(id).isPresent()) {
            log.info("deleted category id=/{}/", id);
            categoryRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }
}
