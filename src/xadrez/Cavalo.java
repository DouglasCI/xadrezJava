/* Cabecalho:
 * Classe herdada da classe Peca. Ela possui a implementacao dos dois metodos abstratos
 * através de polimorfismo por sobrescricao, seguindo as caracteristicas especificas da peca cavalo.
 */
package xadrez;

/**
 *
 * @author dougl
 */
public class Cavalo extends Peca {
    /* Construtor */
    public Cavalo(char cor) {
        super(cor);
    }

    /* Implementacao dos metodos gerais */
    @Override
    public char desenho() {
        //Temos dois casos:
        if(this.cor == '+') {
            // Letra maiuscula para pecas brancas
            return 'C';
        } else {
            // Letra minuscula para pecas pretas
            return 'c';
        }
    }
    
    @Override
    public int checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
        // Atributos temporarios para receber a distancia absoluta que a peca andou
        int difColuna = Math.abs(colunaDestino - colunaOrigem); //distancia na coluna
        int difLinha = Math.abs(linhaDestino - linhaOrigem);    //distancia na linha
        
        // Temos tres casos:
        if((difLinha == 1) && (difColuna == 2)) {
            // Movimento em L deitado
            return 1;
        } else if((difLinha == 2) && (difColuna == 1)) {
            // Movimento em L em pe
            return 1;
        } else {
            // Demais possibilidades
            return 0;
        }
    }
}