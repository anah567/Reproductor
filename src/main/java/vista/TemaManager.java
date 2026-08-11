package vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;

public final class TemaManager {

    private TemaManager() {
    }

    public static Color fondoPrincipal() {
        return Tema.esOscuro() ? TemaOscuro.FONDO_PRINCIPAL : TemaClaro.FONDO_PRINCIPAL;
    }

    public static Color panelSecundario() {
        return Tema.esOscuro() ? TemaOscuro.PANEL_SECUNDARIO : TemaClaro.PANEL_SECUNDARIO;
    }

    public static Color panelDestacado() {
        return Tema.esOscuro() ? TemaOscuro.PANEL_DESTACADO : TemaClaro.PANEL_DESTACADO;
    }

    public static Color borde() {
        return Tema.esOscuro() ? TemaOscuro.BORDE : TemaClaro.BORDE;
    }

    public static Color textoPrincipal() {
        return Tema.esOscuro() ? TemaOscuro.TEXTO_PRINCIPAL : TemaClaro.TEXTO_PRINCIPAL;
    }

    public static Color textoSecundario() {
        return Tema.esOscuro() ? TemaOscuro.TEXTO_SECUNDARIO : TemaClaro.TEXTO_SECUNDARIO;
    }

    public static Color acento() {
        return Tema.esOscuro() ? TemaOscuro.COLOR_ACENTO : TemaClaro.COLOR_ACENTO;
    }

    public static void aplicarPanel(JPanel panel) {
        panel.setBackground(panelSecundario());
    }

    public static void aplicarTextoPrincipal(JLabel etiqueta) {
        etiqueta.setForeground(textoPrincipal());
    }

    public static void aplicarTextoSecundario(JLabel etiqueta) {
        etiqueta.setForeground(textoSecundario());
    }

    public static void aplicarBoton(JButton boton) {
        boton.setBackground(panelDestacado());
        boton.setForeground(textoPrincipal());
        boton.setBorder(BorderFactory.createLineBorder(borde(), 1));
    }

    public static void aplicarCombo(JComboBox<?> combo) {
        combo.setBackground(panelDestacado());
        combo.setForeground(textoPrincipal());
    }

    public static void aplicarCampo(JTextField campo) {
        campo.setBackground(panelDestacado());
        campo.setForeground(textoPrincipal());
        campo.setCaretColor(textoPrincipal());
        campo.setBorder(BorderFactory.createLineBorder(borde(), 1));
    }
}