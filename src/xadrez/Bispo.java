/* Cabecalho:
 * Classe herdada da classe Peca. Ela possui a implementacao dos dois metodos abstratos
 * através de polimorfismo por sobrescricao, seguindo as caracteristicas especificas da peca bispo.
 */
package xadrez;

/**
 *
 * @author dougl
 */
public class Bispo extends Peca {
    /* Construtor */
    public Bispo(char cor) {
        super(cor);
    }
    
    /* Implementacao dos metodos gerais */
    @Override
    public char desenho() {
        //Temos dois casos:
        if(this.cor == '+') {
            // Letra maiuscula para pecas brancas
            return 'B';
        } else {
            // Letra minuscula para pecas pretas
            return 'b';
        }
    }
    
    @Override
    public int checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
        // Atributos temporarios para receber a distancia absoluta que a peca andou
        int difColuna = Math.abs(colunaDestino - colunaOrigem); //distancia na coluna
        int difLinha = Math.abs(linhaDestino - linhaOrigem);    //distancia na linha
        
        // Temos tres casos
        if((difLinha == 0) && (difColuna == 0)) {
            // Nao houve movimento
            return 0;
        } else if(difLinha == difColuna) {
            // Movimento diagonal
            return 1;
        } else {
            // Demais possibilidades
            return 0;
        }
    }
}