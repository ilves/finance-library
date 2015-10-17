package functional;

import ee.golive.finance.domain.IsAsset;
import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IsPrice;
import ee.golive.finance.domain.IsTransaction;
import ee.golive.finance.helper.DateTimeHelper;
import ee.golive.finance.math.TimeWeightedReturn;
import ee.golive.finance.model.SnapshotPeriod;
import ee.golive.finance.service.ListPriceService;
import ee.golive.finance.service.PriceService;
import ee.golive.finance.service.SnapshotService;
import ee.golive.finance.service.ValueService;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Taavi on 16.10.2015.
 */
public class FunctionalTest {

    ValueService valueService;
    SnapshotService snapshotService;
    PriceService priceService;
    List<IsTransaction> transactions;
    IsAsset asset;
    IsAsset eur;

    @Before
    public void setUp() {
        asset = new functional.Asset();
        eur = new functional.Asset();

        valueService = new ValueService();
        priceService = new ListPriceService(getPrices());
        snapshotService = new SnapshotService(priceService);
        transactions = getTransactions();
    }

    @Test
    public void functionalTestTimeWeighted() {
        TimeWeightedReturn calculator = new TimeWeightedReturn();
        DateTime start = DateTime.parse("2011-01-01");
        DateTime end = start.plusYears(1);
        List<Interval> intervals = DateTimeHelper.getIntervals(start, end, DateTimeHelper.MONTHLY);
        List<SnapshotPeriod> snapshotPeriods = snapshotService.generateBetween(intervals, getTransactions());
        BigDecimal ttwr = calculator.calculate(snapshotPeriods);
        assertEquals(new BigDecimal("0.5377"), ttwr.setScale(4, BigDecimal.ROUND_HALF_DOWN));
    }

    private List<IsTransaction> getTransactions() {
        return Arrays.asList(
                getTransaction(DateTime.parse("2011-02-01"), "1000", "1", FlowType.EXTERNAL, eur), // DEPOSIT
                getTransaction(DateTime.parse("2011-02-10"), "1000", "20", FlowType.EXTERNAL, asset), // BUY
                getTransaction(DateTime.parse("2011-02-10"), "-1000", "1", FlowType.EXTERNAL, eur), // BUY REFERENCE
                getTransaction(DateTime.parse("2011-03-02"), "250", "1", FlowType.EXTERNAL, eur), // DEPOSIT
                getTransaction(DateTime.parse("2011-03-06"), "100", "20", FlowType.INTERNAL, eur), // DIVIDEND
                getTransaction(DateTime.parse("2011-04-04"), "250", "1", FlowType.EXTERNAL, eur), // DEPOSIT
                getTransaction(DateTime.parse("2011-05-04"), "-100", "1", FlowType.EXTERNAL, eur), // WITHDRAWAL
                getTransaction(DateTime.parse("2011-05-14"), "-100", "5", FlowType.EXTERNAL, eur), // SELL
                getTransaction(DateTime.parse("2011-05-14"), "100", "1", FlowType.EXTERNAL, eur) // SELL REFERENCE
        );
    }

    private List<IsPrice> getPrices() {
        return Arrays.asList(
                getPrice(DateTime.parse("2011-02-10"), "50", asset),
                getPrice(DateTime.parse("2011-03-10"), "52.5", asset),
                getPrice(DateTime.parse("2011-04-10"), "60", asset),
                getPrice(DateTime.parse("2011-05-15"), "70", asset)
        );
    }

    private Price getPrice(DateTime d, String v, IsAsset as) {
        Price price = new Price();
        price.setAsset(as);
        price.setDateTime(d);
        price.setPrice(new BigDecimal(v));
        return price;
    }

    private Transaction getTransaction(DateTime d, String a, String c, FlowType f, IsAsset as) {
        Transaction transaction = new Transaction();
        transaction.setDateTime(d);
        transaction.setAmount(new BigDecimal(a));
        transaction.setFlowType(f);
        transaction.setAsset(as);
        transaction.setCount(new BigDecimal(c));
        return transaction;
    }
}
