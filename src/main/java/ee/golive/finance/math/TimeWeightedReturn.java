package ee.golive.finance.math;

import ee.golive.finance.model.SnapshotPeriod;

import java.math.BigDecimal;
import java.util.List;

public class TimeWeightedReturn {

    public BigDecimal calculate(List<SnapshotPeriod> periods) {
        return periods.stream()
                .filter(p -> p.getStartSnapshot().getValue().compareTo(BigDecimal.ZERO) != 0)
                .map(p ->
                        p.getEndSnapshot().getValue()
                                .subtract(p.getExternalFlow())
                                .add(p.getInternalFlow())
                                .divide(p.getStartSnapshot().getValue(), 10, BigDecimal.ROUND_HALF_UP))
                .reduce(BigDecimal.ONE, (x, y) -> x.multiply(y)).subtract(BigDecimal.ONE);
    }
}
