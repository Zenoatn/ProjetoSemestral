package Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AudioPlayer {
    
    // ==========================================
    // CACHE NA RAM (Áudios abertos e prontos para tocar)
    // ==========================================
    private static Map<String, Clip> cacheSons = new HashMap<>();
    
    // Gerenciamento dos loops ativos
    private static Map<String, Clip> loopsAtivos = new HashMap<>();

    // ==========================================
    // PRÉ-CARREGAMENTO (NOVO)
    // ==========================================
    public static void carregarSonsNaMemoria(Map<String, String> mapeamento) {
        // 1. Limpa o cache antigo da memória RAM para evitar vazamentos
        for (Clip clip : cacheSons.values()) {
            if (clip != null) {
                clip.stop();
                clip.close();
            }
        }
        cacheSons.clear();
        loopsAtivos.clear();

        // 2. Carrega todos os novos sons na RAM
        for (Map.Entry<String, String> entry : mapeamento.entrySet()) {
            String tecla = entry.getKey();
            String caminho = entry.getValue();

            if (caminho != null && !caminho.trim().isEmpty()) {
                try {
                    File arquivo = new File(caminho);
                    if (arquivo.exists()) {
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivo);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        cacheSons.put(tecla, clip); // Salva o áudio já aberto e pronto na RAM
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao pré-carregar áudio da tecla " + tecla + ": " + e.getMessage());
                }
            }
        }
        System.out.println("Áudios pré-carregados na memória RAM com sucesso!");
    }

    // ==========================================
    // REPRODUÇÃO DE ÁUDIO (ONE-SHOT) - ZERO DELAY
    // ==========================================
    public static void tocarSom(String idTecla) {
        Clip clip = cacheSons.get(idTecla);
        if (clip != null) {
            clip.stop(); // Para o som instantaneamente se você metralhar a mesma tecla
            clip.setFramePosition(0); // Volta a agulha para o milissegundo zero
            clip.start(); // Dispara o som
        }
    }

    // ==========================================
    // CONTROLE DE REPETIÇÃO (LOOPS) - ZERO DELAY
    // ==========================================
    public static void iniciarLoop(String idTecla) {
        Clip clip = cacheSons.get(idTecla);
        if (clip != null) {
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