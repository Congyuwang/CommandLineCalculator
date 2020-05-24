# Minimalism-Calculator

This is a minimal calculator in Java that allows variable assignment and supports basic functions.

Use up and down arrow key to navigate through history.

Press enter to calculate result.

Click help to see help.

## Help Menu

This is a simple calculator program that allows _variable definition_, and supports _basic functions_ like exponential, logarithm, and square root. The smallest floating number is 1E-30. Any number smaller with an absolute value smaller than that is treated as 0.

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
- ```&gt; &lt; &gt;= &lt;=``` (greater, less, greater equal, less equal)
- ```==, !=``` (equals, not equals)
- ```&``` (logical_and)
- ```|``` (logical_or)

### Supported functions

- ```sqrt()``` (square root)
- ```exp()``` (exponent)
- ```gamma()``` (gamma function)
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

### Predefined variables (can be reassigned if needed)

- ```e``` = 3.141592653589793
- ```pi``` = 2.718281828459045

### Supported assignments

- ```=```
- ```+=```
- ```-=```
- ```*=```
- ```/=```
- ```%```
