package functional;

import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.IPrice;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Price implements IPrice {

    private DateTime dateTime;
    private BigDecimal price;
    private IAsset asset;

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
    public IAsset getAsset() {
        return asset;
    }

    public void setAsset(IAsset asset) {
        this.asset = asset;
    }
}
