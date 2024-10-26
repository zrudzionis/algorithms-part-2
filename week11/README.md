
### Compile
```
javac-algs4 MoveToFront.java CircularSuffixArray.java BurrowsWheeler.java
```

### Run MoveToFront
Encode with hex output:
```
java-algs4 MoveToFront - < abra.txt | java-algs4 edu.princeton.cs.algs4.HexDump 16
```

Encode and decode:
```
java-algs4 MoveToFront - < abra.txt | java-algs4 MoveToFront +
```


### Run CircularSuffixArray
```
java-algs4 CircularSuffixArray
```

### Run BurrowsWheeler
Transform:
```
java-algs4 BurrowsWheeler - < abra.txt | java-algs4 edu.princeton.cs.algs4.HexDump 16
```

Inverse transform:
```
java-algs4 BurrowsWheeler - < abra.txt | java-algs4 BurrowsWheeler +
```

### Zip solution
```
zip burrows.zip MoveToFront.java CircularSuffixArray.java BurrowsWheeler.java
```
