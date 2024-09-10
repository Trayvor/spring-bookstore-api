package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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
            Verify save() method works correctly and returns correct CategoryDto with valid input data
            """)
    @Test
    public void save_WithValidCreateCategoryDto_ShouldReturnCorrectCategoryDto() {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Test name");
        createCategoryDto.setDescription("test description");

        Category category = new Category();
        category.setName(createCategoryDto.getName());
        category.setDescription(createCategoryDto.getDescription());

        Long categoryId = 1L;

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        categoryDto.setName(createCategoryDto.getName());
        categoryDto.setDescription(createCategoryDto.getDescription());

        when(categoryMapper.toModel(createCategoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(createCategoryDto);

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
        verify(categoryMapper, times(1)).toModel(createCategoryDto);
    }

    @DisplayName("""
            Verify update() method works correctly and returns correct CategoryDto with valid
            input data
            """)
    @Test
    public void update_WithValidIdAndUpdateCategoryDto_ShouldReturnCorrectCategoryDto() {
        Long categoryId = 1L;

        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto();
        updateCategoryDto.setName("Updated Name");
        updateCategoryDto.setDescription("Updated Description");

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Initial Name");
        category.setDescription("Initial Description");

        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setId(categoryId);
        updatedCategoryDto.setName(updateCategoryDto.getName());
        updatedCategoryDto.setDescription(updateCategoryDto.getDescription());

        doReturn(Optional.of(category)).when(categoryRepository).findById(categoryId);
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

        CategoryDto result = categoryService.update(categoryId, updateCategoryDto);

        assertEquals(updatedCategoryDto, result);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1))
                .updateCategoryFromRequestDto(updateCategoryDto, category);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @DisplayName("""
            Verify update() method works correctly and throws EntityNotFoundException if category
            with such id does not exist
            """)
    @Test
    public void update_WithNonExistingCategoryId_ShouldThrowException() {
        Long categoryId = 1L;

        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto();
        updateCategoryDto.setName("Updated Name");
        updateCategoryDto.setDescription("Updated Description");

        doThrow(EntityNotFoundException.class).when(categoryRepository).findById(categoryId);

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(categoryId, updateCategoryDto));
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @DisplayName("""
            Verify getById() method works correctly and returns correct CategoryDto with valid input
            data
            """)
    @Test
    public void getById_WithValidId_ShouldReturnCorrectCategoryDto() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Test Name");
        category.setDescription("Test Description");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        doReturn(Optional.of(category)).when(categoryRepository).findById(categoryId);
        doReturn(categoryDto).when(categoryMapper).toDto(category);

        CategoryDto result = categoryService.getById(categoryId);

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @DisplayName("""
            Verify getById() method works correctly and throws EntityNotFoundException if category
            with such id does not exist
            """)
    @Test
    public void getById_WithNonExistingCategoryId_ShouldThrowException() {
        Long categoryId = 1L;

        doThrow(EntityNotFoundException.class).when(categoryRepository).findById(categoryId);

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(categoryId));
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @DisplayName("""
            Verify findAll() method works correctly and returns correct List<CategoryDto> with
            valid entities in database
            """)
    @Test
    public void findAll_WithTwoCategoriesInDatabase_ShouldReturnListWithTwoCategories() {
        Category firstCategory = new Category();
        firstCategory.setId(1L);
        firstCategory.setName("First Category");
        firstCategory.setDescription("First Category Description");

        Category secondCategory = new Category();
        secondCategory.setId(2L);
        secondCategory.setName("Second Category");
        secondCategory.setDescription("Second Category Description");

        CategoryDto firstCategoryDto = new CategoryDto();
        firstCategoryDto.setId(firstCategory.getId());
        firstCategoryDto.setName(firstCategory.getName());
        firstCategoryDto.setDescription(firstCategory.getDescription());

        CategoryDto secondCategoryDto = new CategoryDto();
        secondCategoryDto.setId(secondCategory.getId());
        secondCategoryDto.setName(secondCategory.getName());
        secondCategoryDto.setDescription(secondCategory.getDescription());

        Page<Category> categoryPage = new PageImpl<>(List.of(firstCategory, secondCategory));
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(firstCategory)).thenReturn(firstCategoryDto);
        when(categoryMapper.toDto(secondCategory)).thenReturn(secondCategoryDto);

        List<CategoryDto> result = categoryService.findAll(pageable);

        List<CategoryDto> expected = List.of(firstCategoryDto, secondCategoryDto);
        assertEquals(expected, result);
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(firstCategory);
        verify(categoryMapper, times(1)).toDto(secondCategory);
    }

    @DisplayName("""
            Verify findAll() method works correctly and returns empty list with empty database
            """)
    @Test
    public void findAll_WithNoCategoriesInDatabase_ShouldReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        List<CategoryDto> result = categoryService.findAll(pageable);

        List<CategoryDto> expected = List.of();
        assertEquals(expected, result);
        verify(categoryRepository, times(1)).findAll(pageable);
    }

}
