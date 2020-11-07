package ee.golive.finance.service;

import ee.golive.finance.model.Snapshot;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static data.Transactions.getPrices;
import static data.Transactions.getTransactions;
import static org.junit.Assert.assertEquals;

public class SnapshotServiceTest {

    private SnapshotService snapshotService;

    @Before
    public void setUp() {
        PriceService priceService = new ListPriceService(getPrices());
        TransactionService transactionService =  new TransactionService();
        snapshotService = new SnapshotService(
            new ValueService(priceService),
            transactionService,
            new PortfolioService(priceService, transactionService)
        );
    }

    @Test
    public void shouldHandleSimpleOneCurrency() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-01-31"), getTransactions());

        assertEquals(new BigDecimal("950.00"), snapshot.getValue());
        assertEquals(1, snapshot.getPortfolio().size());
        assertEquals(new BigDecimal("1.0"), snapshot.getPortfolio().get(0).getPrice().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("1.0"), snapshot.getPortfolio().get(0).getBasePrice().setScale(1, RoundingMode.HALF_EVEN));

    }

    @Test
    public void shouldHandleTwoCurrencies() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-02-20"), getTransactions());

        assertEquals(new BigDecimal("950.00"), snapshot.getValue());
        assertEquals(2, snapshot.getPortfolio().size());

        // EUR
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));

        // USD
        assertEquals(new BigDecimal("110.0"), snapshot.getPortfolio().get(1).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("100.0"), snapshot.getPortfolio().get(1).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));
    }

    @Test
    public void shouldHandleCurrencyChange() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-02-28"), getTransactions());

        assertEquals(new BigDecimal("938.00"), snapshot.getValue());
        assertEquals(2, snapshot.getPortfolio().size());

        // EUR
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));

        // USD
        assertEquals(new BigDecimal("110.0"), snapshot.getPortfolio().get(1).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("88.0"), snapshot.getPortfolio().get(1).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));
    }

    @Test
    public void shouldHandleStockBuyingInOtherCurrency() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-03-01"), getTransactions());

        assertEquals(new BigDecimal("938.00"), snapshot.getValue());
        assertEquals(3, snapshot.getPortfolio().size());

        // EUR
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));

        // USD
        assertEquals(new BigDecimal("60.0"), snapshot.getPortfolio().get(1).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("48.0"), snapshot.getPortfolio().get(1).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));

        // STOCK 1
        assertEquals(new BigDecimal("5.0"), snapshot.getPortfolio().get(2).getCount());
        assertEquals(new BigDecimal("50.00"), snapshot.getPortfolio().get(2).getValue());
        assertEquals(new BigDecimal("40.0"), snapshot.getPortfolio().get(2).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));
    }

    @Test
    public void shouldHandleStock1PriceIncrease() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-03-02"), getTransactions());

        assertEquals(new BigDecimal("978.00"), snapshot.getValue());
        assertEquals(3, snapshot.getPortfolio().size());

        // EUR
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));

        // USD
        assertEquals(new BigDecimal("60.0"), snapshot.getPortfolio().get(2).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("48.0"), snapshot.getPortfolio().get(2).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));

        // STOCK 1
        assertEquals(new BigDecimal("5.0"), snapshot.getPortfolio().get(1).getCount());
        assertEquals(new BigDecimal("100.00"), snapshot.getPortfolio().get(1).getValue());
        assertEquals(new BigDecimal("80.0"), snapshot.getPortfolio().get(1).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));
    }

    @Test
    public void shouldHandleStock2BuyingInBaseCurrency() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-04-01"), getTransactions());

        assertEquals(new BigDecimal("978.00"), snapshot.getValue());
        assertEquals(4, snapshot.getPortfolio().size());

        // EUR
        assertEquals(new BigDecimal("650.0"), snapshot.getPortfolio().get(0).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("650.0"), snapshot.getPortfolio().get(0).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));

        // USD
        assertEquals(new BigDecimal("60.0"), snapshot.getPortfolio().get(3).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("48.0"), snapshot.getPortfolio().get(3).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));

        // STOCK 1
        assertEquals(new BigDecimal("5.0"), snapshot.getPortfolio().get(2).getCount());
        assertEquals(new BigDecimal("100.0"), snapshot.getPortfolio().get(2).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("80.0"), snapshot.getPortfolio().get(2).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));

        // Stock 2
        assertEquals(new BigDecimal("50.0"), snapshot.getPortfolio().get(1).getCount());
        assertEquals(new BigDecimal("200.0"), snapshot.getPortfolio().get(1).getValue().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("200.0"), snapshot.getPortfolio().get(1).getBaseValue().setScale(1, RoundingMode.HALF_EVEN));
    }
}

