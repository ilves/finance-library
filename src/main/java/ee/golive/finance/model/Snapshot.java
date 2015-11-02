package ee.golive.finance.model;

import ee.golive.finance.domain.ITransaction;
import org.joda.time.DateTime;

import java.math.BigDecimal;
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
    private List<ITransaction> transactions;

    /**
     * Assets at the time of the snapshot
     */
    private List<StatementOfAsset> portfolio;

    /**
     * Snapshot value at snapshot date time
     */
    private BigDecimal value;

    /**
     * True if internalFlow is reinvested otherwise false
     */
    private Boolean reinvestInternalFlow;

    public Snapshot(DateTime snapshotDateTime, List<ITransaction> transactions, boolean reinvestInternalFlow) {
        this.snapshotDateTime = snapshotDateTime;
        this.transactions = transactions;
        this.reinvestInternalFlow = reinvestInternalFlow;
    }

    public List<ITransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<ITransaction> transactions) {
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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getReinvestInternalFlow() {
        return reinvestInternalFlow;
    }

    public void setReinvestInternalFlow(Boolean reinvestInternalFlow) {
        this.reinvestInternalFlow = reinvestInternalFlow;
    }
}
