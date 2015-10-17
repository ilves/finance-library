package ee.golive.finance.service;

import ee.golive.finance.domain.IsAsset;
import ee.golive.finance.domain.IsPrice;
import org.joda.time.DateTime;

import java.util.Optional;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public interface PriceService {
    Optional<IsPrice> getPriceAt(DateTime dateTime, IsAsset asset);
}
