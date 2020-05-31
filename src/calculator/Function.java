package calculator;

import java.math.BigDecimal;

/**
 * The Function interface provides an interface for defining a function
 * supported by the calculator.
 *
 * @throws UnsupportedOperationException if the default method is not overridden.
 */
interface Function {

    default BigDecimal call(MathContextWithMin mathContext) {
        throw new UnsupportedOperationException();
    }

    default BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
        throw new UnsupportedOperationException();
    }

    default BigDecimal call(BigDecimal input1, BigDecimal input2, MathContextWithMin mathContext) {
        throw new UnsupportedOperationException();
    }

    default BigDecimal call(BigDecimal input1, BigDecimal input2, BigDecimal input3, MathContextWithMin mathContext) {
        throw new UnsupportedOperationException();
    }

    default BigDecimal call(BigDecimal input1, BigDecimal input2, BigDecimal input3, BigDecimal input4, MathContextWithMin mathContext) {
        throw new UnsupportedOperationException();
    }

}
