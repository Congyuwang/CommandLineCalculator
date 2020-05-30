package calculator;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * MathContextWithMin adds a minimum value to the original MathContext.
 * The minimum value represents the smallest positive decimal for the system.
 */
class MathContextWithMin {

    private final MathContext mathContext;
    private final BigDecimal minimum;

    MathContextWithMin(MathContext mathContext, BigDecimal minimum) {
        this.mathContext = mathContext;
        this.minimum = minimum;
    }

    public MathContext getMathContext() {
        return mathContext;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }
}
