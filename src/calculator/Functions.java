package calculator;

import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import ch.obermuhlner.math.big.BigDecimalMath;

/**
 * Define functions supported by the calculator here.
 */
enum Functions {

    SQUARE_ROOT("sqrt", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.sqrt(input, context.getMathContext());
        }
    }), NATURAL_LOG("log", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.log(input, context.getMathContext());
        }
    }), LOG_TEN("log10", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.log10(input, context.getMathContext());
        }
    }), LOG_TWO("log2", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.log2(input, context.getMathContext());
        }
    }), EXPONENT("exp", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.exp(input, context.getMathContext());
        }
    }), LOGICAL_NOT("b-", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return input.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), NEGATE("!", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return input.negate();
        }
    }), SINE("sin", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.sin(input, context.getMathContext());
        }
    }), COSINE("cos", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.cos(input, context.getMathContext());
        }
    }), TANGENT("tan", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.tan(input, context.getMathContext());
        }
    }), ARCSINE("asin", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.asin(input, context.getMathContext());
        }
    }), ARCCOSINE("acos", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.acos(input, context.getMathContext());
        }
    }), ARCTANGENT("atan", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.atan(input, context.getMathContext());
        }
    }), H_SINE("sinh", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.sinh(input, context.getMathContext());
        }
    }), H_COSINE("cosh", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.cosh(input, context.getMathContext());
        }
    }), H_TANGENT("tanh", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.tanh(input, context.getMathContext());
        }
    }), GAMMA("gamma", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.gamma(input, context.getMathContext());
        }
    }), FACTORIAL("factorial", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.factorial(input, context.getMathContext());
        }
    });

    private final String name;
    private final Function function;
    private static final int numberOfFunctions = Functions.values().length;
    private static final Map<String, Functions> functionNames = new HashMap<String, Functions>(numberOfFunctions){
        private static final long serialVersionUID = 3860916130056466065L;
        {
            for (Functions f : Functions.values()) {
                put(f.name, f);
            }
        }
    };

    private Functions(String name, Function function) {
        this.name = name;
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
