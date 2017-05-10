---
layout: page
mathjax: true
permalink: operacoesMatrizes/
---

# Operações com Matrizes e Vetores


Nos códigos que implementam os algoritmos estudados a biblioteca [org.apache.commons.math3.linear]{http://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/linear/package-summary.html} será utilizada.  
Especialmente matrizes e vetores serão comumente representados por objetos das classes RealMatrix e RealVector, respectivamente, desta biblioteca.  
Vamos apresentar o resultado de alguns métodos destas classes.

### Criar uma matriz 2 X 4.
```
RealMatrix L2C4 = MatrixUtils.createRealMatrix\(new double\[\]\[\]\{\{1, 2, 3, 4\}, \{5, 6, 7, 8\}\})\;
```
A matriz L2C4 é criada:
$$
\[
  L2C4=
  \left[ {\begin{array}{cccc}
   1 & 2 & 3 & 4\\
   5 & 6 & 7 & 8\\
  \end{array} } \right]
\]
$$

