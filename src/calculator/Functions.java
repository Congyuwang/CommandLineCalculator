package calculator;

import java.util.Set;
import java.util.HashSet;
import java.math.BigDecimal;
import java.util.Arrays;
import ch.obermuhlner.math.big.BigDecimalMath;

/**
 * Define functions supported by the calculator here.
 */
enum Functions {

    SQUARE_ROOT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.sqrt(input, mathContext.getMathContext());
        }
    }), NATURAL_LOG(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.log(input, mathContext.getMathContext());
        }
    }), LOG_TEN(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.log10(input, mathContext.getMathContext());
        }
    }), LOG_TWO(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.log2(input, mathContext.getMathContext());
        }
    }), EXPONENT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.exp(input, mathContext.getMathContext());
        }
    }), LOGICAL_NOT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return input.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), NEGATE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return input.negate();
        }
    }), SINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.sin(input, mathContext.getMathContext());
        }
    }), COSINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.cos(input, mathContext.getMathContext());
        }
    }), TANGENT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.tan(input, mathContext.getMathContext());
        }
    }), ARCSINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.asin(input, mathContext.getMathContext());
        }
    }), ARCCOSINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.acos(input, mathContext.getMathContext());
        }
    }), ARCTANGENT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.atan(input, mathContext.getMathContext());
        }
    }), H_SINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.sinh(input, mathContext.getMathContext());
        }
    }), H_COSINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.cosh(input, mathContext.getMathContext());
        }
    }), H_TANGENT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.tanh(input, mathContext.getMathContext());
        }
    }), GAMMA(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.gamma(input, mathContext.getMathContext());
        }
    }), FACTORIAL(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin mathContext) {
            return BigDecimalMath.factorial(input, mathContext.getMathContext());
        }
    });

    private final Function function;

    private Functions(Function function) {
        this.function = function;
    }

    private static final Set<String> functionNames = new HashSet<>(Arrays.asList("sqrt", "log", "log10", "log2", "exp",
            "b-", "!", "sin", "cos", "tan", "asin", "acos", "atan", "sinh", "cosh", "tanh", "gamma", "factorial"));

    public static final boolean isFunctionName(String s) {
        return s != null && functionNames.contains(s);
    }

    public static final Functions of(String c) {
        switch (c) {
            case "sqrt":
                return SQUARE_ROOT;
            case "log":
                return NATURAL_LOG;
            case "log10":
                return LOG_TEN;
            case "log2":
                return LOG_TWO;
            case "exp":
                return EXPONENT;
            case "b-":
                return NEGATE;
            case "!":
                return LOGICAL_NOT;
            case "sin":
                return SINE;
            case "cos":
                return COSINE;
            case "tan":
                return TANGENT;
            case "asin":
                return ARCSINE;
            case "acos":
                return ARCCOSINE;
            case "atan":
                return ARCTANGENT;
            case "sinh":
                return H_SINE;
            case "cosh":
                return H_COSINE;
            case "tanh":
                return H_TANGENT;
            case "gamma":
                return GAMMA;
            case "factorial":
                return FACTORIAL;
            default:
                return null;
        }
    }

    public final BigDecimal call(BigDecimal i, MathContextWithMin m) {
        return function.call(i, m);
    }

    public final BigDecimal call(BigDecimal i1, BigDecimal i2, MathContextWithMin m) {
        return function.call(i1, i2, m);
    }

    public final BigDecimal call(BigDecimal i1, BigDecimal i2, BigDecimal i3, MathContextWithMin m) {
        return function.call(i1, i2, i3, m);
    }

    public final BigDecimal call(BigDecimal i1, BigDecimal i2, BigDecimal i3, BigDecimal i4, MathContextWithMin m) {
        return function.call(i1, i2, i3, i4, m);
    }

}
