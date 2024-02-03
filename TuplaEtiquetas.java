

public class TuplaEtiquetas {
    private String etTrue;
    private String etFalse;
    //private String etFinal;

    public TuplaEtiquetas(String etTrue, String etFalse/*, String etFinal*/) {
        this.etTrue = etTrue;
        this.etFalse = etFalse;
        //this.etFinal = etFinal;
    }

    public TuplaEtiquetas(String etTrue){
        this.etTrue = etTrue;
        this.etFalse = "";
        //this.etFinal = "";
    }

    public String getEtFalse() {
        return etFalse;
    }

    public String getEtTrue() {
        return etTrue;
    }

    public void setEtTrue(String etTrue) {
        this.etTrue = etTrue;
    }

    /*public String getEtFinal() {
        return etFinal;
    }*/

    /*public void setEtFinal(String etFinal) {
        this.etFinal = etFinal;
    }*/

    public void setEtFalse(String etFalse) {
        this.etFalse = etFalse;
    }

    
    
}

