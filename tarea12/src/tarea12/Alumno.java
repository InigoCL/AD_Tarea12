package tarea12;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Alumno implements Serializable{

	private static final long serialVersionUID = 1L;
	private int dni;
	private String nombre;
	private String apellidos;
	private char genero;
	private Date fechaNacimiento;
	private String ciclo;
	private String curso;
	private String grupo;


	public Alumno() {
	}
	
	

	public Alumno(int dni, String nombre, String apellidos, char genero, Date fechaNacimiento, String ciclo,
			String curso, String grupo) {
		super();
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.genero = genero;
		this.fechaNacimiento = fechaNacimiento;
		this.ciclo = ciclo;
		this.curso = curso;
		this.grupo = grupo;
	}



	public int getDni() {
		return dni;
	}

	public void setDni(int dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCiclo() {
		return ciclo;
	}

	public void setCiclo(String ciclo) {
		this.ciclo = ciclo;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public char getGenero() {
		return genero;
	}

	public void setGenero(char genero) {
		this.genero = genero;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public void pedirDatos(Scanner sc) {
		boolean fechaValida = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			System.out.println("Dime el NIA del alumno");
			setDni(sc.nextInt());
		} catch (InputMismatchException ime) {
			System.out.println("Error: Debes ingresar un número entero para el NIA.");
			sc.nextLine();
		}
		sc.nextLine();

		System.out.println("Dime el nombre del alumno");
		setNombre(sc.nextLine());

		System.out.println("Dime los apellidos del alumno");
		setApellidos(sc.nextLine());

		System.out.println("Dime el genero del alumno (H/M)");
		do {
			setGenero(sc.next().charAt(0));
			if (getGenero() != 'H' && getGenero() != 'M') {
				System.out.println("Error: Debes ingresar 'H' para Hombre o 'M' para Mujer.");
			}
		} while (getGenero() != 'H' && getGenero() != 'M');
		sc.nextLine();

		do {
			try {
				System.out.println("Dime la fecha de nacimiento del alumno (formato: yyyy-MM-dd):");
				String fecha_text = sc.nextLine();
				Date fecha = sdf.parse(fecha_text);
				setFechaNacimiento(fecha);
				fechaValida = true;
			} catch (ParseException e) {
				System.out.println("Error: La fecha ingresada no es válida. Inténtalo de nuevo.");
			}
		} while (!fechaValida);

		System.out.println("Dime el ciclo del alumno");
		setCiclo(sc.nextLine());

		System.out.println("Dime el curso del alumno");
		setCurso(sc.nextLine());

		System.out.println("Dime el grupo del alumno");
		setGrupo(sc.nextLine());
	}

	@Override
	public String toString() {
		return "Alumno [dni=" + dni + ", nombre=" + nombre + ", apellidos=" + apellidos + ", genero=" + genero
				+ ", fechaNacimiento=" + fechaNacimiento + ", ciclo=" + ciclo + ", curso=" + curso + ", grupo=" + grupo
				+ "]";
	}

	
	
	

}