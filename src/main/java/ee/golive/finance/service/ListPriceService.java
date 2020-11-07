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
    private IAsset baseCurrency;

    public ListPriceService(List<? extends IPrice> prices) {
        this.prices = prices;
    }

    public IAsset getBaseCurrency () {
        if (baseCurrency == null) {
            baseCurrency = prices.stream().map(IPrice::getCurrency).filter(IAsset::isBaseCurrency).findFirst().orElse(null);
        }

        return baseCurrency;
    }

    public Optional<? extends IPrice> findPrice(DateTime dateTime, IAsset asset) {
        DateTime compareDate = dateTime.plusDays(1).withTimeAtStartOfDay().minusSeconds(1);
        return prices.stream()
                .filter(price -> price.getAsset().equals(asset) && price.getDateTime().compareTo(compareDate) <= 0)
                .max(Comparator.comparing(IPrice::getDateTime));
    }

    @Override
    public Optional<PriceResult> getPriceAt(DateTime dateTime, IAsset asset, boolean baseCurrency) {
        if (asset.getType().equals(IAsset.AssetType.CURRENCY) && (!baseCurrency || asset.isBaseCurrency())) {
            return Optional.of(PriceResult.builder().price(BigDecimal.ONE).currency(asset).build());
        }

        Optional<? extends IPrice> value = findPrice(dateTime, asset);

        if (!value.isPresent()) {
            return Optional.of(PriceResult.builder().price(BigDecimal.ONE).currency(getBaseCurrency()).build());
        } else  if (baseCurrency && !asset.getType().equals(IAsset.AssetType.CURRENCY) && !value.get().getCurrency().isBaseCurrency()) {
            BigDecimal price = getPriceAt(dateTime, value.get().getCurrency(), true).orElse(PriceResult.builder().price(BigDecimal.ONE).build()).price;

            return Optional.of(PriceResult.builder()
                    .price(price.multiply(value.get().getPrice()))
                    .currency(getBaseCurrency())
                    .build()
            );
        }

        return Optional.of(PriceResult.builder().price(value.get().getPrice()).currency(value.get().getCurrency()).build());
    }

    @Override
    public Optional<BigDecimal> getValue(ITransaction transaction) {
        return Optional.empty();
    }
}
