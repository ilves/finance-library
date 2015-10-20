package ee.golive.finance.math;

import ee.golive.finance.analyzes.NewtonRaphsonMethod;
import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IsTransaction;
import ee.golive.finance.model.SnapshotPeriod;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntToDoubleFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class Xirr {

    private static final String logMessage = "%s -> Date: %s Value: %s";
    private static final Logger logger = Logger.getLogger(Xirr.class.getName());

    public static final DateTime EXCEL_DAY_ZERO = new DateTime(1899, 12, 30, 0, 0);
    public static final double DAYS_OF_YEAR = 365.0;

    private final double[] values;
    private final int[] dates;

    private int _pos = 0;

    public Xirr(double[] values, int[] dates) {
        this.values = Objects.requireNonNull(values);
        this.dates = Objects.requireNonNull(dates);
    }

    public Xirr(SnapshotPeriod period) {
        List<IsTransaction> transactions = period.getTransactions().stream().filter(transactionFilter()).collect(Collectors.toList());
        int n = transactions.size()+2;
        values = new double[n];
        dates = new int[n];
        setDataAt(_pos++, period.getStartSnapshot().getValue().doubleValue(),
                Days.daysBetween(EXCEL_DAY_ZERO, period.getStartSnapshot().getSnapshotDateTime()).getDays());
        transactions.stream().forEach(fillData());
        setDataAt(_pos, -period.getEndSnapshot().getValue().doubleValue(),
                Days.daysBetween(EXCEL_DAY_ZERO, period.getEndSnapshot().getSnapshotDateTime()).getDays());
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
        return i -> {
            double n = (dates[i] - dates[0]) / DAYS_OF_YEAR;
            return -n * values[i] * Math.pow(rate + 1.0, -n - 1.0);
        };
    }

    private Predicate<IsTransaction> transactionFilter() {
        return x -> !x.getFlowType().equals(FlowType.NONE);
    }

    private Consumer<IsTransaction> fillData() {
        return x -> {
            double amount = x.getAmount().doubleValue();
            int day = Days.daysBetween(EXCEL_DAY_ZERO, x.getDateTime()).getDays();
            setDataAt(_pos++, x.getFlowType().equals(FlowType.INTERNAL) ? -amount : amount, day);
        };
    }

    private void setDataAt(int i, double amount, int date) {
        logger.log(Level.FINE, String.format(logMessage, i, date, amount));
        values[i] = amount;
        dates[i] = date;
    }
}
