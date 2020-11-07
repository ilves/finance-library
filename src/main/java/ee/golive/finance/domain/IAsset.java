package ee.golive.finance.domain;

/**
 * TODO Add class comment
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public interface IAsset {
    AssetType getType();

    enum AssetType {
        CURRENCY, ASSET
    }
}
