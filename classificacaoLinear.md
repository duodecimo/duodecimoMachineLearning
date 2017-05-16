---
layout: page
mathjax: true
permalink: classificacaoLinear/
---


## Classificação Linear


Tabela de Conteúdos:

- [Tipos de aprendizagem](#emSeguida)
- [Hora de estudar código Java e testar](#codigoJava)

> Dica: Após visitar um link da tabela de conteúdos, utilize a tecla de retorno do seu navegador para voltar para a tabela.


<a name='emSeguida'></a>

### O que faremos em seguida


O algoritmo do k-vizinho mais próximo apresenta uma série de desvantagens, além do fraco desempenho. Para cada classificação (uma imagem de teste)
ele precisa comparar a imagem com todas as imagens do treino. Podemos dizer que não existe ganho de generalização em cada treino que ele faz.  

Vamos em seguida desenvolver uma abordagem mais poderosa, e que eventual e naturalmente poderá ser estendida para algoritmos que utilizam redes
neurais e redes convolucionais. A base desta nova abordagem vão ser duas funções. A primeira, **uma função de resultado**, encarregada de mapear
uma imagem para o rótulo que a classifica. A segunda, uma **função de custo**, que mede a eficiência dos pesos (parâmetros) utilizados pela
primeira função em sua tarefa. Então, passamos a tratar do problema de otimização, utilizando os resultados da segunda função para ajustar os
pesos da primeira.


<a name='codigoJava'></a>

### Hora de estudar código Java e testar

Visite a área de código do projeto, estude a classe LinearPrediction.java.

