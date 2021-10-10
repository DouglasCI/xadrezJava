/* Cabecalho:
 * Classe herdada da classe Peca. Ela possui a implementacao dos dois metodos abstratos
 * através de polimorfismo por sobrescricao, seguindo as caracteristicas especificas da peca torre.
 */
package xadrez;

/**
 *
 * @author dougl
 */
public class Torre extends Peca {
    /* Construtor */
    /* Parametros:
     * cor = cor da peca
     */
    public Torre(char cor) {
        super(cor);
    }

    /* Implementacao dos metodos gerais */
    @Override
    public char desenho() {
        //Temos dois casos:
        if(this.cor == '+') {
            // Letra maiuscula para pecas brancas
            return 'T';
        } else {
            // Letra minuscula para pecas pretas
            return 't';
        }
    }
    
    @Override
    public int checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
        // Atributos temporarios para receber a distancia absoluta que a peca andou
        int difColuna = Math.abs(colunaDestino - colunaOrigem); //distancia na coluna
        int difLinha = Math.abs(linhaDestino - linhaOrigem);    //distancia na linha
        
        // Temos tres casos:
        if((difLinha > 0) && (difColuna == 0)) {
            // Movimento vertical
            return 1;
        } else if ((difLinha == 0) && (difColuna > 0)) {
            // Movimento horizontal
            return 1;
        } else {
            // Demais possibilidades
            return 0;
        }
    }
}