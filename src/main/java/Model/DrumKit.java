package Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe modelo que representa o kit de bateria customizado do usuário.
 * Gerencia a relação entre as teclas do Launchpad e os arquivos de áudio.
 */
public class DrumKit {
    
    // Mapeia a tecla física (ex: "Q") para o caminho absoluto do arquivo de som no PC.
    private Map<String, String> mapeamentoSons;

    /**
     * Construtor que inicializa um novo kit de bateria vazio.
     */
    public DrumKit() {
        this.mapeamentoSons = new HashMap<>();
    }

    /**
     * Associa um arquivo de áudio a uma tecla específica.
     * Se a tecla já tiver um som, ele será sobrescrito.
     * * @param tecla          A letra da tecla (ex: "Q", "W", "A").
     * @param caminhoArquivo O caminho do arquivo .wav no computador.
     */
    public void associarSom(String tecla, String caminhoArquivo) {
        mapeamentoSons.put(tecla.toUpperCase(), caminhoArquivo);
    }

    /**
     * Recupera o caminho do arquivo de som associado a uma tecla.
     * * @param tecla A letra da tecla pressionada.
     * @return O caminho do arquivo de som, ou null se não houver som configurado.
     */
    public String obterCaminhoSom(String tecla) {
        return mapeamentoSons.get(tecla.toUpperCase());
    }

    /**
     * Retorna todos os mapeamentos atuais. 
     * Útil para o momento em que o colega for salvar tudo no banco de dados.
     * * @return Map contendo todas as teclas e seus respectivos caminhos de áudio.
     */
    public Map<String, String> getTodosOsSons() {
        return mapeamentoSons;
    } 
}