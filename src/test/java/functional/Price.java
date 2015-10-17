package functional;

import ee.golive.finance.domain.IsAsset;
import ee.golive.finance.domain.IsPrice;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Price implements IsPrice {

    private DateTime dateTime;
    private BigDecimal price;
    private IsAsset asset;

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public IsAsset getAsset() {
        return asset;
    }

    public void setAsset(IsAsset asset) {
        this.asset = asset;
    }
}
