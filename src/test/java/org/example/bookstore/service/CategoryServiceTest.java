package org.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryDto;
import org.example.bookstore.dto.category.UpdateCategoryDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CategoryMapper;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.category.CategoryRepository;
import org.example.bookstore.service.impl.CategoryServiceImpl;
import org.example.bookstore.util.CategoryUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @DisplayName("""
            Verify save() method works correctly and returns correct CategoryDto
            with valid input data
            """)
    @Test
    public void save_WithValidCreateCategoryDto_ShouldReturnCorrectCategoryDto() {
        CreateCategoryDto createCategoryDto = CategoryUtil.createCreateFirstCategoryDto();
        Category category = CategoryUtil.createFirstCategory();
        CategoryDto categoryDto = CategoryUtil.createFirstCategoryDto();

        when(categoryMapper.toModel(createCategoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(createCategoryDto);

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).save(any());
        verify(categoryMapper, times(1)).toDto(any());
        verify(categoryMapper, times(1))
                .toModel(any(CreateCategoryDto.class));
    }

    @DisplayName("""
            Verify update() method works correctly and returns correct CategoryDto with valid
            input data
            """)
    @Test
    public void update_WithValidIdAndUpdateCategoryDto_ShouldReturnCorrectCategoryDto() {
        Category category = CategoryUtil.createFirstCategory();
        UpdateCategoryDto updateCategoryDto = CategoryUtil.createUpdateCategoryDto();

        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setId(category.getId());
        updatedCategoryDto.setName(updateCategoryDto.getName());
        updatedCategoryDto.setDescription(updateCategoryDto.getDescription());

        doReturn(Optional.of(category)).when(categoryRepository).findById(category.getId());
        doAnswer(invocationOnMock -> {
            UpdateCategoryDto invocationUpdateCategoryDto =
                    (UpdateCategoryDto) invocationOnMock.getArgument(0);
            Category invocationCategory = (Category) invocationOnMock.getArgument(1);

            invocationCategory.setName(invocationUpdateCategoryDto.getName());
            invocationCategory.setDescription(invocationUpdateCategoryDto.getDescription());

            return null;
        }).when(categoryMapper).updateCategoryFromRequestDto(updateCategoryDto, category);
        doReturn(category).when(categoryRepository).save(category);
        doReturn(updatedCategoryDto).when(categoryMapper).toDto(category);

        CategoryDto result = categoryService.update(category.getId(), updateCategoryDto);

        assertEquals(updatedCategoryDto, result);
        verify(categoryRepository, times(1)).findById(any());
        verify(categoryMapper, times(1))
                .updateCategoryFromRequestDto(any(), any());
        verify(categoryRepository, times(1)).save(any());
        verify(categoryMapper, times(1)).toDto(any());
    }

    @DisplayName("""
            Verify update() method works correctly and throws EntityNotFoundException if category
            with such id does not exist
            """)
    @Test
    public void update_WithNonExistingCategoryId_ShouldThrowException() {
        Category category = CategoryUtil.createFirstCategory();
        UpdateCategoryDto updateCategoryDto = CategoryUtil.createUpdateCategoryDto();

        doThrow(EntityNotFoundException.class).when(categoryRepository).findById(category.getId());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(category.getId(), updateCategoryDto));
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(categoryRepository, never()).save(any());
        verify(categoryMapper, never()).toDto(any());
        verify(categoryMapper, never()).updateCategoryFromRequestDto(any(), any());
    }

    @DisplayName("""
            Verify getById() method works correctly and returns correct CategoryDto with valid input
            data
            """)
    @Test
    public void getById_WithValidId_ShouldReturnCorrectCategoryDto() {
        Category category = CategoryUtil.createFirstCategory();
        CategoryDto categoryDto = CategoryUtil.createFirstCategoryDto();

        doReturn(Optional.of(category)).when(categoryRepository).findById(category.getId());
        doReturn(categoryDto).when(categoryMapper).toDto(category);

        CategoryDto result = categoryService.getById(category.getId());

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).findById(any());
        verify(categoryMapper, times(1)).toDto(any());
    }

    @DisplayName("""
            Verify getById() method works correctly and throws EntityNotFoundException if category
            with such id does not exist
            """)
    @Test
    public void getById_WithNonExistingCategoryId_ShouldThrowException() {
        Category category = CategoryUtil.createFirstCategory();

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(category.getId()));
        verify(categoryRepository, times(1)).findById(any());
        verify(categoryMapper, never()).toDto(any());
    }

    @DisplayName("""
            Verify findAll() method works correctly and returns correct List<CategoryDto> with
            valid entities in database
            """)
    @Test
    public void findAll_WithTwoCategoriesInDatabase_ShouldReturnListWithTwoCategories() {
        Category firstCategory = CategoryUtil.createFirstCategory();
        Category secondCategory = CategoryUtil.createSecondCategory();
        CategoryDto firstCategoryDto = CategoryUtil.createFirstCategoryDto();
        CategoryDto secondCategoryDto = CategoryUtil.createSecondCategoryDto();

        Page<Category> categoryPage = new PageImpl<>(List.of(firstCategory, secondCategory));
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(firstCategory)).thenReturn(firstCategoryDto);
        when(categoryMapper.toDto(secondCategory)).thenReturn(secondCategoryDto);

        List<CategoryDto> expected = List.of(firstCategoryDto, secondCategoryDto);

        List<CategoryDto> result = categoryService.findAll(pageable);

        assertEquals(expected, result);
        verify(categoryRepository, times(1)).findAll(any(Pageable.class));
        verify(categoryMapper, times(expected.size())).toDto(any());
    }

    @DisplayName("""
            Verify findAll() method works correctly and returns empty list with empty database
            """)
    @Test
    public void findAll_WithNoCategoriesInDatabase_ShouldReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        List<CategoryDto> expected = List.of();

        List<CategoryDto> result = categoryService.findAll(pageable);

        assertEquals(expected, result);
        verify(categoryRepository, times(1)).findAll(any(Pageable.class));
        verify(categoryMapper, never()).toDto(any());
    }
}
