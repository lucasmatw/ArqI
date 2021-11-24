package ar.edu.mercadogratis.app.dao;

import ar.edu.mercadogratis.app.model.PurchaseProduct;
import ar.edu.mercadogratis.app.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PurchaseProductRepository extends PagingAndSortingRepository<PurchaseProduct, Long> {
    @Query("select p from PurchaseProduct p where p.buyer = ?1")
    Iterable<PurchaseProduct> findByBuyer(User buyer);
}
