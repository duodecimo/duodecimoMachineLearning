[[retornar ao início|Inicio]]


# Como e porque utilizar o conjunto de dados Cifar-10


O CIFAR-10 e o CIFAR-100 são conjuntos rotulados (com nomes) de 80 milhões de pequenas imagens.
Forma coletados por Alex Krizhevsky, Vinod Nair, and Geoffrey Hinton.
visite: https://www.cs.toronto.edu/~kriz/cifar.html

O CIFAR-10 consiste de um conjunto de dados de 60000 imagens coloridas de 32x32 pixeis, contidas em 10 classes, com 6000 imagens por classe. Existem 50000 conjuntos de imagens para treino e 10000 imagens para testes.
O conjunto de dados é dividido em 5 grupos (arquivos) de treino e um grupo (arquivo) de testes, cada um com 
10000 imagens. O grupo de testes contém exatamente 1000 imagens selecionadas aleatoriamente de cada classe. O grupo de treino contém as imagens restantes em ordem aleatória, mas alguns grupos de treino podem conter mais imagens de uma classe do que de outra. No conjunto do grupo de treino, os grupos contém exatamente 50000 imagens de cada classe.

A classe Cifar10Utils (código disponível neste git) foi construída para lidar com a versão binária (existem outras) do CIFAR-10, baixada de https://www.cs.toronto.edu/~kriz/cifar.html.

A versão binária contém os arquivos data_batch_1.bin, data_batch_2.bin, ..., data_batch_5.bin, bem como o test_batch.bin. Cda um destes arquivos é formatado da seguinte maneira:

Várias linhas, e em cada linha:
<1 x rótulo><3072 x pixel>

Em outras palavras, o primeiro byte é o rótulo (código do rótulo ou nome) da primeira imagem, que é um número entre 0-9. Os próximos 3073 bytes são os valores dos pixeis da imagem. Os primeiros 1024 bytes são os valores do canal vermelho, os próximos 1024 bytes os valores do canal verde, e os últimos 1024 bytes os valores do canal azul. Os valores são armazenados em ordem crescente dentro da linha, portanto, os primeiros 32 bytes da linha correspondem aos primeiros 32 bytes da imagem.

Cada arquivo contém 10000 destas linhas de 3073 bytes de imagens, apesar de não haver nenhum simbolo delimitando as linhas. Portanto, cada arquivo deve possuir exatamente 30730000 bytes de tamanho.

Existe ainda um outro arquivo, chamado batches.meta.txt. É um arquivo ASCII que mapeia os rótulos numéricos no intervalo 0-9 para nomes de classe significativos. é simplesmente uma lista dos nomes das 10 classes, uma em cada linha. O nome da classe na linha i corresponde ao rótulo numero i.


[[retornar ao início|Inicio]]
