package ee.golive.finance.service;

import ee.golive.finance.domain.Asset;
import ee.golive.finance.domain.Priceable;
import org.joda.time.DateTime;

import java.util.Optional;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public interface PriceService {
    Optional<Priceable> getPriceAt(DateTime dateTime, Asset asset);
}
