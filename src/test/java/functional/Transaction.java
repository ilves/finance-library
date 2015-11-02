package functional;

import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.ITransaction;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Transaction implements ITransaction {

    private DateTime dateTime;
    private IAsset asset;
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
    public IAsset getAsset() {
        return asset;
    }

    public void setAsset(IAsset asset) {
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
