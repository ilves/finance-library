package ee.golive.finance.service;

import ee.golive.finance.domain.IsTransaction;
import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.SnapshotPeriod;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class SnapshotService {

    private PriceService priceService;
    private TransactionService transactionService;
    private PortfolioService portfolioService;
    private ValueService valueService;

    public SnapshotService(PriceService priceService) {
        this.priceService = priceService;
        this.transactionService = new TransactionService();
        this.portfolioService = new PortfolioService(priceService, transactionService);
        this.valueService = new ValueService();
    }

    public SnapshotService(PriceService priceService,
                           TransactionService transactionService,
                           PortfolioService portfolioService,
                           ValueService valueService) {
        this.priceService = priceService;
        this.transactionService = transactionService;
        this.portfolioService = portfolioService;
        this.valueService = valueService;
    }

    public Snapshot generateAt(DateTime snapshotDate, List<? extends IsTransaction> rawTransactions) {
        List<IsTransaction> transactions = transactionService.getTransactionsBefore(snapshotDate, rawTransactions);
        Snapshot snapshot = new Snapshot(snapshotDate, transactions);
        snapshot.setPortfolio(portfolioService.createPortfolio(transactions, snapshotDate));
        snapshot.setValue(valueService.getValue(snapshot));
        return snapshot;
    }

    public List<SnapshotPeriod> generateBetween(List<Interval> intervals, List<? extends IsTransaction> rawTransactions) {
        List<SnapshotPeriod> snapshots = new ArrayList<>();
        for (Interval interval : intervals) {
            snapshots.add(createValuedPeriod(interval, rawTransactions));
        }
        return snapshots;
    }

    public SnapshotPeriod createValuedPeriod(Interval interval, List<? extends IsTransaction> transactions) {
        Snapshot start = generateAt(interval.getStart(), transactions);
        Snapshot end = generateAt(interval.getEnd(), transactions);
        return createPeriod(start, end);
    }

    public SnapshotPeriod createPeriod(Snapshot start, Snapshot end) {
        SnapshotPeriod snapshot = new SnapshotPeriod(start, end);
        snapshot.setInternalFlow(valueService.getInternalFlow(snapshot));
        snapshot.setExternalFlow(valueService.getExternalFlow(snapshot));
        return snapshot;
    }
}