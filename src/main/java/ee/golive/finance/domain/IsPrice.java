package ee.golive.finance.domain;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * Created by Taavi on 16.10.2015.
 */
public interface IsPrice {
    BigDecimal getPrice();
    DateTime getDateTime();
    IsAsset getAsset();
}
