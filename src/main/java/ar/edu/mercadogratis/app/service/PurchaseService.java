package ar.edu.mercadogratis.app.service;

import ar.edu.mercadogratis.app.dao.PurchaseProductRepository;
import ar.edu.mercadogratis.app.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final UserService userService;
    private final ProductService productService;
    private final PurchaseProductRepository purchaseProductRepository;
    private final DateService dateService;

    public PurchaseProduct createPurchase(PurchaseRequest purchaseRequest) {
        // validate stock

        Product product = productService.getProduct(purchaseRequest.getProductId())
                .orElseThrow(() -> new ValidationException("Invalid product"));
        User user = userService.getUserForMail(purchaseRequest.getBuyerEmail());

        LocalDateTime creationDate = dateService.getNowDate();

        int quantity = purchaseRequest.getQuantity();

        PurchaseProduct purchaseProduct = new PurchaseProduct(product, user, creationDate, quantity, PurchaseStatus.CONFIRMED);

        // reduce stock

        return purchaseProductRepository.save(purchaseProduct);
    }
}
