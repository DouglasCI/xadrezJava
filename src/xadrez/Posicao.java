/* Cabecalho:
 * Essa eh a classe que representa uma posicao do tabuleiro. Ela sabe sua cor,
 * sua linha e coluna, qual peca a ocupa (null se nao possuir peca), alem de
 * dois atributos auxiliares utilizados para a verificacao do xeque e xeque-mate.
 */
package xadrez;

/**
 *
 * @author dougl
 */
public class Posicao {
    /* Atributos da classe */
    private char cor; //branco = '+' ou preto = '*'
    private int linha;  //armazena o indice da linha da matriz de posicoes
    private char coluna; //armazena o indice da coluna da matriz de posicoes
    private Peca peca;  //referencia para um objeto de Peca
    // Auxiliares especiais para marcar posicoes em que o rei esta em xeque
    private boolean xequeBranco;    //utilizado para marcar posicoes onde o rei branco fica em xeque
    private boolean xequePreto; //utilizado para marcar posicoes onde o rei branco fica em xeque
    
    /* Metodos getters e setters */
    // Cor
    public char getCor() {
        return cor;
    }
    
    // Linha
    public int getLinha() {
        return linha;
    }
    
    // Coluna
    public char getColuna() {
        return coluna;
    }
    
    // Peca
    public Peca getPeca() {
        return peca;
    }
    /* Parametros:
     * p = peca a ser atribuida
     */
    public void setPeca(Peca p) {
        this.peca = p;
    }
    public void removePeca() {
        this.peca = null;
    }
    
    // Xeque
    /* Parametros:
     * cor = cor do rei
     */
    public boolean isXeque(char cor) {
        if(cor == '+') {
            return xequeBranco;
        } else if(cor == '*') {
            return xequePreto;
        } else {
            return false;
        }
    }
    /* Parametros:
     * xeque = estado de xeque a ser atribuido
     * cor = cor do rei
     */
    public void setXeque(boolean xeque, char cor) {
        if(cor == '+') {
            this.xequeBranco = xeque;
        } else if(cor == '*') {
            this.xequePreto = xeque;
        }
    }
    
    // Diz se esta ocupada ou nao
    public boolean isOcupada() {
        return peca != null;
    }
    
    /* Construtor */
    /* Parametros:
     * cor = cor da posicao
     * linha e coluna = linha e coluna da posicao, respectivamente
     */
    public Posicao(char cor, int linha, char coluna) {
        if( (cor == '+') || (cor == '*') ) {
            this.cor = cor;
        } else {
            this.cor = 0;   //caractere nulo
        }
        
        if( (linha >= 0) && (linha < 8) ) {
            this.linha = linha;
        } else {
            this.linha = 0;
        }
        
        if( (coluna >= 0) && (coluna < 8) ) {
            this.coluna = coluna;
        } else {
            this.coluna = 0;
        }
        
        this.peca = null;   //inicializa com valor null (sem peca)
        // Inicializa os valores dos xeques em false
        this.xequeBranco = false;
        this.xequePreto = false;
    }
}
