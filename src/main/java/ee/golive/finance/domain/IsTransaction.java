package ee.golive.finance.domain;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public interface IsTransaction {
    DateTime getDateTime();
    IsAsset getAsset();
    BigDecimal getCount();
    FlowType getFlowType();
    BigDecimal getAmount();
}
