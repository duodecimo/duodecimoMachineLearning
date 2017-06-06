---
layout: page
mathjax: true
permalink: classificacaoLinear/
---


## Classificação Linear


Tabela de Conteúdos:


- [O que faremos em seguida](#emSeguida)
- [Definindo uma função de resultados](#funcaoResultados)
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
por exemplo, temos um conjunto de treinamento com **n** = 50000 imagens, cada uma com **d** = 32X32X3 = 2072 píxeis, e **k** = 10,
já que existem 10 classes distintas (cachorro, gato, carro, etc).Vamos definir em seguida a função de resultados,
$$f:R^d \mapsto R^k$$ que mapeia os píxeis da imagem para resultados das classes.  

Nesta lição vamos começar com o que pode ser considerada a função mais simples, um classificador linear:  

$$f(x_i, W, b) = Wx_i + b$$  

Na quação cima, estamos assumindo que a imagem $$x_i$$ tem todos os píxeis achatados em uma única coluna de um vetor de
dimensionalidade $$[D x 1]$$. A matriz **W** (de tamanho $$[K x D]$$) e o vetor **b** (de tamanho $$[K x 1]$$) são
os parâmetros da função.





<a name='codigoJava'></a>

### Hora de estudar código Java e testar

Visite a área de código do projeto, estude a classe LinearPrediction.java.

