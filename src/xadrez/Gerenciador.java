/* Cabecalho:
 * Classe que recebe os nomes dos jogadores ou recebe o nome de um arquivo 
 * nos casos em que se deseja carregar um jogo salvo, e depois inicia o jogo.
 */
package xadrez;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author dougl
 */
public class Gerenciador {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String nome1, nome2, nomeArquivo;    //nomes
        int resposta = -1;   //resposta do usuario
        Jogo j = null; //declaracao de um objeto da classe Jogo
        
        System.out.println("Deseja carregar um jogo salvo?\n0 - Nao, comecar um jogo do zero\n1 - Sim, carregar um jogo salvo");
        do {
            try {
                resposta = in.nextInt();
                in.nextLine();  //avanca o scanner para evitar erros de entrada
                if( (resposta < 0) || (resposta > 1) )
                    System.out.println("Insira uma resposta valida!");
            } catch(InputMismatchException e) {
                System.out.println("Insira uma resposta valida!");
                in.nextLine();  //avanca o scanner para evitar erros de entrada
            }
        } while( (resposta < 0) || (resposta > 1) );
        
        // Jogo novo
        if(resposta == 0) {
            System.out.print("Insira o nome do jogador que irá utilizar as pecas brancas: ");
            nome1 = in.next();
            System.out.print("Insira o nome do jogador que irá utilizar as pecas pretas: ");
            nome2 = in.next();
            j = new Jogo(nome1, nome2); //criacao do objeto da classe Jogo usando o construtor de jogo novo
        } else {    // Carregamento de um jogo salvo
            System.out.print("Insira o nome do arquivo que deseja carregar (a extensao .txt tambem faz parte do nome): ");
            do {
                nomeArquivo = in.next();
                try {
                    j = new Jogo(nomeArquivo);  //criacao do objeto da classe Jogo usando o construtor de jogo salvo
                    break;  //saida do loop de captura de erro
                } catch (FileNotFoundException e) {
                    System.out.print("Esse arquivo nao existe!\nInsira outro nome: ");
                }
            } while(true);
        }
        
        // A checagem para a falha na criaçao de jogo nao eh necessaria, pois ela nunca deve acontecer,
        // mas ela ainda eh feita apenas por garantia
        if(j != null)
            j.iniciar();    //inicio do jogo
    }
}
