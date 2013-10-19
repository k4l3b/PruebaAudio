package com.boondocksaints.test.pruebaaudio.clases;

import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskMedirDB extends AsyncTask<Integer, Double, Integer> {

	private AsyncTAskMedirDBEventos eventos = null;
	
	
	public AsyncTAskMedirDBEventos getEventos() {
		return eventos;
	}

	public void setEventos(AsyncTAskMedirDBEventos eventos) {
		this.eventos = eventos;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		MedidorSonidoMicrofono mic = new MedidorSonidoMicrofono();
		
		try {
			mic.iniciar();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Double x = 0.0;
		while (! this.isCancelled())
		{
			x = mic.obtenerAmplitudEMA();
			Log.v("AsyncTaskMedirDB", x.toString());
			this.publishProgress(x);
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mic.detener();
		
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Double... values) {
		if (this.eventos != null)
			this.eventos.AsyncTAskMedirDBProgress(values[0]);
	}

}
