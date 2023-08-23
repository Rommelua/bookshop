package com.bookshop.repository;

import com.bookshop.model.Book;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(Long categoryId, Pageable pageable);

    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(@Nullable Specification<Book> spec, Pageable pageable);

    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(Pageable pageable);
}
