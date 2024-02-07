import java.util.HashMap;
import java.util.ArrayList;

public class Compilador {
    private static HashMap<String, Tupla> variables = new HashMap<>();
    private static int variableAuxiliar = -1;
    private static int etiqueta = -1;

    public static void imprimirResultados(){
        PLXC.out.println("\n\n#Variables:");
        for(String key : variables.keySet()){
            String tipo = variables.get(key).getTipo();
            String valor = variables.get(key).getValor();
            String tam = variables.get(key).getTam();
            PLXC.out.println("#(" + key + ", " + tipo + ", " + valor + ", " + tam + ")");
        }
    }

    public static void asig(String ident, String valor, String cast){
        checkDeclaracion("asignacion", ident);
        boolean esVariable = valor.matches("[a-zA-Z][a-zA-Z0-9]*");
        String tipo_valor, tipo = variables.get(ident).getTipo();
        boolean entra = false;
        if(esVariable){ //si valor es una variable
            tipo_valor = variables.get(valor).getTipo();
            if(tipo.equals("float") && tipo_valor.equals("int")){ //si el tipo de ident es float y el tipo del valor es int
                //entra = true;
                asigFloat(ident, valor);
            }else if(tipo.equals("int") && tipo_valor.equals("float")){ //si el tipo del ident es int y el tipo del valor es float
                //entra = true;
                error("error de tipos");
            }else if(!tipo.equalsIgnoreCase(tipo_valor)){ //si el tipo que queremos asignar es distinto al de la variable
                error("error de tipos");
            }else{
                PLXC.out.println("   " + ident + " = " + valor + ";");
            }
            variables.put(ident, new Tupla(tipo, variables.get(valor).getValor(), "0"));
        }else{ //valor no es una variable
            /*if(valor.contains("(char)")){
                valor = valor.substring(6, valor.length());
            }else */
            if(!isType(valor, tipo)){
                error("error de tipos");
            }else if(isType(valor, "char")){
                valor = String.valueOf((int) valor.charAt(1));
            }
            entra = true;
            variables.put(ident, new Tupla(tipo, valor, "0"));
        }
        
        if(entra) PLXC.out.println("   " + ident + " = " + valor + ";");

    }

    public static void asigAlternativo(String ident, String valor, String cast){
        checkDeclaracion("asignacion", ident);
        String tipo_ident = getTypeDefinitivo(ident);
        //PLXC.out.println("#tipo_ident: " + tipo_ident);
        String tipo_valor = getTypeDefinitivo(valor);
        //PLXC.out.println("#tipo_valor: " + tipo_valor);
        //PLXC.out.println("valor: " + valor);
        //boolean valor_isVar = isVar(valor);
        String aux;
        //if(valor_isVar) tipo_valor = variables.get(valor).getTipo();
        //else {
            //tipo_valor = getType(valor);
        if(tipo_valor.equals("char") && !isVar(valor)){
            valor = creaChar(valor);
        }
        //}
        if(cast.isEmpty()){ //si no hay cast
            if(!tipo_ident.equals(tipo_valor)){ //si los tipos son distintos
                if(tipo_ident.equals("int") && tipo_valor.equals("float")){ //int = float
                    error("error de tipos");
                }else if(tipo_ident.equals("char") && tipo_valor.equals("int")){ //char = int
                    error("error de tipos");
                }else if(tipo_ident.equals("int") && tipo_valor.equals("char")){ //int = char
                    error("error de tipos");
                }else if(!variables.get(ident).getTam().equals("0") && !variables.get(valor).getTam().equals("0")){
                    error("error de tipos");
                }else if(tipo_ident.equals("boolean")){
                    //PLXC.out.println("ASCONACOIAN");
                    error("error de tipos");
                }else{
                    if(tipo_ident.equals("float") && tipo_valor.equals("int")){ //float = int
                        asigFloat(ident, valor);
                    }else{
                        variables.put(ident, new Tupla(tipo_valor, valor, "0"));
                        PLXC.out.println("   " + ident + " = " + valor + ";");
                    }
                }
            }else{ //si los tipos son iguales
                if(!variables.get(ident).getTam().equals("0") && !variables.get(valor).getTam().equals("0")){
                    asigDosArrays(ident, valor);
                }else if(tipo_ident.equals("string") && tipo_valor.equals("string")){
                    asigString(ident, valor);
                }else if(tipo_ident.equals("boolean")){
                    asigBoolean(ident, valor);
                }else{
                    variables.put(ident, new Tupla(tipo_valor, valor, "0"));
                    PLXC.out.println("   " + ident + " = " + valor + ";");
                }
            }
        }else{
            if(cast.equals("(int)")){
                if(!tipo_ident.equals("int") && !tipo_ident.equals("char")) error("error de tipos");
                if(tipo_valor.equals("float")){ //(int) float
                    asigInt(ident, valor);
                }else /*if(tipo_valor.equals("char"))*/{
                    PLXC.out.println("   " + ident + " = " + valor + ";");
                }
            }else if(cast.equals("(float)")){
                if(!tipo_ident.equals("float")) error("error de tipos");
                asigFloat(ident, valor);
            }else if(cast.equals("(char)")){
                if(!tipo_ident.equals("char")) error("error de tipos");
                if(tipo_valor.equals("float")){
                    aux = newVar();
                    asigInt(aux, valor);
                    PLXC.out.println("   " + ident + " = " + aux + ";");
                }else{
                    PLXC.out.println("   " + ident + " = " + valor + ";");
                }
            }
        }
    }

    public static void asigBoolean(String ident, String valor){
        //PLXC.out.println("   #asigBoolean");
        String etTrue, etFalse, etFinal;
        if(valor.matches("true")){
            etTrue = newEtiq(); etFalse = newEtiq(); etFinal = newEtiq();
            goTo(etTrue);
            pintarEtiqueta(etTrue);
            PLXC.out.println("   " + ident + " = 1;");
            goTo(etFinal);
            pintarEtiqueta(etFalse);
            PLXC.out.println("   " + ident + " = 0;");
            pintarEtiqueta(etFinal);
        }else if(valor.matches("false")){
            etTrue = newEtiq(); etFalse = newEtiq(); etFinal = newEtiq();
            goTo(etFalse);
            pintarEtiqueta(etTrue);
            PLXC.out.println("   " + ident + " = 1;");
            goTo(etFinal);
            pintarEtiqueta(etFalse);
            PLXC.out.println("   " + ident + " = 0;");
            pintarEtiqueta(etFinal);
        }else if(isVar(valor)){
            String etiquetas = pintarIf("1", valor, "igual");
            etTrue = getEtTrue(etiquetas); etFalse = getEtFalse(etiquetas); etFinal = newEtiq();
            pintarEtiqueta(etTrue);
            PLXC.out.println("   " + ident + " = 1;");
            goTo(etFinal);
            pintarEtiqueta(etFalse);
            PLXC.out.println("   " + ident + " = 0;");
            pintarEtiqueta(etFinal);
        }else{
            etTrue = getEtTrue(valor); etFalse = getEtFalse(valor); etFinal = newEtiq();
            pintarEtiqueta(etTrue);
            PLXC.out.println("   " + ident + " = 1;");
            goTo(etFinal);
            pintarEtiqueta(etFalse);
            PLXC.out.println("   " + ident + " = 0;");
            pintarEtiqueta(etFinal);
        }
    }

    public static void asigArray(String ident, String pos, String valor){
        String aux;
        checkDeclaracion("asignacion", ident);
        comprobacionDeRango(ident, pos);
        String tipo_ident = getTypeDefinitivo(ident);
        String tipo_valor = getTypeDefinitivo(valor);

        if(tipo_valor.equals("char") && !isVar(valor)) valor = creaChar(valor);

        if(!tipo_ident.equals(tipo_valor)){
            if(tipo_ident.equals("int") && tipo_valor.equals("float")){
                error("error de tipos");
            }else if(tipo_ident.equals("char") && !tipo_valor.equals("char")){
                error("error de tipos");
            }else if(tipo_ident.equals("int") && tipo_valor.equals("char")){
                error("error de tipos");
            }else if(tipo_ident.equals("float") && tipo_valor.equals("int")){
                aux = newVar();
                declarar(aux, "float");
                asigFloat(aux, valor);
                PLXC.out.println("   " + ident + "[" + pos + "] = " + aux + ";");
            }

        }else{
            PLXC.out.println("   " + ident + "[" + pos + "] = " + valor + ";");
        }
    }

    public static void asigDosArrays(String ident, String valor){
        int tam_ident = Integer.parseInt(variables.get(ident).getTam());
        int tam_valor = Integer.parseInt(variables.get(valor).getTam());
        String tipo = getTypeDefinitivo(ident);
        String aux;
        if(tam_ident < tam_valor){
            error("rangos incorrectos");
        }else{
            aux = newVar();
            declarar(aux, tipo);
            for(int i=0; i<tam_valor; i++){
                PLXC.out.println("   " + aux + " = " + valor + "[" + i + "];");
                PLXC.out.println("   " + ident + "[" + i + "] = " + aux + ";");
            }
        }
    }

    public static void inicializarArray(String ident, ArrayList<String> valores){
        checkDeclaracion("asignacion", ident);
        int num_valores = valores.size();
        int tam_array = Integer.parseInt(variables.get(ident).getTam());
        String aux;
        String tipo_array = getTypeDefinitivo(ident);
        String tipo_valor;
        String valor;
        if(tam_array < num_valores){
            error("las dimensiones de los arrays no encajan");
        }else{
            aux = newVar();
            declarar(aux, tipo_array);
            for(int i=0; i<num_valores; i++){
                valor = valores.get(i);
                tipo_valor = getTypeDefinitivo(valor);
                if(tipo_valor.equals("char") && !isVar(valor)) valor = creaChar(valor);
                if(!tipo_valor.equals(tipo_array)){
                    error("error de tipos");
                }else{
                    PLXC.out.println("   " + aux + " = " + valor + ";");
                    PLXC.out.println("   " + ident + "[" + i + "] = " + aux + ";");
                }
            }    
        }
    }

    public static void asigString(String ident, String valor){
        String tipo_ident = getTypeDefinitivo(ident);
        //PLXC.out.println("#tipo ident: " + tipo_ident);
        String tipo_valor = getTypeDefinitivo(valor);
        //PLXC.out.println("#tipo valor: " + tipo_valor);
        int tam;
        int tam_real = 0;
        String aux = newVar();
        String elemento;
        String valor_a_asignar;
        if(!tipo_ident.equals(tipo_valor)){
            error("error de tipos");
        }else{
            if(!isVar(valor)){
                valor = valor.substring(1, valor.length()-1);
                tam = valor.length();
                for(int i=0; i<tam; i++){
                    if(valor.charAt(i) == '\\'){
                        elemento = "\'" + valor.charAt(i+1) + "\'";
                        i=i+1;
                    }else{
                        elemento = "\'" + valor.charAt(i) + "\'";
                    }
                    
                    valor_a_asignar = creaChar(elemento);
                    PLXC.out.println("   " + ident + "[" + tam_real + "] = " + valor_a_asignar + ";");
                    tam_real++;
                }
                variables.get(ident).setTam(String.valueOf(tam_real));
            }else{                
                PLXC.out.println("   " + ident + " = " + valor + ";");
                variables.get(ident).setTam(variables.get(valor).getTam());
            }
        }
        
    }

    public static void comprobacionDeRango(String ident, String pos){
        TuplaEtiquetas tupla = new TuplaEtiquetas(newEtiq(), newEtiq());
        String etTrue = tupla.getEtTrue();
        String etFalse = tupla.getEtFalse();
        String tam = variables.get(ident).getTam();
        PLXC.out.println("   if(" + pos + " < 0) goto " + etTrue + ";" );
        PLXC.out.println("   if(" + tam + " < " + pos +") goto " + etTrue + ";" );
        PLXC.out.println("   if(" + tam + " == " + pos + ") goto " + etTrue + ";" );
        PLXC.out.println("   goto " + etFalse + ";");
        pintarEtiqueta(etTrue);
        PLXC.out.println("   error;");
        PLXC.out.println("   halt;");
        pintarEtiqueta(etFalse);
    }

    public static String posicionArray(String ident, String e){
        String aux = newVar();
        String tipo = getTypeDefinitivo(ident);
        comprobacionDeRango(ident, e);
        switch(tipo){
            case "int":
                declarar(aux, "int");
                PLXC.out.println("   " + aux + " = " + ident + "[" + e + "];");
                break;
            case "float":
                declarar(aux, "float");
                PLXC.out.println("   " + aux + " = " + ident + "[" + e + "];");
                break;
            case "char":
                declarar(aux, "char");
                PLXC.out.println("   " + aux + " = " + ident + "[" + e + "];");
                break;
            
            default:
                break;
        }
        return aux;
    }

    public static void asigFloat(String ident, String valor){
        //checkDeclaracion("asignacion", ident);
        //boolean esVariable = valor.matches("[a-zA-Z][a-zA-Z0-9]*");
        //String tipo_valor, tipo = variables.get(ident).getTipo();
        variables.put(ident, new Tupla("float", valor, "0"));        
        PLXC.out.println("   " + ident + " = (float) " + valor + ";");
    }

    public static void asigInt(String ident, String valor){
        //checkDeclaracion("asignacion", ident);
        //boolean esVariable = valor.matches("[a-zA-Z][a-zA-Z0-9]*");
        //String tipo_valor, tipo = variables.get(ident).getTipo();
        if(isType(valor, "char") && !isVar(valor)){
            valor = creaChar(valor);
        }
        variables.put(ident, new Tupla("int", valor, "0"));        
        PLXC.out.println("   " + ident + " = (int) " + valor + ";");

    }


    public static void asigChar(String ident, String valor){
        //checkDeclaracion("asignacion", ident);
        //boolean esVariable = valor.matches("[a-zA-Z][a-zA-Z0-9]*");
        //String tipo_valor, tipo = variables.get(ident).getTipo();
        if(isType(valor, "char") && !isVar(valor)){
            //valor = creaChar(valor);
        }
        variables.put(ident, new Tupla("char", valor, "0"));        
        PLXC.out.println("   " + ident + " = " + valor + ";");

    }
    

    public static void checkDeclaracion(String tipoCheckeo, String ident){
        boolean encontrada = false;

        for(String key : variables.keySet()){
            if(key.equals(ident)){
                encontrada = true;
                break;
            }
        }

        switch(tipoCheckeo){
            case "declaracion":
                if(encontrada) error("variable ya declarada");
                break;
            case "asignacion":
                if(!encontrada) error("variable no declarada");
                break;

            default:
                break;
        }

    }

    public static void declarar(String ident, String tipo){
        checkDeclaracion("declaracion", ident);

        switch(tipo){
            case "int":
                variables.put(ident, new Tupla("int", "0", "0"));
                break;
            case "float":
                variables.put(ident, new Tupla("float", "0.0", "0"));
                break;
            case "char":
                variables.put(ident, new Tupla("char", "0", "0"));
                break;
            case "string":
                variables.put(ident, new Tupla("string", "", "0"));
                break;
            case "boolean":
                variables.put(ident, new Tupla("boolean", "0", "0"));
                break;

            default:
                variables.put(ident, new Tupla("", "", "0"));
                break;
        }
    }

    public static void declararArray(String ident, String tipo, String tam){
        checkDeclaracion("declaracion", ident);

        switch(tipo){
            case "int":
                variables.put(ident, new Tupla("int", "0", tam));
                PLXC.out.println("   " + ident + "_length = " + tam + ";");
                break;
            case "float":
                variables.put(ident, new Tupla("float", "0", tam));
                PLXC.out.println("   " + ident + "_length = " + tam + ";");
                break;
            case "char":
                variables.put(ident, new Tupla("char", "0", tam));
                PLXC.out.println("   " + ident + "_length = " + tam + ";");
                break;

            default:
                variables.put(ident, new Tupla("", "", ""));
                break;
        }
    }

    public static void error(String mensaje){
        PLXC.out.println("   #" + mensaje);
        PLXC.out.println("   error;");
        PLXC.out.println("   halt;");
    }

    public static void printSupremo(String e){
        if(isArray(e)){
            //PLXC.out.println("#printarray");
            printArray(e);
        }else if(isString(e)){
            //PLXC.out.println("#printstring");
            printStringSupremo(e);
        }else{
            //PLXC.out.println("#printnormal");
            print(e);
        }
    }

    public static void print(String e){
        boolean isVar = isVar(e);
        if(isType(e, "char") && isVar){
            PLXC.out.println("   printc " + e + ";");
        }else if(isType(e, "char") && !isVar){
            e = creaChar(e);
            PLXC.out.println("   printc " + e + ";");
        }else if(e.contains("(char)")){
            e = e.substring(6, e.length());
            PLXC.out.println("   printc " + e + ";");
        }else{
            PLXC.out.println("   print " + e + ";");
        }      
    }

    public static void printArray(String e){
        int tam = Integer.parseInt(variables.get(e).getTam());
        String tipo = getTypeDefinitivo(e);
        String aux = newVar();
        declarar(aux, tipo);
        for(int i=0; i<tam; i++){
            PLXC.out.println("   " + aux + " = " + e + "[" + i + "];");
            print(aux);
        }
    }

    public static void printArrayLlaves(ArrayList<String> valores){
        int tam = valores.size();
        String tipo_array = getTypeDefinitivo(valores.get(0));
        String valor, tipo_valor;
        String aux = newVar();
        declarar(aux, tipo_array);
        for(int i=0; i<tam; i++){
            valor = valores.get(i);
            tipo_valor = getTypeDefinitivo(valor);
            if(!tipo_valor.equals(tipo_array)) error("error de tipos");
            if(tipo_valor.equals("char")) valor = creaChar(valor);
            PLXC.out.println("   " + aux + " = " + valor + ";");
            print(aux);
        }
    }

    public static void printStringSupremo(String e){
        if(!isVar(e)) printString(e);
        else{
            printStringVariable(e);
        }
    }

    public static void printString(String k) {
		String aux = k.substring(1, k.length() -1 );
		int x = 0;
		do {
			int ascii = 0;
			if ((aux.charAt(x) == '\\') && (aux.charAt(x + 1) == '\\')) {
				ascii = 92;
				x++;
			} else if ((aux.charAt(x) == '\\') && (aux.charAt(x + 1) == '\"')) {
				ascii = 34;
				x++;
			} else if ((aux.charAt(x) == '\\') && (aux.charAt(x + 1) == 'b')) {
				ascii = 8;
				x++;
			} else if ((aux.charAt(x) == '\\') && (aux.charAt(x + 1) == 'n')) {
				ascii = 10;
				x++;
			} else if ((aux.charAt(x) == '\\') && (aux.charAt(x + 1) == 'r')) {
				ascii = 13;
				x++;
			} else if (aux.substring(x).contains("\\u")) {
				ascii = Integer.parseInt(aux.substring(x, aux.length()),
						16);
			} else {
				ascii = (int) aux.charAt(x);
			}
			PLXC.out.println("writec " + ascii + ";");
			x++;
		} while (x < aux.length());
		PLXC.out.println("writec 10;");
	}

    public static void printStringVariable(String e){
        //PLXC.out.println("#printStringVariable");
        int tam = Integer.parseInt(variables.get(e).getTam());
        String aux = newVar();
        declarar(aux, "string");
        for(int i=0; i<tam; i++){
            PLXC.out.println("   " + aux + " = " + e + "[" + i + "];");
            PLXC.out.println("   writec " + aux + ";"); 
        }
        PLXC.out.println("   writec 10;"); 
    }

    public static String newVar(){
        variableAuxiliar++;
        return "$t" + variableAuxiliar;
    }

    public static String newEtiq(){
        etiqueta++;
        return "L" + etiqueta;
    }

    public static TuplaEtiquetas generarTuplaEtiquetas(){
        return new TuplaEtiquetas(newEtiq(), newEtiq());
    }

    public static String operacion(String e1, String e2, String op, String cast){        
        String aux1 = newVar(), aux2, aux3;

        if(isType(e1, "char") && !isVar(e1)) e1 = creaChar(e1);
        if(isType(e2, "char") && !isVar(e2)) e2 = creaChar(e2);

        boolean esFloat1 = isType(e1, "float");
        boolean esFloat2 = isType(e2, "float");

        boolean float_cast = cast.equals("(float)");
        boolean int_cast = cast.equals("(int)");

        if(float_cast){
            declarar(aux1, "float");
            //asigFloat(aux1, e1);
            //aux2 = newVar();
            //declarar(aux2, "float");
            //asigFloat(aux2, e2);
            //aux3 = newVar();
            //declarar(aux3, "float");
            PLXC.out.println("   " + aux1 + " = " + e1 + " " + op + "r " + e2 + ";");
            return aux1;
        /*}else if(int_cast){
            declarar(aux1, "int");
            asigInt(aux1, e1);
            aux2 = newVar();
            declarar(aux2, "int");
            asigInt(aux2, e2);
            aux3 = newVar();
            declarar(aux3, "int");
            PLXC.out.println("   " + aux3 + " = " + aux1 + " " + op + " " + aux2 + ";");
            return aux3;*/
        }else{
            if(esFloat1 && !esFloat2){
                //aux1 = newVar();
                declarar(aux1, "float");
                asigFloat(aux1, e2);
                aux2 = newVar();
                PLXC.out.println("   " + aux2 + " = " + e1 + " " + op + "r " + aux1 + ";");
                return aux2;
            }else if(!esFloat1 && esFloat2){
                //aux1 = newVar();
                declarar(aux1, "float");
                asigFloat(aux1, e1);
                aux2 = newVar();
                PLXC.out.println("   " + aux2 + " = " + aux1 + " " + op + "r " + e2 + ";");
                return aux2;
            }else if(esFloat1 && esFloat2){
                //aux1 = newVar();
                declarar(aux1, "float");
                PLXC.out.println("   " + aux1 + " = " + e1 + " " + op + "r " + e2 + ";");
            }else{
                //aux1 = newVar();
                PLXC.out.println("   " + aux1 + " = " + e1 + " " + op + " " + e2 + ";");
                declarar(aux1, "int");
            }
            return aux1;
        }
    }

    public static String menosUnario(String e){
        String aux = newVar();
        PLXC.out.println("   " + aux + " = -" + e + ";");
        declarar(aux, "int");
        return aux;
    }

    public static void pintarEtiqueta(String etiqueta){
        PLXC.out.println(etiqueta + ":");
    }

    public static void goTo(String etiqueta){
        PLXC.out.println("   goto " + etiqueta + ";");
    }

    public static String pintarIf(String e1, String e2, String op){

        TuplaEtiquetas tupla = generarTuplaEtiquetas();

        switch(op){
            case "igual":
                PLXC.out.print("   if (" + e1 + " == " + e2 + ")");
                goTo(tupla.getEtTrue());
                goTo(tupla.getEtFalse());
                break;
            case "distinto":
                PLXC.out.print("   if (" + e1 + " != " + e2 + ")");
                goTo(tupla.getEtTrue());
                goTo(tupla.getEtFalse());
                break;
            case "mayor":
                PLXC.out.print("   if (" + e2 + " < " + e1 + ")");
                goTo(tupla.getEtTrue());
                goTo(tupla.getEtFalse());
                break;
            case "menor":
                PLXC.out.print("   if (" + e1 + " < " + e2 + ")");
                goTo(tupla.getEtTrue());
                goTo(tupla.getEtFalse());
                break;
            case "menoroigual":
                PLXC.out.print("   if (" + e2 + " < " + e1 + ")");
                goTo(tupla.getEtFalse());
                goTo(tupla.getEtTrue());
                break;
            case "mayoroigual":
                PLXC.out.print("   if (" + e1 + " < " + e2 + ")");
                goTo(tupla.getEtFalse());
                goTo(tupla.getEtTrue());
                break;

            default:
                break;
        }
        return tupla.getEtTrue() + tupla.getEtFalse();
    }

    public static String getEtTrue(String tupla){
        return "L" + tupla.split("L")[1];
    }

    public static String getEtFalse(String tupla){
        return "L" + tupla.split("L")[2];
    }

    public static String getEtFinal(String tupla){
        return "L" + tupla.split("L")[3];
    }

    public static String generarTuplaEtiquetasString(){
        TuplaEtiquetas tupla = generarTuplaEtiquetas();
        return tupla.getEtTrue() + tupla.getEtFalse();   
    }

    public static String cambiarEtiqueta(String tupla){
        String[] partes = tupla.split("L");
        return "L" + partes[2] + "L" + partes[1];
    }

    public static boolean isType(String e, String type){
        boolean result;
        boolean isVar = isVar(e);
        if(isVar){
            result = type.equals(variables.get(e).getTipo());
        }else{
            switch(type){
                case "int":
                    result = e.matches("0|[1-9][0-9]*");
                    break;
                case "float":
                    result = e.matches("[0-9]*[.][0-9]+([eE][-+]?[0-9]+)?");
                    break;
                case "char":
                    result = e.matches("'(\\\\([\\\"'\\\\bfnrt]|u[0-9A-Fa-f]{4})|[^\\\\'])'");
                    break;
                default:
                    result = true;
            }
        }
        return result;   
    }

    public static String getType(String e){
        String result = "";
        if(e.matches("0|[1-9][0-9]*")){
            result = "int";
        }else if (e.matches("[0-9]*[.][0-9]+([eE][-+]?[0-9]+)?")){
            result = "float";
        }else if (e.matches("'(\\\\([\\\"'\\\\bfnrt]|u[0-9A-Fa-f]{4})|[^\\\\'])'")){
            result = "char";
        }
        return result;
    }

    public static String getTypeDefinitivo(String e){
        String result = "";
        boolean isVar = isVar(e);
        if(isVar){
            result = variables.get(e).getTipo();
        }else{
            if(e.matches("0|[1-9][0-9]*")){
                result = "int";
            }else if (e.matches("[0-9]*[.][0-9]+([eE][-+]?[0-9]+)?")){
                result = "float";
            }else if (e.matches("'(\\\\([\\\"'\\\\bfnrt]|u[0-9A-Fa-f]{4})|[^\\\\'])'")){
                result = "char";
            }else if(e.matches("\".*\"")){
                result = "string";
            }else if(e.matches("(L(\\d)+)+") || e.matches("true") || e.matches("false")){
                result = "boolean";
            }
        }
        return result;
    }

    public static boolean isVar(String e){
        return variables.containsKey(e);
    }

    public static String creaChar(String v) {
		int ascii = 0;
		if (v.matches("\'[^\\']\'")) {
			ascii = (int) v.charAt(1);
		} else if (v.contains("\\u")) {
			return Integer.toString(Integer.parseInt(v.substring(3, v.length() - 1), 16));
		} else {
			switch (v.charAt(2)) {
				case 'b':
					return "8";
				case 'n':
					return "10";
				case 'r':
					return "13";
				case 't':
					return "9";
				case '\\':
					return "92";
				case '\'':
					return "39";
				case '\"':
					return "34";
			}
		}
		return Integer.toString(ascii);
	}

    public static boolean isArray(String x){
        if(isVar(x)) return Integer.parseInt(variables.get(x).getTam())>0 && !getTypeDefinitivo(x).equals("string");
        return false;
    }

    public static boolean isString(String e){
        if(isVar(e)) return getTypeDefinitivo(e).equals("string");
        return e.matches("\".*\"");

    }

}
