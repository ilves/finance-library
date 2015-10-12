package ee.golive.finance.model;

import java.util.List;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class Portfolio {

    private List<StatementOfAsset> assets;

    public List<StatementOfAsset> getAssets() {
        return assets;
    }

    public void setAssets(List<StatementOfAsset> assets) {
        this.assets = assets;
    }
}
