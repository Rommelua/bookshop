package com.bookshop.repository.specification.book;

import com.bookshop.model.Book;
import com.bookshop.repository.specification.SpecificationProvider;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceMaxSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FILTER_KEY = "priceMax";
    private static final String FIELD_NAME = "price";

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, cb) -> {
            Predicate predicate = cb.lessThanOrEqualTo(root.get(FIELD_NAME), params[0]);
            return cb.and(predicate);
        });
    }
}
