/* Cabecalho:
 * Essa eh a classe que representa o jogador.
 * Cada jogador tem seu nome, sua cor e seu conjunto de pecas.
 */
package xadrez;

/**
 *
 * @author dougl
 */
public class Jogador {
    /* Atributos da classe */
    private String nome;    //nome do jogador
    private char cor;   //cor das pecas do jogador
    private Peca[] pecas = new Peca[16];    //conjunto de pecas do jogador
    
    /* Metodos getters e setters */
    // Nome
    public String getNome() {
        return nome;
    }
    
    // Cor
    public char getCor() {
        return cor;
    }
    
    // Getter especifico do rei
    public Peca getRei() {
        return pecas[12];
    }
    
    /* Construtor */
    /* Parametros:
     * n = nome do jogador
     * c = cor das pecas do jogador
     * p = conjunto de pecas do jogador
     */
    public Jogador(String n, char c, Peca[] p) {
        this.nome = n;
        
        if( (c == '+') || (c == '*') ) {
            this.cor = c;
        } else {
            this.cor = 0;   //caractere nulo
        }
        
        this.pecas = p;
    }
}
