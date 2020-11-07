package functional;

import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.IPrice;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Data
@Builder
public class Price implements IPrice {

    private DateTime dateTime;
    private BigDecimal price;
    private IAsset asset;
}
