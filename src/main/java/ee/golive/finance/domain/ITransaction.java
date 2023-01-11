package ee.golive.finance.domain;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * TODO Add class and method comments
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public interface ITransaction {
    DateTime getDateTime();
    BigDecimal getCount();
    BigDecimal getAmount();
    IAsset getAsset();
    FlowType getFlowType();
    TransactionType getType();
    String getDescription();
    BigDecimal getBasePrice();
    void setValueContext(ValueContext valueContext);
    ValueContext getValueContext();
    default BigDecimal getPrice() {
        return getAmount().divide(getCount(), RoundingMode.HALF_EVEN);
    }
}
