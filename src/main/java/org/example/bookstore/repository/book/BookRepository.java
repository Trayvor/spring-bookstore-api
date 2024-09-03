package org.example.bookstore.repository.book;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book>,
        PagingAndSortingRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN FETCH b.categories c"
            + " WHERE c.id = :id AND b.isDeleted = false")
    List<Book> findAllByCategoriesId(Long id);

    @Query("SELECT b FROM Book b JOIN FETCH b.categories c"
            + " WHERE b.id = :id AND b.isDeleted = false")
    Optional<Book> findById(Long id);

    @Query("SELECT b FROM Book b JOIN FETCH b.categories c WHERE b.isDeleted = false")
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "categories")
    List<Book> findAll(Specification<Book> bookSpecification);

}
