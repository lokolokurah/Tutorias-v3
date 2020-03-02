package org.iesalandalus.programacion.tutorias.mvc.modelo.negocio.ficheros;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Alumno;
import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Cita;
import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Sesion;
import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Cita;
import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Tutoria;
import org.iesalandalus.programacion.tutorias.mvc.modelo.negocio.ICitas;

public class Citas implements ICitas {
	
	private static final String NOMBRE_FICHERO_CITAS = "datos/citas.dat";
	
	private List<Cita> coleccionCitas;
	
	public Citas() 
	{
		coleccionCitas = new ArrayList<>();
	}
	
	@Override
	public void comenzar() 
	{
		//Leer
		File ficheroCitas = new File(NOMBRE_FICHERO_CITAS);
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ficheroCitas))) {
			Cita cita = null;
			do 
			{
				cita = (Cita) entrada.readObject();
				insertar(cita);
			} while (cita != null);
		} catch (ClassNotFoundException e) {
			System.out.println("No puedo encontrar la clase que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo abrir el fichero de citas.");
		} catch (EOFException e) {
			System.out.println("Fichero citas leído satisfactoriamente.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida.");
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void terminar() 
	{
		//Escribir
		File ficheroCitas = new File(NOMBRE_FICHERO_CITAS);
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ficheroCitas))){
			for (Cita cita : coleccionCitas)
				salida.writeObject(cita);
			System.out.println("Fichero citas escrito satisfactoriamente.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo crear el fichero de citas.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida.");
		}
	}
	
	@Override
	public List<Cita> get() 
	{
		List<Cita> citasOrdenadas = copiaProfundaCitas();
		Comparator<Profesor> comparadorP = Comparator.comparing(Profesor::getDni);
		Comparator<Tutoria> comparadorT = Comparator.comparing(Tutoria::getProfesor, comparadorP).thenComparing(Tutoria::getNombre);
		Comparator<Sesion> comparadorS = Comparator.comparing(Sesion::getTutoria, comparadorT).thenComparing(Sesion::getFecha);
		citasOrdenadas.sort(Comparator.comparing(Cita::getSesion, comparadorS).thenComparing(Cita::getHora));
		return citasOrdenadas;
	}
	
	private List<Cita> copiaProfundaCitas() 
	{
		List<Cita> copiaCitas = new ArrayList<>();
		for (Cita cita : coleccionCitas) 
		{
			copiaCitas.add(new Cita(cita));
		}
		return copiaCitas;
	}
	
	@Override
	public List<Cita> get(Sesion sesion)
	{
		if (sesion == null) 
		{
			throw new NullPointerException("ERROR: La sesión no puede ser nula.");
		}			
		List<Cita> copiaCitasSesion = new ArrayList<>();
		for (Cita cita : coleccionCitas) 
		{
			if (cita.getSesion().equals(sesion)) 
			{
				copiaCitasSesion.add(new Cita(cita));			
			}
		}
		copiaCitasSesion.sort(Comparator.comparing(Cita::getHora));
		return copiaCitasSesion;
	}

	@Override
	public List<Cita> get(Alumno alumno)
	{
		if (alumno == null) 
		{
			throw new NullPointerException("ERROR: El alumno no puede ser nulo.");
		}			
		List<Cita> copiaCitasAlumno = new ArrayList<>();
		for (Cita cita : coleccionCitas) 
		{
			if (cita.getAlumno().equals(alumno)) 
			{
				copiaCitasAlumno.add(new Cita(cita));			
			}
		}
		Comparator<Profesor> comparadorP = Comparator.comparing(Profesor::getDni);
		Comparator<Tutoria> comparadorT = Comparator.comparing(Tutoria::getProfesor, comparadorP).thenComparing(Tutoria::getNombre);
		Comparator<Sesion> comparadorS = Comparator.comparing(Sesion::getTutoria, comparadorT).thenComparing(Sesion::getFecha);
		copiaCitasAlumno.sort(Comparator.comparing(Cita::getSesion, comparadorS).thenComparing(Cita::getHora));
		return copiaCitasAlumno;
	}
	
	@Override
	public int getTamano() 
	{
		return coleccionCitas.size();
	}
	
	@Override
	public void insertar(Cita cita) throws OperationNotSupportedException
	{
		if (cita == null) 
		{
			throw new NullPointerException("ERROR: No se puede insertar una cita nula.");
		}
		int indice = coleccionCitas.indexOf(cita);
		if (indice == -1) 
		{
			coleccionCitas.add(new Cita(cita));
		} else {
			throw new OperationNotSupportedException("ERROR: Ya existe una cita con esa hora.");
		}		
		
	}

	@Override
	public Cita buscar(Cita cita) 
	{
		if (cita == null) 
		{
			throw new IllegalArgumentException("ERROR: No se puede buscar una cita nula.");
		}
		int indice = coleccionCitas.indexOf(cita);
		if (indice == -1) 
		{
			return null;
		} else {
			return new Cita(coleccionCitas.get(indice));
		}
	}
	
	@Override
	public void borrar(Cita cita) throws OperationNotSupportedException
	{
		if (cita == null)
		{
			throw new IllegalArgumentException("ERROR: No se puede borrar una cita nula.");
		}
		int indice = coleccionCitas.indexOf(cita);
		if (indice == -1) 
		{
			throw new OperationNotSupportedException("ERROR: No existe ninguna cita con esa hora.");
		} else {
			coleccionCitas.remove(indice);
		}
	}
}
