package ar.edu.mercadogratis.app.controller;


import ar.edu.mercadogratis.app.model.PurchaseProduct;
import ar.edu.mercadogratis.app.model.PurchaseRequest;
import ar.edu.mercadogratis.app.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;


    @PostMapping
    public ResponseEntity<PurchaseProduct> createPurchase(@Valid @RequestBody PurchaseRequest purchaseRequest) {

        PurchaseProduct purchase = purchaseService.createPurchase(purchaseRequest);
        return ResponseEntity.ok(purchase);
    }
}
