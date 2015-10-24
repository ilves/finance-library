package ee.golive.finance.math;

import ee.golive.finance.model.SnapshotPeriod;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.IntToDoubleFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class TimeWeightedReturn {

    private static final String logMessage = "%s -> Start: %s Flow: %s End: %s HPR: %s";
    private static final Logger logger = Logger.getLogger(TimeWeightedReturn.class.getName());

    private final double[][] periods;

    public TimeWeightedReturn(double[][] periods) {
        this.periods = periods;
    }

    public TimeWeightedReturn(List<SnapshotPeriod> periods) {
        this.periods = periods
                .stream()
                .filter(x -> !x.getEndSnapshot().getValue().equals(BigDecimal.ZERO))
                .map((x) -> {
                    double start = x.getStartSnapshot().getValue().doubleValue();
                    double flow = x.getExternalFlow().subtract(x.getInternalFlow()).doubleValue();
                    double end = x.getEndSnapshot().getValue().doubleValue();
                    if (start == 0) {
                        start = flow;
                        flow = 0;
                    }
                    return new double[]{
                            start,
                            flow,
                            end,
                    };
                })
                .toArray(double[][]::new);
    }

    public double calculate() {
        return IntStream.range(0, periods.length).mapToDouble(HPR()).reduce(1.0, (x, y) -> x*y) - 1.0;
    }

    public BigDecimal resultOfBigDecimal() {
        return new BigDecimal(calculate());
    }

    private IntToDoubleFunction HPR() {
        return i -> {
            double hpr = (periods[i][2] - periods[i][1]) / periods[i][0];
            if (!Double.isFinite(hpr)) hpr = 1.0;
            logger.log(Level.FINE, String.format(logMessage, i, periods[i][0], periods[i][1], periods[i][2], hpr));
            return hpr;
        };
    }
}
