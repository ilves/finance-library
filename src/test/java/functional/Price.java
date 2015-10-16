package functional;

import ee.golive.finance.domain.Asset;
import ee.golive.finance.domain.Priceable;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Price implements Priceable {

    private DateTime dateTime;
    private BigDecimal price;
    private Asset asset;

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
    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }
}
