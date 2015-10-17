package functional;

import ee.golive.finance.domain.Asset;
import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.Transactional;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Transaction implements Transactional {

    private DateTime dateTime;
    private Asset asset;
    private BigDecimal count;
    private FlowType flowType;
    private BigDecimal amount;

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime getDateTime) {
        this.dateTime = getDateTime;
    }

    @Override
    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    @Override
    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public FlowType getFlowType() {
        return flowType;
    }

    public void setFlowType(FlowType flowType) {
        this.flowType = flowType;
    }
}
