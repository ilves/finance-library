package ee.golive.finance.service;

import ee.golive.finance.domain.IsAsset;
import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IsTransaction;
import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.SnapshotPeriod;
import ee.golive.finance.model.StatementOfAsset;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static ee.golive.finance.MockHelper.getMockTransaction;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class ValueServiceTest {

    ValueService valueService;

    @Before
    public void setUp() {
        valueService = new ValueService();
    }

    @Test
    public void testGetValue() {
        Snapshot snapshot = mock(Snapshot.class);
        when(snapshot.getPortfolio()).thenReturn(Arrays.asList(
                getMockStatement(11.0, 5.5, 60.5),
                getMockStatement(9.0, 5.33, 47.97)
        ));
        assertEquals(new BigDecimal("108.47"),
                valueService.getValue(snapshot).setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    @Test
    public void getExternalFlow() {
        SnapshotPeriod snapshot = new SnapshotPeriod(mock(Snapshot.class), mock(Snapshot.class));
        snapshot.setTransactions(getMockTransactions());
        assertEquals(new BigDecimal("36.53"),
                valueService.getExternalFlow(snapshot).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void getInternalFlow() {
        SnapshotPeriod snapshot = new SnapshotPeriod(mock(Snapshot.class), mock(Snapshot.class));
        snapshot.setTransactions(getMockTransactions());
        assertEquals(new BigDecimal("27.10"),
                valueService.getInternalFlow(snapshot).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    private List<IsTransaction> getMockTransactions() {
        return Arrays.asList(
                getMockTransaction(10.6, FlowType.INTERNAL),
                getMockTransaction(23.30, FlowType.EXTERNAL),
                getMockTransaction(21.44, FlowType.NONE),
                getMockTransaction(16.5, FlowType.INTERNAL),
                getMockTransaction(63.60, FlowType.NONE),
                getMockTransaction(13.23, FlowType.EXTERNAL)
        );
    }

    private StatementOfAsset getMockStatement(double count, double price, double value) {
        StatementOfAsset statement = new StatementOfAsset(mock(IsAsset.class));
        statement.setPrice(new BigDecimal(price));
        statement.setValue(new BigDecimal(value));
        statement.setItemsCount(new BigDecimal(count));
        return statement;
    }
}
