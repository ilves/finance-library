package ee.golive.finance.service;

import ee.golive.finance.domain.Transaction;
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

    public SnapshotService(PriceService priceService, TransactionService transactionService,
                           PortfolioService portfolioService) {
        this.priceService = priceService;
        this.transactionService = transactionService;
        this.portfolioService = portfolioService;
    }

    public Snapshot generateAt(DateTime snapshotDate, List<Transaction> rawTransactions) {
        List<Transaction> transactions = transactionService.getTransactionsBefore(snapshotDate, rawTransactions);
        Snapshot snapshot = new Snapshot(snapshotDate);
        snapshot.setTransactions(transactions);
        snapshot.setPortfolio(portfolioService.createPortfolio(transactions));
        return snapshot;
    }

    public List<SnapshotPeriod> generateBetween(List<Interval> intervals, List<Transaction> rawTransactions) {
        List<SnapshotPeriod> snapshots = new ArrayList<>();
        for(Interval interval: intervals) {
            Snapshot start = generateAt(interval.getStart(), rawTransactions);
            Snapshot end = generateAt(interval.getEnd(), rawTransactions);
            SnapshotPeriod snapshot = new SnapshotPeriod(start, end);
            snapshots.add(snapshot);
        }
        return snapshots;
    }
}
