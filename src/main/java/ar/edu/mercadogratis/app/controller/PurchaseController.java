package ar.edu.mercadogratis.app.controller;


import ar.edu.mercadogratis.app.model.PurchaseProduct;
import ar.edu.mercadogratis.app.model.PurchaseRequest;
import ar.edu.mercadogratis.app.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Iterable<PurchaseProduct>> listPurchases(@RequestParam String buyerEmail) {
        return ResponseEntity.ok(purchaseService.listPurchases(buyerEmail));
    }
}
