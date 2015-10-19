package ee.golive.finance.service;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IsAsset;
import ee.golive.finance.domain.IsPrice;
import ee.golive.finance.domain.IsTransaction;
import ee.golive.finance.model.StatementOfAsset;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static ee.golive.finance.MockHelper.getMockTransaction;
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
    List<IsTransaction> transactions;


    @Before
    public void setUp() {
        priceService = mock(PriceService.class);
        transactionService = mock(TransactionService.class);
        portfolioService = new PortfolioService(priceService, transactionService);
        IsPrice price = mock(IsPrice.class);
        when(price.getPrice()).thenReturn(new BigDecimal(37.34));
        when(priceService.getPriceAt(any(), any())).thenReturn(Optional.of(price));
        transactions = Arrays.asList(
                getMockTransaction(560.1, 15, FlowType.NONE, new DateTime())
        );
    }

    @Test
    public void testCreateStatementOfAsset() {
        IsAsset asset = mock(IsAsset.class);
        when(transactionService.getItemCount(any())).thenReturn(new BigDecimal(15));
        StatementOfAsset statement = portfolioService.createStatementOfAsset(asset, transactions, new DateTime());
        assertEquals(new BigDecimal(15), statement.getItemsCount());
        assertEquals(new BigDecimal("560.10"), statement.getValue().setScale(2, BigDecimal.ROUND_HALF_DOWN));
    }

    @Test
    public void testCreatePortfolio() {
        List<IsTransaction> transactions = new ArrayList<>();
        Map<IsAsset, List<IsTransaction>> map = new HashMap<>();
        map.put(mock(IsAsset.class), new ArrayList<>());
        map.put(mock(IsAsset.class), new ArrayList<>());
        map.put(mock(IsAsset.class), new ArrayList<>());
        when(transactionService.groupByAsset(any())).thenReturn(map);
        when(transactionService.getItemCount(any())).thenReturn(BigDecimal.ONE);
        List<StatementOfAsset> assetList = portfolioService.createPortfolio(transactions, new DateTime());
        assertEquals(3, assetList.size());
    }
}
