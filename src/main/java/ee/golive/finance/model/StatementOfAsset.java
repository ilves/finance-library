package ee.golive.finance.model;

import ee.golive.finance.domain.IAsset;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
@Data
@Builder
public class StatementOfAsset {
    private final IAsset asset;
    private BigDecimal count;
    private BigDecimal price;
    private BigDecimal basePrice;
    private BigDecimal value;
    private BigDecimal baseValue;
}
