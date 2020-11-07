package functional;

import ee.golive.finance.domain.IAsset;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Asset implements IAsset {
    private String name;
    private IAsset currency;
    private AssetType type;
}
