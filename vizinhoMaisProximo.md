[[retornar ao inicio| Inicio]]


# O algoritmo do vizinho mais próximo


## Sobre o algoritmo do vizinho mais próximo


O algoritmo do vizinho mais próximo, chamado também de **nn** (nearest neighbourg, em inglês) vai ser o primeiro a ser apresentado. Vamos construir um programa destinado a classificar imagens. O **nn** não apresenta desempenho razoável para realizar classificação de imagens. Não quer dizer que seja inútil, na verdade, pode ter uso prático com bons resultados em outras atividades.
A vantagem de começar por ele é que as operações que ele realiza nos dados das imagens são de  baixa complexidade. Desta forma, o **nn** vai nos permitir introduzir código para obter os dados de imagens a partir de arquivos, e código capaz de operar nestas grandes quantidades de dados. É um primeiro passo para aprender a desenvolver algoritmos mais eficientes, como redes neurais convolucionais.
E, apesar do desempenho ser fraco, pode ser observado. Ao executar o **nn** utilizando a base CIFAR-10, ele obtém um percentual de acerto em torno de 30%. Se os acertos fossem obra do acaso, como existem 10 categorias de imagens na base de dados, ficaria em torno de 10%.

## Imagens digitais

Imagens são armazenadas em um computador como um conjunto de pixels. Uma das maneiras mais simples de armazenar imagens digitais coloridas utiliza a forma RGB (vermelho, verde e azul, ou red, green e blue, do inglês). Cada pixel é um ponto da imagem, representando uma cor. Todas as cores são obtidas variando a intensidade de vermelho, verde e azul. Uma cor portanto é codificada através de três números inteiros, que variam de 0 a 255, e que determinam o peso de cada uma das três cores citadas.

Observe uma imagem: | Ao ser digitalizada, se transforma em uma fila de bytes:
---------------------------------------------- | --------------------------------------------------------
![2001](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/2001Monkey.png) |  ![bytes](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/filaDeBytes.png)

* Quando uma pessoa vê uma imagem, percebe cores, formas, identifica objetos.
* Quando um programa de computador _vê_ uma imagem, tem diante de si uma sequência de números.
* Creio que podemos afirmar que pessoas e programas de computador vêm imagens de forma diferente.

Mas, o que realmente importa, não é como cada um observa e processa a imagem, mas sim, se é possível que um programa de computador possa obter resultados semelhantes, ao observar e processar uma imagem, dos resultados e obtidos por uma pessoa.

Construímos programas de aprendizagem de máquina considerando que a resposta a esta pergunta teórica é sim.
Podemos considerar que, se obtivermos resultados semelhantes, a teoria está correta. Provavelmente o próprio [[Alan Turing | https://pt.wikipedia.org/wiki/Alan_Turing]]  concordaria com isso!

Como programas de computadores de aprendizagem de máquina têm sido bem sucedidos, ás vezes com resultados melhores do que as pessoas, em tarefas como classificar images, vamos entender isso como evidência de que, da mesma forma que pessoas percebem cores, formas, e identificam objetos em imagens, algo semelhante ocorre quando um programa de computador _vê_ os números de uma imagem digital.

Comparabilidade é uma propriedade matemática que permite tomar decisões operando valores. Podemos aplicar lógica em dados se eles são comparáveis. Vamos esperar que, assim como uma pessoa vê semelhanças e diferenças ao comparar duas imagens, um programa de computador possa perceber as mesmas semelhanças e diferenças comparando os números digitalizados das mesmas duas imagens.

O que vai portanto determinar os algoritmos que vamos usar em aprendizagem de máquina são as operações matemáticas que vamos aplicar aos números, aos valores.

## Um pouco mais sobre digitalização de imagens

Mencionamos acima que se utilizam mais de uma maneira de codificar as cores das imagens. Além disto, tuiliza-se mais de uma forma para codificar a posição dos pixels de uma imagem digitalizada.

Uma ordem natural seria armazenar os pixels da esquerda para direita, de cima para baixo. Mas outras são utilizadas.

As imagens da base de dados Cifar-10 que vamos utilizar aqui em nosso exemplo armazenam os pixeis em uma ordem um pouco diferente. Isso precisa ser considerado se vamos construir um algoritmo para reproduzir a imagem na tela de um computador. E vai ser, quando formos cuidar disto.

O que queremos estabelecer aqui é outra coisa. Ao aplicar operações matemáticas em dados de duas ou mais imagens diferenças não é necessário que os dados estejam na ordem natural, apenas é necessário que estejam em ordens semelhantes. Comutatividade (a+b = b+a), associatividade (a+(b+c) = ((a+b)+c), e outras propriedades são úteis para explicar estas manobras com exatidão, mas explicá-las foge ao escopo da obra aqui.

## A proposta do algoritmo nn

Ao comparar duas imagens, o algoritmo **nn** pretende obter algo chamado de distancia entre elas.
A distância vai ser a soma de todas as diferenças entre os pixeis nas mesmas posições das imagens.

Uma imagem de 32x32 pixeis na forma RGB, possui 32x32x3 bytes, totalizando 3072 bytes.

Suponha que temos imagens disponíveis em dois conjuntos de imagens distintos, um de teste (**CTE**) e outro de treino (**CTR**).

Vamos considerar duas imagens diferentes, retiradas de cada um destes conjuntos,

![TE e TR](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/TeTr.png)

Vamos representar a sequência dos bytes de cada uma das imagens como (n = 3072) :

![Bytes de TE](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/SeqPte.png)
e
![Bytes de TR](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/SeqPtr.png)

A distância utilizada pelo **nn** vai ser:![Distancia Te Tr](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/SumSeqTeTr.png)

Ou seja, vai ser a soma dos valores absolutos das diferenças de cada um dos bytes de **TE** com cada um dos bytes na posição equivalente de **TR**.

Vamos conjecturar sobre a base desta operação. Se, ao invés de **TE** e **TR** serem imagens diferentes, forem a **mesma imagem**, os bytes nas mesmas posições relativas vão ter exatamente o **mesmo valor**, o resultado de cada uma das diferenças entre eles vai ser ,**0**, e portanto a soma dos valores absolutos das diferenças vai ser **0**.

Faz sentido, a distancia entre duas imagens iguais é **0**! :bulb:

Vamos conjecturar sobre o crescimento indutivo da distância. Imagine que as imagens são praticamente iguais, que apenas o primeiro byte de TE seja diferente do primeiro byte de TR. Digamos, **129** em TE e **118** em TR.
A diferença dos dois é **129-118=11**. Como as demais diferenças vão continuar valendo **0**, a distância entre estas duas imagens vai valer **11**, uma distância pequena.

Faz sentido para duas imagens praticamente iguais! :bulb:

Finalmente, vamos conjecturar porque somamos os valores absolutos das diferenças. Imaginemos que as duas imagens diferentes acima tivessem não apenas os bytes na primeira posição diferentes, mas os da segunda também. E que os bytes da segunda posição fossem **44** e **55**. A diferença dos dois, não absoluta, seria **44-55=-11** (valor negativo). Ao somar as diferenças não absolutas. teríamos **11 + -11 + 0 + 0 + 0 ... = 0**. A distancia calculada seria **0**, como no caso das imagens iguais.

Mas isto não faz sentido, pois as imagens com apenas um byte diferente resultaram em uma distancia de **11**, estas com diferença maior resultaram em **0**, como se fossem iguais? :astonished:

Porém, se somarmos as diferenças absolutas, no caso das imagens com os dois primeiros bytes diferentes, teríamos uma distancia de **11 + 11 + 0 + 0 + 0 ... = 22**.

Agora sim, faz sentido, as imagens com os dois primeiros bytes diferentes resultam em uma distância maior que as imagens com o primeiro byte diferente, e as imagens iguais, resultam na menor distancia, **0**. :v:

## Hora de estudar código Java e testar

A classe [NearestNeighbourgh.java](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/cifar10MachineLearnigCodes/src/ml/NearestNeighbourgh.java) :mouse: está neste repositório git, e utiliza de forma importante a classe [Cifar10Utils.java](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/cifar10MachineLearnigCodes/src/cifar10/Cifar10Utils.java) :mouse:.

Para ver e estudar o código basta clicar nos links acima. Recomenda-se fortemente começar lendo e estudando a classe Cifar10Utils.java, e em seguida a classe NearestNeighbourgh.java. Ambas estão bem comentadas e trazem explicações importantes tanto no aspecto dos dados a serem utilizados, com relação ao algoritmo e também explicações relativas ao código Java. Os comentários estão escritos tanto em **inglês** como em **português**.
Se deseja ler em **português**, basta ir avançando que ao final do texto em inglês encontra- se uma linha, **Portuguese version:**, a partir da qual o comentário é repetido em **português**.


Em seguida, vamos ver como [clonar o projeto utilizando o NetBeans IDE](https://github.com/duodecimo/duodecimoMachineLearning/wiki/clonarProjetoNetbeansIDE) para rodar o código.

Eis um exemplo de resultado obtido ao rodar o NearestNeighbourgh utilizando 100% dos dados do CIFAR-10:
```
Accuracy = 38.59% in 10000
CONSTRUÍDO COM SUCESSO (tempo total: 100 minutos 47 segundos)
```


[[retornar ao inicio| Inicio]]
