package Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AudioPlayer {
    
    // Mapa que guarda na memória quais teclas estão com o loop ativado
    private static Map<String, Clip> loopsAtivos = new HashMap<>();

    /**
     * Toca um som normal uma única vez (One-Shot).
     */
    public static void tocarSom(String caminho) {
        if (caminho == null || caminho.trim().isEmpty()) return;
        try {
            File arquivo = new File(caminho);
            if (arquivo.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivo);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Erro ao reproduzir o áudio: " + e.getMessage());
        }
    }

    /**
     * NOVO: Inicia um áudio em repetição infinita.
     */
    public static void iniciarLoop(String caminho, String idTecla) {
        if (caminho == null || caminho.trim().isEmpty()) return;
        try {
            File arquivo = new File(caminho);
            if (arquivo.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivo);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                
                clip.loop(Clip.LOOP_CONTINUOUSLY); // A magia do loop contínuo
                loopsAtivos.put(idTecla, clip); // Guarda a referência para podermos parar depois
            }
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o loop: " + e.getMessage());
        }
    }

    /**
     * NOVO: Para o loop específico de uma tecla.
     */
    public static void pararLoop(String idTecla) {
        Clip clip = loopsAtivos.get(idTecla);
        if (clip != null) {
            clip.stop();
            clip.close();
            loopsAtivos.remove(idTecla);
        }
    }

    /**
     * NOVO: Botão de pânico! Corta todo o som de loop de uma vez.
     */
    public static void pararTodosOsLoops() {
        for (Clip clip : loopsAtivos.values()) {
            if (clip != null) {
                clip.stop();
                clip.close();
            }
        }
        loopsAtivos.clear();
    }
}