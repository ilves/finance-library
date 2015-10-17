package ee.golive.finance.model;

import ee.golive.finance.domain.IsAsset;

import java.math.BigDecimal;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class StatementOfAsset {

    private IsAsset asset;
    private BigDecimal itemsCount;
    private BigDecimal value;
    private BigDecimal price;
    private BigDecimal initialValue;

    public StatementOfAsset(IsAsset asset) {
        this.asset = asset;
    }

    public IsAsset getAsset() {
        return asset;
    }

    public void setAsset(IsAsset asset) {
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

    public BigDecimal getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }
}
