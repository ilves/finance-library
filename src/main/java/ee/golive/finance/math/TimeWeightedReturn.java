package ee.golive.finance.math;

import ee.golive.finance.service.ValueService;
import ee.golive.finance.model.SnapshotPeriod;

import java.math.BigDecimal;
import java.util.List;

public class TimeWeightedReturn {

    private ValueService valueService;

    public TimeWeightedReturn(ValueService valueService) {
        this.valueService = valueService;
    }

    public BigDecimal calculate(List<SnapshotPeriod> periods) {
        return periods.stream()
                .filter(p -> valueService.getValue(p.getStartSnapshot()).compareTo(BigDecimal.ZERO) != 0)
                .map(p ->
                        valueService.getValue(p.getEndSnapshot())
                                .subtract(valueService.getExternalFlow(p))
                                .add(valueService.getInternalFlow(p))
                                .divide(valueService.getValue(p.getStartSnapshot()), 10, BigDecimal.ROUND_HALF_UP))
                .reduce(BigDecimal.ONE, (x, y) -> x.multiply(y)).subtract(BigDecimal.ONE);
    }
}
