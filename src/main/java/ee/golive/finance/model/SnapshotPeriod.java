package ee.golive.finance.model;


import ee.golive.finance.domain.ITransaction;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *  Class that represents period between two snapshots.
 *
 *  <p>It consists of references to period start snapshot and end snapshot.
 *  It also has list of transactions that have happened between the start and end.</p>
 *
 *  @author Taavi Ilves
 */
@Data
public class SnapshotPeriod {

    /**
     * Reference to the start snapshot
     */
    private Snapshot startSnapshot;

    /**
     * Reference to the end snapshot
     */
    private Snapshot endSnapshot;

    /**
     * List of transactions that happened between start and end
     */
    private List<ITransaction> transactions;

    private BigDecimal internalFlow;

    private BigDecimal externalFlow;

    public SnapshotPeriod(Snapshot startSnapshot, Snapshot endSnapshot) {
        this.startSnapshot = startSnapshot;
        this.endSnapshot = endSnapshot;
        this.transactions = new ArrayList<>(endSnapshot.getTransactions());
        this.transactions.removeAll(startSnapshot.getTransactions());
    }
}
