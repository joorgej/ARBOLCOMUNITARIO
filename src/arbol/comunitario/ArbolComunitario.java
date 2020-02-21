/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbol.comunitario;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jorge
 */
public class ArbolComunitario {
    
    //----------------------------------------------------------------------------------------------------------
    //-----------------------------------------------TOKEN------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------
        public static class Token{
            //TIPO (ESTO ES MAS FACIL CON UN ENUM)
            private int tipo;
                //1. PUNTO .
                //2. BARRA |
                //3. ASTERISCO *
                //4. SUMA +
                //5. INTERROGACION ?
                //6. CADENA
                //7. IDENTIFICADOR
                //8. NUMERO
                //9. ACEPTACION #
            
            //LEXEMA
            private String lexema;

            //CONSTRUCTOR
            public Token(int tipo, String lexema) {
                this.tipo = tipo;
                this.lexema = lexema;
            }

            //GETTERS & SETTERS
            public int getTipo() {
                return tipo;
            }

            public void setTipo(int tipo) {
                this.tipo = tipo;
            }

            public String getLexema() {
                return lexema;
            }

            public void setLexema (String lexema) {
                this.lexema = lexema;
            }
        }
    
    //----------------------------------------------------------------------------------------------------------
    //-----------------------------------------------HOJA-------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------
    
        public static class Hoja {
            
            //TIPO (ESTO ES MAS FACIL CON UN ENUM)
            private int tipo;
                //1. PUNTO .
                //2. BARRA |
                //3. ASTERISCO *
                //4. SUMA +
                //5. INTERROGACION ?
                //6. TERMINAL (CADENA, NUMERO, IDENTIFICADOR)
                //7. ACEPTACION #
            
            //VALOR
            private String valor;
            //LISTA DE PRIMEROS
            private ArrayList<Hoja> primeros;
            //LISTA DE ULTIMOS
            private ArrayList<Hoja> ultimos;
            //ANULABLE
            private boolean anulable;
            //IDENTIFICADOR
            private Integer identificador;

            //CONSTRUCTOR
            public Hoja(int tipo, String valor) {
                this.tipo = tipo;
                this.valor = valor;
                this.primeros = new ArrayList();
                this.ultimos = new ArrayList();
            }

            //GETTERS & SETTERS
            public int getTipo() {
                return tipo;
            }

            public void setTipo(int tipo) {
                this.tipo = tipo;
            }

            public String getValor() {
                return valor;
            }

            public void setValor(String valor) {
                this.valor = valor;
            }

            public ArrayList<Hoja> getPrimeros() {
                return primeros;
            }

            public void setPrimeros(ArrayList<Hoja> primeros) {
                this.primeros = primeros;
            }

            public ArrayList<Hoja> getUltimos() {
                return ultimos;
            }

            public void setUltimos(ArrayList<Hoja> ultimos) {
                this.ultimos = ultimos;
            }

            public boolean isAnulable() {
                return anulable;
            }

            public void setAnulable(boolean anulable) {
                this.anulable = anulable;
            }

            public Integer getIdentificador() {
                return identificador;
            }

            public void setIdentificador(Integer identificador) {
                this.identificador = identificador;
            }
            
            //METODOS ADD (SON PARA AGREGAR DATOS A LAS LISTAS DE ULTIMOS Y PRIMEROS)
            
            public void addPrimeros(ArrayList<Hoja> primeros){
                for(Hoja h : primeros)
                {
                    this.primeros.add(h);
                }
            }
            
            public void addUltimos(ArrayList<Hoja> ultimos){
                for(Hoja h : ultimos)
                {
                    this.ultimos.add(h);
                }
            }
            
            public void addPrimero(Hoja hoja){
                this.primeros.add(hoja);
            }
            
            public void addUltimo(Hoja hoja){
                this.ultimos.add(hoja);
            }

    }
    
        
        
    //----------------------------------------------------------------------------------------------------------
    //-----------------------------------------------NODO-------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------

        public static class Nodo {

            //HIJO IZQUIERDO
            private Nodo izq;
            //HIJO DERECHO
            private Nodo der;
            //HOJA
            private Hoja info;
            //IDENTIFICADOR
            private int identificador;
            //CONSTRUCTOR
            public Nodo(Hoja data, int id) {
                this.info = data;
                this.identificador = id;
            }
            
            //GETTERS & SETTERS
            
            public Nodo getIzq() {
                return izq;
            }

            public void setIzq(Nodo izq) {
                this.izq = izq;
            }

            public Nodo getDer() {
                return der;
            }

            public void setDer(Nodo der) {
                this.der = der;
            }

            public Hoja getInfo() {
                return info;
            }

            public void setInfo(Hoja info) {
                this.info = info;
            }

            public int getIdentificador(){
                return identificador;
            }
            
            public void setIdentificador(int identificador){
                this.identificador = identificador;
            }

        }

    //--------------------------------------------------------------------------------------------------------------
    //------------------------------------------------ARBOL---------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------

    public static class Arbol {
        
        //RAIZ
        private Nodo root;
        //TOKENS ENTRANTES DEL ANALISIS LEXICO (SOLAMENTE LOS TOKENS DE LA EXPRESION REGULAR)
        private LinkedList<Token> expresion;
        //CONTADOR PARA PONER IDENTIFICADORES A LOS NODOS
        private int contador;
        //CONTADOR PARA ENUMERAR TERMINALES
        private int numeracion;
        //LISTA SIGUIENTES O FOLLOWS
        private ArrayList<Hoja> siguientes;

        public Arbol(LinkedList<Token> expresion) {
            this.siguientes = new ArrayList();
            this.expresion = expresion;
            this.contador = 0;
            this.numeracion = 1;
            this.root = agregarNodo();
        }

        private Nodo agregarNodo() {

            //-------------------------CASO . ------------------------------------
            if (this.expresion.getFirst().getTipo() == 1) {
                
                Nodo nodo = new Nodo(new Hoja(1, "."), contador);
                this.expresion.removeFirst();
                contador++;
                
                nodo.setIzq(agregarNodo());
                nodo.setDer(agregarNodo());

                //ANULABLE
                if (nodo.getDer().getInfo().isAnulable() && nodo.getIzq().getInfo().isAnulable()) {
                    nodo.getInfo().setAnulable(true);
                } else {
                    nodo.getInfo().setAnulable(false);
                }

                //PRIMEROS
                if (nodo.getIzq().getInfo().isAnulable()) {
                    nodo.getInfo().addPrimeros(nodo.getIzq().getInfo().getPrimeros());
                    nodo.getInfo().addPrimeros(nodo.getDer().getInfo().getPrimeros());
                } else {
                    nodo.getInfo().addPrimeros(nodo.getIzq().getInfo().getPrimeros());
                }

                //ULTIMOS
                if (nodo.getDer().getInfo().isAnulable()) {
                    nodo.getInfo().addUltimos(nodo.getIzq().getInfo().getUltimos());
                    nodo.getInfo().addUltimos(nodo.getDer().getInfo().getUltimos());
                } else {
                    nodo.getInfo().addUltimos(nodo.getDer().getInfo().getUltimos());
                }

                return nodo;

                //-------------------------CASO |------------------------------------
            } else if (this.expresion.getFirst().getTipo() == 2) {
                Nodo nodo = new Nodo(new Hoja(2, "|"), contador);
                this.expresion.removeFirst();
                contador++;
                nodo.setIzq(agregarNodo());
                nodo.setDer(agregarNodo());

                //ANULABLE
                if (nodo.getDer().getInfo().isAnulable() || nodo.getIzq().getInfo().isAnulable()) {
                    nodo.getInfo().setAnulable(true);
                } else {
                    nodo.getInfo().setAnulable(false);
                }

                //PRIMEROS
                nodo.getInfo().addPrimeros(nodo.getIzq().getInfo().getPrimeros());
                nodo.getInfo().addPrimeros(nodo.getDer().getInfo().getPrimeros());
                
                //ULTIMOS
                nodo.getInfo().addUltimos(nodo.getIzq().getInfo().getUltimos());
                nodo.getInfo().addUltimos(nodo.getDer().getInfo().getUltimos());
                
                return nodo;

                //-------------------------CASO *------------------------------------
            } else if (this.expresion.getFirst().getTipo() == 3) {
                Nodo nodo = new Nodo(new Hoja(3, "*"), contador);
                this.expresion.removeFirst();
                contador++;
                nodo.setIzq(agregarNodo());

                //ANULABLES
                nodo.getInfo().setAnulable(true);
                
                //PRIMEROS
                nodo.getInfo().setPrimeros(nodo.getIzq().getInfo().getPrimeros());
                
                //ULTIMOS
                nodo.getInfo().setUltimos(nodo.getIzq().getInfo().getUltimos());

                return nodo;

                //-------------------------CASO +------------------------------------
            } else if (this.expresion.getFirst().getTipo() == 4) {
                Nodo nodo = new Nodo(new Hoja(4, "+"), contador);
                this.expresion.removeFirst();
                contador++;
                
                nodo.setIzq(agregarNodo());

                //ANULABLE
                nodo.getInfo().setAnulable(nodo.getIzq().getInfo().isAnulable());

                //PRIMEROS
                nodo.getInfo().setPrimeros(nodo.getIzq().getInfo().getPrimeros());
                
                //ULTIMOS
                nodo.getInfo().setUltimos(nodo.getIzq().getInfo().getUltimos());


                return nodo;

                //-------------------------CASO ?------------------------------------
            } else if (this.expresion.getFirst().getTipo() == 5) {
                Nodo nodo = new Nodo(new Hoja(5, "?"), contador);
                this.expresion.removeFirst();
                contador++;
                
                nodo.setIzq(agregarNodo());
                
                //ANULABLE
                nodo.getInfo().setAnulable(true);
                    
                //PRIMEROS
                nodo.getInfo().setPrimeros(nodo.getIzq().getInfo().getPrimeros());
                
                //ULTIMOS
                nodo.getInfo().setUltimos(nodo.getIzq().getInfo().getUltimos());

                return nodo;

                //-------------------------CASO TERMINAL------------------------------------
            } else if (this.expresion.getFirst().getTipo() == 6
                    || this.expresion.getFirst().getTipo() == 7
                    || this.expresion.getFirst().getTipo() == 8) {
                Nodo nodo = new Nodo(new Hoja(6, this.expresion.getFirst().getLexema()), contador);
                this.expresion.removeFirst();
                contador++;
                
                //IDENTIFICADOR
                nodo.getInfo().setIdentificador(numeracion);
                numeracion++;
                
                //ANULABLE
                nodo.getInfo().setAnulable(false);
                
                //PRIMEROS
                nodo.getInfo().addPrimero(nodo.getInfo());
                
                //ULTIMOS
                nodo.getInfo().addUltimo(nodo.getInfo());

                return nodo;

            } else if (this.expresion.getFirst().getTipo() == 9){
                Nodo nodo = new Nodo(new Hoja(7, this.expresion.getFirst().getLexema()), contador);
                this.expresion.removeFirst();
                contador++;
                
                //IDENTIFICADOR
                nodo.getInfo().setIdentificador(numeracion);
                numeracion++;
                
                //ANULABLE
                nodo.getInfo().setAnulable(false);
                
                //PRIMEROS
                nodo.getInfo().addPrimero(nodo.getInfo());
                
                //ULTIMOS
                nodo.getInfo().addUltimo(nodo.getInfo());
                
                return nodo;
            }
            return null;
        }
        

        private String dot1(Nodo nodo) {

            String out = "";

            if (nodo == null) {
                return out;
            }
            if (nodo.getIzq() != null) {
                out += "nodo" + nodo.getIdentificador()+ "->" + "nodo" + nodo.getIzq().getIdentificador() + "; \n";
                out += dot1(nodo.getIzq());
            }
            if (nodo.getDer() != null) {
                out += "nodo" + nodo.getIdentificador() + "->" + "nodo" + nodo.getDer().getIdentificador() + "; \n";
                out += dot1(nodo.getDer());
            }

            return out;
        }

        private String dot2(Nodo nodo) {
            String out = "";

            if (nodo == null) {
                return out;
            } else {
                out += "nodo" + nodo.getIdentificador() + " [label = <<table border=\"0\" cellspacing=\"0\"> <tr>";

                //ID
                if (nodo.getInfo().getIdentificador() != null) {
                    out += "<td border=\"1\" bgcolor=\"white\" fixedsize=\"true\" >" + nodo.getInfo().identificador+ "</td>";
                } 

                //VALOR
                if(nodo.getInfo().getTipo() == 6){
                    out += "<td border=\"1\" bgcolor=\"yellow\" fixedsize=\"true\" >" + nodo.getInfo().getValor() + "</td>";
                }else{
                    out += "<td border=\"1\" bgcolor=\"white\" fixedsize=\"true\" >" + nodo.getInfo().getValor() + "</td>";
                }
                //ANULABLE
                if (nodo.getInfo().isAnulable()) {
                    out += "<td border=\"1\" bgcolor=\"white\" fixedsize=\"true\" >" + "ANULABLE" + "</td>";

                } else {
                    out += "<td border=\"1\" bgcolor=\"white\" fixedsize=\"true\">" + "NO ANULABLE" + "</td>";

                }

                //primeros
                if (nodo.getInfo().getPrimeros() == null) {
                    out += "<td border=\"1\" bgcolor=\"white\" fixedsize=\"true\" >" + nodo.getInfo().getIdentificador() + "</td>";
                } else {
                    out += "<td border=\"1\" bgcolor=\"white\" fixedsize=\"true\" >";

                    for (int i = 0; i < nodo.getInfo().getPrimeros().size(); i++) {
                        out += nodo.getInfo().getPrimeros().get(i).getIdentificador();
                        if (i != nodo.getInfo().getPrimeros().size() - 1) {
                            out += ", ";
                        }

                    }

                    out += "</td>";
                }

                //ultimos
                if (nodo.getInfo().getUltimos() == null) {
                    out += "<td border=\"1\" bgcolor=\"white\" fixedsize=\"true\" >" + nodo.getInfo().getIdentificador() + "</td>";
                } else {
                    out += "<td border=\"1\" bgcolor=\"white\" fixedsize=\"true\" >";

                    for (int i = 0; i < nodo.getInfo().getUltimos().size(); i++) {
                        out += nodo.getInfo().getUltimos().get(i).getIdentificador();

                        if (i != nodo.getInfo().getUltimos().size() - 1) {
                            out += ", ";
                        }
                    }

                    out += "</td>";
                }

                out += "</tr></table>>]; \n";

                out += dot2(nodo.getIzq());
                out += dot2(nodo.getDer());

                return out;
            }

        }

        public void dot() throws InterruptedException {
            contador = 0;
            FileWriter fichero = null;
            PrintWriter pw = null;
            try {
                fichero = new FileWriter("arbol.dot");
                pw = new PrintWriter(fichero);

                pw.println("digraph { ");
                pw.println("rankdir = TB; ");
                pw.println("node [shape = none];");

                pw.println(dot2(this.root));

                pw.println(dot1(this.root));

                pw.println("}");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fichero) {
                        fichero.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            sleep(150);

            try {
                String[] cmd = {"dot", "-Tpng", "arbol.dot", "-o", "arbol.png"};
                Runtime.getRuntime().exec(cmd);
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
            sleep(300);
            

        }        

    }

    public static void main(String[] args) throws InterruptedException {

        LinkedList<Token> tokens = new LinkedList(); 
        
        tokens.add(new Token(1,"."));
        tokens.add(new Token(1,"."));
        tokens.add(new Token(1,"."));
        tokens.add(new Token(1,"."));
        tokens.add(new Token(6,"AGRADECIDO"));
        tokens.add(new Token(6,"CON"));
        tokens.add(new Token(6,"EL"));
        tokens.add(new Token(6,"DE"));
        tokens.add(new Token(6,"ARRIBA"));
        
        Arbol arbol = new Arbol(tokens);
        arbol.dot();
        
        
    }

}
