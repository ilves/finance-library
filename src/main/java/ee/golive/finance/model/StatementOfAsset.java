package ee.golive.finance.model;

import ee.golive.finance.domain.IAsset;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
@Data
public class StatementOfAsset {
    private IAsset asset;
    private BigDecimal count;
    private BigDecimal price;
    private BigDecimal basePrice;
    private BigDecimal value;
    private BigDecimal baseValue;

    public StatementOfAsset (IAsset asset) {
        this.asset = asset;
    }
}
