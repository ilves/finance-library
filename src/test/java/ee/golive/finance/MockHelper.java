package ee.golive.finance;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IsTransaction;
import org.joda.time.DateTime;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class MockHelper {
    public static IsTransaction getMockTransaction(double amount, FlowType flowType) {
        IsTransaction transaction = mock(IsTransaction.class);
        when(transaction.getAmount()).thenReturn(new BigDecimal(amount));
        when(transaction.getFlowType()).thenReturn(flowType);
        return transaction;
    }

    public static IsTransaction getMockTransaction(double amount, FlowType flowType, DateTime dateTime) {
        IsTransaction transaction = getMockTransaction(amount, flowType);
        when(transaction.getDateTime()).thenReturn(dateTime);
        return transaction;
    }
}
