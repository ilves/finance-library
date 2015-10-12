package ee.golive.finance.math;

import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.SnapshotPeriod;
import ee.golive.finance.service.ValueService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TimeWeightedReturnTest {

    ValueService service;
    List<SnapshotPeriod> list;
    TimeWeightedReturn calculator;

    @Before
    public void setUp() {
        service = mock(ValueService.class);
        list = new ArrayList<>();
        calculator = new TimeWeightedReturn(service);
    }

    @Test
    public void testFormulaOnePeriodNoFlow() {
        list.add(getMockPeriod(50.0, 100.0, 0.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        assertEquals(new BigDecimal(1), calculator.calculate(list).setScale(0, BigDecimal.ROUND_HALF_DOWN));
    }

    @Test
    public void testFormulaOnePeriodWithInFlow() {
        list.add(getMockPeriod(50.0, 100.0, 25.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        assertEquals(new BigDecimal(0.5), calculator.calculate(list).setScale(1, BigDecimal.ROUND_HALF_DOWN));
    }

    @Test
    public void testFormulaOnePeriodWithOutFlow() {
        list.add(getMockPeriod(50.0, 100.0, -25.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        assertEquals(new BigDecimal(1.5), calculator.calculate(list).setScale(1, BigDecimal.ROUND_HALF_DOWN));
    }

    @Test
    public void testFormulaTwoPeriodsWithOutFlow() {
        list.add(getMockPeriod(50.0, 100.0, 20.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        list.add(getMockPeriod(100.0, 50.0, 20.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        assertEquals(new BigDecimal("-0.52"), calculator.calculate(list).setScale(2, BigDecimal.ROUND_HALF_DOWN));
    }

    @Test
    public void testComplicated() {
        list.add(getMockPeriod(1000.0, 1300.0, 250.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        list.add(getMockPeriod(1300.0, 1700.0, 250.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        list.add(getMockPeriod(1700.0, 1900.0, 0.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        assertEquals(new BigDecimal("0.3089"), calculator.calculate(list).setScale(4, BigDecimal.ROUND_HALF_DOWN));
    }

    @Test
    public void testComplicatedDividend() {
        list.add(getMockPeriod(0.0, 0.0, 0.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        list.add(getMockPeriod(1000.0, 1300.0, 250.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        list.add(getMockPeriod(1300.0, 1700.0, 250.0, 100.0, mock(Snapshot.class), mock(Snapshot.class)));
        list.add(getMockPeriod(1700.0, 1900.0, 0.0, 0.0, mock(Snapshot.class), mock(Snapshot.class)));
        assertEquals(new BigDecimal("0.3992"), calculator.calculate(list).setScale(4, BigDecimal.ROUND_HALF_DOWN));
    }

    private SnapshotPeriod getMockPeriod(double start, double end, double ext, double in, Snapshot s, Snapshot e) {
        SnapshotPeriod period = new SnapshotPeriod(s, e);
        when(service.getValue(s)).thenReturn(new BigDecimal(start));
        when(service.getValue(e)).thenReturn(new BigDecimal(end));
        when(service.getExternalFlow(period)).thenReturn(new BigDecimal(ext));
        when(service.getInternalFlow(period)).thenReturn(new BigDecimal(in));
        return period;
    }
}