package ee.golive.finance.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ValueContext {
    BigDecimal averagePrice;
    BigDecimal averageBasePrice;
}
