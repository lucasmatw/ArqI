package ar.edu.mercadogratis.app.service;

import ar.edu.mercadogratis.app.dao.ProductRepository;
import ar.edu.mercadogratis.app.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class ProductServiceTest {

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public ProductService productService(ProductRepository productRepository) {
            return new ProductService(productRepository);
        }
    }

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void testGetProduct() {
        // given
        Long productId = 1L;
        Product product = mock(Product.class);

        when(productRepository.findById(eq(productId))).thenReturn(Optional.of(product));

        // when
        Optional<Product> result = productService.getProduct(productId);

        // then
        assertThat(result).isPresent();
        assertThat(result).hasValue(product);
    }

    @Test
    void testSaveProduct() {
        // given
        Product product = mock(Product.class);
        when(productRepository.save(eq(product))).thenReturn(product);

        // when
        Product result = productService.saveProduct(product);

        // then
        verify(productRepository).save(eq(product));
        assertThat(result).isEqualTo(product);
    }

    @Test
    void testUpdateProduct() {
        // given
        Product product = mock(Product.class);

        // when
        productService.updateProduct(product);

        // then
        verify(productRepository).save(eq(product));
    }

    @Test
    void testListProducts() {
        // given
        Product product = mock(Product.class);
        String seller = "any_seller";
        when(productRepository.findBySeller(eq(seller))).thenReturn(List.of(product));

        // when
        Iterable<Product> products = productService.listProducts(seller);

        // then
        assertThat(products).containsExactly(product);
    }

    @Test
    void testDeleteProduct() {
        // given
        // when
        productService.deleteProduct(1L);

        // then
        verify(productRepository, times(1)).deleteById(eq(1L));
    }
}