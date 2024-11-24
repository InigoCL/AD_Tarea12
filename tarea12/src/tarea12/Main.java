package tarea12;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		MetodosBBDD metodos = new MetodosBBDD();
		metodos.crearConexion();
		mostrarMenu(sc, metodos);
		
	}
	
	private static void mostrarMenu(Scanner sc, MetodosBBDD metodos) {
		int option;

        do {
        	System.out.println("");
            System.out.println("Elige una opción:");
            System.out.println("1. Insertar alumno");
            System.out.println("2. Insertar grupo");
            System.out.println("3. Mostrar todos los alumnos por consola");
            System.out.println("4. Guardar alumnos en un fichero");
            System.out.println("5. Leer alumnos desde fichero y guardar en BD");
            System.out.println("6. Modificar nombre de un alumno a partir de su Nia");
            System.out.println("7. Eliminar un alumno por su Nia");
            System.out.println("8. Eliminar todos los alumnos de un curso");
            System.out.println("9. Guardar grupos en fichero (XML/JSON)");
            System.out.println("10. Leer grupos desde fichero (XML/JSON) y guardar en BD");
            System.out.println("0. Salir");
            
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                	metodos.insertarAlumnoManualmente();
                    break;
                case 2:
                	metodos.insertarGrupo();
                    break;
                case 3:
                	metodos.mostrarAlumnosConsola();
                    break;
                case 4:
            		metodos.guardarAlumnosDeBBDDaFicheroBinario();
                    break;
                case 5:
                	metodos.guardarAlumnosDeFicheroBinarioaBBDD();
                    break;
                case 6:
                	metodos.modificarNombreAlumno();
                    break;
                case 7:
                	metodos.eliminarAlumnoPK();
                    break;
                case 8:
                	metodos.eliminarAlumnosCurso();
                    break;
                case 9:
                	metodos.guardarGruposXML();
                    break;
                case 10:
                	metodos.guardarGruposDeXMLaBBDD();
                    break;
                case 0:
                    System.out.println("Fin del programa ヾ(^▽^*)))  (⓿_⓿)");
                    break;
                default:
                    System.out.println("Opción no válida, intente nuevamente.");
            }
        } while (option != 0);
	}

}
