package ee.golive.finance.model;

import ee.golive.finance.domain.ITransaction;
import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

/**
 *  Class that represents snapshot of a portfolio at specific time
 *
 *  @author Taavi Ilves
 */
@Data
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

    public Snapshot(DateTime snapshotDateTime, List<ITransaction> transactions) {
        this.snapshotDateTime = snapshotDateTime;
        this.transactions = transactions;
    }
}
