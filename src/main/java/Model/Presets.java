package Model;

import java.util.ArrayList;
import java.util.List;

public class Presets {
    
    public static List<Preset> carregarPresetsPadrao(){

        List<Preset> lista = new ArrayList<>();

        Usuario sistema = new Usuario("0" , "ROOT");

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


        DrumKit trapKit = new DrumKit();

        trapKit.associarSom("Q", "Assets/DrumKits/Kick Dminor.wav");
        trapKit.associarSom("W", null);
        trapKit.associarSom("E", null);
        trapKit.associarSom("R", null);
        trapKit.associarSom("A", null);
        trapKit.associarSom("S", null);
        trapKit.associarSom("D", null);
        trapKit.associarSom("F", null);
        trapKit.associarSom("Z", null);
        trapKit.associarSom("X", null);
        trapKit.associarSom("C", null);
        trapKit.associarSom("V", null);

        Preset trap = new Preset(1, "Trap", sistema, trapKit);

        lista.add(trap);

        DrumKit lofiKit = new DrumKit();

        lofiKit.associarSom("Q", null);
        lofiKit.associarSom("W", null);
        lofiKit.associarSom("E", null);
        lofiKit.associarSom("R", null);
        lofiKit.associarSom("A", null);
        lofiKit.associarSom("S", null);
        lofiKit.associarSom("D", null);
        lofiKit.associarSom("F", null);
        lofiKit.associarSom("Z", null);
        lofiKit.associarSom("X", null);
        lofiKit.associarSom("C", null);
        lofiKit.associarSom("V", null);

        Preset lofi = new Preset(2, "LoFi", sistema, lofiKit);

        lista.add(lofi);

        return lista;
    }
}
