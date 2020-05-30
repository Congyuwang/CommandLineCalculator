package calculator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import ch.obermuhlner.math.big.BigDecimalMath;

/**
 * Define binary operators supported by the calculator here.
 */
enum BinaryOperators {
    OR(-8, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return left.compareTo(BigDecimal.ZERO) == 0 && right.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                    : BigDecimal.ONE;
        }
    }), AND(-7, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return left.compareTo(BigDecimal.ZERO) == 0 || right.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                    : BigDecimal.ONE;
        }
    }), NOT_EQUAL(-6, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return right.subtract(left).abs().compareTo(mathContext.getMinimum()) > 0 ? BigDecimal.ZERO
                    : BigDecimal.ONE;
        }
    }), EQUAL(-6, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return right.subtract(left).abs().compareTo(mathContext.getMinimum()) < 0 ? BigDecimal.ONE
                    : BigDecimal.ZERO;
        }
    }), GREATER(-4, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return left.subtract(right).compareTo(mathContext.getMinimum()) > 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), LESS(-4, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return right.subtract(left).compareTo(mathContext.getMinimum()) > 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), LESS_EQUAL(-4, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return left.subtract(right).compareTo(mathContext.getMinimum()) < 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), GREATER_EQUAL(-4, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return right.subtract(left).compareTo(mathContext.getMinimum()) < 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    }), PLUS(-3, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return left.add(right, mathContext.getMathContext());
        }
    }), MINUS(-3, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return left.subtract(right, mathContext.getMathContext());
        }
    }), MULTIPLY(-2, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return left.multiply(right, mathContext.getMathContext());
        }
    }), DIVIDE(-2, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return left.divide(right, mathContext.getMathContext());
        }
    }), REMAINDER(-2, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            return left.remainder(right, mathContext.getMathContext());
        }
    }), POWER(-1, new Function() {
        @Override
        public BigDecimal call(BigDecimal left, BigDecimal right, MathContextWithMin mathContext) {
            if (right.remainder(BigDecimal.ONE, mathContext.getMathContext()).compareTo(BigDecimal.ZERO) == 0) {
                return left.pow(right.intValue(), mathContext.getMathContext());
            }
            return BigDecimalMath.pow(left, right, mathContext.getMathContext());
        }
    }), UNKNOWN(-100, new Function() {
    });

    private final int priority;

    private BinaryOperators(int priority, Function function) {
        this.priority = priority;
        this.function = function;
    }

    private final Function function;

    private static final Set<String> binaryOperators = new HashSet<>(
            Arrays.asList("+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "^", "%", "&", "|"));

    public static final boolean isBinaryOperator(String s) {
        return s != null && binaryOperators.contains(s);
    }

    public static final BinaryOperators of(String c) {
        switch (c) {
            case "+":
                return PLUS;
            case "-":
                return MINUS;
            case "*":
                return MULTIPLY;
            case "/":
                return DIVIDE;
            case "<":
                return LESS;
            case "<=":
                return LESS_EQUAL;
            case ">":
                return GREATER;
            case ">=":
                return GREATER_EQUAL;
            case "==":
                return EQUAL;
            case "!=":
                return NOT_EQUAL;
            case "^":
                return POWER;
            case "%":
                return REMAINDER;
            case "&":
                return AND;
            case "|":
                return OR;
            default:
                return UNKNOWN;
        }
    }

    public final int comparePriority(BinaryOperators o) {
        return this.priority - o.priority;
    }

    public final BigDecimal call(BigDecimal l, BigDecimal r, MathContextWithMin m) {
        return function.call(l, r, m);
    }

}
