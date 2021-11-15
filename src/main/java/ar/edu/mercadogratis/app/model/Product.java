package ar.edu.mercadogratis.app.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE Product SET status = 'DELETED' WHERE id=?")
public class Product extends BaseEntity {

    @NotBlank
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductCategory category;

    @Min(1)
    private BigDecimal price;

    @Min(0)
    private int stock;

    @NotBlank
    private String seller;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
