package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControladorTeclado implements KeyListener {

    // true = tecla presionada, false = tecla suelta
    public boolean moverIzquierda, moverDerecha, atacarPunio, atacarPatada, recoger, pausa, saltar;

    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_A     || tecla == KeyEvent.VK_LEFT)  moverIzquierda = true;
        if (tecla == KeyEvent.VK_D     || tecla == KeyEvent.VK_RIGHT) moverDerecha   = true;
        if (tecla == KeyEvent.VK_W     || tecla == KeyEvent.VK_UP)    saltar         = true;
        if (tecla == KeyEvent.VK_J)                                    atacarPunio    = true;
        if (tecla == KeyEvent.VK_K)                                    atacarPatada   = true;
        if (tecla == KeyEvent.VK_E)                                    recoger        = true;
        if (tecla == KeyEvent.VK_ESCAPE)                               pausa          = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_A     || tecla == KeyEvent.VK_LEFT)  moverIzquierda = false;
        if (tecla == KeyEvent.VK_D     || tecla == KeyEvent.VK_RIGHT) moverDerecha   = false;
        if (tecla == KeyEvent.VK_W     || tecla == KeyEvent.VK_UP)    saltar         = false;
        if (tecla == KeyEvent.VK_J)                                    atacarPunio    = false;
        if (tecla == KeyEvent.VK_K)                                    atacarPatada   = false;
        if (tecla == KeyEvent.VK_E)                                    recoger        = false;
        if (tecla == KeyEvent.VK_ESCAPE)                               pausa          = false;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }
}
