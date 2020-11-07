package ee.golive.finance.service;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.ITransaction;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static ee.golive.finance.MockHelper.getMockTransaction;
import static org.junit.Assert.assertEquals;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class TransactionServiceTest {

    TransactionService transactionService;

    @Before
    public void setUp() {
        transactionService = new TransactionService();
    }

    @Test
    public void testCalculateAveragePriceWithoutSelling() {
        List<ITransaction> transactions = Arrays.asList(
                getMockTransaction(15000, 150, FlowType.EXTERNAL, DateTime.parse("2011-01-01")),
                getMockTransaction(50000, 250, FlowType.EXTERNAL, DateTime.parse("2011-01-15")),
                getMockTransaction(30000, 100, FlowType.EXTERNAL, DateTime.parse("2011-01-31"))

        );
    }

    @Test
    public void testCalculateAveragePriceWithSelling() {
        List<ITransaction> transactions = Arrays.asList(
                getMockTransaction(15000, 150, FlowType.EXTERNAL, DateTime.parse("2011-01-01")),
                getMockTransaction(-25000, -150, FlowType.EXTERNAL, DateTime.parse("2011-01-01")),
                getMockTransaction(50000, 250, FlowType.EXTERNAL, DateTime.parse("2011-01-15")),
                getMockTransaction(30000, 100, FlowType.EXTERNAL, DateTime.parse("2011-01-31")),
                getMockTransaction(15000, 150, FlowType.EXTERNAL, DateTime.parse("2011-01-31"))
        );
    }
}
