package org.iesalandalus.programacion.tutorias.mvc.modelo.negocio.memoria;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Sesion;
import org.iesalandalus.programacion.tutorias.mvc.modelo.dominio.Tutoria;
import org.iesalandalus.programacion.tutorias.mvc.modelo.negocio.ISesiones;

public class Sesiones implements ISesiones {
	
	private List<Sesion> coleccionSesiones;
	
	public Sesiones() 
	{
		coleccionSesiones = new ArrayList<>();
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
