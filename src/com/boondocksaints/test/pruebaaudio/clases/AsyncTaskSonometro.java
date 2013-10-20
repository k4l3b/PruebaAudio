package com.boondocksaints.test.pruebaaudio.clases;

import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Kaleb
 *
 * Esta clase es un wrapper para poder utitizar el sonometro en un hilo
 * de ejecución separado del main thread; provee métodos y eventos los cuales
 * ayudan a la notificación de la información obtenida con el hilo principal.
 */
public class AsyncTaskSonometro extends AsyncTask<Void, Double, Void> {

// Constantes *****************************************************************
	static final public Integer MUESTREO_LENTO = 1000; // un segundo
	static final public Integer MUESTREO_RAPIDO = 125; // 125 milisegundos, efectivo frente a las fluctuaciones
	static final public Integer MUESTREO_POR_IMPULSO = 35; // 35 milisegundos, respuesta del oido humano
	
// Atributos ******************************************************************
	private AsyncTaskSonometroEventos eventos = null;
	private Boolean debugMode;
	private Integer tiempoMuestreo;
	
// Gets y Sets ****************************************************************
	public AsyncTaskSonometroEventos getEventos() {
		return eventos;
	}

	public void setEventos(AsyncTaskSonometroEventos eventos) {
		this.eventos = eventos;
	}

	public Boolean getDebugMode() {
		return debugMode;
	}

	public void setDebugMode(Boolean debugMode) {
		this.debugMode = debugMode;
	}

	public Integer getTiempoMuestreo() {
		return tiempoMuestreo;
	}

	public void setTiempoMuestreo(Integer tiempoMuestreo) {
		this.tiempoMuestreo = tiempoMuestreo;
	}

// Metodos del AsyncTask ******************************************************
	@Override
	protected Void doInBackground(Void... params) {
		// Protejo el hilo de posibles cambios en los atributos asignando los valores 
		// a variables locales del metodo.
		Integer tm = this.tiempoMuestreo;
		Boolean debug = this.debugMode;

		// Creo una instancia del sonometro
		Sonometro sonometro = new Sonometro();
		
		// Inicializo el sonometro, teniendo en cuenta los posibles errores
		try {
			sonometro.iniciar();
			if (debug) Log.i(this.getClass().getName(), String.format("Sonometro iniciado, tiempo muestreo %d milisegundos", tm)); 
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Double muestra = 0.0;
		// mientras no se cancele la tarea, la sigo ejecutando
		while (! this.isCancelled())
		{
			// hago una espera segun el parametro de tiempo de muestreo
			try {
				Thread.sleep(tm);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// tomo la muestra y publico el resultado
			muestra = sonometro.obtenerAmplitudEMA();
			if (debug) Log.i(this.getClass().getName(), String.format("Muestra: %f", muestra)); 
			
			this.publishProgress(muestra);
		}
		
		// al termiar detengo el sonometro
		sonometro.detener();
		if (debug) Log.i(this.getClass().getName(), "Sonómetro detenido."); 
		
		return null;
	}

// Eventos ********************************************************************	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.eventos != null)
			this.eventos.onAsyncTaskSonometroEventosPreExecute();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (this.eventos != null)
			this.eventos.onAsyncTaskSonometroEventosPostExecute();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (this.eventos != null)
			this.eventos.onAsyncTaskSonometroEventosCancelled();
	}
	
	@Override
	protected void onProgressUpdate(Double... values) {
		super.onProgressUpdate(values);
		if (this.eventos != null)
			this.eventos.onAsyncTaskSonometroEventosProgess(values[0]);
	}

// Constructores **************************************************************
	public AsyncTaskSonometro() {
		super();
		this.tiempoMuestreo = AsyncTaskSonometro.MUESTREO_RAPIDO;
		this.eventos = null;
		this.debugMode = false;
	}

	public AsyncTaskSonometro(Integer tiempoMuestreo, AsyncTaskSonometroEventos eventos, Boolean debugMode) {
		super();
		this.tiempoMuestreo = tiempoMuestreo;
		this.eventos = eventos;
		this.debugMode = debugMode;
	}

}
