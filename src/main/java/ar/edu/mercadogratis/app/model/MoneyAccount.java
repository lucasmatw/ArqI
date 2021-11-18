package ar.edu.mercadogratis.app.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class MoneyAccount extends BaseEntity {

    @OneToOne
    private User user;

    private BigDecimal balance;

    public void addToBalance(BigDecimal amount){
        this.balance = balance.add(amount);
    }

    public void subtractFromBalance(BigDecimal amount){
        this.balance = balance.subtract(amount);
    }
}
