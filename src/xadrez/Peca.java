/* Cabecalho:
 * Essa eh uma classe abstrata que servira de molde para todas as pecas.
 * Ela sabe se esta viva ou nao e sua cor.
 */
package xadrez;

/**
 *
 * @author dougl
 */
public abstract class Peca {
    /* Atributos da classe */
    protected boolean vivo; //checa o estado da peca (true = viva | false = capturada)
    protected char cor; //guarda qual a cor da peca

    /* Metodos getters e setters */
    // Vivo
    public boolean isVivo(){    
        return vivo;
    }
    /* Parametros:
     * vivo = estado da peca a ser atribuido
     */
    public void setVivo(boolean vivo) {    
        this.vivo = vivo;
    }
    
    // Cor
    public char getCor() {
        return cor;
    }

    /* Construtor */
    /* Parametros:
     * cor = cor da peca
     */
    public Peca(char cor) {
        if( (cor == '+') || (cor == '*') ) {
            this.cor = cor;
        } else {
            this.cor = 0;   //caractere nulo
        }
        
        this.vivo = false;  //comecam capturadas (fora do tabuleiro)
    }

    /* Metodos gerais abstratos */
    // Retorna o caractere que representa a peca
    public abstract char desenho();
    
    // Checa se o movimento eh valido para a peca
    /* Parametros:
     * linhaOrigem e colunaOrigem = linha e coluna da posicao de origem, respectivamente
     * linhaDestino e colunaDestino = linha e coluna da posicao de destino, respectivamente
     */
    public abstract int checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino);
}
