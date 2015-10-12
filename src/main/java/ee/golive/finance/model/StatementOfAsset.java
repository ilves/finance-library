package ee.golive.finance.model;

import ee.golive.finance.domain.Asset;

import java.math.BigDecimal;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class StatementOfAsset {

    private Asset asset;
    private BigDecimal itemsCount;
    private BigDecimal value;
    private BigDecimal price;

    public StatementOfAsset(Asset asset) {
        this.asset = asset;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public BigDecimal getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(BigDecimal items) {
        this.itemsCount = items;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
