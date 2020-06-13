package ee.golive.finance.service;

import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.ITransaction;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public interface PriceService {
    Optional<BigDecimal> getPriceAt(DateTime dateTime, IAsset asset);
    Optional<BigDecimal> getValue(ITransaction transaction);
}
