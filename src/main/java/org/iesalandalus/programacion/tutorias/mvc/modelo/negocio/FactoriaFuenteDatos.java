package org.iesalandalus.programacion.tutorias.mvc.modelo.negocio;

import org.iesalandalus.programacion.tutorias.mvc.modelo.negocio.memoria.FactoriaFuenteDatosMemoria;

public enum FactoriaFuenteDatos {

	MEMORIA {
		public IFuenteDatos crear() 
		{
			return new FactoriaFuenteDatosMemoria();
		}
	};

	public abstract IFuenteDatos crear();
	
}
