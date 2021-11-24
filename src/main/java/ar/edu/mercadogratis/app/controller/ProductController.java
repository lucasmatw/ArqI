package ar.edu.mercadogratis.app.controller;

import ar.edu.mercadogratis.app.exceptions.NotFoundException;
import ar.edu.mercadogratis.app.model.Product;
import ar.edu.mercadogratis.app.model.ProductCategory;
import ar.edu.mercadogratis.app.model.SearchProductRequest;
import ar.edu.mercadogratis.app.model.User;
import ar.edu.mercadogratis.app.service.ProductService;
import ar.edu.mercadogratis.app.service.UserService;
import ar.edu.mercadogratis.app.service.batch.ProductBatchLoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductBatchLoadService productBatchLoadService;
    private final UserService userService;

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId)
                .orElseThrow(() -> new NotFoundException("product_not_found", "Product not found: " + productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("ok");
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    @PutMapping
    public ResponseEntity<String> updateProduct(@Valid @RequestBody Product product) {
        productService.updateProduct(product);
        return ResponseEntity.ok("ok");
    }

    @GetMapping
    public Iterable<Product> listProducts(@RequestParam String seller) {
        return productService.listProducts(seller);
    }

    @GetMapping("/search")
    public Iterable<Product> searchProducts(@RequestParam Optional<String> name,
                                            @RequestParam Optional<ProductCategory> category,
                                            @RequestParam(name = "min_price") Optional<BigDecimal> minPrice,
                                            @RequestParam(name = "max_price") Optional<BigDecimal> maxPrice) {
        SearchProductRequest search = SearchProductRequest.builder()
                .category(category)
                .name(name)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .minStock(1)
                .build();
        return productService.searchProduct(search);
    }

    @PostMapping("/batch")
    public ResponseEntity<BatchStatus> batchLoad(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("sellerMail") String sellerMail) throws IOException {
        User seller = userService.getUserForMail(sellerMail);
        if(Objects.isNull(seller)) {
            throw new ValidationException("User not found: " + sellerMail);
        }

        BatchStatus batchStatus = productBatchLoadService.loadCsvInput(seller, file.getInputStream());
        return ResponseEntity.ok(batchStatus);
    }
}
