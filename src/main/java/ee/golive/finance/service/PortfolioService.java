package ee.golive.finance.service;

import ee.golive.finance.domain.Asset;
import ee.golive.finance.domain.Transaction;
import ee.golive.finance.model.StatementOfAsset;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

    public List<StatementOfAsset> createPortfolio(List<Transaction> transactions) {
        Map<Asset, List<Transaction>> assets = transactionService.groupByAsset(transactions);
        return assets
                .keySet()
                .stream()
                .map(p -> createStatementOfAsset(p, assets.get(p)))
                .collect(Collectors.toList());
    }

    public StatementOfAsset createStatementOfAsset(Asset a, List<Transaction> transactions) {
        BigDecimal itemCount = transactionService.getItemCount(transactions);
        BigDecimal price = priceService.getValueOf(a, itemCount);
        StatementOfAsset statement = new StatementOfAsset(a);
        statement.setItemsCount(itemCount);
        statement.setValue(price.multiply(itemCount));
        statement.setPrice(price);
        return statement;
    }
}
