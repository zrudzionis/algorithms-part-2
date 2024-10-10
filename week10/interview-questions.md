# Question #1.
Challenging REs. Construct a regular expression for each of the following languages over the binary alphabet or prove that no such regular expression is possible:

1. All strings except 11 or 111.
2. Strings with 1 in every odd-number bit position.
3. Strings with an equal number of 0s and 1s.
4. Strings with at least two 0s and at most one 1.
5. Strings that when interpreted as a binary integer are a multiple of 3.
6. Strings with no two consecutive 1s.
7. Strings that are palindromes (same forwards and backwards).
8. Strings with an equal number of substrings of the form 01 and 10.

## Answer
1. `(0(0|1)*)|(1$)|(10(0|1)*)|(110(0|1)*)`. Here `$` is end of line symbol.
2. `1(01)*0?`. Here `?` is 0 or 1 symbols.
3. Not possible since it requires memory. We need to keep track of 0s and 1s count.
4. `(100+)|(00+1?0*)|(010+)`.
5. Not possible since it requires memory. To determine if a binary number is a multiple of 3, we can use a remainder-based rule that doesn't require converting the binary number to decimal. Track alternating sums: Traverse the binary number from the least significant bit (rightmost) to the most significant bit (leftmost). Keep two sums:
   * Sum of bits at odd positions.
   * Sum of bits at even positions.
Subtract the two sums: Once you've traversed the binary number, subtract the sum of the even-position bits from the sum of the odd-position bits. If the result is divisible by 3, then the binary number is a multiple of 3. This requires memory.
6. `(1$)|(0+10*)*|(10+)*`.
7. Not possible since it requires memory. We need to keep track of 0s and 1s count.
8. Not possible since it requires memory. We need to keep track of 01s and 10s count.

# Question #2.
Exponential-size DFA.
Design a regular expressions of length n such that any DFA that recognizes the same language has an exponential number of states.
## Answer
TODO

# Question #3.
Extensions to NFA. Add to [NFA.java](http://algs4.cs.princeton.edu/54regexp/NFA.java.html) the ability to handle multiway or, wildcard, and the + closure operator.

## Answer
TODO
