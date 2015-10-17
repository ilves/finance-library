package functional;

import ee.golive.finance.domain.IsAsset;
import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IsTransaction;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Transaction implements IsTransaction {

    private DateTime dateTime;
    private IsAsset asset;
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
    public IsAsset getAsset() {
        return asset;
    }

    public void setAsset(IsAsset asset) {
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
