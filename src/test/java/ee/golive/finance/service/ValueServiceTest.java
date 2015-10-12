package ee.golive.finance.service;

import ee.golive.finance.domain.Asset;
import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.StatementOfAsset;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class ValueServiceTest {

    ValueService valueService;

    @Before
    public void setUp() {
        valueService = new ValueService();
    }

    @Test
    public void testGetValue() {
        Snapshot snapshot = mock(Snapshot.class);
        when(snapshot.getPortfolio()).thenReturn(Arrays.asList(
                getMockStatement(11.0, 5.5, 60.5),
                getMockStatement(9.0, 5.33, 47.97)
        ));
        assertEquals(new BigDecimal("108.47"), valueService.getValue(snapshot).setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    private StatementOfAsset getMockStatement(double count, double price, double value) {
        StatementOfAsset statement = new StatementOfAsset(mock(Asset.class));
        statement.setPrice(new BigDecimal(price));
        statement.setValue(new BigDecimal(value));
        statement.setItemsCount(new BigDecimal(count));
        return statement;
    }
}
