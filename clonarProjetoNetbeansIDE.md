---
layout: page
mathjax: true
permalink: clonarProjetoNetbeansIDE/
---

***

# Clonar o projeto no NetBeans IDE

Recomenda-se a leitura do documento <a href="https://netbeans.org/kb/docs/ide/git_pt_BR.html">Usando Suporte Git no NetBeans IDE</a>
![bulb](https://github.global.ssl.fastly.net/images/icons/emoji/bulb.png?v5).

## Clonando o projeto passo a passo com imagens ilustrativas
  
  
**Passo 1:** Abra o projeto no seu navegador e copie a URL de clonagem HTTPS para a área de transferência, é só clicar com o mouse no ícone, conforme mostra a Figura 1.  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb02.png" width = "600" height="400">
  <div class="figcaption">Figura 1: O projeto no github.</div>
</div>
  
  
**Passo 2:**  No menu principal do NetBeans IDE , selecione: Equipe + Git + Clonar, veja a Figura 2.  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb03.png" width = "600" height="400" >
  <div class="figcaption">Figura 2: O NetBeans IDE.</div>
</div>
  
  
**Passo 3:**  Cole a URL na caixa URL do Repositório. Selecione mais abaixo o destino do projeto na caixa Clonar para. clique no botão Próximo, veja a Figura 3.  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb04.png" width = "600" height="400" >
  <div class="figcaption">Figura 3: Repositório remoto.</div>
</div>
  
  
**Passo 4:**  Aceite a seleção de ramificação remota master. clique no botão Próximo, veja a Figura 4.  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb05.png" width = "600" height="400" >
  <div class="figcaption">Figura 4: Ramificações remotas.</div>
</div>
  
  
**Passo 5:** Confira as seleções, provavelmente estão corretas. Deixe a caixa Procurar Projetos do Netbeans após clonagem marcada. clique no botão Finalizar, veja a Figura 5.  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb06.png" width = "600" height="400" >
  <div class="figcaption">Figura 5: Diretório de destino.</div>
</div>
  
  
**Passo 6:**  Observe na caixa de saída do NetBeans IDE que a clonagem deve inciar, veja a Figura 6.  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb07.png" width = "600" height="400" >
  <div class="figcaption">Figura 6: Detalhe da janela de saída.</div>
</div>
  
**Passo 7:** Após finalizada, na aba de projetos o projeto clonado deve aparecer. Expanda as pastas para verificar se está como na imagem abaixo, veja a Figura 7.  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb08.png" width = "600" height="400" >
  <div class="figcaption">Figura 7: Aba de projetos.</div>
</div>
  
**Passo 8:** Na aba de Arquivos, a mesma verificação pode ser feita. Observe em especial a pasta data, que contém as bases de dados do CIFAR-10, veja a Figura 8.   

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb09.png" width = "600" height="400" >
  <div class="figcaption">Figura 8: Aba de arquivos.</div>
</div>
  
**Passo 9:** Na aba de Projetos, clique com o botão direito do mouse sobre NearestNeighbourg.Java e selecione para executar o arquivo, veja a Figura 9.  
  
<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb10.png" width = "600" height="400" >
  <div class="figcaption">Figura 9: O proejto expandido (estágio inicial).</div>
</div>
  
**Passo 10:** Observe na Janela de Saída a execução, no final o tempo e resultado aparecem. Pode demorar mais de 60 minutos se a execução for com todas as imagens, veja a Figura 10.   
  
<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb11.png" width = "600" height="400" >
  <div class="figcaption">Figura 10: Janela de saída com saída do código nearest neighbourg em execução.</div>
</div>
  
**Passo 11:** Durante a execução, uma janela é aberta para mostrar algumas imagens. Claro que não adianta tentar mostrar todas, então, apenas alguns pares de imagens são mostrados. Para melhor observação, são alternados pares onde o algoritmo erra a classificação ![-1](https://github.global.ssl.fastly.net/images/icons/emoji/-1.png?v5) com pares em que ele acerta ![v](https://github.global.ssl.fastly.net/images/icons/emoji/v.png?v5).  

<div class="fig figcenter fighighlight">
  <img src="https://duodecimo.github.io/duodecimoMachineLearning/assets/images/nb12.png" width = "600" height="400" >
  <div class="figcaption">Figura 11: Janela de saída com algumas imagens do CIFAR-10.</div>
</div>
