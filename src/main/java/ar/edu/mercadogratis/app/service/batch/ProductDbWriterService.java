package ar.edu.mercadogratis.app.service.batch;

import ar.edu.mercadogratis.app.model.Product;
import ar.edu.mercadogratis.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDbWriterService implements ItemWriter<Product> {

    private final ProductService productService;

    @Override
    public void write(List<? extends Product> list) {
        Iterable<Product> products = productService.saveAllProducts((List<Product>) list);
        log.debug("Saved products: " + products);
    }
}
