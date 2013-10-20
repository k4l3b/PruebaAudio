package com.boondocksaints.test.pruebaaudio.clases;

public interface AsyncTaskSonometroEventos {

	void onAsyncTaskSonometroEventosProgess(Double ema);

	void onAsyncTaskSonometroEventosPreExecute();

	void onAsyncTaskSonometroEventosPostExecute();

	void onAsyncTaskSonometroEventosCancelled();

}
