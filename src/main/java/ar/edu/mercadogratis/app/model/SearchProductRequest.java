package ar.edu.mercadogratis.app.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Optional;

@Data
@Builder
public class SearchProductRequest {
    private Optional<String> name;
    private Optional<ProductCategory> category;
    private Optional<BigDecimal> minPrice;
    private Optional<BigDecimal> maxPrice;
    private int minStock;
}
