package ee.golive.finance.math;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IsTransaction;
import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.SnapshotPeriod;
import org.joda.time.DateTime;
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
public class XirrTest {

    @Test
    public void testXirrWithSourceArrays() {
        double[] values = {-6000, 2134, 1422, 1933, 1422};
        int[] dates = {36175, 36254, 36289, 36597, 36647};
        Xirr xirr = new Xirr(values, dates);
        assertEquals(0.224837, xirr.calculate(), 1e-6);
    }

    @Test
    public void testNull() {
        double[] values = {1084.64, -87.23, -0.44, 110.0, -107.39, -6.92, -1000.02};
        int[] dates = {42144, 42155, 42155, 42156, 42185, 42185, 42297};
        Xirr xirr = new Xirr(values, dates);
        assertEquals(0.017282, xirr.calculate(), 1e-6);
    }

    @Test
    public void testXirrWithPeriodForPortfolio() {
        SnapshotPeriod period = mock(SnapshotPeriod.class);
        List<IsTransaction> transactions = getMockTransactions();
        when(period.getTransactions()).thenReturn(transactions);
        Snapshot start = mock(Snapshot.class);
        when(start.getSnapshotDateTime()).thenReturn(new DateTime("1999-01-15"));
        when(start.getValue()).thenReturn(new BigDecimal("6000"));
        Snapshot end = mock(Snapshot.class);
        when(end.getSnapshotDateTime()).thenReturn(new DateTime("2000-05-01"));
        when(end.getValue()).thenReturn(new BigDecimal("12422"));
        when(period.getStartSnapshot()).thenReturn(start);
        when(period.getEndSnapshot()).thenReturn(end);
        Xirr xirr = new Xirr(period);
        assertEquals(0.404036, xirr.calculate(), 1e-6);
    }

    private List<IsTransaction> getMockTransactions() {
        return Arrays.asList(
                getMockTransaction(2134, FlowType.EXTERNAL, new DateTime("1999-04-04")),
                getMockTransaction(-1422, FlowType.EXTERNAL, new DateTime("1999-05-09")),
                getMockTransaction(1933, FlowType.EXTERNAL, new DateTime("2000-03-12"))
        );
    }

}
