package Model;

public class Persona {
	
	private String nombre;
	private String apellido;
	private int edad;
	private String oficio;
	
	public Persona() {
		
	}
	
	public Persona(String nombre , String apellido, int edad , String oficio) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.oficio = oficio;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getOficio() {
		return oficio;
	}

	public void setOficio(String oficio) {
		this.oficio = oficio;
	}
	
	
	
}
