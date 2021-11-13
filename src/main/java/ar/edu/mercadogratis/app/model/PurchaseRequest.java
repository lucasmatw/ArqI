package ar.edu.mercadogratis.app.model;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurchaseRequest {

    private Long productId;
    private String buyerEmail;
    private int quantity;
}
