package functional;

import ee.golive.finance.domain.*;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Data
@Builder
public class Transaction implements ITransaction {
    private DateTime dateTime;
    private IAsset asset;
    private BigDecimal count;
    private FlowType flowType;
    private BigDecimal amount;
    private String description;
    private BigDecimal basePrice;
    private TransactionType type;

    @Override
    public void setValueContext(ValueContext valueContext) {

    }

    @Override
    public ValueContext getValueContext() {
        return null;
    }
}
