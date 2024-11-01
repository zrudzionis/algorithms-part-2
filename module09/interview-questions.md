# Question #1.
Cyclic rotation of a string.
A string *s* is a cyclic rotation of a string *t* if *s* and *t* have the same length
and *s* consists of a suffix of *t* followed by a prefix of *t*.
For example, "winterbreak" is a cyclic rotation of "breakwinter" (and vice versa).
Design a linear-time algorithm to determine whether one string is a cyclic rotation of another.

## Answer
We can use KMP algorithm. Construct a pattern from *T* and try to match *S*. We make a cyclic match by matching substring from index *L* (inclusive) to the end of *S*. Once end of *S* is reached we continue from start of *S* until *L - 1*.

Runtime is *O(T + S)* where *T* is pattern length and *S* is the other string length. Preprocessing takes *O(R * T)* time where *R* is alphabet size (usually A-Z).
Memory is *O(R * T)*.

# Question #2.
Tandem repeat.
A tandem repeat of a base string *b* within a string *s* is a substring of *s* consisting of at least one consecutive copy of the base string 
*b*.
Given *b* and *s*, design an algorithm to find a tandem repeat of *b* within *s* of maximum length. Your algorithm should run in time proportional to *M + N*, where *M* is length of *b* and *N* is the length *s*.

For example, if *s* is "abcabcababcaba" and *b* is "abcab", then "abcababcab" is the tandem substring of maximum length (2 copies).

## Answer
We can use KMP algorithm. Construct a pattern from *B* and try to match *S*. Once we have a full match on *B* we can go back to starting one and from there continue matching.
While we are matching we should keep track of number of matched characters.

Runtime is *O(N + M)* where *N* is pattern length and M is the other string length.
Preprocessing takes *O(R * N)* time where R is alphabet size (usually A-Z).
Memory is *O(R * N)*.

# Question #3.
Longest palindromic substring. Given a string *s*, find the longest substring that is a palindrome in expected linearithmic time.

Signing bonus: Do it in linear time in the worst case.

## Answer
Manacher's Algorithm. Runtime *O(N)* where *N* is the length of *S*.