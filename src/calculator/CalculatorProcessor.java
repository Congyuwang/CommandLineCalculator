package calculator;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.math.MathContext;
import ch.obermuhlner.math.big.BigDecimalMath;

/**
 * The Calculator class allows simple calculations and variable assignments.
 * <p>
 * Variable names must start with a Latin letter, and follows latin letters or
 * numbers and underscores. Examples: {@code a1}, {@code number_of_apples},
 * {@code alpha__}.
 * </p>
 * <p>
 * Supported <em>operators</em> (in descending order of priority): {@code ()}
 * (parenthesis), {@code !, +, -} (logical_not, unary positive, unary negative),
 * {@code ^} (power) {@code *, /, %} (multiply, divide, remainder), {@code +, -}
 * (plus, minus), {@code > < >= <=} (greater, less, greater equal, less equal),
 * {@code ==, !=} (equals, not equals), {@code &} (logical_and), {@code |}
 * logical_or.
 * </p>
 * <p>
 * Supported <em>assignments</em>: {@code =, +=, -=, *=, /=, %=} assignments
 * </p>
 * <p>
 * Supported <em>functions</em>: {@code sqrt()} (square root), {@code exp()}
 * (exponent), {@code log()} (natural logarithm), {@code log10()} (logarithm
 * base 10), {@code sin()}, {@code cos()}, {@code tan()}
 * </p>
 * <p>
 * Predefined <em>variables</em>: {@code e} and {@code pi}, which can be
 * reassigned if needed.
 * </p>
 */
public class CalculatorProcessor {

    private static final String VARIABLE = "[a-zA-Z][a-zA-Z0-9_]*";
    private static final String NUMBER = "(([1-9]\\d*|0)(\\.\\d*)?)|(\\.\\d+)";
    private static final String PARENTHESIS = "[()]";
    private static final String OPERATORS = "(!|\\||&|%|!=|==|>=|<=|[+\\-*/^><])";
    private static final String COMBINED_REGEX = "(?<variable>" + VARIABLE + ")\\s*|" +
                                                 "(?<number>" + NUMBER + ")\\s*|" +
                                                 "(?<parenthesis>" + PARENTHESIS + ")\\s*|" +
                                                 "(?<operator>" + OPERATORS + ")\\s*";
    private static final String EVALUATION = "(!|\\||&|%|!=|==|>=|<=|[a-zA-Z0-9_.+\\-*/^()>< ])*";
    private static final String ASSIGNMENT = "^\\s*(?<variable>" + VARIABLE + ")\\s*" +
                                                  "(?<assignment>[+\\-*/%]?)=\\s*" +
                                                  "(?<evaluation>" + EVALUATION + ")\\s*$";
    private static final Pattern EVALUATION_PATTERN = Pattern.compile(EVALUATION);
    private static final Pattern ASSIGNMENT_PATTERN = Pattern.compile(ASSIGNMENT);
    private static final Pattern MAIN_PATTERN = Pattern.compile(COMBINED_REGEX);
    private static final MathContext DECIMAL128 = MathContext.DECIMAL128;
    private static final BigDecimal MINIMUM = BigDecimal.ONE.movePointLeft(30);
    private final HashMap<String, BigDecimal> variables = new HashMap<>();

    public CalculatorProcessor() {
        variables.put("e", BigDecimalMath.e(DECIMAL128));
        variables.put("pi", BigDecimalMath.pi(DECIMAL128));
    }

    private enum Functions {
        SQUARE_ROOT, NATURAL_LOG, LOG_TEN, EXPONENT, LOGICAL_NOT, NEGATE, SINE,
        COSINE, TANGENT;

        private static final Set<String> functionNames = new HashSet<>(Arrays.asList("sqrt", "log", "log10", "exp", "b-", "!", "sin", "cos", "tan"));

        public static boolean isFunctionName(String s) {
            return s != null && functionNames.contains(s);
        }

        public static Functions of(String c) {
            switch (c) {
                case "sqrt":
                    return SQUARE_ROOT;
                case "log":
                    return NATURAL_LOG;
                case "log10":
                    return LOG_TEN;
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
                default:
                    return null;
            }
        }
    }

    private enum Operators {
        OR(-8), AND(-7), NOT_EQUAL(-6), EQUAL(-6), GREATER(-4), LESS(-4), LESS_EQUAL(-4), GREATER_EQUAL(-4),
        PLUS(-3), MINUS(-3), MULTIPLY(-2), DIVIDE(-2), REMAINDER(-2), POWER(-1), UNKNOWN(-100);

        private final int priority;

        Operators(int p) {
            priority = p;
        }

        private static final Set<String> binaryOperators = new HashSet<>(Arrays.asList("+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=",
                "^", "%", "&", "|"));

        public static boolean isBinaryOperator(String s) {
            return s != null && binaryOperators.contains(s);
        }

        public static Operators of(String c) {
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

        public final int comparePriority(Operators o) {
            return this.priority - o.priority;
        }
    }

    private BigDecimal binaryOperator(BigDecimal right, BigDecimal left, Operators o) {
        switch (o) {
            case DIVIDE:
                return left.divide(right, DECIMAL128);
            case REMAINDER:
                return left.remainder(right, DECIMAL128);
            case MINUS:
                return left.subtract(right, DECIMAL128);
            case PLUS:
                return left.add(right, DECIMAL128);
            case MULTIPLY:
                return left.multiply(right, DECIMAL128);
            case POWER:
                if (right.remainder(BigDecimal.ONE, DECIMAL128).compareTo(BigDecimal.ZERO) == 0) {
                    return left.pow(right.intValue(), DECIMAL128);
                }
                return BigDecimalMath.pow(left, right, DECIMAL128);
            case LESS:
                return right.subtract(left).compareTo(MINIMUM) > 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            case LESS_EQUAL:
                return left.subtract(right).compareTo(MINIMUM) < 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            case GREATER:
                return left.subtract(right).compareTo(MINIMUM) > 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            case GREATER_EQUAL:
                return right.subtract(left).compareTo(MINIMUM) < 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            case EQUAL:
                return right.subtract(left).abs().compareTo(MINIMUM) < 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            case NOT_EQUAL:
                return right.subtract(left).abs().compareTo(MINIMUM) > 0 ? BigDecimal.ZERO : BigDecimal.ONE;
            case AND:
                return left.compareTo(BigDecimal.ZERO) == 0 || right.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.ONE;
            case OR:
                return left.compareTo(BigDecimal.ZERO) == 0 && right.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.ONE;
            default:
                return null;
        }
    }

    private BigDecimal functions(BigDecimal input, Functions f) {
        switch (f) {
            case SQUARE_ROOT:
                return BigDecimalMath.sqrt(input, DECIMAL128);
            case NATURAL_LOG:
                return BigDecimalMath.log(input, DECIMAL128);
            case LOG_TEN:
                return BigDecimalMath.log10(input, DECIMAL128);
            case EXPONENT:
                return BigDecimalMath.exp(input, DECIMAL128);
            case LOGICAL_NOT:
                return input.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            case NEGATE:
                return input.negate();
            case SINE:
                return BigDecimalMath.sin(input, DECIMAL128);
            case COSINE:
                return BigDecimalMath.cos(input, DECIMAL128);
            case TANGENT:
                return BigDecimalMath.tan(input, DECIMAL128);
            default:
                return null;
        }
    }

    /**
     * Process expressions like 3 * --2, (2^4 + 1) / +-5, !(128 % 5 == 0 && 128 % 2
     * == 0). Support operations {@code + - * / % ^ == != >= <= > < && || !}
     *
     * @param input the input expression
     * @return null if it is an ASSIGNMENT_PATTERN, or return the result
     * @throws IllegalArgumentException if the expression is invalid.
     */
    public BigDecimal expression(String input) {
        Matcher isAssignment = ASSIGNMENT_PATTERN.matcher(input);
        if (isAssignment.find()) {
            String LHS = isAssignment.group("variable").replaceAll("\\s*", "");
            if (Functions.isFunctionName(LHS)) {
                throw new PreservedKeywordException(String.format("%s is preserved", LHS));
            }
            String assignment = isAssignment.group("assignment");
            String RHS = isAssignment.group("evaluation");
            switch (assignment) {
                case "+": case "-": case "*": case "/": case "%":
                    if (variables.containsKey(LHS)) {
                        BigDecimal numberOfLHS = variables.get(LHS);
                        variables.put(LHS, binaryOperator(evaluate(RHS), numberOfLHS, Operators.of(assignment)));
                    } else {
                        throw new IllegalArgumentException(
                                String.format("Invalid expression: unknown variable %s", LHS));
                    }
                    break;
                default:
                    variables.put(LHS, evaluate(RHS));
                    break;
            }
        } else {
            Matcher isLegal = EVALUATION_PATTERN.matcher(input);
            if (!isLegal.matches()) {
                throw new IllegalArgumentException("Invalid expression: illegal operators or input");
            }
            BigDecimal result = evaluate(input);
            if (MINIMUM.compareTo(result.abs()) > 0) {
                return BigDecimal.ZERO;
            }
            return evaluate(input).round(MathContext.DECIMAL64).stripTrailingZeros();
        }
        return null;
    }

    private BigDecimal evaluate(String input) {
        Deque<String> postFix = toPosFix(input);
        Deque<BigDecimal> cache = new ArrayDeque<>();
        while (!postFix.isEmpty()) {
            String temp = postFix.pollLast();
            if (Functions.isFunctionName(temp)) {
                Functions f = Functions.of(temp);
                assert f != null;
                try {
                    cache.push(functions(cache.pop(), f));
                } catch (NoSuchElementException e) {
                    throw new IllegalArgumentException("Invalid expression: fail to evaluate function");
                }
            } else if (Operators.isBinaryOperator(temp)) {
                Operators o = Operators.of(temp);
                assert o != null;
                try {
                    cache.push(binaryOperator(cache.pop(), cache.pop(), o));
                } catch (NoSuchElementException e) {
                    throw new IllegalArgumentException("Invalid expression: fail to evaluate operator");
                }
            } else {
                cache.push(new BigDecimal(temp));
            }
        }
        if (cache.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: error");
        }
        return cache.pop();
    }

    private Deque<String> toPosFix(String input) {
        Deque<String> postFix = new ArrayDeque<>();
        Deque<String> cache = new ArrayDeque<>();
        Matcher mainMatcher = MAIN_PATTERN.matcher(input);
        String previousInput = null;
        boolean negativeSign = false;
        boolean logicalNot = false;
        loop: while (!mainMatcher.hitEnd()) {
            if (mainMatcher.find()) {
                String capturedParenthesis = mainMatcher.group("parenthesis");
                String capturedOperator = mainMatcher.group("operator");
                String capturedVariableOrFunction = mainMatcher.group("variable");
                String capturedNumber = mainMatcher.group("number");
                if (capturedOperator != null) {
                    if (Functions.isFunctionName(previousInput)) {
                        throw new IllegalArgumentException("Invalid expression: invalid function call");
                    }
                    if ("!".equals(capturedOperator)) {
                        if ("operand".equals(previousInput) || ")".equals(previousInput)) {
                            throw new IllegalArgumentException("Invalid expression: misplaced logical_not");
                        }
                    }
                    if ("operator".equals(previousInput) || "(".equals(previousInput) || previousInput == null) {
                        switch (capturedOperator) {
                            case "!":
                                logicalNot = !logicalNot;
                                continue loop;
                            case "-":
                                negativeSign = !negativeSign;
                            case "+":
                                continue loop;
                            default:
                                throw new IllegalArgumentException(String.format("Invalid expression: no operand preceding %s",
                                        capturedOperator));
                        }
                    }
                    if (!cache.isEmpty() && !"(".equals(cache.peek()) && !"f(".equals(cache.peek())) {
                        Operators thisO = Operators.of(capturedOperator);
                        assert thisO != null;
                        Operators previousO = Operators.of(cache.peek());
                        assert previousO != null;
                        if (thisO.comparePriority(previousO) <= 0) {
                            while (true) {
                                String temp = cache.pop();
                                postFix.push(temp);
                                if (cache.isEmpty() || "(".equals(cache.peek()) || "f(".equals(cache.peek())) {
                                    break;
                                }
                                Operators o0 = Operators.of(cache.peek());
                                assert o0 != null;
                                if (thisO.comparePriority(o0) > 0) {
                                    break;
                                }
                            }
                        }
                    }
                    cache.push(capturedOperator);
                    previousInput = "operator";
                }

                if (capturedParenthesis != null) {
                    if ("(".equals(capturedParenthesis)) {
                        if ("operand".equals(previousInput) || ")".equals(previousInput)) {
                            throw new IllegalArgumentException("Invalid expression: incomplete expression");
                        }
                        if (logicalNot) {
                            cache.push("!");
                            if (Functions.isFunctionName(previousInput)) {
                                cache.push(previousInput);
                            }
                            cache.push("f(");
                            logicalNot = false;
                        } else if (negativeSign) {
                            cache.push("b-");
                            if (Functions.isFunctionName(previousInput)) {
                                cache.push(previousInput);
                            }
                            cache.push("f(");
                            negativeSign = false;
                        } else if (Functions.isFunctionName(previousInput)) {
                            cache.push(previousInput);
                            cache.push("f(");
                        } else {
                            cache.push("(");
                        }
                    } else {
                        if (Functions.isFunctionName(previousInput) || "operator".equals(previousInput)
                                || "(".equals(previousInput)) {
                            throw new IllegalArgumentException("Invalid expression: incomplete expression");
                        }
                        if (cache.isEmpty()) {
                            throw new IllegalArgumentException(
                                    "Invalid expression: cannot find matching left parenthesis");
                        }
                        while (true) {
                            String temp = cache.pop();
                            if ("(".equals(temp)) {
                                break;
                            }
                            if ("f(".equals(temp)) {
                                postFix.push(cache.pop());
                                break;
                            }
                            if (cache.isEmpty()) {
                                throw new IllegalArgumentException(
                                        "Invalid expression: cannot find matching left parenthesis");
                            }
                            postFix.push(temp);
                        }
                    }
                    previousInput = capturedParenthesis;
                }

                if (capturedVariableOrFunction != null) {
                    if (Functions.isFunctionName(previousInput) || "operand".equals(previousInput) || ")".equals(previousInput)) {
                        throw new IllegalArgumentException("Invalid expression: misplaced function or variable name");
                    }
                    if (Functions.isFunctionName(capturedVariableOrFunction)) {
                        previousInput = capturedVariableOrFunction;
                    } else if (variables.containsKey(capturedVariableOrFunction)) {
                        if (logicalNot) {
                            postFix.push(variables.get(capturedVariableOrFunction).compareTo(BigDecimal.ZERO) == 0 ? "1" : "0");
                            logicalNot = false;
                        } else if (negativeSign) {
                            postFix.push("-" + variables.get(capturedVariableOrFunction).toString());
                            negativeSign = false;
                        } else {
                            postFix.push(variables.get(capturedVariableOrFunction).toString());
                        }
                        previousInput = "operand";
                    } else {
                        throw new IllegalArgumentException(
                                String.format("Invalid expression: unknown variable %s", capturedVariableOrFunction));
                    }
                }

                if (capturedNumber != null) {
                    if (Functions.isFunctionName(previousInput)) {
                        throw new IllegalArgumentException(
                                String.format("Invalid expression: missing ( after %s", capturedVariableOrFunction));
                    }
                    if (logicalNot) {
                        postFix.push("0".equals(capturedNumber) ? "1" : "0");
                        logicalNot = false;
                    } else if (negativeSign) {
                        postFix.push("-" + capturedNumber);
                        negativeSign = false;
                    } else {
                        postFix.push(capturedNumber);
                    }
                    previousInput = "operand";
                }

            } else {
                throw new IllegalArgumentException("Invalid expression: empty expression");
            }
        }
        while (!cache.isEmpty()) {
            String temp = cache.pop();
            if ("(".equals(temp) || "f(".equals(temp)) {
                throw new IllegalArgumentException(
                        "Invalid expression: cannot find matching right parenthesis - missing )?");
            }
            postFix.push(temp);
        }
        return postFix;
    }
}
