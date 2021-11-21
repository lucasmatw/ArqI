package ar.edu.mercadogratis.app.controller;

import ar.edu.mercadogratis.app.model.Product;
import ar.edu.mercadogratis.app.model.PurchaseProduct;
import ar.edu.mercadogratis.app.service.PurchaseEventProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class TestKafkaController {

    private final PurchaseEventProducerService purchaseEventProducerService;

    @GetMapping
    public ResponseEntity<String> testProduce() {

        PurchaseProduct purchaseProduct = PurchaseProduct.builder()
                .product(Product.builder()
                        .name("product name")
                        .build())
                .build();

        purchaseProduct.setId(1L);

        purchaseEventProducerService.publish(purchaseProduct);

        return ResponseEntity.ok("success");
    }
}
