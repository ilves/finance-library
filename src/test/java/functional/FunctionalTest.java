package functional;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.IPrice;
import ee.golive.finance.domain.ITransaction;
import ee.golive.finance.math.TimeWeightedReturn;
import ee.golive.finance.math.Xirr;
import ee.golive.finance.model.Snapshot;
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
import java.util.ArrayList;
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
    List<ITransaction> transactions;
    IAsset asset;
    IAsset eur;

    @Before
    public void setUp() {
        asset = new functional.Asset();
        eur = new functional.Asset();

        valueService = new ValueService(priceService);
        priceService = new ListPriceService(getPrices());
        snapshotService = new SnapshotService(priceService);
        transactions = getTransactions();
    }

    @Test
    public void functionalTestTimeWeighted() {
        DateTime start = DateTime.parse("2011-01-01");
        DateTime end = start.plusYears(1);

        List<Interval> intervals =  snapshotService.getIntervalsAtFlow(getTransactions(), end);
        List<SnapshotPeriod> snapshotPeriods = snapshotService.atIntervals(intervals, getTransactions(), true);

        TimeWeightedReturn calculator = new TimeWeightedReturn(snapshotPeriods);
        BigDecimal ttwr = calculator.resultOfBigDecimal();
        assertEquals(new BigDecimal("-1.3906"), ttwr.setScale(4, BigDecimal.ROUND_HALF_DOWN));
    }

    private List<ITransaction> getTransactions() {
        return Arrays.asList(
                //getTransaction(DateTime.parse("2010-02-01T09:00:00.000"), "800", "2", FlowType.EXTERNAL, asset), // DEPOSIT
                getTransaction(DateTime.parse("2011-02-01T09:00:00.000"), "1000", "1", FlowType.EXTERNAL, asset), // DEPOSIT
                getTransaction(DateTime.parse("2011-03-02T09:00:00.000"), "250", "1", FlowType.EXTERNAL, asset), // DEPOSIT
                getTransaction(DateTime.parse("2011-03-06"), "100", "20", FlowType.INTERNAL, eur), // DIVIDEND
                getTransaction(DateTime.parse("2011-04-04"), "250", "1", FlowType.EXTERNAL, asset), // DEPOSIT
                getTransaction(DateTime.parse("2011-05-04"), "100", "1", FlowType.EXTERNAL, asset) // DEPOSIT
        );
    }

    @Test
    public void functionalTestIrrBond() {
        List<ITransaction> list = getBondTest();
        DateTime start = DateTime.parse("2011-01-01");
        DateTime end = start.plusYears(1);
        Snapshot snapshotStart = snapshotService.at(start, list, false);
        Snapshot snapshotEnd = snapshotService.at(end, list, false);
        SnapshotPeriod period = snapshotService.createPeriod(snapshotStart, snapshotEnd);
        assertEquals(new BigDecimal("1000"), snapshotStart.getValue());
        assertEquals(new BigDecimal("950"), snapshotEnd.getValue());
        assertEquals(new BigDecimal("-50"), period.getExternalFlow());
        assertEquals(new BigDecimal("100"), period.getInternalFlow());
    }

    private List<ITransaction> getBondTest() {
        return Arrays.asList(
                getTransaction(DateTime.parse("2011-01-01"), "1000", "1000", FlowType.EXTERNAL, eur), // BUY
                getTransaction(DateTime.parse("2011-03-06"), "100", "100", FlowType.INTERNAL, eur), // INTEREST
                getTransaction(DateTime.parse("2011-05-14"), "-50", "-50", FlowType.EXTERNAL, eur) // PRINCIPAL
        );
    }

    @Test
    public void functionalTestIrrBondPortfolio() {
        List<ITransaction> list = getPortfolioBondTest();
        DateTime start = DateTime.parse("2011-01-01");
        DateTime end = start.plusYears(1);
        Snapshot snapshotStart = snapshotService.at(start, list, false);
        Snapshot snapshotEnd = snapshotService.at(end, list, false);
        SnapshotPeriod period = snapshotService.createPeriod(snapshotStart, snapshotEnd);
        assertEquals(new BigDecimal("900"), snapshotStart.getValue());
        assertEquals(new BigDecimal("500"), snapshotEnd.getValue());
        assertEquals(new BigDecimal("-500"), period.getExternalFlow());
        assertEquals(new BigDecimal("0"), period.getInternalFlow());
    }

    private List<ITransaction> getPortfolioBondTest() {
        return Arrays.asList(
                getTransaction(DateTime.parse("2011-01-01"), "900", "900", FlowType.EXTERNAL, eur), // DEPOSIT
                getTransaction(DateTime.parse("2011-01-01"), "-800", "-800", FlowType.OTHER, eur), // BUY
                getTransaction(DateTime.parse("2011-01-01"), "800", "800", FlowType.OTHER, eur), // BUY
                getTransaction(DateTime.parse("2011-03-06"), "100", "100", FlowType.OTHER, eur), // INTEREST
                getTransaction(DateTime.parse("2011-05-14"), "-30", "-30", FlowType.OTHER, eur), // PRINCIPAL
                getTransaction(DateTime.parse("2011-05-14"), "30", "30", FlowType.OTHER, eur), // PRINCIPAL
                getTransaction(DateTime.parse("2011-06-01"), "-500", "-500", FlowType.EXTERNAL, eur) // WITHDRAW

        );
    }

    @Test
    public void test() {
        List<ITransaction> list = getTest();
        DateTime start = DateTime.parse("2011-01-01");
        DateTime end = start.plusYears(1);
        Snapshot snapshotStart = snapshotService.at(start, list, false);
        Snapshot snapshotEnd = snapshotService.at(end, list, false);
        SnapshotPeriod period = snapshotService.createPeriod(snapshotStart, snapshotEnd);
        assertEquals(new BigDecimal("200"), snapshotStart.getValue());
        assertEquals(new BigDecimal("150"), snapshotEnd.getValue());
        assertEquals(new BigDecimal("-50"), period.getExternalFlow());
        assertEquals(new BigDecimal("50"), period.getInternalFlow());
        Xirr xirr = new Xirr(period);
        System.out.println(xirr.calculate());

        List<SnapshotPeriod> periods = new ArrayList<>();
        periods.add(period);
        TimeWeightedReturn calc = new TimeWeightedReturn(periods);
        System.out.println(calc.calculate());
    }

    private List<ITransaction> getTest() {
        return Arrays.asList(
                getTransaction(DateTime.parse("2011-01-01"), "200", "200", FlowType.EXTERNAL, eur), // BUY
                getTransaction(DateTime.parse("2011-01-30"), "50", "50", FlowType.INTERNAL, eur), // INTEREST
                getTransaction(DateTime.parse("2011-02-26"), "-50", "-50", FlowType.EXTERNAL, eur) // INTEREST

        );
    }

    private List<IPrice> getPrices() {
        return Arrays.asList(
                getPrice(DateTime.parse("2011-02-01T09:00:00.000"), "30", asset),

                getPrice(DateTime.parse("2011-02-10"), "50", asset),
                getPrice(DateTime.parse("2011-03-10"), "52.5", asset),
                getPrice(DateTime.parse("2011-04-10"), "60", asset),
                getPrice(DateTime.parse("2011-05-15"), "70", asset)
        );
    }

    private Price getPrice(DateTime d, String v, IAsset as) {
        Price price = new Price();
        price.setAsset(as);
        price.setDateTime(d);
        price.setPrice(new BigDecimal(v));
        return price;
    }

    private Transaction getTransaction(DateTime d, String a, String c, FlowType f, IAsset as) {
        Transaction transaction = new Transaction();
        transaction.setDateTime(d);
        transaction.setAmount(new BigDecimal(a));
        transaction.setFlowType(f);
        transaction.setAsset(as);
        transaction.setCount(new BigDecimal(c));
        return transaction;
    }
}
