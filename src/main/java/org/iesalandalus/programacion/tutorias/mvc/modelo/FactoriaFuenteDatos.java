package org.iesalandalus.programacion.tutorias.mvc.modelo;

import org.iesalandalus.programacion.tutorias.mvc.modelo.negocio.IFuenteDatos;
import org.iesalandalus.programacion.tutorias.mvc.modelo.negocio.ficheros.FactoriaFuenteDatosFicheros;

public enum FactoriaFuenteDatos {

	FICHEROS {
		public IFuenteDatos crear() 
		{
			return new FactoriaFuenteDatosFicheros();
		}
	};

	public abstract IFuenteDatos crear();
	
}
