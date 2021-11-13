package ar.edu.mercadogratis.app.dao;

import ar.edu.mercadogratis.app.model.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("select p from Product p where p.seller = ?1 and p.status <> 'DELETED'")
    Iterable<Product> findBySeller(String seller);
}
