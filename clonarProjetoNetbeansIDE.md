[[retornar ao inicio| Inicio]]

# Clonar o projeto no NetBeans IDE

Recomenda-se a leitura do documento [[Usando Suporte Git no NetBeans IDE | https://netbeans.org/kb/docs/ide/git_pt_BR.html]] :bulb:.

| Clonando o projeto passo a passo com imagens ilustrativas                                       |
| ------------------------------------------------------------------------------------------------|
| 1. Abra o projeto no seu navegador e copie a URL de clonagem HTTPS para a área de transferência, é só clicar com o mouse no ícone :mouse: |
![passo 2](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb02.png)
| 2. No menu principal do NetBeans IDE , selecione: Equipe > Git > Clonar. :world_map: |
![passo 3](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb03.png)
| 3. Cole a URL na caixa URL do Repositório. Selecione mais abaixo o destino do projeto na caixa Clonar para. clique no botão Próximo :mouse: |
![passo 4](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb04.png)
| 4. Aceite a seleção de ramificação remota master. clique no botão Próximo :mouse: |
![passo 5](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb05.png)
| 5. Confira as seleções, provavelmente estão corretas. Deixe a caixa Procurar Projetos do Netbeans após clonagem marcada. clique no botão Finalizar :mouse: |
![passo 6](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb06.png)
| 6.  Observe na caixa de saída do NetBeans IDE que a clonagem deve inciar :eyes: |
![passo 7](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb07.png)
| 7.  Após finalizada, na aba de projetos o projeto clonado deve aparecer. Expanda as pastas para verificar se está como na imagem abaixo. :eyes: |
![passo 8](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb08.png)
| 8. Na aba de Arquivos, a mesma verificação pode ser feita. Observe em especial a pasta data, que contém as bases de dados do CIFAR-10. :eyes: |
![passo 9](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb09.png)
| 9. Na aba de Projetos, clique com o botão direito do mouse sobre NearestNeighbourg.Java e selecione para executar o arquivo. :mouse: |
![passo 10](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb10.png)
| 10. Observe na Janela de Saída a execução, no final o tempo e resultado aparecem. Pode demorar mais de 60 minutos se a execução for com todas as imagens. :eyes: |
![passo 11](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb11.png)
| 11. Durante a execução, uma janela é aberta para mostrar algumas imagens. Claro que não adianta tentar mostrar todas, então, apenas alguns pares de imagens são mostrados. Para melhor observação, são alternados pares onde o algoritmo erra a classificação :-1: com pares em que ele acerta :v: |
![passo 12](https://github.com/duodecimo/duodecimoMachineLearning/blob/master/images/nb12.png)

[[retornar ao inicio| Inicio]]
