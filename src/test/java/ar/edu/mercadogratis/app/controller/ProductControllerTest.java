package ar.edu.mercadogratis.app.controller;

import ar.edu.mercadogratis.app.dao.ProductRepository;
import ar.edu.mercadogratis.app.model.Product;
import ar.edu.mercadogratis.app.model.ProductCategory;
import ar.edu.mercadogratis.app.model.ProductStatus;
import ar.edu.mercadogratis.app.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ProductService productService;

    @AfterEach
    public void tearDown() {
        productService.deleteAll();
    }

    @Test
    void testGetProduct() {

        // given
        Product product = Product.builder()
                .name("name")
                .description("description")
                .category(ProductCategory.FASHION)
                .price(new BigDecimal("10"))
                .stock(5)
                .seller("seller")
                .status(ProductStatus.ACTIVE)
                .build();

        Product savedProduct = productService.saveProduct(product);

        String url = String.format("http://localhost:%s/products/%s", port, savedProduct.getId());

        // when
        ResponseEntity<Product> entity = restTemplate.getForEntity(url, Product.class);

        // then
        assertThat(entity.getBody()).isEqualTo(savedProduct);
    }

    @Test
    void testPostProduct() throws JsonProcessingException {

        // given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = String.format("http://localhost:%s/products", port);

        Product product = Product.builder()
                .name("name")
                .description("description")
                .category(ProductCategory.FASHION)
                .price(new BigDecimal("10"))
                .stock(5)
                .seller("seller")
                .status(ProductStatus.ACTIVE)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(product), headers);

        // when
        ResponseEntity<Product> entity = restTemplate.postForEntity(url, request, Product.class);

        // then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getId()).isNotNull();
    }

    @Test
    void testSearchProduct() {

        // given
        Product product = Product.builder()
                .name("celular samsung")
                .description("description")
                .category(ProductCategory.FASHION)
                .price(new BigDecimal("10"))
                .stock(5)
                .seller("seller")
                .status(ProductStatus.ACTIVE)
                .build();

        Product savedProduct = productService.saveProduct(product);

        String nameQuery = "samsung";
        String categoryQuery = ProductCategory.FASHION.name();
        String url = String.format("http://localhost:%s/products/search?name=%s&category=%s", port, nameQuery, categoryQuery);

        // when
        ResponseEntity<Product[]> products = restTemplate.getForEntity(url, Product[].class);

        // then
        assertThat(products.getBody()).hasSize(1);
        assertThat(products.getBody()[0]).isEqualTo(savedProduct);
    }

    @Test
    void testSearchProduct_emptyStock() {

        // given
        Product product = Product.builder()
                .name("celular samsung")
                .description("description")
                .category(ProductCategory.FASHION)
                .price(new BigDecimal("10"))
                .stock(0)
                .seller("seller")
                .status(ProductStatus.ACTIVE)
                .build();

        Product savedProduct = productService.saveProduct(product);

        String nameQuery = "samsung";
        String categoryQuery = ProductCategory.FASHION.name();
        String url = String.format("http://localhost:%s/products/search?name=%s&category=%s", port, nameQuery, categoryQuery);

        // when
        ResponseEntity<Product[]> products = restTemplate.getForEntity(url, Product[].class);

        // then
        assertThat(products.getBody()).hasSize(0);
    }
}