package ee.golive.finance.service;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.ITransaction;
import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.SnapshotPeriod;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class SnapshotService {

    private TransactionService transactionService;
    private PortfolioService portfolioService;
    private ValueService valueService;

    public SnapshotService(PriceService priceService) {
        this.transactionService = new TransactionService();
        this.portfolioService = new PortfolioService(priceService, transactionService);
        this.valueService = new ValueService();
    }

    public SnapshotService(TransactionService transactionService,
                           PortfolioService portfolioService,
                           ValueService valueService) {
        this.transactionService = transactionService;
        this.portfolioService = portfolioService;
        this.valueService = valueService;
    }

    /**
     * Generates portfolio snapshot at specified date and time (inclusive) using provided transactions.
     *
     * @param dateTime Snapshot date and time
     * @param transactions List of transactions that lead to the dateTime
     * @param reinvestInternalFlow True if internalFlow is reinvested otherwise false
     * @return Snapshot object containing portfolio at specified date and time
     */
    public Snapshot at(DateTime dateTime, List<? extends ITransaction> transactions, boolean reinvestInternalFlow) {
        List<ITransaction> filteredTransactions = transactionService.getTransactionsBefore(dateTime, transactions);
        Snapshot snapshot = new Snapshot(dateTime, filteredTransactions, reinvestInternalFlow);
        snapshot.setPortfolio(portfolioService.portfolioOf(snapshot));
        snapshot.setValue(valueService.getValue(snapshot));
        return snapshot;
    }

    /**
     * Generates List of SnapshotPeriods at specified intervals.
     *
     * @param intervals List of intervals
     * @param transactions List of transactions that lead to the dateTime
     * @param reinvestInternalFlow True if internalFlow is reinvested otherwise false
     * @return List of SnapshotPeriod objects
     */
    public List<SnapshotPeriod> atIntervals(List<Interval> intervals, List<? extends ITransaction> transactions,
                                            boolean reinvestInternalFlow) {
        return intervals.stream()
                .map(interval -> snapshotPeriod(interval, transactions, reinvestInternalFlow))
                .collect(Collectors.toList());
    }

    private SnapshotPeriod snapshotPeriod(Interval interval, List<? extends ITransaction> transactions,
                                         boolean reinvestInternalFlow) {
        Snapshot start = at(interval.getStart(), transactions, reinvestInternalFlow);
        Snapshot end = at(interval.getEnd(), transactions, reinvestInternalFlow);
        return createPeriod(start, end);
    }

    public SnapshotPeriod createPeriod(Snapshot start, Snapshot end) {
        SnapshotPeriod snapshot = new SnapshotPeriod(start, end);
        snapshot.setInternalFlow(valueService.getInternalFlow(snapshot));
        snapshot.setExternalFlow(valueService.getExternalFlow(snapshot));
        return snapshot;
    }

    public List<Interval> getIntervalsAtFlow(List<? extends ITransaction> transactions, DateTime last) {
        if (transactions.size() < 1) {
            return new ArrayList<>();
        }
        return getIntervalsAtFlow(transactions, transactions.get(0).getDateTime(), last);
    }

    public List<Interval> getIntervalsAtFlow(List<? extends ITransaction> transactions, DateTime start, DateTime last) {
        List<DateTime> dates = transactions
                .stream()
                .filter(x -> !x.getFlowType().equals(FlowType.OTHER) && x.getDateTime().compareTo(start) >= 0)
                .map(x -> x.getDateTime().withTimeAtStartOfDay().plusDays(1))
                .distinct()
                .collect(Collectors.toList());
        dates.add(last);
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(start, dates.get(0)));
        for (int n = 1; n < dates.size(); n++) {
            DateTime tmp = dates.get(n-1);
            while (tmp.compareTo(dates.get(n)) >= 0 && n < dates.size()-1) {
                n++;
            }
            try {
                intervals.add(new Interval(tmp, dates.get(n).plusSeconds(1)));
            } catch (Exception e) {
                System.out.println(tmp + " " + dates.get(n));
            }
        }
        return intervals;
    }
}