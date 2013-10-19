package com.boondocksaints.test.pruebaaudio;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boondocksaints.test.pruebaaudio.clases.AsyncTAskMedirDBEventos;
import com.boondocksaints.test.pruebaaudio.clases.AsyncTaskMedirDB;

public class MainActivity extends Activity implements AsyncTAskMedirDBEventos {

	private TextView textViewDB;
	private Button buttonDetener;
	private AsyncTaskMedirDB atmDB;
	private ProgressBar progressBarDB;
	
	private void obtenerInstancias()
	{
		this.textViewDB = (TextView) findViewById(R.id.textViewDB);
		this.buttonDetener = (Button) findViewById(R.id.buttonDetener);
		this.progressBarDB = (ProgressBar) findViewById(R.id.progressBarDB);
	}
	
	private void asignarListeners()
	{
		this.buttonDetener.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				atmDB.cancel(true);
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.obtenerInstancias();
		this.asignarListeners();

		this.progressBarDB.setMax(1);

		atmDB = new AsyncTaskMedirDB();
		atmDB.setEventos(this);
		atmDB.execute(1);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void AsyncTAskMedirDBProgress(Double ema) {
		this.textViewDB.setText(ema.toString());

		double nivel = ema * 100;
		int nivel_int = (int)nivel;
		if (nivel_int > this.progressBarDB.getMax())
			this.progressBarDB.setMax(nivel_int);
		this.progressBarDB.setProgress(nivel_int);
	}

}
