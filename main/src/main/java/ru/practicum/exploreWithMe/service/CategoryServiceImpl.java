package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.exception.ConflictException;
import ru.practicum.exploreWithMe.exception.InvalidDataException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.repository.CategoryRepository;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category add(Category category) {
        if (category.getName() == null) {
            throw new InvalidDataException(HttpStatus.BAD_REQUEST);
        }
        if (categoryRepository.getCountByName(category.getName()) > 0) {
            throw new ConflictException(HttpStatus.CONFLICT);
        }
        log.info("---===>>>CATG_SERV added category /{}/", category);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Category category) {
        if (category.getId() == null) {
            throw new InvalidDataException(HttpStatus.BAD_REQUEST);
        }
        Category updCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        log.info("---===>>>CATG_SERV try updated category=/{}/,upd=/{}/", category, updCategory);
        if (!category.getName().isEmpty()) {
            if (categoryRepository.getCountByName(category.getName()) > 0) {
                throw new ConflictException(HttpStatus.CONFLICT);
            }
            updCategory.setName(category.getName());
        }
        categoryRepository.save(updCategory);
        log.info("---===>>>CATG_SERV updated category=/{}/", updCategory);
        return updCategory;
    }

    @Override
    public Category getById(Integer id) {
        log.info("---===>>>CATG_SERV get category id=/{}/", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Collection<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void remove(Integer id) {
        if (categoryRepository.findById(id).isPresent()) {
            log.info("deleted category id=/{}/", id);
            categoryRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }
}
