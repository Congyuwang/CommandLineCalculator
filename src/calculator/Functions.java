package calculator;

import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Deque;
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
    }), ROOT("root", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal n, MathContextWithMin context) {
            return BigDecimalMath.root(x, n, context.getMathContext());
        }
    }), POWER("pow", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal n, MathContextWithMin context) {
            return BigDecimalMath.pow(x, n, context.getMathContext());
        }
    }), BETA("beta", new Function() {
        @Override
        public BigDecimal call(BigDecimal p, BigDecimal q, MathContextWithMin context) {
            return BigDecimalMath.gamma(p, context.getMathContext())
                    .multiply(BigDecimalMath.gamma(p, context.getMathContext()), context.getMathContext())
                    .divide(BigDecimalMath.gamma(p.add(q, context.getMathContext()), context.getMathContext()));
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

    public final BigDecimal call(MathContextWithMin m) {
        return function.call(m);
    }

    public final BigDecimal call(Deque<BigDecimal> params, MathContextWithMin m) {
        switch(params.size()) {
            case 0:
                return function.call(m);
            case 1:
                return function.call(params.pop(), m);
            case 2:
                return function.call(params.pop(), params.pop(), m);
            case 3:
                return function.call(params.pop(), params.pop(), params.pop(), m);
            case 4:
                return function.call(params.pop(), params.pop(), params.pop(), params.pop(), m);
            default:
                throw new UnsupportedOperationException("wrong number of arguments");
        }
    }

}
