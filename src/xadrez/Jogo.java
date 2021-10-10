/* Cabecalho:
 * Essa eh a classe responsavel pela comunicacao entre o jogo e os jogadores,
 * tratando e traduzindo retornos de metodos da classe Tabuleiro, alem da captura
 * e envio (atraves de parametros) de inputs dos jogadores.
 * Ela tambem possui dois jogadores, o tabuleiro e checagens para inputs invalidos.
 * Alem disso, nessa classe fica a parte principal do codigo para salvar as configuracoes
 * do jogo em um arquivo e carregar esse arquivo quando requisitado pelo usuario.
 */
package xadrez;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter; 
import java.io.IOException;

/**
 *
 * @author dougl
 */
public class Jogo {
    /* Atributos da classe */
    private Jogador p1, p2; //jogadores
    private Tabuleiro tab;  //tabuleiro
    private Peca[] pBrancas = new Peca[16];  //conjunto de pecas brancas
    private Peca[] pPretas = new Peca[16];  //conjunto de pecas pretas
    private int turno = 0;  //contador de turnos
    
    /* Construtores */
    // Construtor para um jogo novo
    /* Parametros:
     * nome1 = nome do primeiro jogador
     * nome2 = nome do segundo jogador
     */
    public Jogo(String nome1, String nome2) {
        // Inicializa 8 peoes no inicio do vetor
        for(int i = 0; i < 8; i++) {
            this.pBrancas[i] = new Peao('+');
            this.pPretas[i] = new Peao('*');
        }
        // Inicializa o resto das pecas em ordem como no formato inicial no tabuleiro
        this.pBrancas[8] = new Torre('+');
        this.pPretas[8] = new Torre('*');
        this.pBrancas[9] = new Cavalo('+');
        this.pPretas[9] = new Cavalo('*');
        this.pBrancas[10] = new Bispo('+');
        this.pPretas[10] = new Bispo('*');
        this.pBrancas[11] = new Rainha('+');
        this.pPretas[11] = new Rainha('*');
        this.pBrancas[12] = new Rei('+');
        this.pPretas[12] = new Rei('*');
        this.pBrancas[13] = new Bispo('+');
        this.pPretas[13] = new Bispo('*');
        this.pBrancas[14] = new Cavalo('+');
        this.pPretas[14] = new Cavalo('*');
        this.pBrancas[15] = new Torre('+');
        this.pPretas[15] = new Torre('*');
        
        // Inicializacao dos jogadores
        this.p1 = new Jogador(nome1, '+', pBrancas);
        this.p2 = new Jogador(nome2, '*', pPretas);
        
        // Inicializacao do tabuleiro
        this.tab = new Tabuleiro();
        
        Peca peca_aux;
        // Loop para inserir as pecas em um novo jogo
        for(int i = 7; i >= 0; i--) {
            for(int j = 0; j < 8; j++) {
                // Seleciona as pecas para inserir nas posicoes
                switch(i) {
                    case 7:
                        // Adiciona as pecas pretas em ordem, menos os peoes
                        peca_aux = pPretas[j + 8];
                        break;

                    case 6:
                        // Adiciona os peoes pretos
                        peca_aux = pPretas[j];
                        break;

                    case 1:
                        // Adiciona os peoes brancos
                        peca_aux = pBrancas[j];
                        break;

                    case 0:
                        // Adiciona o resto das pecas brancas em ordem
                        peca_aux = pBrancas[j + 8];
                        break;

                    default:
                        // Null sinaliza que nao ha peca na dada posicao
                        peca_aux = null;
                        break;
                }
                if(peca_aux != null)
                    tab.inserePeca(i, j, peca_aux);
            }
        }
    }
    
    // Sobrecarga do construtor para carregar um jogo salvo
    /* Parametros:
     * nomeArquivo = nome do arquivo salvo
     */
    public Jogo(String nomeArquivo) throws FileNotFoundException {
        // Inicializa 8 peoes no inicio do vetor
        for(int i = 0; i < 8; i++) {
            this.pBrancas[i] = new Peao('+');
            this.pPretas[i] = new Peao('*');
        }
        // Inicializa o resto das pecas em ordem como no formato inicial no tabuleiro
        this.pBrancas[8] = new Torre('+');
        this.pPretas[8] = new Torre('*');
        this.pBrancas[9] = new Cavalo('+');
        this.pPretas[9] = new Cavalo('*');
        this.pBrancas[10] = new Bispo('+');
        this.pPretas[10] = new Bispo('*');
        this.pBrancas[11] = new Rainha('+');
        this.pPretas[11] = new Rainha('*');
        this.pBrancas[12] = new Rei('+');
        this.pPretas[12] = new Rei('*');
        this.pBrancas[13] = new Bispo('+');
        this.pPretas[13] = new Bispo('*');
        this.pBrancas[14] = new Cavalo('+');
        this.pPretas[14] = new Cavalo('*');
        this.pBrancas[15] = new Torre('+');
        this.pPretas[15] = new Torre('*');
        
        // Abre o arquivo para leitura
        File arquivo = new File(nomeArquivo);
        Scanner in = new Scanner(arquivo);
        
        // Leitura dos nomes e inicializacao dos jogadores
        String dado = in.nextLine();
        this.p1 = new Jogador(dado, '+', pBrancas);
        dado = in.nextLine();
        this.p2 = new Jogador(dado, '*', pPretas);
        
        // Inicializacao do tabuleiro
        tab = new Tabuleiro();
        
        // Inicializacao do contador de turnos
        dado = in.nextLine();
        this.turno = Integer.parseInt(dado);
        
        // Inicializacao das posicoes e pecas
        // Contadores para evitar que a mesma peca seja inserida denovo
        int pB = 0, pP = 0, tB = 8, tP = 8, cB = 9, cP = 9, bB = 10, bP = 10;
        // Loop para receber e passar os dados para as posicoes
        for(int i = 7; i >= 0; i--) {
            for(int j = 0; j < 8; j++) {
                dado = in.nextLine();
                
                switch(dado.charAt(0)) {
                    case 'P':   // Peao branco
                        tab.inserePeca(i, j, this.pBrancas[pB]);
                        pB++;
                        break;
                    case 'p':   // Peao preto
                        tab.inserePeca(i, j, this.pPretas[pP]);
                        pP++;
                        break;
                    case 'T':   // Torre branca
                        tab.inserePeca(i, j, this.pBrancas[tB]);
                        tB = 15;
                        break;
                    case 't':   // Torre preta
                        tab.inserePeca(i, j, this.pPretas[tP]);
                        tP = 15;
                        break;
                    case 'C':   // Cavalo branco
                        tab.inserePeca(i, j, this.pBrancas[cB]);
                        cB = 14;
                        break;
                    case 'c':   // Cavalo preto
                        tab.inserePeca(i, j, this.pPretas[cP]);
                        cP = 14;
                        break;
                    case 'B':   // Bispo branco
                        tab.inserePeca(i, j, this.pBrancas[bB]);
                        bB = 13;
                        break;
                    case 'b':   // Bispo preto
                        tab.inserePeca(i, j, this.pPretas[bP]);
                        bP = 13;
                        break;
                    case 'D':   // Rainha branca
                        tab.inserePeca(i, j, this.pBrancas[11]);
                        break;
                    case 'd':   // Rainha preta
                        tab.inserePeca(i, j, this.pPretas[11]);
                        break;
                    case 'R':   // Rei branco
                        tab.inserePeca(i, j, this.pBrancas[12]);
                        break;
                    case 'r':   // Rei preto
                        tab.inserePeca(i, j, this.pPretas[12]);
                        break;
                }
            }
        }
        // Ha a chamada desse metodo para recuperar o estado do xeque
        houveXequeMate(this.p1, this.p2);
        
        // Fecha a leitura do arquivo
        in.close();
    }
   
    /* Metodos gerais */
    // Executa o jogo em si e mantem o fluxo de turnos
    @SuppressWarnings("empty-statement")
    public void iniciar() {
        System.out.println("Jogo iniciado!");
        // Imprime pela primeira vez o tabuleiro sempre no inicio da partida
        tab.printTab();
        
        Jogador player; //referencia para o jogador do turno atual
        int condSaida = -1;  //sinalizador utilizado para as condicoes de termino de jogo
        Scanner in = new Scanner(System.in);
        
        while(true) {
            // Turno do jogador com pecas brancas
            if((this.turno % 2) == 0) {
                player = p1;
                System.out.println("Eh a vez de " + player.getNome() + " (pecas brancas)!");
            } else {    // Turno do jogador com pecas pretas
                player = p2;
                System.out.println("Eh a vez de " + player.getNome() + " (pecas pretas)!");
            }
            
            // Proposta de empate
            if(condSaida == 1) {
                System.out.println("Seu inimigo deseja propor um empate!\n0 - Recusar\n1 - Aceitar");
                int resposta = -1;  //flag de resposta do jogador
                // Loop para evitar respostas invalidas
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
                
                // Tratamento da resposta
                if(resposta == 0) {
                    System.out.println(player.getNome() + " recusou a proposta de empate! O jogo continuara.");
                } else if(resposta == 1) {
                    System.out.println(player.getNome() + " aceitou a proposta de empate! O jogo empatou!");
                    System.out.println("Finalizando o jogo...");
                    break;  //fim da partida
                }
            }
            
            // Loop para continuar lendo a jogada se o movimento for invalido
            while(!lerJogada(player.getCor()));
            System.out.println("Movimento executado com sucesso!");
            
            // Print do tabuleiro depois de cada jogada
            tab.printTab();
            
            // Verificacao se houve xeque-mate
            if(houveXequeMate(p1, p2))
                break;  //fim da partida
            
            // Passa a vez para o proximo
            this.turno++;
            
            // Modos de terminar o jogo alem do xeque-mate
            System.out.println("Escolha uma opcao:\n0 - Continuar jogando\n1 - Propor empate\n2 - Desistir\n3 - Interromper e salvar a partida");
            // Loop para evitar respostas invalidas
            do {
                try {
                    condSaida = in.nextInt();
                    in.nextLine();  //avanca o scanner para evitar erros de entrada
                    if( (condSaida < 0) || (condSaida > 3) )
                        System.out.println("Insira uma resposta valida!");
                } catch(InputMismatchException e) {
                    System.out.println("Insira uma resposta valida!");
                    in.nextLine();  //avanca o scanner para evitar erros de entrada
                }
            } while( (condSaida < 0) || (condSaida > 3) );
            
            // Desistencia
            if(condSaida == 2) {
                System.out.println("O jogador " + player.getNome() + " desistiu do jogo!");
                System.out.println("Finalizando o jogo...");
                break;  //fim da partida
            }
            
            // Salva e fecha o jogo
            if(condSaida == 3) {
                salvar(this.turno);
                break;  //fim da partida
            }
        }
    }
    
    // Metodo para ler a jogada
    /* Parametros:
     * cor = cor das pecas do jogador desse turno
     */
    private boolean lerJogada(char cor) {
        Scanner in = new Scanner(System.in);
        int lOrigem, lDestino;
        String cOrigem, cDestino;
        
        try {   /* Tratador quando eh inserido um tipo de dado diferente que int */
            // Origem
            System.out.print("Insira a coluna da peca que deseja mover ('a' a 'h'): ");
            cOrigem = in.next();
            // Se a entrada tiver mais de 1 caractere, retornar false
            if(cOrigem.length() > 1) {
                System.out.println("ERRO! Insira APENAS UMA letra de 'a' a 'h'!");
                return false;
            }
            System.out.print("Insira a linha da peca que deseja mover (1 a 8): ");
            lOrigem = in.nextInt();
        
            // Destino
            System.out.print("Insira a coluna para onde deseja move-la ('a' a 'h'): ");
            cDestino = in.next();
            if(cDestino.length() > 1) {
                System.out.println("ERRO! Insira APENAS UMA letra de 'a' a 'h'!");
                return false;
            }
            System.out.print("Insira a linha para onde deseja move-la (1 a 8): ");
            lDestino = in.nextInt();
            
        } catch(InputMismatchException e) {
            System.out.println("ERRO! Insira um numero entre 1 e 8!");
            return false;
        }
        
        // Retorna true se a jogada for valida ou false se ela for invalida
        return tab.movimento(lOrigem, cOrigem.charAt(0), lDestino, cDestino.charAt(0), cor);
    }
    
    // Checa a situacao dos reis e retorna o estado em que o jogo se encontra
    /* Parametros:
     * p1 e p2 = jogador de peças brancas e jogador de peças pretas, respectivamente
     */
    private boolean houveXequeMate(Jogador p1, Jogador p2) {
        // Se o rei branco foi capturado
        if(!p1.getRei().isVivo()) {
            System.out.println("XEQUE-MATE! " + p2.getNome() + "(pecas pretas) ganhou o jogo!");
            return true;
        }
        // Se o rei preto foi capturado
        if(!p2.getRei().isVivo()) {
            System.out.println("XEQUE-MATE! " + p1.getNome() + "(pecas brancas) ganhou o jogo!");
            return true;
        }
        
        tab.limpaXeque();   //reseta o estado do xeque
        int condXequeBranco = tab.verificaXeque('+'); //verifica o rei branco
        int condXequePreto = tab.verificaXeque('*');   //verifica o rei preto
        
        // Xeque-mate do rei branco
        if(condXequeBranco == 2) {
            System.out.println("XEQUE-MATE! " + p2.getNome() + " (pecas pretas) ganhou o jogo!");
            return true;   
        }
        // Xeque-mate do rei preto
        if(condXequePreto == 2) {   
            System.out.println("XEQUE-MATE! " + p1.getNome() + " (pecas brancas) ganhou o jogo!");
            return true;   
        }
        // Xeque do rei branco
        if(condXequeBranco == 1) {  
            System.out.println("> O rei branco esta em xeque! <");
            return false;   
        }
        // Xeque do rei preto
        if(condXequePreto == 1) {   
            System.out.println("> O rei preto esta em xeque! <");
            return false;   
        }
        // Nao houve xeque
        return false;
    }
    
    // Cria um arquivo de texto e salva o jogo nele
    /* Parametros:
     * turno = contador de turnos (utiliza a wrapper class Integer para depois converter o numero em string)
     */
    private void salvar(Integer turno) {
        Scanner in = new Scanner(System.in);
        String nome;    //nome do arquivo
        
        // Criacao do arquivo
        do {
            System.out.print("Insira o nome do arquivo em que deseja salvar seu jogo: ");
            nome = in.next();
            try { /* Tratador de excecao quando a criacao do arquivo falha */
                File arquivo = new File(nome);  //instanciamento de um objeto de arquivo
                // Criacao do arquivo e tratamento para caso um arquivo com o mesmo nome ja exista
                if(arquivo.createNewFile()) {
                    System.out.println("O jogo sera salvo no arquivo " + arquivo.getName() + ".");
                    break;
                } else {
                    System.out.println("Ja existe um arquivo com esse nome.");
                }
            } catch(IOException e) {
                System.out.println("Houve um erro na criacao do arquivo.");
            }
        } while(true);
        
        // Escrita dos dados no arquivo
        try {
            FileWriter out = new FileWriter(nome);
            // Armazena os nomes dos jogadores
            out.write(p1.getNome() + "\n" + p2.getNome() + "\n");
            // Armazena o turno
            out.write(turno.toString() + "\n");
            // Armazena uma imagem do tabuleiro com as pecas
            String dados;
            for(int i = 7; i >= 0; i--) {
                for(int j = 0; j < 8; j++) {
                    out.write(tab.getInfoPosicao(i, j) + "\n");
                }
            }
            out.close();
            System.out.println("Jogo salvo com sucesso!");
        } catch(IOException e) {
            System.out.println("Houve um erro na hora de escrever os dados no arquivo.");
        }
    }
}
