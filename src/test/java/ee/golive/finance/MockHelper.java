package ee.golive.finance;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.ITransaction;
import org.joda.time.DateTime;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class MockHelper {
    public static ITransaction getMockTransaction(double amount, FlowType flowType) {
        ITransaction transaction = mock(ITransaction.class);
        when(transaction.getAmount()).thenReturn(new BigDecimal(amount));
        when(transaction.getFlowType()).thenReturn(flowType);
        return transaction;
    }

    public static ITransaction getMockTransaction(double amount, FlowType flowType, DateTime dateTime) {
        ITransaction transaction = getMockTransaction(amount, flowType);
        when(transaction.getDateTime()).thenReturn(dateTime);
        return transaction;
    }

    public static ITransaction getMockTransaction(double amount, double count, FlowType flowType, DateTime dateTime) {
        ITransaction transaction = getMockTransaction(amount, flowType, dateTime);
        when(transaction.getCount()).thenReturn(new BigDecimal(count));
        return transaction;
    }

    public static ITransaction getMockTransaction(double amount, double count, FlowType flowType, DateTime dateTime,
                                                   IAsset asset) {
        ITransaction transaction = getMockTransaction(amount, count, flowType, dateTime);
        when(transaction.getAsset()).thenReturn(asset);
        return transaction;
    }
}
