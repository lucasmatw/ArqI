package ar.edu.mercadogratis.app.dao.specification;

import ar.edu.mercadogratis.app.model.Product;
import ar.edu.mercadogratis.app.model.ProductStatus;
import ar.edu.mercadogratis.app.model.SearchProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private static final String NAME = "name";
    private static final String CATEGORY = "category";
    private static final String PRICE = "price";
    private static final String STATUS = "status";
    private static final String STOCK = "stock";

    private final SearchProductRequest searchProductRequest;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get(STATUS), ProductStatus.ACTIVE));

        searchProductRequest.getName().ifPresent(name ->
                predicates.add(criteriaBuilder.like(root.get(NAME), "%" + name + "%")));
        searchProductRequest.getCategory().ifPresent(category ->
                predicates.add(criteriaBuilder.equal(root.get(CATEGORY), category)));
        searchProductRequest.getMinPrice().ifPresent(minPrice ->
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE), minPrice)));
        searchProductRequest.getMaxPrice().ifPresent(maxPrice ->
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(PRICE), maxPrice)));

        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(STOCK), searchProductRequest.getMinStock()));

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
