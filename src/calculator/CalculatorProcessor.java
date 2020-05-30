package calculator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
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
 * Supported <em>unary operators</em>: {@code !, +, -} (logical_not, unary
 * positive, unary negative).
 * </p>
 * <p>
 * Supported <em>binary operators</em>: defined in
 * {@link calculator#BinaryOperators}.
 * </p>
 * <p>
 * Supported <em>assignments</em>: {@code =, +=, -=, *=, /=, %=, ^=} assignments
 * </p>
 * <p>
 * Supported <em>functions</em>: defined in {@link calculator#Functions}.
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
    private static final String PERIOD = ",";
    private static final String OPERATORS = "(\\||&|%|!=|==|>=|<=|[!+\\-*/^><])";
    private static final String COMBINED_REGEX = "(?<variable>" + VARIABLE + ")\\s*|" +
                                                 "(?<number>" + NUMBER + ")\\s*|" +
                                                 "(?<parenthesis>" + PARENTHESIS + ")\\s*|" +
                                                 "(?<period>" + PERIOD + ")\\s*|" +
                                                 "(?<operator>" + OPERATORS + ")\\s*";
    private static final String EVALUATION = "(\\||&|%|!=|==|>=|<=|[a-zA-Z0-9_.!+\\-*/^()>< ,])*";
    private static final String ASSIGNMENT = "^\\s*(?<variable>" + VARIABLE + ")\\s*" +
                                                  "(?<assignment>[+\\-*/%^]?)=\\s*" +
                                                  "(?<evaluation>" + EVALUATION + ")\\s*$";
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("^(?<negate>-)?(?<variable>" + VARIABLE + ")$");
    private static final Pattern EVALUATION_PATTERN = Pattern.compile(EVALUATION);
    private static final Pattern ASSIGNMENT_PATTERN = Pattern.compile(ASSIGNMENT);
    private static final Pattern MAIN_PATTERN = Pattern.compile(COMBINED_REGEX);
    private static final MathContextWithMin MATH_CONTEXT_WITH_MIN = new MathContextWithMin(MathContext.DECIMAL128, BigDecimal.ONE.movePointLeft(30));
    private final HashMap<String, BigDecimal> variables = new HashMap<>();

    public CalculatorProcessor() {
        variables.put("e", BigDecimalMath.e(MATH_CONTEXT_WITH_MIN.getMathContext()));
        variables.put("pi", BigDecimalMath.pi(MATH_CONTEXT_WITH_MIN.getMathContext()));
    }

    /**
     * Process expressions like 3 * --2, (2^4 + 1) / +-5, !(128 % 5 == 0 && 128 % 2
     * == 0). Support operations {@code + - * / % ^ == != >= <= > < && || !}
     *
     * @param input the input expression
     * @return null if it is an ASSIGNMENT_PATTERN, or return the result as String.
     *         Force to use plain output if the absolute value of the result is
     *         between 10^17 and 10^-10.
     * @throws IllegalArgumentException if the expression is invalid.
     */
    public String expression(String input) {
        Matcher isAssignment = ASSIGNMENT_PATTERN.matcher(input);
        if (isAssignment.find()) {
            String LHS = isAssignment.group("variable").replaceAll("\\s*", "");
            if (Functions.isFunctionName(LHS)) {
                throw new PreservedKeywordException(String.format("%s is preserved", LHS));
            }
            String assignment = isAssignment.group("assignment");
            String RHS = isAssignment.group("evaluation");
            switch (assignment) {
                case "+": case "-": case "*": case "/": case "%": case "^":
                    if (variables.containsKey(LHS)) {
                        variables.put(LHS, BinaryOperators.of(assignment).call(variables.get(LHS), evaluate(RHS), MATH_CONTEXT_WITH_MIN));
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
            if (MATH_CONTEXT_WITH_MIN.getMinimum().compareTo(result.abs()) > 0) {
                return "0";
            }
            BigDecimal plainDisplayUpper = BigDecimal.ONE.movePointRight(17);
            BigDecimal plainDisplayLower = BigDecimal.ONE.movePointLeft(10);
            if (plainDisplayUpper.compareTo(result.abs()) > 0 && plainDisplayLower.compareTo(result.abs()) < 0) {
                return result.round(MathContext.DECIMAL64).stripTrailingZeros().toPlainString();
            }
            return result.round(MathContext.DECIMAL64).stripTrailingZeros().toString();
        }
        return null;
    }

    private BigDecimal evaluate(String input) {
        Deque<String> postFix = toPosFix(input);
        Deque<BigDecimal> cache = new LinkedList<>();
        System.out.println(">>>>eval:\n");
        while (!postFix.isEmpty()) {
            String temp = postFix.pollLast();
            if (Functions.isFunctionName(temp)) {
                Functions f = Functions.of(temp);
                assert f != null;
                Deque<BigDecimal> parameters = new ArrayDeque<>(4);
                int paramCount = cache.pop().intValue();
                if (paramCount > 20) {
                    throw new IllegalArgumentException("Invalid expression: too many parameters");
                }
                for (int i = 0; i < paramCount; i++) {
                    parameters.push(cache.pop());
                }
                try {
                    cache.push(f.call(parameters, MATH_CONTEXT_WITH_MIN));
                } catch (UnsupportedOperationException e) {
                    throw new IllegalArgumentException("Invalid expression: invalid number of parameters");
                }
            } else if (BinaryOperators.isBinaryOperator(temp)) {
                BinaryOperators o = BinaryOperators.of(temp);
                assert o != null;
                try {
                    BigDecimal RHS = cache.pop();
                    BigDecimal LHS = cache.pop();
                    cache.push(o.call(LHS, RHS, MATH_CONTEXT_WITH_MIN));
                } catch (NoSuchElementException e) {
                    throw new IllegalArgumentException("Invalid expression: fail to evaluate operator");
                }
            } else {
                Matcher isVariable = VARIABLE_PATTERN.matcher(temp);
                if(isVariable.matches()) {
                    BigDecimal number = variables.get(isVariable.group("variable"));
                    if (isVariable.group("negate") != null) {
                        cache.push(number.negate());
                    } else {
                        cache.push(number);
                    }
                } else {
                    cache.push(new BigDecimal(temp));
                }
            }
            System.out.println("postFix" + postFix);
            System.out.println("deCache" + cache);
            System.out.println();
        }
        if (cache.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: error");
        }
        return cache.pop();
    }

    private Deque<String> toPosFix(String input) {
        Deque<String> postFix = new ArrayDeque<>();
        Deque<String> cache = new ArrayDeque<>();
        Deque<Integer> functionParameterCount = new ArrayDeque<>();
        Matcher mainMatcher = MAIN_PATTERN.matcher(input);
        String previousInput = null;
        boolean negativeSign = false;
        boolean logicalNot = false;
        loop: while (!mainMatcher.hitEnd()) {
            if (mainMatcher.find()) {
                String capturedParenthesis = mainMatcher.group("parenthesis");
                String capturedOperator = mainMatcher.group("operator");
                String capturedPeriod = mainMatcher.group("period");
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
                    if ("operator".equals(previousInput) || "(".equals(previousInput)
                            || "f(".equals(previousInput) || previousInput == null) {
                        switch (capturedOperator) {
                            case "!":
                                logicalNot = !logicalNot;
                                continue loop;
                            case "-":
                                negativeSign = !negativeSign;
                            case "+":
                                continue loop;
                            default:
                                throw new IllegalArgumentException(String.format("Invalid expression: no operand preceding %s", capturedOperator));
                        }
                    }
                    if (!cache.isEmpty() && !"(".equals(cache.peek()) && !"f(".equals(cache.peek())) {
                        BinaryOperators thisO = BinaryOperators.of(capturedOperator);
                        assert thisO != null;
                        BinaryOperators previousO = BinaryOperators.of(cache.peek());
                        assert previousO != null;
                        if (thisO.comparePriority(previousO) <= 0) {
                            while (true) {
                                postFix.push(cache.pop());
                                if (cache.isEmpty() || "(".equals(cache.peek()) || "f(".equals(cache.peek())) {
                                    break;
                                }
                                BinaryOperators o0 = BinaryOperators.of(cache.peek());
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

                if ("(".equals(capturedParenthesis)) {
                    if ("operand".equals(previousInput) || ")".equals(previousInput)) {
                        throw new IllegalArgumentException("Invalid expression: incomplete expression");
                    }
                    if (Functions.isFunctionName(previousInput)) {
                        if (logicalNot) {
                            cache.push("!");
                            logicalNot = false;
                        } else if (negativeSign) {
                            cache.push("b-");
                            negativeSign = false;
                        }
                    functionParameterCount.push(0);
                    cache.push(previousInput);
                    cache.push("f(");
                    previousInput = "f(";
                    } else {
                        cache.push("(");
                        previousInput = "(";
                    }
                }

                if (")".equals(capturedParenthesis)) {
                    if (Functions.isFunctionName(previousInput) || "operator".equals(previousInput) || "(".equals(previousInput) || ",".equals(previousInput)) {
                        throw new IllegalArgumentException("Invalid expression: incomplete expression");
                    }
                    if (cache.isEmpty()) {
                        throw new IllegalArgumentException("Invalid expression: cannot find matching left parenthesis");
                    }
                    while (true) {
                        String temp = cache.pop();
                        if ("(".equals(temp)) {
                            break;
                        }
                        if ("f(".equals(temp)) {
                            postFix.push(functionParameterCount.pop().toString());
                            postFix.push(cache.pop());
                            break;
                        }
                        if (cache.isEmpty()) {
                            throw new IllegalArgumentException("Invalid expression: cannot find matching left parenthesis");
                        }
                        postFix.push(temp);
                    }
                    previousInput = ")";
                }

                if (capturedPeriod != null) {
                    if (cache.isEmpty() || ",".equals(previousInput) || "operator".equals(previousInput) || "(".equals(previousInput)
                            || "f(".equals(previousInput) || Functions.isFunctionName(previousInput)) {
                        throw new IllegalArgumentException("Invalid Expression: invalid period ',' position");
                    }
                    while (true) {
                        String temp = cache.pop();
                        if ("(".equals(temp) || cache.isEmpty()) {
                            throw new IllegalArgumentException("Invalid Expression: period ',' only allowed function");
                        }
                        if ("f(".equals(temp)) {
                            cache.push("f(");
                            break;
                        }
                        postFix.push(temp);
                    }
                    functionParameterCount.push(functionParameterCount.pop() + 1);
                    previousInput = ",";
                }

                if (capturedVariableOrFunction != null) {
                    if (Functions.isFunctionName(previousInput) || "operand".equals(previousInput) || ")".equals(previousInput)) {
                        throw new IllegalArgumentException("Invalid expression: misplaced function or variable name");
                    }
                    if (!functionParameterCount.isEmpty() && functionParameterCount.peek() == 0) {
                        functionParameterCount.pop();
                        functionParameterCount.push(1);
                    }
                    if (Functions.isFunctionName(capturedVariableOrFunction)) {
                        previousInput = capturedVariableOrFunction;
                    } else if (variables.containsKey(capturedVariableOrFunction)) {
                        if (logicalNot) {
                            postFix.push(variables.get(capturedVariableOrFunction).compareTo(BigDecimal.ZERO) == 0 ? "1" : "0");
                            logicalNot = false;
                        } else if (negativeSign) {
                            postFix.push("-" + capturedVariableOrFunction);
                            negativeSign = false;
                        } else {
                            postFix.push(capturedVariableOrFunction);
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
                    if (!functionParameterCount.isEmpty() && functionParameterCount.peek() == 0) {
                        functionParameterCount.pop();
                        functionParameterCount.push(1);
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
            System.out.println("cache: " + cache);
            System.out.println("postFix: " + postFix);
            System.out.println();
        }
        while (!cache.isEmpty()) {
            String temp = cache.pop();
            if ("(".equals(temp) || "f(".equals(temp)) {
                throw new IllegalArgumentException("Invalid expression: cannot find matching right parenthesis - missing )?");
            }
            postFix.push(temp);
        }
        return postFix;
    }
}
