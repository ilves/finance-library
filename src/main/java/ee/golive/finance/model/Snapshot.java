package ee.golive.finance.model;

import ee.golive.finance.domain.Transactional;
import org.joda.time.DateTime;

import java.util.List;

/**
 *  Class that represents snapshot of a portfolio at specific time
 *
 *  @author Taavi Ilves
 */
public class Snapshot {

    /**
     * Date and time of the snapshot
     */
    private DateTime snapshotDateTime;

    /**
     * Transactions that happened before the snapshot date time
     */
    private List<Transactional> transactions;

    /**
     * Assets at the time of the snapshot
     */
    private List<StatementOfAsset> portfolio;

    public Snapshot(DateTime snapshotDateTime) {
        this.snapshotDateTime = snapshotDateTime;
    }

    public List<Transactional> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactional> transactions) {
        this.transactions = transactions;
    }

    public DateTime getSnapshotDateTime() {
        return snapshotDateTime;
    }

    public void setSnapshotDateTime(DateTime snapshotDateTime) {
        this.snapshotDateTime = snapshotDateTime;
    }

    public List<StatementOfAsset> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(List<StatementOfAsset> portfolio) {
        this.portfolio = portfolio;
    }
}
