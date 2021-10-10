/* Cabecalho:
 * Classe herdada da classe Peca. Ela possui a implementacao dos dois metodos abstratos
 * através de polimorfismo por sobrescricao, seguindo as caracteristicas especificas da peca peao.
 */
package xadrez;

/**
 *
 * @author dougl
 */
public class Peao extends Peca {
    /* Construtor */
    /* Parametros:
     * cor = cor da peca
     */
    public Peao(char cor) {
        super(cor);
    }

    /* Implementacao dos metodos gerais */
    @Override
    public char desenho() {
        //Temos dois casos:
        if(this.cor == '+') {
            // Letra maiuscula para pecas brancas
            return 'P';
        } else {
            // Letra minuscula para pecas pretas
            return 'p';
        }
    }
    
    @Override
    public int checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
        // Atributos temporarios para receber a distancia que a peca andou
        int difColuna = Math.abs(colunaDestino - colunaOrigem); //distancia na coluna
        int difLinha;   //distancia na linha
        int corretorPosicaoInicial = 1; //sera usada para decidir a distancia na checagem
        
        if(this.cor == '*') {
            // Se o peao for preto, seu sentido do movimento eh para baixo
            difLinha = linhaOrigem - linhaDestino;
            if(linhaOrigem == 6) {
                // Se a linha de origem eh 6, entao ele estava na posicao inicial
                corretorPosicaoInicial = 2;
            }
        } else {
            // Se o peao for branco, seu sentido do movimento eh para cima
            difLinha = linhaDestino - linhaOrigem;
            if(linhaOrigem == 1) {
                // Se a linha de origem eh 1, entao ele estava na posicao inicial
                corretorPosicaoInicial = 2;
            }
        }
        
        // Temos tres casos:
        if((difLinha <= corretorPosicaoInicial) && (difLinha > 0) && (difColuna == 0)) {
            // Move no máximo duas ou uma casa para frente (depende do corretorPosicaoInicial)
            return 2;
        } else if((difLinha == 1) && (difColuna == 1)){
            // Move uma casa na diagonal quando captura
            return 3;
        } else {
            // Demais possibilidades
            return 0;
        }
    }
}