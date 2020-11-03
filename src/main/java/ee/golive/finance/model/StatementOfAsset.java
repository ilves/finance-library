package ee.golive.finance.model;

import ee.golive.finance.domain.IAsset;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
@Builder
@Data
public class StatementOfAsset {
    private IAsset asset;
    private BigDecimal itemsCount;
    private BigDecimal value;
    private BigDecimal price;
    private BigDecimal localPrice;
    private BigDecimal weightedAveragePrice;
    private BigDecimal initialValue;
}
