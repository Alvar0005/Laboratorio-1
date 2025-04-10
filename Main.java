import java.util.*;
import java.io.*;
public class Main{
    public static void TiempoMatriz(){
        long tiempo=System.nanoTime();
        System.out.println("Tabla de encriptado vigenere:");
        BigVigenere clave = new BigVigenere();
        int position;
        try{
            BufferedReader isr = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("\n\nIngrese el mensaje a encriptar: ");
            String message = isr.readLine();
            String mensaje_encrypt=clave.Encrypt(message);
            System.out.println("\nEncrypt: " + mensaje_encrypt);
            System.out.println("Decrypt: " + clave.Decrypt(mensaje_encrypt));
            System.out.print("Ingrese una posición para buscar caracter en la matriz: ");
            Scanner sc = new Scanner(System.in);
            position = sc.nextInt();
            System.out.println("Search posición(" + position + "): " + clave.Search(position - 1) + "\nOpti-Search posición(" + position + "): " + clave.optimalSearch(position - 1));
        }
        catch(IOException e){
            System.err.println("Error de entrada: " + e.getMessage());
        }
        long tiempo_final=System.nanoTime();
        System.out.println(String.format("\nLa ejecución del testeo de la Matriz, se demoró: " + tiempo_final + " nanosegundos.\n"));
    }
    public static void TiempoTexto(String numericKey) {
        System.out.println("Tabla de encriptado vigenere:");
        long tiempo = System.nanoTime();
        BigVigenere clave = new BigVigenere(numericKey);
        try {                                                                  /*Cambiar la dirrección para que pueda leer el archivo*/
            BufferedReader Fr = new BufferedReader(new FileReader("C:\\Users\\matia\\OneDrive\\Escritorio\\Universidad\\Estructuras de Datos y Algoritmos\\Laboratorio 1\\Java\\Main-BigVigenere\\src\\El Ultimo Viaje de Elian.txt"//nombre del .txt que desee leer));
            StringBuilder texto = new StringBuilder();
            String linea;
            while ((linea = Fr.readLine()) != null) {
                texto.append(linea).append("\n");
            }
            Fr.close();

            String mensajeOriginal = texto.toString();
            String mensajeEncriptado = clave.Encrypt(mensajeOriginal);
            String mensajeDesencriptado = clave.Decrypt(mensajeEncriptado);
            System.out.println("\nTexto encriptado:\n" + mensajeEncriptado);
            System.out.println("\n\nDesea re encriptar el mensaje con una nueva llave?\n1)Si.\n2)No.\n= ");
            Scanner sc = new Scanner(System.in);
            int respuesta = sc.nextInt();
            switch (respuesta) {
                case 1:
                    clave.reEncrypt(mensajeEncriptado);
                    break;
                case 2:
                    break;
                default:
                    System.out.print("Respuesta inválida.");
                    break;
            }
            sc.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        long tiempo_final = System.nanoTime();
        System.out.println(String.format("\nLa ejecución del testeo de la Matriz, se demoró: " + tiempo_final + " nanosegundos.\n"));
    }
    public static void IniciarMatriz(char[][] matriz){    //Inicializar la matriz   27+27+10 = 64
        char indice='a', c;
        for(int i=0; i<64; i++){
            c=indice;
            for(int j=0; j<64; j++){
                matriz[i][j]=c;
                switch(c){
                    case 'n': c='ñ'; break;
                    case 'ñ': c='o'; break;
                    case 'N': c='Ñ'; break;
                    case 'Ñ': c='O'; break;
                    case 'z': c='A'; break;
                    case 'Z': c='0'; break;
                    case '9': c='a'; break;
                    default: c++; break;
                }
            }
            switch(indice){
                case 'n': indice='ñ'; break;
                case 'ñ': indice='o'; break;
                case 'N': indice='Ñ'; break;
                case 'Ñ': indice='O'; break;
                case 'z': indice='A'; break;
                case 'Z': indice='0'; break;
                case '9': indice='a'; break;
                default: indice++; break;
            }
        }
    }

    public static class BigVigenere{
        private int [] key;
        private char[][] alphabet;

        public BigVigenere(){
            alphabet = new char[64][64];
            IniciarMatriz(alphabet);
            Print();
            System.out.print("Ingrese una llave: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try{
                String clave = br.readLine();
                if(clave!=null && clave.matches("[0-9]+")){
                    this.key = new int[clave.length()];
                    System.out.print("Llave ingresada: |");
                }
                for(int i=0; i<clave.length(); i++){
                    if(clave.charAt(i)>='0' && clave.charAt(i)<='9'){
                        key[i]=Integer.parseInt(String.valueOf(clave.charAt(i)));
                        System.out.print(key[i] + "|"); //imprimir para probar
                    }
                    else{
                        System.out.print("Error de entrada: LLave no numérica.");
                        break;
                    }
                }
            }
            catch(IOException e){
                System.err.println("Error de entrada: " + e.getMessage());
            }
        }
        public BigVigenere(String numericKey){
            alphabet = new char[64][64];
            IniciarMatriz(alphabet);
            Print();
            if(numericKey!=null && numericKey.matches("[0-9]+")){
                this.key = new int[numericKey.length()];
                System.out.print("\nLlave ingresada: |");
            }
            for(int i=0; i<numericKey.length(); i++){
                if(numericKey.charAt(i)>='0' && numericKey.charAt(i)<='9'){
                    key[i]=Integer.parseInt(String.valueOf(numericKey.charAt(i)));
                    System.out.print(key[i] + "|"); //imprimir para probar
                }
                else{
                    System.out.print("Error de entrada: LLave no numérica.");
                    break;
                }
            }
        }
        public String Encrypt(String message){
            String encrypt_mensaje="";      //('a' - 'z' | 0 - 26) ; ('A' - 'Z' | 27 - 53) ; ('0' - '9' | 54 - 63)
            char clave;
            int a=0, k, load=0;
            for(int i=0; i<message.length(); i++){
                clave=message.charAt(i);
                if((clave<'a' || clave>'z') && (clave<'A' || clave>'Z') && (clave<'0' || clave>'9') && (clave!='ñ') && (clave!='Ñ')){
                    encrypt_mensaje+=clave;
                }
                else{
                    if((clave>='a' && clave<='z') || (clave=='ñ')){
                        load=0;
                    }
                    else if((clave>='A' && clave<='Z')  || (clave=='Ñ')){
                        load=27;
                    }
                    else if(clave>='0' && clave<='9'){
                        load=54;
                    }
                    for(int j=load; j<64; j++){
                        if(clave==alphabet[0][j]){
                            k=key[a] + 54;
                            encrypt_mensaje+=alphabet[k][j];
                            break;
                        }
                    }
                    if(key.length-1>a){ //key[4] (0 - 3)
                        a++;
                    }
                    else{
                        a=0;
                    }
                }
            }
            return encrypt_mensaje;
        }
        public String Decrypt(String encryptedMessage){
            String decrypt_mensaje="";      //('0' - '9' | 0 - 10) ; ('A' - 'Z' | 11 - 37 ) ; ('a' - 'z' | 38 - 63)
            char clave;
            int a=0, k;
            for(int i=0; i<encryptedMessage.length(); i++){
                clave=encryptedMessage.charAt(i);
                if((clave<'a' || clave>'z') && (clave<'A' || clave>'Z') && (clave<'0' || clave>'9') && (clave!='ñ') && (clave!='Ñ')){
                    decrypt_mensaje+=clave;
                }
                else{
                    k=key[a] + 54;
                    for(int j=0; j<64; j++){
                        if(clave==alphabet[k][j]){
                            decrypt_mensaje+=alphabet[0][j];
                            break;
                        }
                    }
                    if(key.length-1>a){ //key[4] (0 - 3)
                        a++;
                    }
                    else{
                        a=0;
                    }
                }
            }
            return decrypt_mensaje;
        }
        public void reEncrypt(){
            try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
                System.out.print("\nIngrese el mensaje encriptado: ");
                String mensaje = br.readLine();
                String mensaje_desencrypt=Decrypt(mensaje);
                System.out.println("\nMensaje decifrado: " + mensaje_desencrypt);
                System.out.print("Se le solicita ingresar una nueva llave numérica: ");
                String clave = br.readLine();
                if(clave!=null && clave.matches("[0-9]+")){
                    this.key = new int[clave.length()];
                    System.out.print("\nLlave ingresada: ");
                    for(int i=0; i<clave.length(); i++){
                        if(clave.charAt(i)>='0' && clave.charAt(i)<='9'){
                            key[i]=Integer.parseInt(String.valueOf(clave.charAt(i)));
                            System.out.print(key[i] + " "); //imprimir para probar
                        }
                        else{
                            System.out.print("Error de entrada: LLave no numérica.");
                        }
                    }
                    System.out.println("Nueva encryptacion: " + Encrypt(mensaje_desencrypt));
                }
                else{
                    System.out.print("LLave no ingresada.");
                }
            }
            catch (IOException e){
                System.err.println("Error de entrada: " + e.getMessage());
            }
        }
        public void reEncrypt(String encryptedMessage){
            try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
                String mensaje_desencrypt=Decrypt(encryptedMessage);
                System.out.println("\nMensaje decifrado: " + mensaje_desencrypt);
                System.out.print("Se le solicita ingresar una nueva llave numérica: ");
                String clave = br.readLine();
                if(clave!=null && clave.matches("[0-9]+")){
                    this.key = new int[clave.length()];
                    System.out.print("\nLlave ingresada: ");
                    for(int i=0; i<clave.length(); i++){
                        if(clave.charAt(i)>='0' && clave.charAt(i)<='9'){
                            key[i]=Integer.parseInt(String.valueOf(clave.charAt(i)));
                            System.out.print(key[i] + " ");
                        }
                        else{
                            System.out.print("Error de entrada: LLave no numérica.");
                        }
                    }
                    System.out.println("Nueva encryptacion:\n" + Encrypt(mensaje_desencrypt));
                }
                else{
                    System.out.print("LLave no ingresada.");
                }
            }
            catch (IOException e){
                System.err.println("Error de entrada: " + e.getMessage());
            }
        }
        public char Search(int position){
            if(position<0 || position>=64*64){
                System.err.println("Posición fuera de rango (0 - 4095).");
                return '?';
            }
            int contador=0;
            for(int fila=0; fila<64; fila++){
                for(int columna=0; columna<64; columna++){
                    if(contador==position){
                        return alphabet[fila][columna];
                    }
                    contador++;
                }
            }
            return '?';
        }
        public char optimalSearch(int position){
            if(position<0 || position>=64*64){
                System.err.println("Posición fuera de rango (0 - 4095).");
                return '?';
            }
            int a=position/64;
            int b=position%64;
            return alphabet[a][b];
        }
        public void Print(){
            for(int i=0; i<64; i++){
                for(int j=0; j<64; j++){
                    System.out.print(alphabet[i][j] + " ");
                }
                System.out.print("\n");
            }
        }
    }

    public static void main(String[] args){
        TiempoMatriz();
        Scanner sc = new Scanner(System.in);
        System.out.print("\n¿Desea usar una llave predeterminada o ingresar una propia?\n1)LLave predeterminada.\n2)Ingresar llave.\n=");
        int respuesta = sc.nextInt();
        switch(respuesta){
            case 1:
                System.out.print("¿Cuál tamaño de clave desea utilizar?\n a) 10 digitos.\n b) 50 digitos.\n c) 100 digitos.\n d) 500 digitos.\n e) 1000 digitos.\n f) 5000 digitos.\n= ");
                String opcion = sc.next();
                String llave="";
                try{
                    BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\matia\\OneDrive\\Escritorio\\Universidad\\Estructuras de Datos y Algoritmos\\Laboratorio 1\\Java\\Main-BigVigenere\\src\\5Mil_Numbers.txt"));
                    String cantidad = br.readLine();
                    br.close();
                    if(cantidad!=null && cantidad.length()>=5000){
                        switch(opcion){
                            case "a": llave = cantidad.substring(0, 10); break;
                            case "b": llave = cantidad.substring(0, 50); break;
                            case "c": llave = cantidad.substring(0, 100); break;
                            case "d": llave = cantidad.substring(0, 500); break;
                            case "e": llave = cantidad.substring(0, 1000); break;
                            case "f": llave = cantidad.substring(0, 5000); break;
                            default : System.out.println("Opción inválida."); return;
                        }
                        TiempoTexto(llave);
                    }
                    else{
                        System.out.println("El archivo no contiene suficientes números.");
                    }
                }
                catch(IOException e){
                    System.err.println("Error leyendo el archivo: " + e.getMessage());
                }
                break;
            case 2:
                System.out.print("Ingrese una llave numérica: ");
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String clave = br.readLine();
                    if(clave!=null && clave.matches("[0-9]+")){
                        TiempoTexto(clave);
                    }
                    else{
                        System.out.println("Clave inválida, debe ser numérica.");
                    }
                }
                catch(IOException e){
                    System.err.println("Error de entrada: " + e.getMessage());
                }
                break;
            default : System.out.print("Opción inválida."); break;
        }
    }
}