package ar.edu.mercadogratis.app.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class PurchaseProduct extends BaseEntity {

    @ManyToOne
    private Product product;

    @ManyToOne
    private User buyer;

    private LocalDateTime creationDate;

    private int quantity;

    private PurchaseStatus status;

    public BigDecimal getPrice() {
        return product.getPrice();
    }
}
