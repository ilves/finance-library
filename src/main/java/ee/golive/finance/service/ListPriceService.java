package ee.golive.finance.service;

import ee.golive.finance.domain.Asset;
import ee.golive.finance.domain.Priceable;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Optional;

public class ListPriceService implements PriceService {

    List<Priceable> prices;

    public ListPriceService(List<Priceable> prices) {
        this.prices = prices;
    }

    @Override
    public Optional<Priceable> getPriceAt(DateTime dateTime, Asset asset) {
        return prices.stream()
                .filter(p -> p.getAsset().equals(asset) && p.getDateTime().compareTo(dateTime) <= 0)
                .sorted((b, a) -> a.getDateTime().compareTo(b.getDateTime()))
                .findFirst();
    }
}
