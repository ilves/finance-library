package ee.golive.finance.service;

import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.ITransaction;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public interface PriceService {
    Optional<PriceResult> getPriceAt(DateTime dateTime, IAsset asset, boolean baseCurrency);
    Optional<BigDecimal> getValue(ITransaction transaction);

    @Data
    @Builder
    class PriceResult {
        public BigDecimal price;
        public IAsset currency;
    }
}
