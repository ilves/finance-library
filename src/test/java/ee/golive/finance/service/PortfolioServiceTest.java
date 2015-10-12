package ee.golive.finance.service;

import ee.golive.finance.domain.Asset;
import ee.golive.finance.domain.Transaction;
import ee.golive.finance.model.StatementOfAsset;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class PortfolioServiceTest {

    PortfolioService portfolioService;
    PriceService priceService;
    TransactionService transactionService;


    @Before
    public void setUp() {
        priceService = mock(PriceService.class);
        transactionService = mock(TransactionService.class);
        portfolioService = new PortfolioService(priceService, transactionService);
    }

    @Test
    public void testCreateStatementOfAsset() {
        Asset asset = mock(Asset.class);
        when(transactionService.getItemCount(any())).thenReturn(new BigDecimal(15));
        when(priceService.getValueOf(any(), any())).thenReturn(new BigDecimal(560));
        StatementOfAsset statement = portfolioService.createStatementOfAsset(asset, new ArrayList<>());
        assertEquals(new BigDecimal(15), statement.getItemsCount());
        assertEquals(new BigDecimal(560), statement.getValue());
    }

    @Test
    public void testCreatePortfolio() {
        List<Transaction> transactions = new ArrayList<>();
        Map<Asset, List<Transaction>> map = new HashMap<>();
        map.put(mock(Asset.class), new ArrayList<>());
        map.put(mock(Asset.class), new ArrayList<>());
        map.put(mock(Asset.class), new ArrayList<>());
        when(transactionService.groupByAsset(any())).thenReturn(map);
        List<StatementOfAsset> assetList = portfolioService.createPortfolio(transactions);
        assertEquals(3, assetList.size());
    }
}
