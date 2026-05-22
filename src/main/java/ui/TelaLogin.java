package ui;

import Model.Usuario;
import Model.DrumKit;
import Model.Preset;
import Model.Presets;
import Database.UsuarioDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Tela de Login / Identificação do Usuário.
 * Interface gráfica atualizada com botões separados para Login e Registro.
 */
public class TelaLogin extends JFrame {

    private JTextField txtNome;
    private JTextField txtRa;

    public TelaLogin() {
        // 1. CONFIGURAÇÕES DA JANELA
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true); 

        getContentPane().setBackground(Color.decode("#1E1E24"));

        // 2. CRIANDO A BARRA DE TÍTULO ESCURA CUSTOMIZADA
        JPanel barraTitulo = new JPanel(new BorderLayout());
        barraTitulo.setBackground(Color.decode("#121212"));
        barraTitulo.setPreferredSize(new Dimension(getWidth(), 35));

        JLabel titulo = new JLabel("   Identificação do Usuário");
        titulo.setForeground(Color.LIGHT_GRAY);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        barraTitulo.add(titulo, BorderLayout.WEST);

        JButton btnFechar = new JButton("X");
        btnFechar.setFocusPainted(false);
        btnFechar.setBorderPainted(false);
        btnFechar.setBackground(Color.decode("#121212"));
        btnFechar.setForeground(Color.GRAY);
        btnFechar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnFechar.addActionListener(e -> System.exit(0));

        btnFechar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnFechar.setForeground(Color.WHITE);
                btnFechar.setBackground(Color.RED);
            }
            public void mouseExited(MouseEvent evt) {
                btnFechar.setForeground(Color.GRAY);
                btnFechar.setBackground(Color.decode("#121212"));
            }
        });
        barraTitulo.add(btnFechar, BorderLayout.EAST);

        MoverJanela listenerMover = new MoverJanela(this);
        barraTitulo.addMouseListener(listenerMover);
        barraTitulo.addMouseMotionListener(listenerMover);

        add(barraTitulo, BorderLayout.NORTH);

        // 3. PAINEL CENTRAL DO FORMULÁRIO
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBackground(Color.decode("#1E1E24"));
        painelForm.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campo: Nome Completo
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setForeground(Color.LIGHT_GRAY);
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 14));
        painelForm.add(lblNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtNome = new JTextField();
        txtNome.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtNome.setBackground(Color.decode("#2A2A35"));
        txtNome.setForeground(Color.WHITE);
        txtNome.setCaretColor(Color.WHITE);
        txtNome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#3A3A45"), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        painelForm.add(txtNome, gbc);

        // Campo: R.A.
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel lblRa = new JLabel("R.A. (Registro Acadêmico):");
        lblRa.setForeground(Color.LIGHT_GRAY);
        lblRa.setFont(new Font("SansSerif", Font.BOLD, 14));
        painelForm.add(lblRa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        txtRa = new JTextField();
        txtRa.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtRa.setBackground(Color.decode("#2A2A35"));
        txtRa.setForeground(Color.WHITE);
        txtRa.setCaretColor(Color.WHITE);
        txtRa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#3A3A45"), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        painelForm.add(txtRa, gbc);

        // ==========================================
        // 4. PAINEL DE BOTÕES (LOGIN E REGISTRAR)
        // ==========================================
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(25, 10, 10, 10);
        
        // Criamos um painel interno com 2 colunas para colocar os botões lado a lado
        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 15, 0));
        painelBotoes.setBackground(Color.decode("#1E1E24"));

        // Botão REGISTRAR (Secundário)
        JButton btnRegistrar = new JButton("REGISTRAR");
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setBackground(Color.decode("#f5b041")); // Cor cinza escuro
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRegistrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btnRegistrar.setBackground(Color.decode("#4A4A55")); }
            public void mouseExited(MouseEvent evt) { btnRegistrar.setBackground(Color.decode("#3A3A45")); }
        });
        btnRegistrar.addActionListener(e -> executarRegistro());

        // Botão ENTRAR (Primário)
        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEntrar.setForeground(Color.BLACK);
        btnEntrar.setBackground(Color.decode("#06e2c5")); // Cor Ciano
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnEntrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btnEntrar.setBackground(Color.decode("#05c4ab")); }
            public void mouseExited(MouseEvent evt) { btnEntrar.setBackground(Color.decode("#06e2c5")); }
        });
        btnEntrar.addActionListener(e -> executarLogin());

        // Adiciona os botões no painel
        painelBotoes.add(btnRegistrar);
        painelBotoes.add(btnEntrar);

        painelForm.add(painelBotoes, gbc);
        add(painelForm, BorderLayout.CENTER);
    }

    /**
     * Tenta entrar com um usuário já existente.
     */
    private void executarLogin() {
        String nome = txtNome.getText().trim();
        String ra = txtRa.getText().trim();

        if (nome.isEmpty() || ra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarPorRa(ra);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado! Verifique o R.A. digitado ou clique em 'REGISTRAR'.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        abrirLaunchpad(usuario);
    }

    /**
     * Tenta cadastrar um novo usuário caso o R.A. não exista.
     */
    private void executarRegistro() {
        String nome = txtNome.getText().trim();
        String ra = txtRa.getText().trim();

        if (nome.isEmpty() || ra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos para se registrar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioExistente = usuarioDAO.buscarPorRa(ra);

        if (usuarioExistente != null) {
            JOptionPane.showMessageDialog(this, "Este R.A. já possui cadastro! Por favor, clique em 'ENTRAR'.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario novoUsuario = new Usuario(ra, nome);
        usuarioDAO.inserir(novoUsuario);
        
        JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso! Bem-vindo(a), " + nome + ".", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        
        abrirLaunchpad(novoUsuario);
    }

    /**
     * Centraliza a abertura da tela principal para não repetir código.
     */
    private void abrirLaunchpad(Usuario usuario) {
        List<Preset> presets = Presets.carregarPresetsPadrao();
        Preset presetInicial = presets.get(0);
        DrumKit kit = presetInicial.getKit();

        new LaunchpadAvancado(usuario, kit).setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }

    // Subclasse para permitir arrastar a janela customizada
    class MoverJanela extends MouseAdapter {
        private Point clickInicial;
        private JFrame janela;

        public MoverJanela(JFrame janela) {
            this.janela = janela;
        }

        public void mousePressed(MouseEvent e) {
            clickInicial = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            int thisX = janela.getLocation().x;
            int thisY = janela.getLocation().y;
            int xMoved = e.getX() - clickInicial.x;
            int yMoved = e.getY() - clickInicial.y;
            janela.setLocation(thisX + xMoved, thisY + yMoved);
        }
    }
}