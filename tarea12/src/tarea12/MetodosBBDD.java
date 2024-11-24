package tarea12;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MetodosBBDD {

	private Connection conexionBBDD;
	private static Scanner sc = new Scanner(System.in);

	protected void crearConexion() {
		final String URL = "jdbc:mysql://localhost:3306/Alumnos10";
		final String USUARIO = "root";
		final String PASSW = "manager";

		try {

			conexionBBDD = DriverManager.getConnection(URL, USUARIO, PASSW);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	protected void insertarGrupo() {
		System.out.println("Dime el nombre del grupo que quieres guardar");
		String nombreGrupo = sc.nextLine();
		System.out.println("Dime el ID del grupo que quieres guardar");
		String idGrupo = sc.nextLine();

		String insercionGrupos = "INSERT INTO grupos (ID, NombreGrupo) VALUES (?,?)";

		try {
			PreparedStatement ps = conexionBBDD.prepareStatement(insercionGrupos);
			ps.setString(1, idGrupo);
			ps.setString(2, nombreGrupo);

			if (ps.executeUpdate() == 1) {
				System.out.println("SE ha insertado el grupo con exito");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void insertarAlumnoManualmente() {
		Alumno a = new Alumno();
		a.pedirDatos(sc);
		insertarAlumno(a);
	}
	
	protected void insertarAlumno(Alumno a) {
		String comprobarGrupo = "SELECT COUNT(*) FROM grupos WHERE ID = ?";
		String insercionAlumno = "INSERT INTO alumno (NIA, Nombre, Apellidos, Genero, FechaNacimiento, Ciclo, Curso, Grupo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			PreparedStatement psComprobar = conexionBBDD.prepareStatement(comprobarGrupo);
			psComprobar.setString(1, a.getGrupo());
			ResultSet rs = psComprobar.executeQuery();
			rs.next();

			if (rs.getInt(1) != 0) {
				PreparedStatement psInsertar = conexionBBDD.prepareStatement(insercionAlumno);
				psInsertar.setInt(1, a.getDni());
				psInsertar.setString(2, a.getNombre());
				psInsertar.setString(3, a.getApellidos());
				psInsertar.setString(4, String.valueOf(a.getGenero()));
				psInsertar.setDate(5, new java.sql.Date(a.getFechaNacimiento().getTime()));
				psInsertar.setString(6, a.getCiclo());
				psInsertar.setString(7, a.getCurso());
				psInsertar.setString(8, a.getGrupo());

				if (psInsertar.executeUpdate() == 1) {
					System.out.println("Se ha insertado el alumno correctamente.");
				}

			} else {
				System.out.println("El grupo del alumno no existe.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	protected void mostrarAlumnosConsola() {
		try {
			Statement s = conexionBBDD.createStatement();
			String selectSQL = "SELECT Nia, Nombre, Apellidos, Genero, FechaNacimiento, Ciclo, Curso, Grupo FROM alumno";
			
			ResultSet rs = s.executeQuery(selectSQL);
			
			while(rs.next()) {
				int nia = rs.getInt("Nia");
				String nombre = rs.getString("Nombre");
				String apellidos = rs.getString("Apellidos");
				String genero = rs.getString("Genero");
				Date fecha = rs.getDate("FechaNacimiento");
				String ciclo = rs.getString("Ciclo");
				String curso = rs.getString("Curso");
				String grupo= rs.getString("Grupo");
				
				java.util.Date fechaString = new java.util.Date(fecha.getTime());
				
				System.out.println("Nia: "+nia+" Nombre: "+nombre+" Apellidos: "+apellidos+" Género: "+genero+" FechaNacimiento: "+fechaString+" Ciclo: "+ciclo+" Curso: "+curso+" Grupo: "+grupo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	protected void guardarAlumnosDeBBDDaFicheroBinario() {
		List<Alumno> listaAlumno = new ArrayList<Alumno>();
		String fichero = "alumnosBinario.dat";
		ObjectOutputStream oos = null;
		Statement s;
		try {
			s = conexionBBDD.createStatement();
			String selectSQL = "SELECT Nia, Nombre, Apellidos, Genero, FechaNacimiento, Ciclo, Curso, Grupo FROM alumno";
			
			ResultSet rs = s.executeQuery(selectSQL);
			
			while(rs.next()) {
				int nia = rs.getInt("Nia");
				String nombre = rs.getString("Nombre");
				String apellidos = rs.getString("Apellidos");
				String genero = rs.getString("Genero");
				Date fecha = rs.getDate("FechaNacimiento");
				String ciclo = rs.getString("Ciclo");
				String curso = rs.getString("Curso");
				String grupo= rs.getString("Grupo");
				
				java.util.Date fechaString = new java.util.Date(fecha.getTime());
				
				Alumno a = new Alumno(nia, nombre, apellidos, genero.charAt(0), fechaString, ciclo, curso, grupo);
				
				listaAlumno.add(a);	
			}
			oos = new ObjectOutputStream( new FileOutputStream(fichero));
			
			oos.writeObject(listaAlumno);
			
			System.out.println("El alumno se ha guardado correctamente en el fichero: "+fichero);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(oos!=null) {
				oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	protected void guardarAlumnosDeFicheroBinarioaBBDD() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream("alumnosBinario.dat"));
			
			@SuppressWarnings("unchecked")
			List<Alumno> listaAlumno = (List<Alumno>) ois.readObject();
			
			for (Alumno alumno : listaAlumno) {
				insertarAlumno(alumno);
			}
			
			System.out.println("Se han cargado correctamente los alumnos del fichero a la base de datos.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			try {
				if(ois!=null) {
				ois.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	protected void modificarNombreAlumno() {
		System.out.println("Dime el nia del alumno al que quieres modificar el nombre.");
		int niaAlumnoModificar = sc.nextInt();
		sc.nextLine();
		System.out.println("Dime el nombre que quieres poner");
		String nombreNuevo = sc.nextLine();
		
		String updateSQL = "UPDATE alumno SET Nombre = ? WHERE Nia = ?";
		
		try {
			PreparedStatement ps = conexionBBDD.prepareStatement(updateSQL);
			ps.setString(1, nombreNuevo);
			ps.setInt(2, niaAlumnoModificar);
			
			if(ps.executeUpdate()==1) {
				System.out.println("El alumno se ha modificado correctamente.");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void eliminarAlumnoPK() {
		System.out.println("Dime el nia del alumno a borrar.");
		int niaAlumnoBorrar = sc.nextInt();
		
		String deleteSQL = "DELETE FROM alumno WHERE Nia = ?";
		
		try {
			PreparedStatement ps = conexionBBDD.prepareStatement(deleteSQL);
			ps.setInt(1, niaAlumnoBorrar);
			
			int filasBorradas = ps.executeUpdate();
			
			if (filasBorradas == 1) {
				System.out.println("El exterminio ha sido un exito");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void eliminarAlumnosCurso() {
		System.out.println("Dime el curso de los alumnos a borrar");
		String cursoBorrar = sc.nextLine();
		
		String deleteSQL = "DELETE FROM alumno WHERE Curso = ?";
		
		try {
			PreparedStatement ps = conexionBBDD.prepareStatement(deleteSQL);
			
			ps.setString(1, cursoBorrar);
			
			int alumnosBorrados = ps.executeUpdate();
			
			if (alumnosBorrados != 0) {
				System.out.println("Se ha(n) borrado "+alumnosBorrados+" alumno(s).");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void guardarGruposXML() {
		DOMImplementation di = crearDBF().getDOMImplementation();
		Document doc = di.createDocument(null, "Grupos", null);
		doc.setXmlVersion("1.0");
		
		try {
			Statement sGrupo = conexionBBDD.createStatement();
			String selectGrupo = "SELECT ID, NombreGrupo FROM grupos";
			ResultSet rsGrupo = sGrupo.executeQuery(selectGrupo);
			
			while(rsGrupo.next()) {
				Element grupo = doc.createElement("Grupo");
				
				grupo.setAttribute("ID", String.valueOf(rsGrupo.getInt("ID")));
				grupo.setAttribute("Nombre", rsGrupo.getString("NombreGrupo"));
				
//				Element idGrupo = doc.createElement("ID");
//				idGrupo.setTextContent(String.valueOf(rsGrupo.getInt("ID")));
//				grupo.appendChild(idGrupo);
//				
//				Element nombreGrupo = doc.createElement("Nombre");
//				nombreGrupo.setTextContent(rsGrupo.getString("NombreGrupo"));
//				grupo.appendChild(nombreGrupo);
				
				doc.getDocumentElement().appendChild(grupo);
				
				Statement sAlumnos = conexionBBDD.createStatement();
				String selectAlumno = "SELECT NIA, Nombre, Apellidos, Genero, FechaNacimiento, Ciclo, Curso, Grupo FROM alumno WHERE Grupo ="+rsGrupo.getInt("ID");
				ResultSet rsAlumno = sAlumnos.executeQuery(selectAlumno);
				
				while(rsAlumno.next()) {
					Element alumno = doc.createElement("Alumno");
					alumno.setAttribute("Nia", String.valueOf(rsAlumno.getInt("Nia")));
					alumno.setAttribute("Nombre", rsAlumno.getString("Nombre"));
					alumno.setAttribute("Apellido", rsAlumno.getString("Apellidos"));
					alumno.setAttribute("Genero", rsAlumno.getString("Genero"));
					alumno.setAttribute("FechaNacimiento", String.valueOf(rsAlumno.getDate("FechaNacimiento")));
					alumno.setAttribute("Ciclo", rsAlumno.getString("Ciclo"));
					alumno.setAttribute("Curso", rsAlumno.getString("Curso"));
					alumno.setAttribute("Grupo", rsAlumno.getString("Grupo"));
					grupo.appendChild(alumno);
				}
				
			}
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformador = tf.newTransformer();
			DOMSource fuente = new DOMSource(doc);
			StreamResult resultado = new StreamResult(new File("Grupos.xml"));
			transformador.transform(fuente, resultado);

			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void guardarGruposDeXMLaBBDD() {
		try {
			Document doc = crearDBF().parse("Grupos.xml");
			doc.getDocumentElement().normalize();
			
			NodeList grupos = doc.getElementsByTagName("Grupo");
			
			for(int i = 0; i<grupos.getLength();i++) {
				Element grupo = (Element) grupos.item(i);
				int idGrupo = Integer.valueOf(grupo.getAttribute("ID"));
				String nombreGrupo = grupo.getAttribute("Nombre");
				
				String insertSQL = "INSERT INTO grupos(ID, NombreGrupo) VALUES (?,?)";
				
				PreparedStatement ps = conexionBBDD.prepareStatement(insertSQL);
				
				ps.setInt(1, idGrupo);
				ps.setString(2, nombreGrupo);
				
				if(ps.executeUpdate()==1) {
					System.out.println("Se ha insertado con exito el grupo");
				}
			}
			
			
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	
	protected DocumentBuilder crearDBF() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;

	}
	
}
