package ee.golive.finance.service;

import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.IPrice;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class ListPriceService implements PriceService {

    List<IPrice> prices;

    public ListPriceService(List<IPrice> prices) {
        this.prices = prices;
    }

    @Override
    public Optional<BigDecimal> getPriceAt(DateTime dateTime, IAsset asset) {
        return prices.stream()
                .filter(p -> p.getAsset().equals(asset) && p.getDateTime().compareTo(dateTime) <= 0)
                .sorted((b, a) -> a.getDateTime().compareTo(b.getDateTime()))
                .map(IPrice::getPrice)
                .findFirst();
    }
}
