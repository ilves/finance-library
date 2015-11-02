package ee.golive.finance.optimization;

import org.junit.Test;

import java.util.function.UnaryOperator;

import static org.junit.Assert.assertEquals;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class NewtonRaphsonMethodTest {

    @Test
    public void testSquareRoot() {
        NewtonRaphsonMethod analyzer = new NewtonRaphsonMethod();
        double sqrt = analyzer.solve(squareRoot(612.0), dSquareRoot(612.0), 0.1);
        assertEquals(24.738633, sqrt, 1e-5);
    }

    private UnaryOperator<Double> squareRoot(double n) {
            return x -> Math.pow(x,2.0) - n;
    }

    private UnaryOperator<Double> dSquareRoot(double n) {
        return x -> 2*x;
    }
}
