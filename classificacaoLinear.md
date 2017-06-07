---
layout: page
mathjax: true
permalink: classificacaoLinear/
---


<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/linearClassification.png" width = "600" height="400">
  <div class="figcaption">Figura 1: Classificação Linear (Produzida com o Autodraw).</div>
</div>  


## Classificação Linear


Tabela de Conteúdos:


- [O que faremos em seguida](#emSeguida)
- [Definindo uma função de resultados](#funcaoResultados)
- [Interpretando um classificador linear](#InterpretandoCL)
- [Hora de estudar código Java e testar](#codigoJava)

> Dica: Após visitar um link da tabela de conteúdos, utilize a tecla de retorno do seu navegador para voltar para a tabela.


<a name='emSeguida'></a>

### O que faremos em seguida


Na aula anterior foi introduzido o problema de classificar images. Estudamos também dois algorítimos, o **vizinho mais próximo** e o
**k-vizinho mais próximo**, capazes de lidar com a tarefa. Estes algorítimos, porém, apresentam uma série de desvantagens, além do fraco desempenho.
Para cada classificação (uma imagem de teste), precisam comparar a imagem com todas as imagens do treino. Podemos dizer que não existe ganho de 
generalização em cada treino que eles fazem.  

Vamos em seguida desenvolver uma abordagem mais poderosa, e que eventual e naturalmente poderá ser estendida para algorítimos que utilizam redes
neurais e redes convolucionais. A base desta nova abordagem vão ser duas funções. A primeira, **uma função de resultados**, parametrizada,
encarregada de mapear uma imagem para o rótulo que a classifica. A segunda, uma **função de perdas**, também chamada **função de custo**, 
que verifica as predições de resultados contra os resultados reais de cada imagem. Podemos considerar portanto que usamos a **função de perda**
para medir a eficiência dos pesos (parâmetros) utilizados pela **função de resultados** em sua tarefa.
Passaremos então a tratar estes valores obtidos como um problema de minimação, ou seja, vamos tentar minimizar a **função de perdas** com relação
aos parâmetros da **função de resultados**.  


<a name='funcaoResultados'></a>

### Definindo uma função de resultados

Devemos começar esta abordagem definindo a **função de resultados**, que mapeia os valores dos pixeis de uma imagem para percentuais de confiança
de resultados de classificação possíveis de cada classe. Vamos desenvolver a abordagem com um exemplo concreto. Vamos considerar um conjunto de
imagens de treino $$x_i \in R^d$$, cada uma associada com uma etiqueta $$y_i$$, onde $$i = 1 \dots n$$, e $$y_i \in 1 \dots k$$.  
Isto significa que temos **n** exemplos (cada um com dimensionalidade **d**) e **k** categorias distintas. No caso do CIFAR-10,
por exemplo, temos um conjunto de treinamento com **n** = 50000 imagens, cada uma com **d** = 32X32X3 = 3072 pixeis, e **k** = 10,
já que existem 10 classes distintas (cachorro, gato, carro, etc).Vamos definir em seguida a função de resultados,
$$f:R^d \masto R^k$$ que mapeia os pixeis da imagem para resultados das classes.  

Nesta lição vamos começar com o que pode ser considerada a função mais simples, um classificador linear:  

$$f(x_i, W, b) = Wx_i + b$$  

Na equação acima, estamos assumindo que a imagem $$x_i$$ tem todos os píxeis achatados em uma única coluna de um vetor de
dimensionalidade [D x 1]. A matriz **W** (de tamanho [K x D]) e o vetor **b** (de tamanho [K x 1]) são
os parâmetros da função. No CIFAR-10, $$x_i$$ possui todos os bytes da iésima imagem achatados em uma única [3072 x 1]
coluna, **W** é [10 x 3072] e **b** é [10 x 1], portanto, 3072 números entram na função (os pixeis da imagem), e
10 saem (os resultados de cada classe, podemos entender aqui como a chance de cada imagem pertencer a cada classe).
Os parâmetros em **W** são normalmente chamados de **pesos**, e **b** é normalmente chamado de **vetor de bias**, por que
ele influencia os resultados obtidos, sem interagir porém diretamente com os dados em $$x_i$$. Mesmo assim é comum usar
indistintamente os termos parâmetros e pesos.  

Observações importantes:

- Note que a multiplicação de matriz única $$Wx_i$$ está efetivamente calculando 10 classificadores distintos em paralelo,
(um para cada classe), onde cada classificador é uma linha de **W**.
- Note também que consideramos os dados de entrada $$(x_i, y_i)$$ como dados e fixo, porém, temos controle sobre a
atribuição dos valores dos parâmetros **W,b**. Nosso objetivo é atribuir estes valores de tal forma que os resultados
da função coincidam com os valores reais da classificação das imagens em todo o conjunto de entrada. Teremos que aprofundar
bastante aqui, mas podemos inicialmente construir a ideia de que desejamos que a classe correta tenha maior valor nos
resultados do que as demais classes (as erradas).
- Uma vantagem desta abordagem é que os dados de treinamento são utilizados para aprender os parâmetros **W,b**, mas,
uma vez que a aprendizagem estiver completa, podemos descartar os dados de treino e guardar apenas os parâmetros.Uma
imagem de teste pode apenas ser inserida na função e sua classificação baseada nos resultados obtidos.
- Por último, observemos que para classificar uma imagem de teste basta uma multiplicação e adição de matriz, o que é
significativamente mais rápido do que comparar cada pixel da imagem com cada pixel de todas as imagens do conjunto
de treino.  


<a name='InterpretandoCL'></a>

### Interpretando um classificador linear

Um classificador linear calcula o resultado de uma classe através de uma soma, na qual se coloca pesos, dos valores dos
pixeis de uma imagem ao longo de cada um dos seus três canais de cores. Dependendo dos valores que atribuímos a estes
pesos, a função tem a capacidade de concordar ou discordar (dependendo do sinal do peso) de certas cores em determinadas
posições da imagem. Por exemplo, podemos imaginar que um navio tende a ter quantidades de azul em ambos os lados
de sua imagem, correspondentes a água. Portanto, um classificador de navio deve ter mais pesos positivos no canal azul
nas posições ao laterais (presença de azul aumenta o resultado navio), e menos pesos negativos nos canais vermelho e
verde nas mesmas posições (presença de verde e vermelho diminuem o resultado navio).  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/interpretingImages.png" width = "800" height="400">
  <div class="figcaption">Figura 2: Uma imagem é representada digitalmente por pixeis. Os valores de cada byte de cada pixel pode
 ser armazenado em um vetor.</div>
</div>  

No caso do CIFAR-10, armazenamos 50000 imagens de treino, cada uma com 3072 bytes, em uma matriz **X** de dimensões
**[50000 x 3072]**. Armazenamos os pesos em uma matriz **W**, com dimensões **[10 x 3072]**. Finalmente, armazenamos
o bias em um vetor **b** de dimensão **[10 x 1]**.  

A operação de classificação que desejamos executar consistem em multiplicar **W** por **X** e somar **b**.  
Podemos executar a operação de três maneiras, e ainda, podemos simplificar a operação, de maneira que ao invés
de fazermos um produto e uma soma, façamos apenas um produto. Vamos chamar esta simplificação de **truque do bias**.
O **truque do bias**, bem como quaisquer operações de matrizes estão detalhadamente explicados na aula
 [Operações com Matrizes](https://duodecimo.github.io/duodecimoMachineLearning/operacoesMatrizes/).




<a name='codigoJava'></a>

### Hora de estudar código Java e testar

Visite a área de código do projeto, estude a classe LinearPrediction.java.

