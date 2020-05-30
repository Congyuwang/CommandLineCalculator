package calculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import ch.obermuhlner.math.big.BigDecimalMath;

/**
 * Define binary operators supported by the calculator here.
 */
enum BinaryOperators {
    OR("|", -8, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return left.compareTo(BigDecimal.ZERO) == 0 && right.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.ONE;
        }
    }), AND("&", -7, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return left.compareTo(BigDecimal.ZERO) == 0 || right.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.ONE;
        }
    }), NOT_EQUAL("!=", -6, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return right.subtract(left).abs().compareTo(context.getMinimum()) >= 0 ? BigDecimal.ONE: BigDecimal.ZERO;
        }
    }), EQUAL("==", -6, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return right.subtract(left).abs().compareTo(context.getMinimum()) < 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), GREATER(">", -4, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return left.subtract(right).compareTo(context.getMinimum()) > 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), LESS("<", -4, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return right.subtract(left).compareTo(context.getMinimum()) > 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), LESS_EQUAL("<=", -4, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return left.subtract(right).compareTo(context.getMinimum()) < 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), GREATER_EQUAL(">=", -4, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return right.subtract(left).compareTo(context.getMinimum()) < 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), PLUS("+", -3, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return left.add(right, context.getMathContext());
        }
    }), MINUS("-", -3, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return left.subtract(right, context.getMathContext());
        }
    }), MULTIPLY("*", -2, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return left.multiply(right, context.getMathContext());
        }
    }), DIVIDE("/", -2, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return left.divide(right, context.getMathContext());
        }
    }), REMAINDER("%", -2, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            return left.remainder(right, context.getMathContext());
        }
    }), POWER("^", -1, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin context) {
            if (right.remainder(BigDecimal.ONE, context.getMathContext()).compareTo(BigDecimal.ZERO) == 0) {
                return left.pow(right.intValue(), context.getMathContext());
            }
            return BigDecimalMath.pow(left, right, context.getMathContext());
        }
    });

    private final String name;
    private final int priority;
    private final Function function;
    private static final Map<String, BinaryOperators> binaryOperators = new HashMap<String, BinaryOperators>(BinaryOperators.values().length) {
        private static final long serialVersionUID = 2668592046929251579L;
        {
            for (BinaryOperators o : BinaryOperators.values()) {
                put(o.name, o);
            }
        }
    };

    private BinaryOperators(String name, int priority, Function function) {
        this.name = name;
        this.priority = priority;
        this.function = function;
    }

    public static final boolean isBinaryOperator(String s) {
        return s != null && binaryOperators.containsKey(s);
    }

    public static final BinaryOperators of(String c) {
        return binaryOperators.get(c);
    }

    public final int comparePriority(BinaryOperators o) {
        return this.priority - o.priority;
    }

    public final BigDecimal call(BigDecimal l, BigDecimal r, MathContextWithMin m) {
        return function.call(l, r, m);
    }

}
