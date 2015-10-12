package ee.golive.finance.service;

import ee.golive.finance.domain.Asset;

import java.math.BigDecimal;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class PriceService {

    public BigDecimal getValueOf(Asset asset, BigDecimal count) {
        return BigDecimal.ONE;
    }
}
