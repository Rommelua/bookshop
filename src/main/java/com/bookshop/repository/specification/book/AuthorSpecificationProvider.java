package com.bookshop.repository.specification.book;

import com.bookshop.model.Book;
import com.bookshop.repository.specification.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FILTER_KEY = "authorIn";
    private static final String FIELD_NAME = "author";

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(FIELD_NAME)
                .in(Arrays.stream(params).toArray());
    }
}
