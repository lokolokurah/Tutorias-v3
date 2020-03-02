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

import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Sesion;
import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Tutoria;
import org.iesalandalus.programacion.tutorias.mvc.modelo.negocio.ISesiones;

public class Sesiones implements ISesiones {
	
	private static final String NOMBRE_FICHERO_SESIONES = "datos/sesiones.dat";
	
	private List<Sesion> coleccionSesiones;
	
	public Sesiones() 
	{
		coleccionSesiones = new ArrayList<>();
	}
	
	@Override
	public void comenzar() 
	{
		//Leer
		File ficheroSesiones = new File(NOMBRE_FICHERO_SESIONES);
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ficheroSesiones))) {
			Sesion sesion = null;
			do 
			{
				sesion = (Sesion) entrada.readObject();
				insertar(sesion);
			} while (sesion != null);
		} catch (ClassNotFoundException e) {
			System.out.println("No puedo encontrar la clase que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo abrir el fichero de sesiones.");
		} catch (EOFException e) {
			System.out.println("Fichero sesiones leído satisfactoriamente.");
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
		File ficheroSesiones = new File(NOMBRE_FICHERO_SESIONES);
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ficheroSesiones))){
			for (Sesion sesion : coleccionSesiones)
				salida.writeObject(sesion);
			System.out.println("Fichero sesiones escrito satisfactoriamente.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo crear el fichero de sesiones.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida.");
		}
	}
	
	@Override
	public List<Sesion> get() 
	{
		List<Sesion> sesionesOrdenadas = copiaProfundaSesiones();
		Comparator<Profesor> comparadorP = Comparator.comparing(Profesor::getDni);
		Comparator<Tutoria> comparadorT = Comparator.comparing(Tutoria::getProfesor, comparadorP).thenComparing(Tutoria::getNombre);
		sesionesOrdenadas.sort(Comparator.comparing(Sesion::getTutoria, comparadorT).thenComparing(Sesion::getFecha));
		return sesionesOrdenadas;
	}
	
	private List<Sesion> copiaProfundaSesiones() 
	{
		List<Sesion> copiaSesiones = new ArrayList<>();
		for (Sesion sesion : coleccionSesiones) 
		{
			copiaSesiones.add(new Sesion(sesion));
		}
		return copiaSesiones;
	}
	
	@Override
	public List<Sesion> get(Tutoria tutoria)
	{
		if (tutoria == null) 
		{
			throw new NullPointerException("ERROR: La tutoría no puede ser nula.");
		}			
		List<Sesion> copiaSesionesTutoria = new ArrayList<>();
		for (Sesion sesion : coleccionSesiones) 
		{
			if (sesion.getTutoria().equals(tutoria)) 
			{
				copiaSesionesTutoria.add(new Sesion(sesion));			
			}
		}
		copiaSesionesTutoria.sort(Comparator.comparing(Sesion::getFecha));
		return copiaSesionesTutoria;
	}

	@Override
	public int getTamano() 
	{
		return coleccionSesiones.size();
	}
	
	@Override
	public void insertar(Sesion sesion) throws OperationNotSupportedException
	{
		if (sesion == null) 
		{
			throw new NullPointerException("ERROR: No se puede insertar una sesión nula.");
		}
		int indice = coleccionSesiones.indexOf(sesion);
		if (indice == -1) 
		{
			coleccionSesiones.add(new Sesion(sesion));
		} else {
			throw new OperationNotSupportedException("ERROR: Ya existe una sesión con esa fecha.");
		}		
		
	}

	@Override
	public Sesion buscar(Sesion sesion) 
	{
		if (sesion == null) 
		{
			throw new IllegalArgumentException("ERROR: No se puede buscar una sesión nula.");
		}
		int indice = coleccionSesiones.indexOf(sesion);
		if (indice == -1) 
		{
			return null;
		} else {
			return new Sesion(coleccionSesiones.get(indice));
		}
	}
	
	@Override
	public void borrar(Sesion sesion) throws OperationNotSupportedException
	{
		if (sesion == null)
		{
			throw new IllegalArgumentException("ERROR: No se puede borrar una sesión nula.");
		}
		int indice = coleccionSesiones.indexOf(sesion);
		if (indice == -1) 
		{
			throw new OperationNotSupportedException("ERROR: No existe ninguna sesión con esa fecha.");
		} else {
			coleccionSesiones.remove(indice);
		}
	}
}
