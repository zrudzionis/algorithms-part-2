# Question #1.
Question 1
Prefix free codes. In data compression, a set of binary strings is  if no string is a prefix of another. For example, {01,10,0010,1111} is prefix free, but {01,10,0010,10100} is not because 10 is a prefix of 10100. Design an efficient algorithm to determine if a set of binary strings is prefix-free. The running time of your algorithm should be proportional the number of bits in all of the binary stings.

## Answer
2-way (binary) trie.
Node that finish some binary string have value 1 and others have value 0. Insert binary strings one by one, if while inserting we encounter a node that has 1 it means that set of strings are not prefix free.

Running time *O(n)*.  
*n* - is the number of bits in binary strings.


# Question #2.
Boggle.

Boggle is a word game played on an 4-by-4 grid of tiles, where each tile contains one letter in the alphabet.
The goal is to find all words in the dictionary that can be made by following a path of adjacent tiles (with no tile repeated),
where two tiles are adjacent if they are horizontal, vertical, or diagonal neighbors.

## Answer
TODO

# Question #3.
Suffix trees.

Learn about and implement , the ultimate string searching data structure.

## Answer
