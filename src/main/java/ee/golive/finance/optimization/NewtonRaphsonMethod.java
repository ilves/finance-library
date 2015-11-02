package ee.golive.finance.optimization;

import java.util.function.UnaryOperator;

/**
 * This is class for finding successively better approximations to
 * the roots (or zeroes) of a real-valued function.
 *
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 *
 * @see <a href="https://en.wikipedia.org/wiki/Newton's_method">Newton's method</a>
 */
public class NewtonRaphsonMethod {

    /**
     * Maximal number of iterations
     * Don't allow the iterations to continue indefinitely
     */
    private int maxIterations = 200;

    /**
     * Result minimal value
     * Don't want to divide by a number smaller than this
     */
    private double epsilon = 1e-14;

    /**
     * Desired accuracy of the result
     */
    private double tolerance = 1e-7;

    /**
     * Creates NewtonRaphsonMethod object using default configuration options.
     */
    public NewtonRaphsonMethod() {}

    /**
     * Creates NewtonRaphsonMethod object by providing configuration options.
     *
     * @param maxIterations Maximal number of iterations
     * @param epsilon Result minimal value
     * @param tolerance Desired accuracy of the result
     */
    public NewtonRaphsonMethod(int maxIterations , double epsilon, double tolerance) {
        this.maxIterations = maxIterations;
        this.epsilon = epsilon;
        this.tolerance = tolerance;
    }

    /**
     * Solves the equation using Newton's method
     *
     * @param fx implementation of function f(x)
     * @param dfx implementation of function f'(x)
     * @param guess the initial guess of x
     * @return result found, or null if exhausted the loop count but the result is not within minimal value
     */
    public Double solve(UnaryOperator<Double> fx, UnaryOperator<Double> dfx, double guess) {
        double x0 = guess;
        double x1;
        int i = 0;
        while (i++ < maxIterations) {
            double y = fx.apply(x0);
            double yPrime = dfx.apply(x0);
            if (Math.abs(yPrime) < epsilon) {
                break;
            }
            x1 = x0 - y / yPrime;
            if(Math.abs(x1 - x0) / Math.abs(x1) < tolerance) {
                return x1;
            }
            x0 = x1;
        }
        if (Math.abs(fx.apply(x0)) >= epsilon) {
            return null;
        }
        return x0;
    }
}
