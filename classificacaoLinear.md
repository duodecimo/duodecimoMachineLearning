---
layout: page
mathjax: true
permalink: classificacaoLinear/
---


## Classificação Linear


Tabela de Conteúdos:


- [O que faremos em seguida](#emSeguida)
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

### Definindo uma função de resultados

Devemos começar esta abordagem definindo a **função de resultados**, que mapeia os valores dos pixeis de uma imagem para percentuais de confiança
de resultados de classificação possíveis de cada classe. Vamos desenvolver a abordagem com um exemplo concreto. Vamos considerar um conjunto de
imagens de treino $$x_i \in \R^D$$, cada uma associada com uma etiqueta $$y_i$$.



<a name='codigoJava'></a>

### Hora de estudar código Java e testar

Visite a área de código do projeto, estude a classe LinearPrediction.java.

