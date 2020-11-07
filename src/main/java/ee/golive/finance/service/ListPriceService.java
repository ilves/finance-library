package ee.golive.finance.service;

import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.IPrice;
import ee.golive.finance.domain.ITransaction;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class ListPriceService implements PriceService {

    private List<? extends IPrice> prices;

    public ListPriceService(List<? extends IPrice> prices) {
        this.prices = prices;
    }

    @Override
    public Optional<BigDecimal> getPriceAt(DateTime dateTime, IAsset asset) {
        return getPriceAt(dateTime, asset, false);
    }

    @Override
    public Optional<BigDecimal> getPriceAt(DateTime dateTime, IAsset asset, boolean baseCurrency) {
        if (asset.getType().equals(IAsset.AssetType.CURRENCY) && !baseCurrency) {
            return Optional.of(BigDecimal.valueOf(1d));
        }

        DateTime compareDate = dateTime.plusDays(1).withTimeAtStartOfDay().minusSeconds(1);
        Optional<? extends IPrice> value = prices.stream()
                .filter(price -> price.getAsset().equals(asset) && price.getDateTime().compareTo(compareDate) <= 0)
                .max(Comparator.comparing(IPrice::getDateTime));

        if (!value.isPresent()) {
            return Optional.of(BigDecimal.valueOf(1d));
        } else  if (baseCurrency && !asset.getType().equals(IAsset.AssetType.CURRENCY) && !value.get().getCurrency().isBaseCurrency()) {
            return Optional.of(getPriceAt(dateTime, value.get().getCurrency(), true).orElse(BigDecimal.ONE).multiply(value.get().getPrice()));
        }

        return value.map(IPrice::getPrice);
    }

    @Override
    public Optional<BigDecimal> getValue(ITransaction transaction) {
        return Optional.empty();
    }
}
