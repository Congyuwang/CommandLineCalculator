package calculator;

import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Deque;
import ch.obermuhlner.math.big.BigDecimalMath;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937a;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

/**
 * Define functions supported by the calculator here.
 *
 * @throws UnsupportedOperationException if input parameters has unsupported length
 * @throws ArithmeticException if argument does not meet arithmetic requirements
 * @throws OutOfRangeException if argument is out of range for some functions
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
    }), FLOOR("floor", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return input.subtract(BigDecimalMath.fractionalPart(input));
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
                    .divide(BigDecimalMath.gamma(p.add(q, context.getMathContext()), context.getMathContext()),
                            context.getMathContext())
                    .multiply(BigDecimalMath.gamma(q, context.getMathContext()), context.getMathContext());
        }
    }), SIGMOID("sigmoid", new Function() {
        @Override
        public BigDecimal call(BigDecimal input, MathContextWithMin context) {
            return BigDecimalMath.reciprocal(BigDecimal.ONE
                    .add(BigDecimalMath.exp(input.negate(), context.getMathContext()), context.getMathContext()),
                    context.getMathContext());
        }
    }),

    // probability distribution functions

    P_BETA("pbeta", new Function() {
        @Override
        public BigDecimal call(BigDecimal q, BigDecimal alpha, BigDecimal beta, MathContextWithMin context) {
            BetaDistribution betaDistribution = new BetaDistribution(alpha.doubleValue(), beta.doubleValue(), ACCURACY);
            return new BigDecimal(betaDistribution.cumulativeProbability(q.doubleValue()), context.getMathContext());
        }
    }), P_GAMMA("pgamma", new Function() {
        @Override
        public BigDecimal call(BigDecimal q, BigDecimal shape, BigDecimal scale, MathContextWithMin context) {
            GammaDistribution gammaDistribution = new GammaDistribution(shape.doubleValue(), scale.doubleValue(), ACCURACY);
            return new BigDecimal(gammaDistribution.cumulativeProbability(q.doubleValue()), context.getMathContext());
        }
    }), P_NORMAL("pnorm", new Function() {
        @Override
        public BigDecimal call(BigDecimal q, MathContextWithMin context) {
            NormalDistribution normalDistribution = new NormalDistribution(0.0, 1.0, ACCURACY);
            return new BigDecimal(normalDistribution.cumulativeProbability(q.doubleValue()), context.getMathContext());
        }
        @Override
        public BigDecimal call(BigDecimal q, BigDecimal mean, BigDecimal sd, MathContextWithMin context) {
            NormalDistribution normalDistribution = new NormalDistribution(mean.doubleValue(), sd.doubleValue(), ACCURACY);
            return new BigDecimal(normalDistribution.cumulativeProbability(q.doubleValue()), context.getMathContext());
        }
    }), P_BINOMIAL("pbinom", new Function() {
        @Override
        public BigDecimal call(BigDecimal q, BigDecimal trials, BigDecimal p, MathContextWithMin context) {
            BinomialDistribution binomialDistribution = new BinomialDistribution(trials.intValueExact(), p.doubleValue());
            return new BigDecimal(binomialDistribution.cumulativeProbability(q.subtract(BigDecimalMath.fractionalPart(q)).intValueExact()), context.getMathContext());
        }
    }), P_T("pt", new Function() {
        @Override
        public BigDecimal call(BigDecimal q, BigDecimal degreeOfFreedom, MathContextWithMin context) {
            TDistribution tDistribution = new TDistribution(degreeOfFreedom.intValueExact(), ACCURACY);
            return new BigDecimal(tDistribution.cumulativeProbability(q.doubleValue()), context.getMathContext());
        }
    }), P_CHI_SQUARED("pchisq", new Function() {
        @Override
        public BigDecimal call(BigDecimal q, BigDecimal degreeOfFreedom, MathContextWithMin context) {
            ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(degreeOfFreedom.intValueExact(), ACCURACY);
            return new BigDecimal(chiSquaredDistribution.cumulativeProbability(q.doubleValue()), context.getMathContext());
        }
    }), P_CAUCHY("pcauchy", new Function() {
        @Override
        public BigDecimal call(BigDecimal q, MathContextWithMin context) {
            CauchyDistribution cauchyDistribution = new CauchyDistribution(0.0, 1.0, ACCURACY);
            return new BigDecimal(cauchyDistribution.cumulativeProbability(q.doubleValue()), context.getMathContext());
        }
        @Override
        public BigDecimal call(BigDecimal q, BigDecimal median, BigDecimal scale, MathContextWithMin context) {
            CauchyDistribution cauchyDistribution = new CauchyDistribution(median.doubleValue(), scale.doubleValue(), ACCURACY);
            return new BigDecimal(cauchyDistribution.cumulativeProbability(q.doubleValue()), context.getMathContext());
        }
    }), P_EXPONENTIAL("pexp", new Function() {
        @Override
        public BigDecimal call(BigDecimal q, BigDecimal mean, MathContextWithMin context) {
            ExponentialDistribution exponentialDistribution = new ExponentialDistribution(mean.doubleValue(), ACCURACY);
            return new BigDecimal(exponentialDistribution.cumulativeProbability(q.doubleValue()), context.getMathContext());
        }
    }), P_F("pf", new Function() {
        @Override
        public BigDecimal call(BigDecimal q, BigDecimal numeratorDegreeOfFreedom, BigDecimal denominatorDegreeOfFreedom, MathContextWithMin context) {
            FDistribution fDistribution = new FDistribution(numeratorDegreeOfFreedom.doubleValue(), denominatorDegreeOfFreedom.doubleValue(), ACCURACY);
            return new BigDecimal(fDistribution.cumulativeProbability(q.doubleValue()), context.getMathContext());
        }
    }), P_HYPER_GEOMETRIC("phyper", new Function() {
        @Override
        public BigDecimal call(BigDecimal q, BigDecimal populationSize, BigDecimal numberOfSuccesses, BigDecimal sampleSize, MathContextWithMin context) {
            HypergeometricDistribution hyperGeometricDistribution = new HypergeometricDistribution(populationSize.intValueExact(), numberOfSuccesses.intValueExact(), sampleSize.intValueExact());
            return new BigDecimal(hyperGeometricDistribution.cumulativeProbability(q.subtract(BigDecimalMath.fractionalPart(q)).intValueExact()), context.getMathContext());
        }
    }),

    // probability density functions

    D_BETA("dbeta", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal alpha, BigDecimal beta, MathContextWithMin context) {
            BetaDistribution betaDistribution = new BetaDistribution(alpha.doubleValue(), beta.doubleValue(), ACCURACY);
            return new BigDecimal(betaDistribution.density(x.doubleValue()), context.getMathContext());
        }
    }), D_GAMMA("dgamma", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal shape, BigDecimal scale, MathContextWithMin context) {
            GammaDistribution gammaDistribution = new GammaDistribution(shape.doubleValue(), scale.doubleValue(), ACCURACY);
            return new BigDecimal(gammaDistribution.density(x.doubleValue()), context.getMathContext());
        }
    }), D_NORMAL("dnorm", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, MathContextWithMin context) {
            NormalDistribution normalDistribution = new NormalDistribution(0.0, 1.0, ACCURACY);
            return new BigDecimal(normalDistribution.density(x.doubleValue()), context.getMathContext());
        }
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal mean, BigDecimal sd, MathContextWithMin context) {
            NormalDistribution normalDistribution = new NormalDistribution(mean.doubleValue(), sd.doubleValue(), ACCURACY);
            return new BigDecimal(normalDistribution.density(x.doubleValue()), context.getMathContext());
        }
    }), D_BINOMIAL("dbinom", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal trials, BigDecimal p, MathContextWithMin context) {
            BinomialDistribution binomialDistribution = new BinomialDistribution(trials.intValueExact(), p.doubleValue());
            return new BigDecimal(binomialDistribution.probability(x.intValueExact()), context.getMathContext());
        }
    }), D_T("dt", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal degreeOfFreedom, MathContextWithMin context) {
            TDistribution tDistribution = new TDistribution(degreeOfFreedom.intValueExact(), ACCURACY);
            return new BigDecimal(tDistribution.density(x.doubleValue()), context.getMathContext());
        }
    }), D_CHI_SQUARED("dchisq", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal degreeOfFreedom, MathContextWithMin context) {
            ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(degreeOfFreedom.intValueExact(), ACCURACY);
            return new BigDecimal(chiSquaredDistribution.density(x.doubleValue()), context.getMathContext());
        }
    }), D_CAUCHY("dcauchy", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, MathContextWithMin context) {
            CauchyDistribution cauchyDistribution = new CauchyDistribution(0.0, 1.0, ACCURACY);
            return new BigDecimal(cauchyDistribution.density(x.doubleValue()), context.getMathContext());
        }
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal median, BigDecimal scale, MathContextWithMin context) {
            CauchyDistribution cauchyDistribution = new CauchyDistribution(median.doubleValue(), scale.doubleValue(), ACCURACY);
            return new BigDecimal(cauchyDistribution.density(x.doubleValue()), context.getMathContext());
        }
    }), D_EXPONENTIAL("dexp", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal mean, MathContextWithMin context) {
            ExponentialDistribution exponentialDistribution = new ExponentialDistribution(mean.doubleValue(), ACCURACY);
            return new BigDecimal(exponentialDistribution.density(x.doubleValue()), context.getMathContext());
        }
    }), D_F("df", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal numeratorDegreeOfFreedom, BigDecimal denominatorDegreeOfFreedom, MathContextWithMin context) {
            FDistribution fDistribution = new FDistribution(numeratorDegreeOfFreedom.doubleValue(), denominatorDegreeOfFreedom.doubleValue(), ACCURACY);
            return new BigDecimal(fDistribution.density(x.doubleValue()), context.getMathContext());
        }
    }), D_HYPER_GEOMETRIC("dhyper", new Function() {
        @Override
        public BigDecimal call(BigDecimal x, BigDecimal populationSize, BigDecimal numberOfSuccesses, BigDecimal sampleSize, MathContextWithMin context) {
            HypergeometricDistribution hyperGeometricDistribution = new HypergeometricDistribution(populationSize.intValueExact(), numberOfSuccesses.intValueExact(), sampleSize.intValueExact());
            return new BigDecimal(hyperGeometricDistribution.probability(x.intValueExact()), context.getMathContext());
        }
    }),

    // inverse probability distribution functions

    Q_BETA("qbeta", new Function() {
        @Override
        public BigDecimal call(BigDecimal p, BigDecimal alpha, BigDecimal beta, MathContextWithMin context) {
            BetaDistribution betaDistribution = new BetaDistribution(alpha.doubleValue(), beta.doubleValue(), ACCURACY);
            return new BigDecimal(betaDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
    }), Q_GAMMA("qgamma", new Function() {
        @Override
        public BigDecimal call(BigDecimal p, BigDecimal shape, BigDecimal scale, MathContextWithMin context) {
            GammaDistribution gammaDistribution = new GammaDistribution(shape.doubleValue(), scale.doubleValue(), ACCURACY);
            return new BigDecimal(gammaDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
    }), Q_NORMAL("qnorm", new Function() {
        @Override
        public BigDecimal call(BigDecimal p, MathContextWithMin context) {
            NormalDistribution normalDistribution = new NormalDistribution(0.0, 1.0, ACCURACY);
            return new BigDecimal(normalDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
        @Override
        public BigDecimal call(BigDecimal p, BigDecimal mean, BigDecimal sd, MathContextWithMin context) {
            NormalDistribution normalDistribution = new NormalDistribution(mean.doubleValue(), sd.doubleValue(), ACCURACY);
            return new BigDecimal(normalDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
    }), Q_BINOMIAL("qbinom", new Function() {
        @Override
        public BigDecimal call(BigDecimal P, BigDecimal trials, BigDecimal p, MathContextWithMin context) {
            BinomialDistribution binomialDistribution = new BinomialDistribution(trials.intValueExact(), p.doubleValue());
            return new BigDecimal(binomialDistribution.inverseCumulativeProbability(P.doubleValue()), context.getMathContext());
        }
    }), Q_T("qt", new Function() {
        @Override
        public BigDecimal call(BigDecimal p, BigDecimal degreeOfFreedom, MathContextWithMin context) {
            TDistribution tDistribution = new TDistribution(degreeOfFreedom.intValueExact(), ACCURACY);
            return new BigDecimal(tDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
    }), Q_CHI_SQUARED("qchisq", new Function() {
        @Override
        public BigDecimal call(BigDecimal p, BigDecimal degreeOfFreedom, MathContextWithMin context) {
            ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(degreeOfFreedom.intValueExact(), ACCURACY);
            return new BigDecimal(chiSquaredDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
    }), Q_CAUCHY("qcauchy", new Function() {
        @Override
        public BigDecimal call(BigDecimal p, MathContextWithMin context) {
            CauchyDistribution cauchyDistribution = new CauchyDistribution(0.0, 1.0, ACCURACY);
            return new BigDecimal(cauchyDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
        @Override
        public BigDecimal call(BigDecimal p, BigDecimal median, BigDecimal scale, MathContextWithMin context) {
            CauchyDistribution cauchyDistribution = new CauchyDistribution(median.doubleValue(), scale.doubleValue(), ACCURACY);
            return new BigDecimal(cauchyDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
    }), Q_EXPONENTIAL("qexp", new Function() {
        @Override
        public BigDecimal call(BigDecimal p, BigDecimal mean, MathContextWithMin context) {
            ExponentialDistribution exponentialDistribution = new ExponentialDistribution(mean.doubleValue(), ACCURACY);
            return new BigDecimal(exponentialDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
    }), Q_F("qf", new Function() {
        @Override
        public BigDecimal call(BigDecimal p, BigDecimal numeratorDegreeOfFreedom, BigDecimal denominatorDegreeOfFreedom, MathContextWithMin context) {
            FDistribution fDistribution = new FDistribution(numeratorDegreeOfFreedom.doubleValue(), denominatorDegreeOfFreedom.doubleValue(), ACCURACY);
            return new BigDecimal(fDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
    }), Q_HYPER_GEOMETRIC("qhyper", new Function() {
        @Override
        public BigDecimal call(BigDecimal p, BigDecimal populationSize, BigDecimal numberOfSuccesses, BigDecimal sampleSize, MathContextWithMin context) {
            HypergeometricDistribution hyperGeometricDistribution = new HypergeometricDistribution(populationSize.intValueExact(), numberOfSuccesses.intValueExact(), sampleSize.intValueExact());
            return new BigDecimal(hyperGeometricDistribution.inverseCumulativeProbability(p.doubleValue()), context.getMathContext());
        }
    }),

    // distributions random sampling

    R_UNIFORM("runif", new Function() {

        @Override
        public BigDecimal call(MathContextWithMin context) {
            return new BigDecimal(RANDOM.nextDouble(), context.getMathContext());
        }

        @Override
        public BigDecimal call(BigDecimal high, MathContextWithMin context) {
            if (BigDecimal.ZERO.compareTo(high) >= 0) {
                throw new ArithmeticException("high must be greater than zero");
            }
            return high.multiply(
                            new BigDecimal(RANDOM.nextDouble(), context.getMathContext()), context.getMathContext());
        }

        @Override
        public BigDecimal call(BigDecimal low, BigDecimal high, MathContextWithMin context) {
            if (low.compareTo(high) >= 0) {
                throw new ArithmeticException("high must be greater than low");
            }
            return low.add(
                    high.subtract(low, context.getMathContext()).multiply(
                            new BigDecimal(RANDOM.nextDouble(), context.getMathContext()), context.getMathContext()),
                    context.getMathContext());
        }

    }), R_BETA("rbeta", new Function() {
        @Override
        public BigDecimal call(BigDecimal alpha, BigDecimal beta, MathContextWithMin context) {
            BetaDistribution betaDistribution = new BetaDistribution(RANDOM, alpha.doubleValue(), beta.doubleValue(), ACCURACY);
            return new BigDecimal(betaDistribution.sample(), context.getMathContext());
        }
    }), R_GAMMA("rgamma", new Function() {
        @Override
        public BigDecimal call(BigDecimal shape, BigDecimal scale, MathContextWithMin context) {
            GammaDistribution gammaDistribution = new GammaDistribution(RANDOM, shape.doubleValue(), scale.doubleValue(), ACCURACY);
            return new BigDecimal(gammaDistribution.sample(), context.getMathContext());
        }
    }), R_NORMAL("rnorm", new Function() {
        @Override
        public BigDecimal call(MathContextWithMin context) {
            NormalDistribution normalDistribution = new NormalDistribution(RANDOM, 0.0, 1.0, ACCURACY);
            return new BigDecimal(normalDistribution.sample(), context.getMathContext());
        }
        @Override
        public BigDecimal call(BigDecimal mean, BigDecimal sd, MathContextWithMin context) {
            NormalDistribution normalDistribution = new NormalDistribution(RANDOM, mean.doubleValue(), sd.doubleValue(), ACCURACY);
            return new BigDecimal(normalDistribution.sample(), context.getMathContext());
        }
    }), R_BINOMIAL("rbinom", new Function() {
        @Override
        public BigDecimal call(BigDecimal trials, BigDecimal p, MathContextWithMin context) {
            BinomialDistribution binomialDistribution = new BinomialDistribution(RANDOM, trials.intValueExact(), p.doubleValue());
            return new BigDecimal(binomialDistribution.sample(), context.getMathContext());
        }
    }), R_T("rt", new Function() {
        @Override
        public BigDecimal call(BigDecimal degreeOfFreedom, MathContextWithMin context) {
            TDistribution tDistribution = new TDistribution(RANDOM, degreeOfFreedom.intValueExact(), ACCURACY);
            return new BigDecimal(tDistribution.sample(), context.getMathContext());
        }
    }), R_CHI_SQUARED("rchisq", new Function() {
        @Override
        public BigDecimal call(BigDecimal degreeOfFreedom, MathContextWithMin context) {
            ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(RANDOM,
                    degreeOfFreedom.intValueExact(), ACCURACY);
            return new BigDecimal(chiSquaredDistribution.sample(), context.getMathContext());
        }
    }), R_CAUCHY("rcauchy", new Function() {
        @Override
        public BigDecimal call(MathContextWithMin context) {
            CauchyDistribution cauchyDistribution = new CauchyDistribution(RANDOM, 0.0, 1.0, ACCURACY);
            return new BigDecimal(cauchyDistribution.sample(), context.getMathContext());
        }
        @Override
        public BigDecimal call(BigDecimal median, BigDecimal scale, MathContextWithMin context) {
            CauchyDistribution cauchyDistribution = new CauchyDistribution(RANDOM, median.doubleValue(), scale.doubleValue(), ACCURACY);
            return new BigDecimal(cauchyDistribution.sample(), context.getMathContext());
        }
    }), R_EXPONENTIAL("rexp", new Function() {
        @Override
        public BigDecimal call(BigDecimal mean, MathContextWithMin context) {
            ExponentialDistribution exponentialDistribution = new ExponentialDistribution(RANDOM, mean.doubleValue(), ACCURACY);
            return new BigDecimal(exponentialDistribution.sample(), context.getMathContext());
        }
    }), R_F("rf", new Function() {
        @Override
        public BigDecimal call(BigDecimal numeratorDegreeOfFreedom, BigDecimal denominatorDegreeOfFreedom,
                MathContextWithMin context) {
            FDistribution fDistribution = new FDistribution(RANDOM, numeratorDegreeOfFreedom.doubleValue(),
                    denominatorDegreeOfFreedom.doubleValue(), ACCURACY);
            return new BigDecimal(fDistribution.sample(), context.getMathContext());
        }
    }), R_HYPER_GEOMETRIC("rhyper", new Function() {
        @Override
        public BigDecimal call(BigDecimal populationSize, BigDecimal numberOfSuccesses, BigDecimal sampleSize,
                MathContextWithMin context) {
            HypergeometricDistribution hyperGeometricDistribution = new HypergeometricDistribution(RANDOM,
                    populationSize.intValueExact(), numberOfSuccesses.intValueExact(), sampleSize.intValueExact());
            return new BigDecimal(hyperGeometricDistribution.sample(), context.getMathContext());
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
    private static final RandomGenerator RANDOM = new Well19937a();
    private static final double ACCURACY = 1e-16;

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
