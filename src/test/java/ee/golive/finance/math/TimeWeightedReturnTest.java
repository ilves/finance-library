package ee.golive.finance.math;

import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.SnapshotPeriod;
import ee.golive.finance.service.ValueService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TimeWeightedReturnTest {

    ValueService service;
    List<SnapshotPeriod> list;
    TimeWeightedReturn calculator;

    @Before
    public void setUp() {
        service = mock(ValueService.class);
        list = new ArrayList<>();
    }

    @Test
    public void testFormulaOnePeriodNoFlow() {
        list.add(getMockPeriod(50.0, 100.0, 0.0, 0.0));
        calculator = new TimeWeightedReturn(list);
        assertEquals(1.0, calculator.calculate(), 0.0000001);
    }

    @Test
    public void testFormulaOnePeriodWithInFlow() {
        list.add(getMockPeriod(50.0, 100.0, 25.0, 0.0));
        calculator = new TimeWeightedReturn(list);
        assertEquals(0.5, calculator.calculate(), 0.0000001);
    }

    @Test
    public void testFormulaOnePeriodWithOutFlow() {
        list.add(getMockPeriod(50.0, 100.0, -25.0, 0.0));
        calculator = new TimeWeightedReturn(list);
        assertEquals(new BigDecimal(1.5), calculator.resultOfBigDecimal().setScale(1, BigDecimal.ROUND_HALF_DOWN));
    }

    @Test
    public void testFormulaTwoPeriodsWithOutFlow() {
        list.add(getMockPeriod(50.0, 100.0, 20.0, 0.0));
        list.add(getMockPeriod(100.0, 50.0, 20.0, 0.0));
        calculator = new TimeWeightedReturn(list);
        assertEquals(new BigDecimal("-0.52"), calculator.resultOfBigDecimal().setScale(2, BigDecimal.ROUND_HALF_DOWN));
    }

    @Test
    public void testComplicated() {
        list.add(getMockPeriod(1000.0, 1300.0, 250.0, 0.0));
        list.add(getMockPeriod(1300.0, 1700.0, 250.0, 0.0));
        list.add(getMockPeriod(1700.0, 1900.0, 0.0, 0.0));
        calculator = new TimeWeightedReturn(list);
        assertEquals(new BigDecimal("0.3089"), calculator.resultOfBigDecimal().setScale(4, BigDecimal.ROUND_HALF_DOWN));
    }

    @Test
    public void testComplicatedDividend() {
        list.add(getMockPeriod(0.0, 0.0, 0.0, 0.0));
        list.add(getMockPeriod(1000.0, 1300.0, 250.0, 0.0));
        list.add(getMockPeriod(1300.0, 1700.0, 250.0, 100.0));
        list.add(getMockPeriod(1700.0, 1900.0, 0.0, 0.0));
        calculator = new TimeWeightedReturn(list);
        assertEquals(new BigDecimal("0.3992"), calculator.resultOfBigDecimal().setScale(4, BigDecimal.ROUND_HALF_DOWN));
    }

    private SnapshotPeriod getMockPeriod(double start, double end, double ext, double in) {
        Snapshot s = new Snapshot(new DateTime(), new ArrayList<>());
        Snapshot e = new Snapshot(new DateTime(), new ArrayList<>());
        SnapshotPeriod period = new SnapshotPeriod(s, e);
        s.setValue(new BigDecimal(start));
        e.setValue(new BigDecimal(end));
        period.setExternalFlow(new BigDecimal(ext));
        period.setInternalFlow(new BigDecimal(in));
        return period;
    }
}