---
layout: page
mathjax: true
permalink: operacoesMatrizes/
---

# Operações com Matrizes e Vetores


Tabela de Conteúdos:

- [Criar uma matriz 2 X 4](#crimat)
- [Multiplicar duas matrizes, a primeira com dimensões 2 x 3, e a segunda com dimensões 3 x 4](#matmult)
- [Multiplicar uma matriz com dimensões 2 x 3 por um vetor com dimensão 3](#matvetmult)

> Dica: Após visitar um link da tabela de conteúdos, utilize a tecla de retorno do seu navegador para voltar para a tabela.


Nos códigos que implementam os algoritmos estudados a biblioteca de classes
[org.apache.commons.math3](http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/index.html) será utilizada.  
Especialmente matrizes e vetores serão comumente representados por objetos das classes [RealMatrix](http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/org/apache/commons/math3/linear/RealMatrix.html) e 
[RealVector](http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/org/apache/commons/math3/linear/RealVector.html), respectivamente, desta biblioteca.  
Vamos apresentar o resultado de alguns métodos destas classes.

<a name='crimat'></a>

### Criar uma matriz de dimensões 2 X 4.

Com a linha de comando Java  
```java
RealMatrix M = 
MatrixUtils.createRealMatrix(new double[][] { {1, 2, 3, 4}, {5, 6, 7, 8} });
```
A matriz M(2x4) é criada.  
Observe a notação, M é o nome da matriz, os valores entre parêntesis em seguida significam as dimensões (linhas x colunas), 
sempre nesta ordem.

$$
  M = 
  \left[ {\begin{array}{cccc}
   1 & 2 & 3 & 4\\
   5 & 6 & 7 & 8\\
  \end{array} } \right]
$$

O método estático `createRealMatrix()` da classe [`MatrixUtils`](http://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/linear/MatrixUtils.html) foi utilizado para criar a martriz com os elementos 
já populados, a partir de uma constante do tipo `doube[][]`.

<a name='matmult'></a>

### Multiplicar duas matrizes, a primeira com dimensões 2 x 3, e a segunda com dimensões 3 x 4

Comandos para criar as duas matrizes exemplo em Java e multiplicar uma pela outra:  

```java
RealMatrix M1 = 
MatrixUtils.createRealMatrix(new double[][] { {1, 2, 3}, {4, 5, 6} });

RealMatrix M2 = 
MatrixUtils.createRealMatrix(new double[][] { {1, 2, 3, 4}, 
	{5, 6, 7, 8}, {9, 10, 11, 12 } });

RealMatrix M3 = M1.multiply(M2);
```

$$
	M3 = 
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


##### Comentários sobre a operação multiplicação de matrizes

Definição da operação de multiplicação realizada acima:  
Digamos que a matriz da esquerda da multiplicação tem dimensões (o x p), a da direita dimensões (p x q), 
e a matriz resultante dimensões (o, q).
Então, cada elemento da matriz M3 vai valer $$ M3_{i,j} = \sum\limits_{k=1}^{p} {M1_{i,k} \times M2_{k,j}} $$.  
Ou seja,  
$$ M3_{1,1} = 1 \times 1 + 2 \times 5 + 3 \times 9 = 1 + 10 + 27 = 38 $$.  
$$ M3_{1,2} = 1 \times 2 + 2 \times 6 + 3 \times 10 = 2 + 12 + 30 = 44\\
{}\ldots{} $$.  
$$ M3_{2,1} = 4 \times 1 + 5 \times 5 + 6 \times 9 = 4 + 25 + 54 = 83\\
{}\ldots{} $$.  
$$ M3_{2,4} = 4 \times 4 + 5 \times 8 + 6 \times 12 = 16 + 40 + 72 = 128 $$.

+ Existem definições para mais de uma operação de multiplicação de matrizes.
+ A que mostramos acima pode ser considerada a multiplicação padrão.
+ Só pode ser aplicada se o número de colunas da matriz da esquerda é igual ao número de linhas da matriz da direita.
+ A matriz resultante vai ter o número de linhas da matriz da direita e o número de colunas da matriz da esquerda.
+ Note que a multiplicação de matrizes, assim definida, não é **comutativa** ($$ M1 \times M2 \ne M2 \times M1 $$).


##### Comentários sobre os comandos Java utilizados para a multiplicação de matrizes

Leia a documentação do [org.apache.commons.math3](http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/index.html).
Especialmente as das classes 
[MatrixUtils](http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/org/apache/commons/math3/linear/MatrixUtils.html) e
[RealMatrix](http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/org/apache/commons/math3/linear/RealMatrix.html).
![link](https://github.global.ssl.fastly.net/images/icons/emoji/link.png?v5)  



<a name='matvetmult'></a>

### Multiplicar uma matriz com dimensões 2 x 3 por um vetor com dimensão 3

Comandos para criar o vetor $$v1$$ e multiplicar $$M1 \times v1$$ :  

```java
RealVector v1 = new ArrayRealVector(new double[]{1, 2, 3});
RealVector v2 = M1.operate(v1);
```

$$
	v2 = 
  M1 \times v1 = 
  \left[ {\begin{array}{ccc}
   1 & 2 & 3\\
   4 & 5 & 6\\
  \end{array} } \right] \times 
  \left[ {\begin{array}{c}
   1\\
   2\\
   3\\
  \end{array} } \right] = 
  \left[ {\begin{array}{c}
   14\\
   32\\
  \end{array} } \right]
$$


##### Comentários sobre a operação multiplicação de matriz por vetor

Definição da operação de multiplicação realizada acima:  
Digamos que a matriz da esquerda da multiplicação tem dimensões (o x p), o vetor tem dimensão (p), e o vetor resultante dimensão (o).
Então, os elementos do vetor v2 vão valer $$ v2_i = \sum\limits_{j=1}^{p} {M1_{i,j} \times v1_j}, \mbox{para} i=1, \ldots, o $$.  
Ou seja,  
$$ v2_1 = 1 \times 1 + 2 \times 2 + 3 \times 3 = 1 + 4 + 9 = 14 $$.  
$$ v2_2 = 4 \times 1 + 5 \times 2 + 6 \times 3 = 4 + 10 + 18 = 32 $$.  

+ Existem definições para mais de uma operação de multiplicação de matriz por vetor.
+ A que mostramos acima pode ser considerada a multiplicação padrão.
+ Só pode ser aplicada se o número de colunas da matriz é igual ao número de elementos do vetor.
+ O vetor resultante vai ter dimensão igual ao número de linhas da matriz.


#####  Comentários sobre os comandos Java utilizados para a multiplicação de matriz por vetor

Leia a documentação do [org.apache.commons.math3](http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/index.html).
Especialmente as das classes 
[RealMatrix](http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/org/apache/commons/math3/linear/RealMatrix.html) e
[RealVector](http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/org/apache/commons/math3/linear/RealVector.html).
![link](https://github.global.ssl.fastly.net/images/icons/emoji/link.png?v5)  




