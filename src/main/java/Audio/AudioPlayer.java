package Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AudioPlayer {
    
    // ==========================================
    // CACHE NA RAM (POOL DE CLIPS / POLIFONIA)
    // ==========================================
    private static Map<String, Clip[]> cacheSons = new HashMap<>();
    private static Map<String, Integer> indiceAtual = new HashMap<>(); 
    
    // Gerenciamento dos loops ativos
    private static Map<String, Clip> loopsAtivos = new HashMap<>();

    // ==========================================
    // PRÉ-CARREGAMENTO (NOVO)
    // ==========================================
    public static void carregarSonsNaMemoria(Map<String, String> mapeamento) {
        // 1. Limpa o cache antigo da memória RAM para evitar vazamentos
        for (Clip[] pool : cacheSons.values()) {
            if (pool != null) {
                for (Clip clip : pool) {
                    if (clip != null) {
                        clip.stop();
                        clip.close();
                    }
                }
            }
        }
        cacheSons.clear();
        loopsAtivos.clear();
        indiceAtual.clear();

        // 2. Carrega todos os novos sons na RAM com polifonia (4 vozes)
        for (Map.Entry<String, String> entry : mapeamento.entrySet()) {
            String tecla = entry.getKey();
            String caminho = entry.getValue();

            if (caminho != null && !caminho.trim().isEmpty()) {
                try {
                    File arquivo = new File(caminho);
                    if (arquivo.exists()) {
                        Clip[] pool = new Clip[4];
                        for (int i = 0; i < 4; i++) {
                            AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivo);
                            Clip clip = AudioSystem.getClip();
                            clip.open(audioStream);
                            pool[i] = clip;
                        }
                        cacheSons.put(tecla, pool);
                        indiceAtual.put(tecla, 0); 
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao pré-carregar áudio da tecla " + tecla + ": " + e.getMessage());
                }
            }
        }
        System.out.println("Áudios pré-carregados na memória RAM com sucesso (Polifonia 4x)!");
    }

    // ==========================================
    // REPRODUÇÃO DE ÁUDIO (ONE-SHOT) - ZERO DELAY + OVERLAP
    // ==========================================
    public static void tocarSom(String idTecla) {
        Clip[] pool = cacheSons.get(idTecla);
        if (pool != null) {
            int idx = indiceAtual.get(idTecla);
            Clip clip = pool[idx];
            
            clip.stop(); 
            clip.setFramePosition(0); 
            clip.start(); 
            
            indiceAtual.put(idTecla, (idx + 1) % pool.length);
        }
    }

    // ==========================================
    // CONTROLE DE REPETIÇÃO (LOOPS) - ZERO DELAY
    // ==========================================
    public static void iniciarLoop(String idTecla) {
        Clip[] pool = cacheSons.get(idTecla);
        if (pool != null) {
            Clip clip = pool[0]; // Usa sempre a primeira voz para loops
            clip.stop();
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            loopsAtivos.put(idTecla, clip); 
        }
    }

    public static void pararLoop(String idTecla) {
        Clip clip = loopsAtivos.get(idTecla);
        if (clip != null) {
            clip.stop();
            loopsAtivos.remove(idTecla);
        }
    }

    public static void pararTodosOsLoops() {
        for (Clip clip : loopsAtivos.values()) {
            if (clip != null) {
                clip.stop();
            }
        }
        loopsAtivos.clear();
    }
}