/* Cabecalho:
 * Essa eh a classe que tem a responsabilidade de controlar o movimento das pecas,
 * de garantir a configuracao correta das pecas e das posicoes, por reconhecer o xeque ou xeque-mate, 
 * mandando sinais que serao tratados pela classe Jogo, além de imprimir o tabuleiro em si.
 * Ela possui a matriz de posicoes, e sempre armazena as posicoes dos dois reis.
 */
package xadrez;

/**
 *
 * @author dougl
 */
public class Tabuleiro {
    /* Atributos da classe */
    private Posicao[][] pos = new Posicao[8][8]; //matriz de posicoes do tabuleiro
    // Atributos auxiliares para armazenar as posicoes dos reis
    private Posicao posReiBranco;    //armazena a posicao do rei branco
    private Posicao posReiPreto; //armazena a posicao do rei preto
    
    /* Metodos getters e setters */
    // Recupera a posicao do rei da cor que foi passada por parametro
    /* Parametros:
     * cor = cor do rei
     */
    private Posicao getPosRei(char cor) {
        if(cor == '+') {
            return this.posReiBranco;
        } else if(cor == '*') {
            return this.posReiPreto;
        } else {
            return null;
        }
    }
    
    // Retorna informacoes sobre tal posicao em formato de string
    /* Parametros:
     * linha = linha da posicao
     * coluna = coluna da posicao
     */
    public String getInfoPosicao(int linha, int coluna) {
        String info;    //string com as informacoes
        
        // Se essa posicao estiver ocupada
        if(this.pos[linha][coluna].isOcupada()) {
            // A primeira palavra da string sera o caractere que representa a peca
            info = String.valueOf(this.pos[linha][coluna].getPeca().desenho());
        } else {
            // Caso, contrario apenas coloca o caractere X
            info = String.valueOf('X');
        }
        
        return info;
    }
    
    /* Construtor */
    public Tabuleiro() {
        // Loop para inicializar todas as posicoes
        for(int i = 7; i >= 0; i--) {
            for(int j = 0; j < 8; j++) {
                this.pos[i][j] = new Posicao((char)(42 + ((j+i) % 2)), i, (char)j); //instanciamento
            }
        }
        
        // Inicializa as posicoes do rei com a referencia em valor null
        this.posReiBranco = null;
        this.posReiPreto = null;
    }
    
    /* Metodos gerais */
    // Checa se o movimento for valido, em caso positivo, o faz
    /* Parametros:
     * lOrigem e cOrigem = linha e coluna da posicao de origem, respectivamente
     * lDestino e cDestino = linha e coluna da posicao de destino, respectivamente
     * cor = cor das pecas do jogador desse turno
     */
    public boolean movimento(int lOrigem, int cOrigem, int lDestino, int cDestino, char cor) {
        // Conversao das coordenadas para os indices reais das posicoes
        lOrigem -= 1;
        cOrigem -= 97;
        lDestino -= 1;
        cDestino -= 97;
        
        // Checa se for escolhida uma posicao fora do tabuleiro
        if( (lOrigem < 0) || (lOrigem > 7) || (cOrigem < 0) || (cOrigem > 7) ||
            (lDestino < 0) || (lDestino > 7) || (cDestino < 0) || (cDestino > 7) ) {
            System.out.println("Movimento invalido (fora do tabuleiro)!");
            return false;
        }
        
        // Checa se existe uma peca no local de origem do movimento
        if(!this.pos[lOrigem][cOrigem].isOcupada()) {
            System.out.println("O local de origem nao possui uma peca...");
            return false;
        }
        
        // Checa se a peca que se deseja mover eh do jogador
        if(this.pos[lOrigem][cOrigem].getPeca().getCor() != cor) {
            System.out.println("Essa peca nao eh sua!");
            return false;
        }
        
        // Checa se o movimento eh valido para essa peca
        int condMovimento = this.pos[lOrigem][cOrigem].getPeca().checaMovimento(lOrigem, (char)cOrigem, lDestino, (char)cDestino);
        if(condMovimento == 0) {
            System.out.println("Movimento invalido para essa peca!");
            return false;
        }
        // Caso especial para o peao
        if( (condMovimento == 3) && !this.pos[lDestino][cDestino].isOcupada() ) {
            System.out.println("Movimento invalido para o peao (nao ha peca para ele capturar)!");
            return false;
        }
        
        // Se estiver movimentando o rei
        if(this.pos[lOrigem][cOrigem] == getPosRei(cor)) {
            // E se a posicao de destino nao estiver ocupada
            if(!this.pos[lDestino][cDestino].isOcupada()) {
                // E estiver marcada como controlada por inimigo
                if(this.pos[lDestino][cDestino].isXeque(cor)) {
                    System.out.println("Movimento invalido para o rei (casa controlada por peca adversaria)!");
                    return false;
                }
            }
        }
        
        /* Checagem de obstaculos no caminho do movimento */
        Posicao condObstaculo = caminhoObstaculo(lOrigem, cOrigem, lDestino, cDestino);
        // Caso em que nao ha obstaculo
        if(condObstaculo == null) {
            // Se for o rei, tambem atualiza sua posicao no atributo auxiliar que segura sua posicao especifica
            if(this.pos[lOrigem][cOrigem] == getPosRei(cor))
                atualizaPosRei(lDestino, cDestino, cor);
                
            movePeca(lOrigem, cOrigem, lDestino, cDestino); //movimenta a peca
            return true;
        } else {    // Caso com obstaculo
            // Se o obstaculo estiver exatamente na posicao de destino
            if( (condObstaculo.getLinha() == lDestino) && (condObstaculo.getColuna() == cDestino) ) {
                // Se a peca no destino eh aliada
                if(condObstaculo.getPeca().getCor() == this.pos[lOrigem][cOrigem].getPeca().getCor()) {
                    System.out.println("Movimento invalido (posicao de destino ocupada por peca aliada)!");
                    return false;
                } else {    // Se a peca eh inimiga
                    // Tratamento especial do peao
                    if(condMovimento == 2) {
                        System.out.println("O peao nao pode capturar pecas a sua frente!");
                        return false;
                    }
                    // Tratamento especial do rei
                    if(this.pos[lOrigem][cOrigem] == getPosRei(cor)) {
                        // Caso em que a peca esta numa posicao de mesma cor (protegida)
                        if(this.pos[lDestino][cDestino].getPeca().getCor() == this.pos[lDestino][cDestino].getCor()) {
                            System.out.println("O rei nao pode capturar pecas protegidas!");
                            return false;
                        } else {    // Se a peca pode ser capturada, atualiza o armazenamento da posicao do rei
                            atualizaPosRei(lDestino, cDestino, cor);
                        }
                    }

                    this.pos[lDestino][cDestino].getPeca().setVivo(false);  //captura a peca
                    movePeca(lOrigem, cOrigem, lDestino, cDestino); //move a peca
                    System.out.printf("A peca na posicao %c%d foi capturada!\n", (char)(cDestino + 97), lDestino + 1);
                    return true;
                }
            } else {    // Se o obstaculo nao estiver na posicao de destino
                System.out.println("Movimento invalido (caminho com obstaculo)!");
                return false;
            }
        }
    }
    
    // Move uma peca para uma posicao
    /* Parametros:
     * lOrigem e cOrigem = linha e coluna da posicao de origem, respectivamente
     * lDestino e cDestino = linha e coluna da posicao de destino, respectivamente
     */
    private void movePeca(int lOrigem, int cOrigem, int lDestino, int cDestino) {
        this.pos[lDestino][cDestino].setPeca(this.pos[lOrigem][cOrigem].getPeca());
        this.pos[lOrigem][cOrigem].removePeca();
    }
    
    // Insere uma peca em dada posicao (utilizado apenas pelo construtor da classe Jogo)
    /* Parametros:
     * linha = linha da posicao
     * coluna = coluna da posicao
     * p = peca a ser inserida
     */
    public void inserePeca(int linha, int coluna, Peca p) {
        // Se a peca for um rei
        if(p instanceof Rei)
            // Tambem atualiza o atributo auxiliar que sempre armazena a posicao do rei
            atualizaPosRei(linha, coluna, p.getCor());
        
        this.pos[linha][coluna].setPeca(p); //adiciona a peca na posicao
        p.setVivo(true);    //a peca foi adicionada no tabuleiro, logo nao esta capturada
    }
    
    // Checa se ha alguma peca no caminho, retornando a posicao do obstaculo
    // Caso nao ache um obstaculo, ele retorna null
    /* Parametros:
     * lOrigem e cOrigem = linha e coluna da posicao de origem, respectivamente
     * lDestino e cDestino = linha e coluna da posicao de destino, respectivamente
     */
    private Posicao caminhoObstaculo(int lOrigem, int cOrigem, int lDestino, int cDestino) {
        int distLinha = lDestino - lOrigem;
        int distColuna = cDestino - cOrigem;
        
        // Reconhecimento e tratamento do tipo de movimento
        if( (distLinha == 0) && (distColuna < 0) ) {    // Horizontal para a esquerda
            while(cOrigem > cDestino) {
                cOrigem--;
                // Achou uma posicao ocupada
                if(this.pos[lOrigem][cOrigem].isOcupada())
                    return this.pos[lOrigem][cOrigem];
            }
        } else if( (distLinha == 0) && (distColuna > 0) ) { // Horizontal para a direita
            while(cOrigem < cDestino) {
                cOrigem++;
                if(this.pos[lOrigem][cOrigem].isOcupada())
                    return this.pos[lOrigem][cOrigem];
            }
        } else if( (distLinha > 0) && (distColuna == 0) ) { // Vertical para cima
            while(lOrigem < lDestino) {
                lOrigem++;
                if(this.pos[lOrigem][cOrigem].isOcupada()) {
                    return this.pos[lOrigem][cOrigem];
                }
            }
        } else if( (distLinha < 0) && (distColuna == 0) ) { // Vertical para baixo
            while(lOrigem > lDestino) {
                lOrigem--;
                if(this.pos[lOrigem][cOrigem].isOcupada()) {
                    return this.pos[lOrigem][cOrigem];
                }
            }
        } else if(Math.abs(distLinha) == Math.abs(distColuna)) {   // Diagonal
            if( (distLinha > 0) && (distColuna < 0) ) { // Esquerda-cima
                while(lOrigem < lDestino) {
                    lOrigem++;
                    cOrigem--;
                    if(this.pos[lOrigem][cOrigem].isOcupada())
                        return this.pos[lOrigem][cOrigem];
                }
            } else if( (distLinha > 0) && (distColuna > 0) ) { // Direita-cima
                while(lOrigem < lDestino) {
                    lOrigem++;
                    cOrigem++;
                    if(this.pos[lOrigem][cOrigem].isOcupada())
                        return this.pos[lOrigem][cOrigem];
                }
            } else if( (distLinha < 0) && (distColuna < 0) ) {  // Esquerda-baixo
                while(lOrigem > lDestino) {
                    lOrigem--;
                    cOrigem--;
                    if(this.pos[lOrigem][cOrigem].isOcupada())
                        return this.pos[lOrigem][cOrigem];
                }
            } else {  // Direita-baixo
                while(lOrigem > lDestino) {
                    lOrigem--;
                    cOrigem++;
                    if(this.pos[lOrigem][cOrigem].isOcupada())
                        return this.pos[lOrigem][cOrigem];
                }
            }
        } else if( ((Math.abs(distLinha) == 1) && (Math.abs(distColuna) == 2)) || 
                   ((Math.abs(distLinha) == 2) && (Math.abs(distColuna) == 1)) ) {    // Casos do cavalo
            if(this.pos[lDestino][cDestino].isOcupada())
                return this.pos[lDestino][cDestino];
        }
        // Nao achou obstaculo
        return null;
    }
    
    // Atualiza o armazenamento da posicao dos reis
    /* Parametros:
     * lOrigem e cOrigem = linha e coluna da posicao de origem, respectivamente
     * lDestino e cDestino = linha e coluna da posicao de destino, respectivamente
     */
    private void atualizaPosRei(int linha, int coluna, char cor) {
        if(cor == '+') {
            this.posReiBranco = this.pos[linha][coluna];
        } else {
            this.posReiPreto = this.pos[linha][coluna];
        }
    }
    
    // Reconhece se a posicao passada por parametro esta em xeque
    /* Parametros:
     * linha e coluna = linha e coluna da posicao a ser verificada, respectivamente
     * corRei = a cor do rei a ser verificado
     */
    private boolean posicaoAmeacada(int linha, char coluna, char cor) {
        Posicao temp;   //temporario para segurar a posicao retornada do metodo caminhoObstaculo()
        
        // Percorre ate o final pela horizontal para esquerda
        temp = caminhoObstaculo(linha, coluna, linha, 0);   
        // Se achou um obstaculo
        if(temp != null) {
            // Se ele for inimigo
            if(temp.getPeca().getCor() != cor) {
                // Utilizacao do checaMovimento() para saber se a peca inimiga encontrada possui movimento valido ate essa posicao
                int condChecaMovimento = temp.getPeca().checaMovimento(temp.getLinha(), temp.getColuna(), linha, coluna);
                if( (condChecaMovimento == 1) || (condChecaMovimento == 3) ) {
                    // Se a peça inimiga NAO for o rei
                    if(!(temp.getPeca() instanceof Rei)) {
                        return true;    //a posicao esta controlada por inimigo
                    } else {    // Se for o rei
                        // Se a peca da posicao inicial NAO estiver protegida
                        if(this.pos[linha][coluna].getPeca().getCor() != this.pos[linha][coluna].getCor())
                            return true;    //a posicao esta controlada pelo rei inimigo
                    }
                }
            }
        }
        // Percorre ate o final pela horizontal para direita
        temp = caminhoObstaculo(linha, coluna, linha, 7);
        // Mesmo caso
        if(temp != null) {
            if(temp.getPeca().getCor() != cor) {
                int condChecaMovimento = temp.getPeca().checaMovimento(temp.getLinha(), temp.getColuna(), linha, coluna);
                if( (condChecaMovimento == 1) || (condChecaMovimento == 3) ) {
                    if(!(temp.getPeca() instanceof Rei)) {
                        return true;
                    } else {
                        if(this.pos[linha][coluna].getPeca().getCor() != this.pos[linha][coluna].getCor())
                            return true;
                    }
                }
            }
        }
        // Percorre ate o final pela vertical para cima
        temp = caminhoObstaculo(linha, coluna, 7, coluna);
        // Mesmo caso
        if(temp != null) {
            if(temp.getPeca().getCor() != cor) {
                int condChecaMovimento = temp.getPeca().checaMovimento(temp.getLinha(), temp.getColuna(), linha, coluna);
                if( (condChecaMovimento == 1) || (condChecaMovimento == 3) ) {
                    if(!(temp.getPeca() instanceof Rei)) {
                        return true;
                    } else {
                        if(this.pos[linha][coluna].getPeca().getCor() != this.pos[linha][coluna].getCor())
                            return true;
                    }
                }
            }
        }
        // Percorre ate o final pela vertical para baixo
        temp = caminhoObstaculo(linha, coluna, 0, coluna);    
        // Mesmo caso
        if(temp != null) {
            if(temp.getPeca().getCor() != cor) {
                int condChecaMovimento = temp.getPeca().checaMovimento(temp.getLinha(), temp.getColuna(), linha, coluna);
                if( (condChecaMovimento == 1) || (condChecaMovimento == 3) ) {
                    if(!(temp.getPeca() instanceof Rei)) {
                        return true;
                    } else {
                        if(this.pos[linha][coluna].getPeca().getCor() != this.pos[linha][coluna].getCor())
                            return true;
                    }
                }
            }
        }
        // Percorre ate o final pela diagonal esquerda-cima
        int l = linha, c = coluna;
        while( (c > 0) && (l < 7) ) {
            l++;
            c--;
            // Se achou um obstaculo
            if(this.pos[l][c].isOcupada()) {
                // Se ele for inimigo
                if(this.pos[l][c].getPeca().getCor() != cor) {
                    // Utilizacao do checaMovimento() para saber se a peca inimiga encontrada possui movimento valido ate essa posicao
                    int condChecaMovimento = this.pos[l][c].getPeca().checaMovimento(l, (char)c, linha, coluna);
                    if( (condChecaMovimento == 1) || (condChecaMovimento == 3) ) {
                        // Se a peça inimiga NAO for o rei
                        if(!(this.pos[l][c].getPeca() instanceof Rei)) {
                            return true;    //a posicao esta controlada por inimigo
                        } else {    // Se for o rei
                            // Se a peca da posicao inicial NAO estiver protegida
                            if(this.pos[linha][coluna].getPeca().getCor() != this.pos[linha][coluna].getCor())
                                return true;    //a posicao esta controlada pelo rei inimigo
                        }
                    }
                }
                break; //assim que encontra um obstaculo, sai do loop
            }
        }
        // Percorre ate o final pela diagonal direita-cima
        l = linha; c = coluna;
        while( (c < 7) && (l < 7) ) {
            l++;
            c++;
            // Mesmo caso
            if(this.pos[l][c].isOcupada()) {
                if(this.pos[l][c].getPeca().getCor() != cor) {
                    int condChecaMovimento = this.pos[l][c].getPeca().checaMovimento(l, (char)c, linha, coluna);
                    if( (condChecaMovimento == 1) || (condChecaMovimento == 3) ) {
                        if(!(this.pos[l][c].getPeca() instanceof Rei)) {
                            return true;
                        } else {
                            if(this.pos[linha][coluna].getPeca().getCor() != this.pos[linha][coluna].getCor())
                                return true;
                        }
                    }
                }
                break;
            }
        }
        // Percorre ate o final pela diagonal esquerda-baixo
        l = linha; c = coluna;
        while( (c > 0) && (l > 0) ) {
            l--;
            c--;
            // Mesmo caso
            if(this.pos[l][c].isOcupada()) {
                if(this.pos[l][c].getPeca().getCor() != cor) {
                    int condChecaMovimento = this.pos[l][c].getPeca().checaMovimento(l, (char)c, linha, coluna);
                    if( (condChecaMovimento == 1) || (condChecaMovimento == 3) ) {
                        if(!(this.pos[l][c].getPeca() instanceof Rei)) {
                            return true;
                        } else {
                            if(this.pos[linha][coluna].getPeca().getCor() != this.pos[linha][coluna].getCor())
                                return true;
                        }
                    }
                }
                break;
            }
        }
        // Percorre ate o final pela diagonal direita-baixo
        l = linha; c = coluna;
        while( (c < 7) && (l > 0) ) {
            l--;
            c++;
            // Mesmo caso
            if(this.pos[l][c].isOcupada()) {
                if(this.pos[l][c].getPeca().getCor() != cor) {
                    int condChecaMovimento = this.pos[l][c].getPeca().checaMovimento(l, (char)c, linha, coluna);
                    if( (condChecaMovimento == 1) || (condChecaMovimento == 3) ) {
                        if(!(this.pos[l][c].getPeca() instanceof Rei)) {
                            return true;
                        } else {
                            if(this.pos[linha][coluna].getPeca().getCor() != this.pos[linha][coluna].getCor())
                                return true;
                        }
                    }
                }
                break;
            }
        }
        // Casos do cavalo
        l = linha + 1; c = coluna - 2;
        if( (l < 8) && (c >= 0) ) {
            // Se essa posicao estiver ocupada e ter um cavalo nela
            if(this.pos[l][c].isOcupada() && (this.pos[l][c].getPeca() instanceof Cavalo)) {
                // Se for um cavalo inimigo
                if(this.pos[l][c].getPeca().getCor() != cor)
                    return true;
            }
        }
        l = linha + 2; c = coluna - 1;
        if( (l < 8) && (c >= 0) ) {
            if(this.pos[l][c].isOcupada() && (this.pos[l][c].getPeca() instanceof Cavalo)) {
                if(this.pos[l][c].getPeca().getCor() != cor)
                    return true;
            }
        }
        l = linha + 2; c = coluna + 1;
        if( (l < 8) && (c < 8) ) {
            if(this.pos[l][c].isOcupada() && (this.pos[l][c].getPeca() instanceof Cavalo)) {
                if(this.pos[l][c].getPeca().getCor() != cor)
                    return true;
            }
        }
        l = linha + 1; c = coluna + 2;
        if( (l < 8) && (c < 8) ) {
            if(this.pos[l][c].isOcupada() && (this.pos[l][c].getPeca() instanceof Cavalo)) {
                if(this.pos[l][c].getPeca().getCor() != cor)
                    return true;
            }
        }
        l = linha - 1; c = coluna + 2;
        if( (l >= 0) && (c < 8) ) {
            if(this.pos[l][c].isOcupada() && (this.pos[l][c].getPeca() instanceof Cavalo)) {
                if(this.pos[l][c].getPeca().getCor() != cor)
                    return true;
            }
        }
        l = linha - 2; c = coluna + 1;
        if( (l >= 0) && (c < 8) ) {
            if(this.pos[l][c].isOcupada() && (this.pos[l][c].getPeca() instanceof Cavalo)) {
                if(this.pos[l][c].getPeca().getCor() != cor)
                    return true;
            }
        }
        l = linha - 2; c = coluna - 1;
        if( (l >= 0) && (c >= 0) ) {
            if(this.pos[l][c].isOcupada() && (this.pos[l][c].getPeca() instanceof Cavalo)) {
                if(this.pos[l][c].getPeca().getCor() != cor)
                    return true;
            }
        }
        l = linha - 1; c = coluna - 2;
        if( (l >= 0) && (c >= 0) ) {
            if(this.pos[l][c].isOcupada() && (this.pos[l][c].getPeca() instanceof Cavalo)) {
                if(this.pos[l][c].getPeca().getCor() != cor)
                    return true;
            }
        }
        // Nao achou ameacas
        return false;
    }
    
    // Marca uma posicao onde o rei fica em xeque ou onde ha bloqueios para o rei
    // Esse metodo eh utilizado pelo metodo marcaPosicoesEmXeque()
    /* Parametros:
     * l e c = linha e coluna da posicao a ser verificada para o rei, respectivamente
     * pRei = posicao do rei
     */
    private void marcaUmaPosicao(int l, int c, Posicao pRei) {
        char corRei = pRei.getPeca().getCor();  //cor do rei
        int lOriginal = pRei.getLinha();    //linha da posicao original do rei
        int cOriginal = pRei.getColuna();   //coluna da posicao original do rei
        
        try {
            // Se a posicao nao estiver ocupada
            if(!this.pos[l][c].isOcupada()) {
                movePeca(lOriginal, cOriginal, l, c); //move o rei para a posicao a ser analisada
                // Checa se a posicao deixara o rei em xeque
                if(posicaoAmeacada(l, (char)c, corRei))
                    this.pos[l][c].setXeque(true, corRei);

                movePeca(l, c, lOriginal, cOriginal); //retorna o rei para a posicao original
            } else {    // Se estiver ocupada
                // Se for uma peca aliada
                if(this.pos[l][c].getPeca().getCor() == pRei.getPeca().getCor()) {
                    // Sera um caminho bloqueado para o rei
                    this.pos[l][c].setXeque(true, corRei);
                } else {    // Se for uma peca inimiga
                    // E ela estiver protegida
                    if(this.pos[l][c].getPeca().getCor() == this.pos[l][c].getCor()) {
                        // Se ela nao pode ser capturada por outra peca alem do rei
                        if(!posicaoAmeacada(l, (char)c, this.pos[l][c].getPeca().getCor()))
                            // Tambem sera um caminho bloqueado para o rei
                            this.pos[l][c].setXeque(true, corRei);
                    }
                }
            }
        } catch(IndexOutOfBoundsException e) {}
    }
    
    // Marca todas as posicoes onde o rei fica em xeque ou onde ha bloqueios para o rei
    /* Parametros:
     * pRei = posicao do rei
     */
    private void marcaPosicoesEmXeque(Posicao pRei) {
        char corRei = pRei.getPeca().getCor();  //cor do rei
        int l = pRei.getLinha();    //linha da posicao do rei
        int c = pRei.getColuna();   //coluna da posicao do rei
        
        // Verifica se a posicao atual da peca esta em xeque
        if(posicaoAmeacada(l, (char)c, corRei))
            this.pos[l][c].setXeque(true, corRei);
        
        // Agora sao feitas verificacoes para os possiveis movimentos do rei
        marcaUmaPosicao(l+1, c, pRei);
        marcaUmaPosicao(l+1, c+1, pRei);
        marcaUmaPosicao(l, c+1, pRei);
        marcaUmaPosicao(l-1, c+1, pRei);
        marcaUmaPosicao(l-1, c, pRei);
        marcaUmaPosicao(l-1, c-1, pRei);
        marcaUmaPosicao(l, c-1, pRei);
        marcaUmaPosicao(l+1, c-1, pRei);
    }
    
    // Reseta as posicoes marcadas com xeque
    public void limpaXeque() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.pos[i][j].setXeque(false, '+');
                this.pos[i][j].setXeque(false, '*');
            }
        }
    }
    
    // Usa as posicoes marcadas pelo metodo marcaPosicoesEmXeque para
    // decidir se o rei esta em xeque ou nao, ou se houve xeque-mate
    /* Parametros:
     * corRei = cor do rei a ser verificado
     */
    public int verificaXeque(char corRei) {
        Posicao pRei = getPosRei(corRei);   //cor do rei
        int l = pRei.getLinha();    //linha da posicao do rei
        int c = pRei.getColuna();   //coluna da posicao do rei
        int count = 0;  //contador de movimentos invalidos para o rei
        boolean flagXeque;  //flag para ver se o rei esta em xeque
        
        // Marca as posicoes em xeque
        marcaPosicoesEmXeque(pRei);
        
        // Checa se a posicao atual do rei esta ameacada
        flagXeque = this.pos[l][c].isXeque(pRei.getPeca().getCor());
        
        // Verificacao das posicoes em torno do rei
        try {
            if(this.pos[l+1][c].isXeque(pRei.getPeca().getCor()))
                count++;
        } catch(IndexOutOfBoundsException e) {
            // Movimento fora do tabuleiro
            count++;
        }
        try {
            if(this.pos[l+1][c+1].isXeque(pRei.getPeca().getCor()))
                count++;
        } catch(IndexOutOfBoundsException e) {
            // Movimento fora do tabuleiro
            count++;
        }
        try {
            if(this.pos[l][c+1].isXeque(pRei.getPeca().getCor()))
                count++;
        } catch(IndexOutOfBoundsException e) {
            // Movimento fora do tabuleiro
            count++;
        }
        try {
            if(this.pos[l-1][c+1].isXeque(pRei.getPeca().getCor()))
                count++;
        } catch(IndexOutOfBoundsException e) {
            // Movimento fora do tabuleiro
            count++;
        }
        try {
            if(this.pos[l-1][c].isXeque(pRei.getPeca().getCor()))
                count++;
        } catch(IndexOutOfBoundsException e) {
            // Movimento fora do tabuleiro
            count++;
        }
        try {
            if(this.pos[l-1][c-1].isXeque(pRei.getPeca().getCor()))
                count++;
        } catch(IndexOutOfBoundsException e) {
            // Movimento fora do tabuleiro
            count++;
        }
        try {
            if(this.pos[l][c-1].isXeque(pRei.getPeca().getCor()))
                count++;
        } catch(IndexOutOfBoundsException e) {
            // Movimento fora do tabuleiro
            count++;
        }
        try {
            if(this.pos[l+1][c-1].isXeque(pRei.getPeca().getCor()))
                count++;
        } catch(IndexOutOfBoundsException e) {
            // Movimento fora do tabuleiro
            count++;
        }
        
        if(!flagXeque) {
            return 0;   //nao esta em xeque
        } else {
            if(count < 8) {
                return 1;   //em xeque
            } else {
                return 2;   //xeque-mate
            }
        }
    }
    
    // Print do tabuleiro
    public void printTab() {
        System.out.println(" # |a|b|c|d|e|f|g|h| #");
        for(int i = 7; i >= 0; i--) {
            for(int j = 0; j < 8; j++) {
                if(j == 0)
                    System.out.printf("|%d| ", i+1);
                
                // Imprime o caractere da peca se a posicao estiver ocupada
                if(this.pos[i][j].isOcupada()) {
                    System.out.print(this.pos[i][j].getPeca().desenho() + " ");
                } else {    // Se nao, imprime apenas a cor da posicao
                    System.out.print(this.pos[i][j].getCor() + " ");
                }
                
                if(j == 7)
                    System.out.printf("|%d|", i+1);
            }
            System.out.print("\n");
        }
        System.out.println(" # |a|b|c|d|e|f|g|h| #");
    }
}
