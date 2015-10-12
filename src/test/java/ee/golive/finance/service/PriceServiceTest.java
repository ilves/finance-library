package ee.golive.finance.service;

import ee.golive.finance.domain.Asset;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class PriceServiceTest {

    PriceService priceService;

    @Before
    public void setUp() {
        priceService = new PriceService();
    }

    @Test
    public void testGetValueOf() {
        Asset asset = mock(Asset.class);
        BigDecimal price = priceService.getValueOf(asset, new BigDecimal(15));
        assertEquals(new BigDecimal("83.25"), price);
    }
}
