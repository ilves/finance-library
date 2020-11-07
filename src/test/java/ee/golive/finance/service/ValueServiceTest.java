package ee.golive.finance.service;

import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.ITransaction;
import ee.golive.finance.model.Snapshot;
import ee.golive.finance.model.SnapshotPeriod;
import ee.golive.finance.model.StatementOfAsset;
import functional.Transaction;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ee.golive.finance.MockHelper.getMockTransaction;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class ValueServiceTest {

    ValueService valueService;
    PriceService priceService;

    @Before
    public void setUp() {
        priceService = mock(PriceService.class);
        valueService = new ValueService(priceService);

        when(priceService.getPriceAt(any(DateTime.class), any(IAsset.class))).thenReturn(Optional.empty());
        when(priceService.getValue(any(Transaction.class))).thenReturn(Optional.empty());
    }

    @Test
    public void getExternalFlow() {
        SnapshotPeriod snapshot = new SnapshotPeriod(mock(Snapshot.class), mock(Snapshot.class));
        snapshot.setTransactions(getMockTransactions());
        assertEquals(new BigDecimal("36.53"),
                valueService.getExternalFlow(snapshot).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void getInternalFlow() {
        SnapshotPeriod snapshot = new SnapshotPeriod(mock(Snapshot.class), mock(Snapshot.class));
        snapshot.setTransactions(getMockTransactions());
        assertEquals(new BigDecimal("27.10"),
                valueService.getInternalFlow(snapshot).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    private List<ITransaction> getMockTransactions() {
        return Arrays.asList(
                getMockTransaction(10.6, FlowType.INTERNAL),
                getMockTransaction(23.30, FlowType.EXTERNAL),
                getMockTransaction(21.44, FlowType.OTHER),
                getMockTransaction(16.5, FlowType.INTERNAL),
                getMockTransaction(63.60, FlowType.OTHER),
                getMockTransaction(13.23, FlowType.EXTERNAL)
        );
    }

    private StatementOfAsset getMockStatement(double count, double price, double value) {
        return StatementOfAsset.builder()
            .asset(mock(IAsset.class))
            .price(new BigDecimal(price))
            .value(new BigDecimal(value))
            .count(new BigDecimal(count))
            .build();
    }
}
