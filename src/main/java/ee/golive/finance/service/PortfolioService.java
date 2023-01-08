package ee.golive.finance.service;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.ITransaction;
import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.StatementOfAsset;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This class contains methods for creating list of assets in a portfolio
 * at a snapshot and their values (and other metrics) at that time.
 *
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class PortfolioService {

    /**
     * Price service for assets valuation
     */
    private final PriceService priceService;

    /**
     * Transaction service for transaction related methods
     */
    private final TransactionService transactionService;

    /**
     * Initiates portfolio service using provided PriceService and TransactionService
     *
     * @param priceService service for asset pricing
     * @param transactionService service containing transaction related methods
     */
    public PortfolioService(PriceService priceService, TransactionService transactionService) {
        this.priceService = priceService;
        this.transactionService = transactionService;
    }

    /**
     * Creates List of StatementOfAssets using info from snapshot object
     *
     * @param snapshot input Snapshot object
     * @return  List of StatementOfAssets
     */
    public List<StatementOfAsset> portfolioOf(Snapshot snapshot) {
        return portfolioAt(snapshot.getSnapshotDateTime(), snapshot.getTransactions());
    }

    /**
     * Creates List of StatementOfAssets at specific time using provided transactions.
     *
     * @param dateTime date and time of the statement (inclusive). Never transactions are filtered out.
     * @param transactions list of transactions
     * @return List of StatementOfAssets
     */
    public List<StatementOfAsset> portfolioAt(DateTime dateTime, List<ITransaction> transactions) {
        return transactions.stream()
                .filter(TransactionService.before(dateTime))
                .filter(x -> !x.getFlowType().equals(FlowType.INTERNAL))
                .collect(Collectors.groupingBy(ITransaction::getAsset))
                .entrySet().stream()
                .map(p -> statement(p.getKey(), p.getValue(), dateTime))
                .sorted((b, a) -> a.getBaseValue().compareTo(b.getBaseValue()))
                .collect(Collectors.toList());
    }

    /**
     * Creates single StatementOfAsset sets value and other metrics.
     *
     * @param asset asset object
     * @param transactions should include only transactions that contain the asset
     * @param dateTime time of the statement
     * @return created StatementOfAsset
     */
    private StatementOfAsset statement(IAsset asset, List<? extends ITransaction> transactions, DateTime dateTime) {
        Optional<PriceService.PriceResult> price = priceService.getPriceAt(dateTime, asset, false);
        Optional<PriceService.PriceResult> basePrice = priceService.getPriceAt(dateTime, asset, true);
        BigDecimal count = transactionService.sumCount(transactions);
        BigDecimal value = transactionService.sumAmount(transactions);
        TransactionService.AvgPrice averagePrice = transactionService.averagePrice(transactions);
        Supplier<BigDecimal> defaultPrice = () -> value.divide(count.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : count, RoundingMode.HALF_EVEN);

        return StatementOfAsset.builder()
            .asset(asset)
            .count(count)
            .value(price.map(result -> result.price.multiply(count)).orElse(count))
            .baseValue(basePrice.map(result -> result.price.multiply(count)).orElse(count))
            .price(price.isPresent() ? price.get().price : defaultPrice.get())
            .avgPrice(averagePrice.price)
            .avgBasePrice(averagePrice.basePrice)
            .basePrice(basePrice.isPresent() ? basePrice.get().price : defaultPrice.get())
            .currency(price.map(PriceService.PriceResult::getCurrency).orElse(null))
            .build();
    }
}
