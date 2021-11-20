package ar.edu.mercadogratis.app.batch;

import ar.edu.mercadogratis.app.model.Product;
import ar.edu.mercadogratis.app.model.ProductStatus;
import org.springframework.batch.item.ItemProcessor;

public class ProductItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(Product product) {
        return product.toBuilder()
                .status(ProductStatus.ACTIVE)
                .build();
    }
}
