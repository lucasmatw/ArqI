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

        PurchaseProduct purchaseProduct = productService.getProduct(purchaseRequest.getProductId())
                .map(product -> takeStock(product, purchaseRequest.getQuantity()))
                .map(product -> createPurchase(purchaseRequest, product))
                .orElseThrow(() -> new ValidationException("Invalid product"));

        return purchaseProductRepository.save(purchaseProduct);
    }

    private PurchaseProduct createPurchase(PurchaseRequest purchaseRequest, Product product) {
        User user = userService.getUserForMail(purchaseRequest.getBuyerEmail());
        LocalDateTime creationDate = dateService.getNowDate();
        return new PurchaseProduct(product, user, creationDate, purchaseRequest.getQuantity(), PurchaseStatus.CONFIRMED);
    }

    private Product takeStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new ValidationException("No stock available");
        }

        product.setStock(product.getStock() - quantity);
        productService.updateProduct(product);
        return product;
    }
}
