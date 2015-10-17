package ee.golive.finance.math;

import ee.golive.finance.analyzes.NewtonRaphsonMethod;
import ee.golive.finance.domain.FlowType;
import ee.golive.finance.model.SnapshotPeriod;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntToDoubleFunction;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class Xirr {

    public static final DateTime EXCEL_DAY_ZERO = new DateTime(1899, 12, 30, 0, 0);
    public static final double DAYS_OF_YEAR = 365.0;

    private final double[] values;
    private final int[] dates;

    public Xirr(double[] values, int[] dates) {
        this.values = Objects.requireNonNull(values);
        this.dates = Objects.requireNonNull(dates);
    }

    public Xirr(SnapshotPeriod period) {
        List<Double> values = new ArrayList<>();
        List<Integer> dates = new ArrayList<>();
        values.add(-period.getStartSnapshot().getValue().doubleValue());
        dates.add(Days.daysBetween( EXCEL_DAY_ZERO, period.getStartSnapshot().getSnapshotDateTime()).getDays());
        period.getTransactions()
                .stream()
                .filter(t -> t.getFlowType().equals(FlowType.EXTERNAL))
                .forEach(t -> {
            values.add(t.getAmount().doubleValue());
            dates.add(Days.daysBetween(EXCEL_DAY_ZERO, t.getDateTime()).getDays());
        });
        values.add(period.getEndSnapshot().getValue().doubleValue());
        dates.add(Days.daysBetween(EXCEL_DAY_ZERO, period.getEndSnapshot().getSnapshotDateTime()).getDays());
        this.values = values.stream().mapToDouble(x->x).toArray();
        this.dates = dates.stream().mapToInt(x -> x).toArray();
    }

    public double calculate() {
        NewtonRaphsonMethod newton = new NewtonRaphsonMethod();
        return newton.solve(NPV(), dNPV(), 0.1);
    }

    private UnaryOperator<Double> NPV() {
        return r -> IntStream.range(0, values.length).mapToDouble(PV(r)).sum();
    }

    private UnaryOperator<Double> dNPV() {
        return r -> IntStream.range(0, values.length).mapToDouble(dPV(r)).sum();
    }

    private IntToDoubleFunction PV(double rate) {
        return i -> values[i] / Math.pow(rate + 1.0, (dates[i] - dates[0]) / DAYS_OF_YEAR);
    }

    private IntToDoubleFunction dPV(double rate) {
        return i -> -i*values[i] * Math.pow(rate + 1.0, -((dates[i] - dates[0]) / DAYS_OF_YEAR) - 1);
    }
}
