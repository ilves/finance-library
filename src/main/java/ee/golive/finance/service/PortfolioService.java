package ee.golive.finance.service;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.ITransaction;
import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.StatementOfAsset;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
    private PriceService priceService;

    /**
     * Transaction service for transaction related methods
     */
    private TransactionService transactionService;

    /**
     * Initiates portfolio service using provided PriceService and default TransactionService
     * implementation
     *
     * @param priceService PriceService for asset pricing
     */
    public PortfolioService(PriceService priceService) {
        this(priceService, new TransactionService());
    }

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
        return portfolioAt(snapshot.getSnapshotDateTime(), snapshot.getTransactions(),
                snapshot.getReinvestInternalFlow());
    }

    /**
     * Creates List of StatementOfAssets at specific time using provided transactions.
     *
     * @param dateTime date and time of the statement (inclusive). Never transactions are filtered out.
     * @param transactions list of transactions
     * @param reinvestInternalFlow false if internal flow (dividend etc.) are not reinvested and false if they are.
     * @return List of StatementOfAssets
     */
    public List<StatementOfAsset> portfolioAt(DateTime dateTime, List<ITransaction> transactions,
                                              boolean reinvestInternalFlow) {
        return transactions.stream()
                .filter(TransactionService.before(dateTime))
                .filter(x -> reinvestInternalFlow || !x.getFlowType().equals(FlowType.INTERNAL))
                .collect(Collectors.groupingBy(ITransaction::getAsset))
                .entrySet().stream()
                .map(p -> statement(p.getKey(), p.getValue(), dateTime))
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
        StatementOfAsset statement = new StatementOfAsset(asset);
        BigDecimal count = transactionService.sumCount(transactions);
        BigDecimal value = transactionService.sumAmount(transactions);
        Optional<BigDecimal> price = priceService.getPriceAt(dateTime, asset);
        statement.setItemsCount(count);
        statement.setValue(price.isPresent() ? price.get().multiply(count) : value);
        statement.setPrice(price.isPresent() ? price.get() : value.divide(count, BigDecimal.ROUND_UP));
        statement.setInitialValue(value);
        return statement;
    }
}
