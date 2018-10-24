package jhondoe.com.domicilios.data.model.entities;

import java.io.Serializable;

public class Tienda implements Serializable{
    private String id;
    private String descripcion;
    private String imagen;

    public Tienda() {
    }

    public Tienda(String id, String descripcion, String imagen) {
        this.id = id;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    // Get & Set
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
