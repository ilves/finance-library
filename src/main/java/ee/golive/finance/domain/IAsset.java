package ee.golive.finance.domain;

/**
 * TODO Add class comment
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public interface IAsset {
    String getName();
    AssetType getType();
    IAsset getCurrency();

    enum AssetType {
        CURRENCY, ASSET
    }
}
