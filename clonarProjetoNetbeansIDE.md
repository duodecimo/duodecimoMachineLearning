---
layout: page
mathjax: true
permalink: clonarProjetoNetbeansIDE/
---

***

# Clonar o projeto no NetBeans IDE

Recomenda-se a leitura do documento ![Usando Suporte Git no NetBeans IDE](https://netbeans.org/kb/docs/ide/git_pt_BR.html)
![bulb](https://github.global.ssl.fastly.net/images/icons/emoji/bulb.png?v5).

## Clonando o projeto passo a passo com imagens ilustrativas

Passo 1:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb02.png" width = "600" height="400">
  <div class="figcaption">Abra o projeto no seu navegador e copie a URL de clonagem HTTPS para a área de transferência, é só clicar com o mouse no ícone</div>
</div>

Passo 2:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb03.png" width = "600" height="400" >
  <div class="figcaption">No menu principal do NetBeans IDE , selecione: Equipe + Git + Clonar.</div>
</div>

Passo 3:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb04.png" width = "600" height="400" >
  <div class="figcaption">Cole a URL na caixa URL do Repositório. Selecione mais abaixo o destino do projeto na caixa Clonar para. clique no botão Próximo.</div>
</div>

Passo 4:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb05.png" width = "600" height="400" >
  <div class="figcaption">Aceite a seleção de ramificação remota master. clique no botão Próximo.</div>
</div>

Passo 5:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb06.png" width = "600" height="400" >
  <div class="figcaption">Confira as seleções, provavelmente estão corretas. Deixe a caixa Procurar Projetos do Netbeans após clonagem marcada. clique no botão Finalizar.</div>
</div>

Passo 6:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb07.png" width = "600" height="400" >
  <div class="figcaption">Observe na caixa de saída do NetBeans IDE que a clonagem deve inciar.</div>
</div>

Passo 7:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb08.png" width = "600" height="400" >
  <div class="figcaption">Após finalizada, na aba de projetos o projeto clonado deve aparecer. Expanda as pastas para verificar se está como na imagem abaixo.</div>
</div>

Passo 8:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb09.png" width = "600" height="400" >
  <div class="figcaption">Na aba de Arquivos, a mesma verificação pode ser feita. Observe em especial a pasta data, que contém as bases de dados do CIFAR-10..</div>
</div>

Passo 9:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb10.png" width = "600" height="400" >
  <div class="figcaption">Na aba de Projetos, clique com o botão direito do mouse sobre NearestNeighbourg.Java e selecione para executar o arquivo.</div>
</div>

Passo 10:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb11.png" width = "600" height="400" >
  <div class="figcaption">Observe na Janela de Saída a execução, no final o tempo e resultado aparecem. Pode demorar mais de 60 minutos se a execução for com todas as imagens.</div>
</div>

Passo 11:  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb12.png" width = "600" height="400" >
  <div class="figcaption">Figura 11: Durante a execução, uma janela é aberta para mostrar algumas imagens. Claro que não adianta tentar mostrar todas, então, apenas alguns pares de imagens são mostrados. Para melhor observação, são alternados pares onde o algoritmo erra a classificação ![-1](https://github.global.ssl.fastly.net/images/icons/emoji/-1.png?v5) com pares em que ele acerta ![v](https://github.global.ssl.fastly.net/images/icons/emoji/v.png?v5).</div>
</div>
