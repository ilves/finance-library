package ee.golive.finance.service;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.SnapshotPeriod;
import ee.golive.finance.model.StatementOfAsset;

import java.math.BigDecimal;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class ValueService {

    private final PriceService priceService;

    public ValueService(PriceService priceService) {
        this.priceService = priceService;
    }

    public BigDecimal getValue(Snapshot snapshot) {
        return snapshot.getPortfolio().stream()
                .map(StatementOfAsset::getBaseValue)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getExternalFlow(SnapshotPeriod period) {
        return sumAmountOfFlowType(period, FlowType.EXTERNAL);
    }

    public BigDecimal getInternalFlow(SnapshotPeriod period) {
        return sumAmountOfFlowType(period, FlowType.INTERNAL);
    }

    private BigDecimal sumAmountOfFlowType(SnapshotPeriod period, FlowType flowType) {
        return period.getTransactions().stream()
                .filter(t -> t.getFlowType().equals(flowType))
                .map(t -> priceService.getValue(t).orElse(t.getCount()))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}
