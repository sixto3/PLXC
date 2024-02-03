

public class Tupla {
    private String tipo;
    private String valor;
    private String tam;

    public Tupla(String tipo, String valor, String tam) {
        this.tipo = tipo;
        this.valor = valor;
        this.tam = tam;
    }

    public Tupla(String tipo){
        this.tipo = tipo;
        this.valor = "";
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTam() {
        return tam;
    }

    public void setTam(String tam) {
        this.tam = tam;
    }
    
    
}
