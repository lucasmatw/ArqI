package ar.edu.mercadogratis.app.dao;

import ar.edu.mercadogratis.app.model.PurchaseProduct;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PurchaseProductRepository extends PagingAndSortingRepository<PurchaseProduct, Long> {
}
