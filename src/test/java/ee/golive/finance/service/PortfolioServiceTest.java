package ee.golive.finance.service;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.ITransaction;
import ee.golive.finance.model.StatementOfAsset;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ee.golive.finance.MockHelper.getMockTransaction;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class PortfolioServiceTest {

    PortfolioService portfolioService;
    PriceService priceService;
    TransactionService transactionService;
    List<ITransaction> transactions;
    IAsset stock;
    IAsset money;

    @Before
    public void setUp() {
        priceService = mock(PriceService.class);
        transactionService = new TransactionService();
        portfolioService = new PortfolioService(priceService, transactionService);

        money = mock(IAsset.class);
        stock = mock(IAsset.class);

        when(priceService.getPriceAt(any(), eq(stock)))
                .thenReturn(Optional.of(new BigDecimal("40")));
        when(priceService.getPriceAt(any(), eq(money)))
                .thenReturn(Optional.empty());
        when(priceService.getPriceAt(any(), eq(money), any(Boolean.class)))
            .thenReturn(Optional.empty());


        transactions = Arrays.asList(
                getMockTransaction(500, 500, FlowType.EXTERNAL, DateTime.parse("2011-03-10"), money), // DEPOSIT
                getMockTransaction(300, 10, FlowType.OTHER, DateTime.parse("2011-03-11"), stock), // BUY STOCK
                getMockTransaction(-300, -300, FlowType.OTHER, DateTime.parse("2011-03-11"), money), // STOCK COST
                getMockTransaction(20, 20, FlowType.INTERNAL, DateTime.parse("2011-04-15"), money), // DIVIDENDS
                getMockTransaction(-400, -5, FlowType.OTHER, DateTime.parse("2011-05-21"), stock), // SELL STOCK
                getMockTransaction(400, 400, FlowType.OTHER, DateTime.parse("2011-05-21"), money), // STOCK PROFIT
                getMockTransaction(-100, -100, FlowType.EXTERNAL, DateTime.parse("2011-06-10"), money) // WITHDRAW
        );
    }

    @Test
    public void testCreatePortfolioNormalUseCase() {
        List<StatementOfAsset> portfolio;

        portfolio = portfolioService.portfolioAt(DateTime.parse("2011-01-01"), transactions, true);
        assertEquals(0, portfolio.size());

        portfolio = portfolioService.portfolioAt(DateTime.parse("2011-03-10"), transactions, true);
        assertEquals(1, portfolio.size());
        assertEquals(new BigDecimal(500), portfolio.get(0).getValue());

        portfolio = portfolioService.portfolioAt(DateTime.parse("2011-03-11"), transactions, true);
        assertEquals(2, portfolio.size());
        assertEquals(new BigDecimal(400), find(stock, portfolio).getValue());
        assertEquals(new BigDecimal(200), find(money, portfolio).getValue());

        portfolio = portfolioService.portfolioAt(DateTime.parse("2011-04-15"), transactions, true);
        assertEquals(new BigDecimal(220), find(money, portfolio).getValue());

        portfolio = portfolioService.portfolioAt(DateTime.parse("2011-04-15"), transactions, false);
        assertEquals(new BigDecimal(200), find(money, portfolio).getValue());

        portfolio = portfolioService.portfolioAt(DateTime.parse("2011-05-21"), transactions, true);
        assertEquals(new BigDecimal(200), find(stock, portfolio).getValue());
        assertEquals(new BigDecimal(620), find(money, portfolio).getValue());

        portfolio = portfolioService.portfolioAt(DateTime.parse("2011-06-11"), transactions, true);
        assertEquals(new BigDecimal(520), find(money, portfolio).getValue());
    }

    private StatementOfAsset find(IAsset asset, List<StatementOfAsset> assets) {
        return assets.stream().filter(x -> x.getAsset().equals(asset)).findFirst().get();
    }
}
