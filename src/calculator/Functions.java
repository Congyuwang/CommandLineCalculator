package calculator;

import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import ch.obermuhlner.math.big.BigDecimalMath;

/**
 * Define functions supported by the calculator here.
 */
enum Functions {

    SQUARE_ROOT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.sqrt(input, context.getMathContext());
        }
    }), NATURAL_LOG(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.log(input, context.getMathContext());
        }
    }), LOG_TEN(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.log10(input, context.getMathContext());
        }
    }), LOG_TWO(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.log2(input, context.getMathContext());
        }
    }), EXPONENT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.exp(input, context.getMathContext());
        }
    }), LOGICAL_NOT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return input.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), NEGATE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return input.negate();
        }
    }), SINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.sin(input, context.getMathContext());
        }
    }), COSINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.cos(input, context.getMathContext());
        }
    }), TANGENT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.tan(input, context.getMathContext());
        }
    }), ARCSINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.asin(input, context.getMathContext());
        }
    }), ARCCOSINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.acos(input, context.getMathContext());
        }
    }), ARCTANGENT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.atan(input, context.getMathContext());
        }
    }), H_SINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.sinh(input, context.getMathContext());
        }
    }), H_COSINE(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.cosh(input, context.getMathContext());
        }
    }), H_TANGENT(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.tanh(input, context.getMathContext());
        }
    }), GAMMA(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.gamma(input, context.getMathContext());
        }
    }), FACTORIAL(new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.factorial(input, context.getMathContext());
        }
    });

    private final Function function;
    private static final Map<String, Functions> functionNames = new HashMap<String, Functions>(){
        private static final long serialVersionUID = 3860916130056466065L;
        {
            put("sqrt", SQUARE_ROOT);
            put("log", NATURAL_LOG);
            put("log10", LOG_TEN);
            put("log2", LOG_TWO);
            put("exp", EXPONENT);
            put("b-", NEGATE);
            put("!", LOGICAL_NOT);
            put("sin", SINE);
            put("cos", COSINE);
            put("tan", TANGENT);
            put("asin", ARCSINE);
            put("acos", ARCCOSINE);
            put("atan", ARCTANGENT);
            put("sinh", H_SINE);
            put("cosh", H_COSINE);
            put("tanh", H_TANGENT);
            put("gamma", GAMMA);
            put("factorial", FACTORIAL);
        }
    };

    private Functions(Function function) {
        this.function = function;
    }

    public static final boolean isFunctionName(String s) {
        return s != null && functionNames.containsKey(s);
    }

    public static final Functions of(String c) {
        return functionNames.get(c);
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
