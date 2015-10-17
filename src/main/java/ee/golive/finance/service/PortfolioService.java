package ee.golive.finance.service;

import ee.golive.finance.domain.IsAsset;
import ee.golive.finance.domain.IsPrice;
import ee.golive.finance.domain.IsTransaction;
import ee.golive.finance.model.StatementOfAsset;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class PortfolioService {

    private PriceService priceService;
    private TransactionService transactionService;

    public PortfolioService(PriceService priceService, TransactionService transactionService) {
        this.priceService = priceService;
        this.transactionService = transactionService;
    }

    public List<StatementOfAsset> createPortfolio(List<IsTransaction> transactions, DateTime portfolioDateTime) {
        Map<IsAsset, List<IsTransaction>> assets = transactionService.groupByAsset(transactions);
        return assets
                .keySet()
                .stream()
                .map(p -> createStatementOfAsset(p, assets.get(p), portfolioDateTime))
                .collect(Collectors.toList());
    }

    public StatementOfAsset createStatementOfAsset(IsAsset asset, List<IsTransaction> transactions, DateTime dateTime) {
        BigDecimal itemCount = transactionService.getItemCount(transactions);
        BigDecimal initialValue = transactionService.getAmountSum(transactions);
        Optional<IsPrice> price = priceService.getPriceAt(dateTime, asset);
        StatementOfAsset statement = new StatementOfAsset(asset);
        statement.setItemsCount(itemCount);
        statement.setValue(price.isPresent() ? price.get().getPrice().multiply(itemCount) : initialValue);
        statement.setPrice(price.isPresent() ? price.get().getPrice() : initialValue.divide(itemCount, BigDecimal.ROUND_UP));
        statement.setInitialValue(initialValue);
        return statement;
    }
}
