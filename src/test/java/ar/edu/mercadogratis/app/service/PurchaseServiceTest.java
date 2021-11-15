package ar.edu.mercadogratis.app.service;

import ar.edu.mercadogratis.app.dao.PurchaseProductRepository;
import ar.edu.mercadogratis.app.model.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class PurchaseServiceTest {

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public PurchaseService purchaseService(UserService userService, ProductService productService,
                                               PurchaseProductRepository purchaseProductRepository, DateService dateService) {
            return new PurchaseService(userService, productService, purchaseProductRepository, dateService);
        }
    }

    @Autowired
    private PurchaseService purchaseService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    @MockBean
    private PurchaseProductRepository purchaseProductRepository;

    @MockBean
    private DateService dateService;

    @Test
    public void testCreatePurchase() {

        PurchaseRequest purchaseRequest = PurchaseRequest.builder()
                .productId(1L)
                .buyerEmail("mail")
                .quantity(3)
                .build();

        User user = mock(User.class);

        Product product = Product.builder()
                .stock(3)
                .build();

        LocalDateTime today = LocalDateTime.of(2021, 10, 10, 5, 5, 5);

        when(productService.getProduct(eq(1L))).thenReturn(Optional.of(product));
        when(dateService.getNowDate()).thenReturn(today);
        when(userService.getUserForMail(eq("mail"))).thenReturn(user);

        PurchaseProduct expected = PurchaseProduct.builder()
                .buyer(user)
                .creationDate(today)
                .product(product)
                .quantity(3)
                .status(PurchaseStatus.CONFIRMED)
                .build();

        when(purchaseProductRepository.save(argThat(new PurchaseMatcher(expected)))).thenReturn(expected);

        PurchaseProduct purchase = purchaseService.createPurchase(purchaseRequest);

        assertThat(purchase).isEqualTo(expected);

        assertThat(product.getStock()).isEqualTo(0);

        verify(purchaseProductRepository).save(any(PurchaseProduct.class));
    }

    @RequiredArgsConstructor
    static class PurchaseMatcher implements ArgumentMatcher<PurchaseProduct> {

        private final PurchaseProduct expectedPurchase;

        @Override
        public boolean matches(PurchaseProduct purchaseProduct) {

            return Objects.equals(purchaseProduct.getProduct(), expectedPurchase.getProduct()) &&
                    Objects.equals(purchaseProduct.getBuyer(), expectedPurchase.getBuyer()) &&
                    Objects.equals(purchaseProduct.getCreationDate(), expectedPurchase.getCreationDate()) &&
                    Objects.equals(purchaseProduct.getQuantity(), expectedPurchase.getQuantity()) &&
                    Objects.equals(purchaseProduct.getStatus(), expectedPurchase.getStatus());
        }
    }
}