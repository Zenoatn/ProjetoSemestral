package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// ==========================================
// CLASSE UTILITÁRIA PARA CARREGAMENTO DE PRESETS LOCAIS
// ==========================================
public class Presets {
    
    public static List<Preset> carregarPresetsPadrao(){

        List<Preset> lista = new ArrayList<>();
        
        // Definição do usuário padrão do sistema
        Usuario sistema = new Usuario("0" , "ROOT");

        // ==========================================
        // PRESET 1: VAZIO (SEM ÁUDIO)
        // ==========================================
        DrumKit kitInicial = new DrumKit();

        kitInicial.associarSom("Q", null);
        kitInicial.associarSom("W", null);
        kitInicial.associarSom("E", null);
        kitInicial.associarSom("R", null);
        kitInicial.associarSom("A", null);
        kitInicial.associarSom("S", null);
        kitInicial.associarSom("D", null);
        kitInicial.associarSom("F", null);
        kitInicial.associarSom("Z", null);
        kitInicial.associarSom("X", null);
        kitInicial.associarSom("C", null);
        kitInicial.associarSom("V", null);  
        
        Preset vazio = new Preset(0,"Vazio", sistema, kitInicial);
        lista.add(vazio);

        // ==========================================
        // PRESET 2: DRUMKIT TRAP
        // ==========================================
        DrumKit trapKit = new DrumKit();

        trapKit.associarSom("Q", new File("Assets/DrumKits/Kick/Kick - Drake.wav").getAbsolutePath());
        trapKit.associarSom("W", new File("Assets/DrumKits/Clap Snaps/Clap - Metro.wav").getAbsolutePath());
        trapKit.associarSom("E", new File("Assets/DrumKits/Hihat/hihatglock.wav").getAbsolutePath());
        trapKit.associarSom("R", new File("Assets/DrumKits/Loops/loop_gut.wav").getAbsolutePath());
        trapKit.associarSom("A", new File("Assets/DrumKits/Open hat/OH - Wheezy Outta Here.wav").getAbsolutePath());
        trapKit.associarSom("S", new File("Assets/DrumKits/Snare/snaremusthave.wav").getAbsolutePath());
        trapKit.associarSom("D", new File("Assets/DrumKits/Hihat/hihatlove.wav").getAbsolutePath());
        trapKit.associarSom("F", new File("Assets/DrumKits/Loops/loop_hff.wav").getAbsolutePath());
        trapKit.associarSom("Z", new File("Assets/DrumKits/Percs/percmain.wav").getAbsolutePath());
        trapKit.associarSom("X", new File("Assets/DrumKits/Percs/perccow.wav").getAbsolutePath());
        trapKit.associarSom("C", new File("Assets/DrumKits/Percs/Shawty Redd.wav").getAbsolutePath());
        trapKit.associarSom("V", new File("Assets/DrumKits/Loops/loop_rlx.wav").getAbsolutePath());

        Preset trap = new Preset(1, "Trap", sistema, trapKit);
        lista.add(trap);

        // ==========================================
        // PRESET 3: DRUMKIT LOFI
        // ==========================================
        DrumKit lofiKit = new DrumKit();

        lofiKit.associarSom("Q", new File("Assets/DrumKits/Kick/CG_Kick (11).wav").getAbsolutePath());
        lofiKit.associarSom("W", new File("Assets/DrumKits/Clap Snaps/clapsnap5.wav").getAbsolutePath());
        lofiKit.associarSom("E", new File("Assets/DrumKits/Hihat/Hi-Hat LoFi.wav").getAbsolutePath());
        lofiKit.associarSom("R", new File("Assets/DrumKits/Loops/loop_hch.wav").getAbsolutePath());
        lofiKit.associarSom("A", new File("Assets/DrumKits/Open hat/OH - Goat.wav").getAbsolutePath());
        lofiKit.associarSom("S", new File("Assets/DrumKits/Snare/CG_Snare (22).wav").getAbsolutePath());
        lofiKit.associarSom("D", new File("Assets/DrumKits/Hihat/hihatzay.wav").getAbsolutePath());
        lofiKit.associarSom("F", new File("Assets/DrumKits/Loops/loop_njz.wav").getAbsolutePath());
        lofiKit.associarSom("Z", new File("Assets/DrumKits/Percs/Perc - Ting.wav").getAbsolutePath());
        lofiKit.associarSom("X", new File("Assets/DrumKits/Percs/Perc - Sosa Triangle.wav").getAbsolutePath());
        lofiKit.associarSom("C", new File("Assets/DrumKits/Percs/percxirx.wav").getAbsolutePath());
        lofiKit.associarSom("V", null);

        Preset lofi = new Preset(2, "LoFi", sistema, lofiKit);
        lista.add(lofi);

        return lista;
    }
}