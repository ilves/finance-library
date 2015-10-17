package ee.golive.finance.service;

import ee.golive.finance.domain.IsAsset;
import ee.golive.finance.domain.IsPrice;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Optional;

public class ListPriceService implements PriceService {

    List<IsPrice> prices;

    public ListPriceService(List<IsPrice> prices) {
        this.prices = prices;
    }

    @Override
    public Optional<IsPrice> getPriceAt(DateTime dateTime, IsAsset asset) {
        return prices.stream()
                .filter(p -> p.getAsset().equals(asset) && p.getDateTime().compareTo(dateTime) <= 0)
                .sorted((b, a) -> a.getDateTime().compareTo(b.getDateTime()))
                .findFirst();
    }
}
