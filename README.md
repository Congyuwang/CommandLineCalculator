# Minimalism-Calculator

This is a simple calculator program that allows _variable definition_,
and supports many useful functions.

Use UP and DOWN to browse input history.
Press ENTER to calculate result.

Use ';' to separate multiple expressions in a line.

## Help Menu

### Keymap

- ```ENTER``` (calculate result)
- ```UP_ARROW``` (previous input)
- ```DOWN_ARROW``` (next input)

### Variable names

Must start with a latin letter, and follows latin letters or numbers and underscores. Examples: ```a1, number_of_apples, alpha__```.

### Supported operators (in descending order of priority)

- ```()``` (parenthesis)
- ```!, +, -``` (logical_not, unary positive, unary negative)
- ```^``` (power)
- ```*, /, %``` (multiply, divide, remainder)
- ```+, -``` (plus, minus)
- ```>, <, >=, <=``` (greater, less, greater equal, less equal)
- ```==, !=``` (equals, not equals)
- ```&``` (logical_and)
- ```|``` (logical_or)

### Supported functions

- ```sqrt()``` (square root)
- ```root(x, n)``` (n-th root)
- ```pow(x, y)``` (power: the same as x^y)
- ```exp()``` (exponent)
- ```gamma()``` (gamma function)
- ```beta(a, b)``` (beta function)
- ```factorial()``` (factorial function)
- ```log()``` (natural logarithm)
- ```log2()``` (logarithm base 2)
- ```log10()``` (logarithm base 10)
- ```sin()``` (sine)
- ```cos()``` (cosine)
- ```tan()``` (tangent)
- ```asin()``` (inverse of sine)
- ```acos()``` (inverse of cosine)
- ```atan()``` (inverse of tangent)
- ```sinh()``` (hyperbolic sine)
- ```cosh()``` (hyperbolic cosine)
- ```tanh()``` (hyperbolic tangent)

#### Probability related functions

##### UniformDistribution

- ```runif()``` random sample from Uniform Distribution [0, 1)
- ```runif(b)``` random sample from Beta Distribution [0, b)
- ```runif(a, b)``` random sample from Beta Distribution [a, b)

##### BetaDistribution

- ```pbeta(q, alpha, beta)``` distribution function of Beta Distribution
- ```dbeta(x, alpha, beta)``` density function of Beta Distribution
- ```qbeta(p, alpha, beta)``` inverse distribution function of Beta Distribution
- ```rbeta(alpha, beta)``` random sample from Beta Distribution

##### GammaDistribution

- ```pgamma(q, shape, scale)``` distribution function of Gamma Distribution
- ```dgamma(x, shape, scale)``` density function of Gamma Distribution
- ```qgamma(p, shape, scale)``` inverse distribution function of Gamma Distribution
- ```rgamma(shape, scale)``` random sample from Gamma Distribution

##### NormalDistribution

- ```pnorm(q)``` distribution function of Normal Distribution (0, 1)
- ```dnorm(x)``` density function of Normal Distribution (0, 1)
- ```qnorm(p)``` inverse distribution function of Normal Distribution (0, 1)
- ```rnorm()``` random sample from Normal Distribution (0, 1)
- ```pnorm(q, mean, sd)``` distribution function of Normal Distribution
- ```dnorm(x, mean, sd)``` density function of Normal Distribution
- ```qnorm(p, mean, sd)``` inverse distribution function of Normal Distribution
- ```rnorm(mean, sd)``` random sample from Normal Distribution

##### BinomialDistribution

- ```pbinom(q, trials, p)``` distribution function of Binomial Distribution
- ```dbinom(x, trials, p)``` density function of Binomial Distribution
- ```qbinom(p, trials, p)``` inverse distribution function of Binomial Distribution
- ```rbinom(trials, p)``` random sample from Binomial Distribution

##### TDistribution

- ```pt(q, degreeOfFreedom)``` distribution function of T Distribution
- ```dt(x, degreeOfFreedom)``` density function of T Distribution
- ```qt(p, degreeOfFreedom)``` inverse distribution function of T Distribution
- ```rt(degreeOfFreedom)``` random sample from T Distribution

##### ChiSquaredDistribution

- ```pchisq(q, degreeOfFreedom)``` distribution function of ChiSquared Distribution
- ```dchisq(x, degreeOfFreedom)``` density function of ChiSquared Distribution
- ```qchisq(p, degreeOfFreedom)``` inverse distribution function of ChiSquared Distribution
- ```rchisq(degreeOfFreedom)``` random sample from ChiSquared Distribution

##### CauchyDistribution

- ```pcauchy(q)``` distribution function of Cauchy Distribution with median = 0, scale = 1
- ```dcauchy(x)``` density function of Cauchy Distribution with median = 0, scale = 1
- ```qcauchy(p)``` inverse distribution function of Cauchy Distribution with median = 0, scale = 1
- ```rcauchy()``` random sample from Cauchy Distribution with median = 0, scale = 1
- ```pcauchy(q, median, scale)``` distribution function of Cauchy Distribution
- ```dcauchy(x, median, scale)``` density function of Cauchy Distribution
- ```qcauchy(p, median, scale)``` inverse distribution function of Cauchy Distribution
- ```rcauchy(median, scale)``` random sample from Cauchy Distribution

##### ExponentialDistribution

- ```pexp(q)``` distribution function of Exponential Distribution with mean = 1
- ```dexp(x)``` density function of Exponential Distribution with mean = 1
- ```qexp(p)``` inverse distribution function of Exponential Distribution with mean = 1
- ```rexp()``` random sample from Exponential Distribution with mean = 1
- ```pexp(q, mean)``` distribution function of Exponential Distribution
- ```dexp(x, mean)``` density function of Exponential Distribution
- ```qexp(p, mean)``` inverse distribution function of Exponential Distribution
- ```rexp(mean)``` random sample from Exponential Distribution

##### FDistribution

- ```pf(q, numeratorDegreeOfFreedom, denominatorDegreeOfFreedom)``` distribution function of F Distribution
- ```df(x, numeratorDegreeOfFreedom, denominatorDegreeOfFreedom)``` density function of F Distribution
- ```qf(p, numeratorDegreeOfFreedom, denominatorDegreeOfFreedom)``` inverse distribution function of F Distribution
- ```rf(numeratorDegreeOfFreedom, denominatorDegreeOfFreedom)``` random sample from F Distribution

##### HypergeometricDistribution

- ```phyper(q, populationSize, numberOfSuccesses, sampleSize)``` distribution function of Hypergeometric Distribution
- ```dhyper(x, populationSize, numberOfSuccesses, sampleSize)``` density function of Hypergeometric Distribution
- ```qhyper(p, populationSize, numberOfSuccesses, sampleSize)``` inverse distribution function of Hypergeometric Distribution
- ```rhyper(populationSize, numberOfSuccesses, sampleSize)``` random sample from Hypergeometric Distribution

### Predefined variables (can be reassigned if needed)

- ```e``` = 3.141592653589793
- ```pi``` = 2.718281828459045

### Supported assignments

- ```=``` normal assignment
- ```+=``` add
- ```-=``` subtract
- ```*=``` multiply
- ```/=``` divide
- ```%=``` remainder
- ```^=``` power

### Other Notes

The smallest positive floating number is 1e-15.
Any number with an absolute value smaller than 1e-15 is treated as 0.
Any number with a difference smaller than 1e-15 are regarded as equal.
