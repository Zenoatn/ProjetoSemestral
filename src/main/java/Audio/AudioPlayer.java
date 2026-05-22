package Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Classe responsável por reproduzir os arquivos de áudio (.wav).
 */
public class AudioPlayer {

    /**
     * Toca um arquivo de áudio em uma nova Thread para não travar a interface gráfica.
     * @param caminhoArquivo O caminho do arquivo .wav no computador.
     */
    public static void tocarSom(String caminhoArquivo) {
        // Se o caminho for nulo ou vazio (tecla sem som configurado), não faz nada.
        if (caminhoArquivo == null || caminhoArquivo.trim().isEmpty()) {
            return;
        }

        // Inicia uma nova Thread
        new Thread(() -> {
            try {
                File arquivoAudio = new File(caminhoArquivo);
                
                if (arquivoAudio.exists()) {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivoAudio);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    
                    // Adicionar um listener para fechar o clip quando o som acabar
                    // liberando memória RAM
                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.close();
                        }
                    });
                } else {
                    System.err.println("Aviso: Arquivo de som não encontrado -> " + caminhoArquivo);
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                System.err.println("Erro ao reproduzir o áudio: " + ex.getMessage());
            }
        }).start();
    }
}