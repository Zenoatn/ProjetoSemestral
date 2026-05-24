package Model;

import java.util.HashMap;
import java.util.Map;

// ==========================================
// ENTIDADE DRUMKIT: MAPEAMENTO TECLA -> ÁUDIO
// ==========================================
public class DrumKit {
    
    // Estrutura de dados para guardar o estado na memória (Tecla = Caminho do Arquivo)
    private Map<String, String> mapeamentoSons;

    // ==========================================
    // CONSTRUTOR
    // ==========================================
    public DrumKit() {
        this.mapeamentoSons = new HashMap<>();
    }

    // ==========================================
    // CONFIGURAÇÃO DE SOM ESPECÍFICO
    // ==========================================
    public void associarSom(String tecla, String caminhoArquivo) {
        mapeamentoSons.put(tecla.toUpperCase(), caminhoArquivo);
    }

    // ==========================================
    // CONSULTA DE ÁUDIO PARA REPRODUÇÃO
    // ==========================================
    public String obterCaminhoSom(String tecla) {
        return mapeamentoSons.get(tecla.toUpperCase());
    }

    // ==========================================
    // EXPORTAÇÃO DO MAPEAMENTO COMPLETO (USADO PELO DAO)
    // ==========================================
    public Map<String, String> getTodosOsSons() {
        return mapeamentoSons;
    } 
}