package ee.golive.finance.service;

import ee.golive.finance.model.Snapshot;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static data.Transactions.getPrices;
import static data.Transactions.getTransactions;
import static org.junit.Assert.assertEquals;

public class SnapshotServiceTest {

    private SnapshotService snapshotService;
    private PriceService priceService;

    @Before
    public void setUp() {
        priceService = new ListPriceService(getPrices());
        snapshotService = new SnapshotService(priceService);
    }

    @Test
    public void shouldHandleSimpleOneCurrency() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-01-31"), getTransactions(), false);

        assertEquals(new BigDecimal("950.00"), snapshot.getValue());
        assertEquals(1, snapshot.getPortfolio().size());
        assertEquals(new BigDecimal("1.0"), snapshot.getPortfolio().get(0).getPrice());
        assertEquals(new BigDecimal("1.0"), snapshot.getPortfolio().get(0).getBasePrice());

    }

    @Test
    public void shouldHandleTwoCurrencies() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-02-20"), getTransactions(), false);

        assertEquals(new BigDecimal("950.00"), snapshot.getValue());
        assertEquals(2, snapshot.getPortfolio().size());

        // EUR
        assertEquals(new BigDecimal("850.00"), snapshot.getPortfolio().get(0).getValue());
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getBaseValue());

        // USD
        assertEquals(new BigDecimal("110.00"), snapshot.getPortfolio().get(1).getValue());
        assertEquals(new BigDecimal("100.0"), snapshot.getPortfolio().get(1).getBaseValue().setScale(1, BigDecimal.ROUND_HALF_EVEN));
    }

    @Test
    public void shouldHandleCurrencyChange() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-02-28"), getTransactions(), false);

        assertEquals(new BigDecimal("938.00"), snapshot.getValue());
        assertEquals(2, snapshot.getPortfolio().size());

        // EUR
        assertEquals(new BigDecimal("850.00"), snapshot.getPortfolio().get(0).getValue());
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getBaseValue());

        // USD
        assertEquals(new BigDecimal("110.00"), snapshot.getPortfolio().get(1).getValue());
        assertEquals(new BigDecimal("88.0"), snapshot.getPortfolio().get(1).getBaseValue().setScale(1, BigDecimal.ROUND_HALF_EVEN));
    }

    @Test
    public void shouldHandleStockBuyingInOtherCurrency() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-03-01"), getTransactions(), false);

        assertEquals(new BigDecimal("938.00"), snapshot.getValue());
        assertEquals(3, snapshot.getPortfolio().size());

        // EUR
        assertEquals(new BigDecimal("850.00"), snapshot.getPortfolio().get(0).getValue());
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getBaseValue());

        // USD
        assertEquals(new BigDecimal("60.00"), snapshot.getPortfolio().get(1).getValue());
        assertEquals(new BigDecimal("48.0"), snapshot.getPortfolio().get(1).getBaseValue().setScale(1, BigDecimal.ROUND_HALF_EVEN));

        // STOCK 1
        assertEquals(new BigDecimal("5.0"), snapshot.getPortfolio().get(2).getCount());
        assertEquals(new BigDecimal("50.00"), snapshot.getPortfolio().get(2).getValue());
        assertEquals(new BigDecimal("40.0"), snapshot.getPortfolio().get(2).getBaseValue().setScale(1, BigDecimal.ROUND_HALF_EVEN));
    }

    @Test
    public void shouldHandleStock1PriceIncrease() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-03-02"), getTransactions(), false);

        assertEquals(new BigDecimal("978.00"), snapshot.getValue());
        assertEquals(3, snapshot.getPortfolio().size());

        // EUR
        assertEquals(new BigDecimal("850.00"), snapshot.getPortfolio().get(0).getValue());
        assertEquals(new BigDecimal("850.0"), snapshot.getPortfolio().get(0).getBaseValue());

        // USD
        assertEquals(new BigDecimal("60.00"), snapshot.getPortfolio().get(2).getValue());
        assertEquals(new BigDecimal("48.0"), snapshot.getPortfolio().get(2).getBaseValue().setScale(1, BigDecimal.ROUND_HALF_EVEN));

        // STOCK 1
        assertEquals(new BigDecimal("5.0"), snapshot.getPortfolio().get(1).getCount());
        assertEquals(new BigDecimal("100.00"), snapshot.getPortfolio().get(1).getValue());
        assertEquals(new BigDecimal("80.0"), snapshot.getPortfolio().get(1).getBaseValue().setScale(1, BigDecimal.ROUND_HALF_EVEN));
    }

    @Test
    public void shouldHandleStock2BuyingInBaseCurrency() {
        Snapshot snapshot = snapshotService.at(DateTime.parse("2020-04-01"), getTransactions(), false);

        assertEquals(new BigDecimal("978.00"), snapshot.getValue());
        assertEquals(4, snapshot.getPortfolio().size());

        // EUR
        assertEquals(new BigDecimal("650.00"), snapshot.getPortfolio().get(0).getValue());
        assertEquals(new BigDecimal("650.0"), snapshot.getPortfolio().get(0).getBaseValue());

        // USD
        assertEquals(new BigDecimal("60.00"), snapshot.getPortfolio().get(3).getValue());
        assertEquals(new BigDecimal("48.0"), snapshot.getPortfolio().get(3).getBaseValue().setScale(1, BigDecimal.ROUND_HALF_EVEN));

        // STOCK 1
        assertEquals(new BigDecimal("5.0"), snapshot.getPortfolio().get(2).getCount());
        assertEquals(new BigDecimal("100.00"), snapshot.getPortfolio().get(2).getValue());
        assertEquals(new BigDecimal("80.0"), snapshot.getPortfolio().get(2).getBaseValue().setScale(1, BigDecimal.ROUND_HALF_EVEN));

        // Stock 2
        assertEquals(new BigDecimal("50.0"), snapshot.getPortfolio().get(1).getCount());
        assertEquals(new BigDecimal("200.00"), snapshot.getPortfolio().get(1).getValue());
        assertEquals(new BigDecimal("200.00"), snapshot.getPortfolio().get(1).getBaseValue());
    }
}

