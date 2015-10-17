package ee.golive.finance.domain;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public interface Transactional {
    public DateTime getDateTime();
    public Asset getAsset();
    public BigDecimal getCount();
    public FlowType getFlowType();
    public BigDecimal getAmount();
}
