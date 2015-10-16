package ee.golive.finance.model;


import ee.golive.finance.domain.Transactional;

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
    private List<Transactional> transactions;

    public SnapshotPeriod(Snapshot startSnapshot, Snapshot endSnapshot) {
        this.startSnapshot = startSnapshot;
        this.endSnapshot = endSnapshot;
        initTransactions();
    }

    public Snapshot getStartSnapshot() {
        return startSnapshot;
    }

    public void setStartSnapshot(Snapshot startSnapshot) {
        this.startSnapshot = startSnapshot;
    }

    public Snapshot getEndSnapshot() {
        return endSnapshot;
    }

    public void setEndSnapshot(Snapshot endSnapshot) {
        this.endSnapshot = endSnapshot;
    }

    public List<Transactional> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactional> transactions) {
        this.transactions = transactions;
    }

    private void initTransactions() {
        this.transactions = new ArrayList<>(endSnapshot.getTransactions());
        this.transactions.removeAll(startSnapshot.getTransactions());
    }
}
