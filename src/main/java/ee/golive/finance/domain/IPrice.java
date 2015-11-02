package ee.golive.finance.domain;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * TODO Add class comment and method comments
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public interface IPrice {
    BigDecimal getPrice();
    DateTime getDateTime();
    IAsset getAsset();
}
