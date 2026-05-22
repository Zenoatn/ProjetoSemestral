package ui;
import Model.Usuario;
import Model.DrumKit;
import Model.Preset;
import Model.Presets;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Tela de Login / Identificação do Usuário.
 * Interface gráfica desenvolvida manualmente para coleta do Nome e R.A.
 * Atua como a porta de entrada do aplicativo.
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
        setUndecorated(true); // Mantém o visual moderno sem a barra nativa do sistema

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

        // Permite arrastar a janela por essa barra
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

        // Botão de Entrar
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(25, 10, 10, 10);
        
        JButton btnEntrar = new JButton("ENTRAR NO LAUNCHPAD");
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEntrar.setForeground(Color.BLACK);
        btnEntrar.setBackground(Color.decode("#06e2c5")); // Cor ciano neon para dar identidade ao app
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnEntrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnEntrar.setBackground(Color.decode("#05c4ab")); // Escurece no hover
            }
            public void mouseExited(MouseEvent evt) {
                btnEntrar.setBackground(Color.decode("#06e2c5"));
            }
        });

        btnEntrar.addActionListener(e -> executarLogin());
        painelForm.add(btnEntrar, gbc);

        add(painelForm, BorderLayout.CENTER);
    }

    /**
     * Valida as entradas e passa os dados para a janela do Launchpad.
     */
    /**
     * Valida as entradas, salva o usuário no banco na nuvem e passa os dados para a janela do Launchpad.
     */
    private void executarLogin() {
        String nome = txtNome.getText().trim();
        String ra = txtRa.getText().trim();

        // Validação básica de campos vazios
        if (nome.isEmpty() || ra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // =======================================================================
        // O PULO DO GATO: Chama o banco de dados para buscar ou cadastrar o usuário na nuvem!
        // =======================================================================
        Database.UsuarioDAO usuarioDAO = new Database.UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarOuCriar(ra, nome);
        // =======================================================================

        List<Preset> presets = Presets.carregarPresetsPadrao();
        Preset presetInicial = presets.get(0);
        DrumKit kit = presetInicial.getKit();

        // Abre a janela principal passando a sessão criada e confirmada no banco
        new LaunchpadAvancado(usuario, kit).setVisible(true);
        
        // Fecha e libera os recursos da tela de login
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