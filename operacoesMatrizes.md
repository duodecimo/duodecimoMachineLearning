---
layout: page
mathjax: true
permalink: operacoesMatrizes/
---

# Operações com Matrizes e Vetores


Nos códigos que implementam os algoritmos estudados a biblioteca 
[org.apache.commons.math3.linear](http://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/linear/package-summary.html) será utilizada.  
Especialmente matrizes e vetores serão comumente representados por objetos das classes [RealMatrix](http://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/linear/RealMatrix.html) e 
[RealVector](http://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/linear/RealVector.html), respectivamente, desta biblioteca.  
Vamos apresentar o resultado de alguns métodos destas classes.

### Criar uma matriz 2 X 4.

Com a linha de comando Java  
```java
RealMatrix M = 
MatrixUtils.createRealMatrix(new double[][] { {1, 2, 3, 4}, {5, 6, 7, 8} });
```
A matriz M(2x4) é criada.  
Observe a notação, M é o nome da matriz, os valores entre parêntesis em seguida significam (linhas x colunas).
Sempre nesta ordem.

$$
  M = 
  \left[ {\begin{array}{cccc}
   1 & 2 & 3 & 4\\
   5 & 6 & 7 & 8\\
  \end{array} } \right]
$$

O método estático `createRealMatrix()` da classe [`MatrixUtils`](http://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/linear/MatrixUtils.html) foi utilizado para criar a martriz com os elementos 
já populados, a partir de uma constante do tipo `doube[][]`.

### Multiplicar duas matrizes, a primeira 2 x 3, e a segunda 3 x 4

Comandos para criar as duas matrizes exemplo em Java:  

```java
RealMatrix M1 = 
MatrixUtils.createRealMatrix(new double[][] { {1, 2, 3}, {4, 5, 6} });

RealMatrix M2 = 
MatrixUtils.createRealMatrix(new double[][] { {1, 2, 3, 4}, 
	{5, 6, 7, 8}, {9, 10, 11, 12 } });

RealMatrix M3 = M1.multiply(M2);
```

$$
  M1 \times M2 = 
  \left[ {\begin{array}{ccc}
   1 & 2 & 3\\
   4 & 5 & 6\\
  \end{array} } \right] \times 
  \left[ {\begin{array}{cccc}
   1 & 2 & 3 & 4\\
   5 & 6 & 7 & 8\\
   9 & 10 & 11 & 12\\
  \end{array} } \right] = 
  \left[ {\begin{array}{cccc}
   38 & 44 & 50 & 56\\
   83 & 98 & 113 & 128\\
  \end{array} } \right]
$$


