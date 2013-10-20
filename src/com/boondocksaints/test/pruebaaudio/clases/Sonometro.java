package com.boondocksaints.test.pruebaaudio.clases;

import java.io.IOException;

import android.media.MediaRecorder;

/**
 * @author Kaleb
 *
 * Esta clase funciona como un medidor de sonido captado por el micrófono
 * del dispositivo móvil.
 */
public class Sonometro {

// Constantes *****************************************************************
	static final private double FILTRO_EMA = 0.6;
	static final private int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
	static final private int DEFAULT_OUTPUT_FORMAT = MediaRecorder.OutputFormat.THREE_GPP;
	static final private int DEFAULT_AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB;
	static final private String DEFAULT_OUTPUT_FILE = "/dev/null"; 

// Atributos ******************************************************************
	private MediaRecorder mediaRec = null;
    private double ema = 0.0;
    
	private int audioSource;
    private int outputFormat;
    private int audioEncoder;
    private String outputFile;

// Gets y Sets ****************************************************************
    public int getAudioSource() {
		return audioSource;
	}
	public void setAudioSource(int audioSource) {
		this.audioSource = audioSource;
	}

	public int getOutputFormat() {
		return outputFormat;
	}
	public void setOutputFormat(int outputFormat) {
		this.outputFormat = outputFormat;
	}

	public int getAudioEncoder() {
		return audioEncoder;
	}
	public void setAudioEncoder(int audioEncoder) {
		this.audioEncoder = audioEncoder;
	}

	public String getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

// Metodos ********************************************************************
    public void iniciar() throws IllegalStateException, IOException {
        if (this.mediaRec == null) {
        	// Inicializo el MediaRecorder.
        	// Como la clase no puede reutilizarse, la tengo que volver a crear
        	// cada vez que hago un iniciar() en base a los parametros.
        	this.mediaRec = new MediaRecorder();

        	// Configuro el MediaRecorder
        	this.mediaRec.setAudioSource(this.audioSource);
            this.mediaRec.setOutputFormat(this.outputFormat);
            this.mediaRec.setAudioEncoder(this.audioEncoder);
            this.mediaRec.setOutputFile(this.outputFile); 
            this.mediaRec.prepare();
            this.mediaRec.start();

            // Inicializo la amplitud a devolver
            this.ema = 0.0;
        }
    }
    
    public void detener() {
        if (this.mediaRec != null) {
        	// Detengo, libero y limpio el MediaRecorder
        	this.mediaRec.stop();       
            this.mediaRec.release();
            this.mediaRec = null;
        }
    }
    
    public double obtenerAmplitud() {
        if (this.mediaRec != null)
        {
        	// Devuelvo la amplitud
            return  (this.mediaRec.getMaxAmplitude()/2700.0);
        }
        else
            return 0;
    }

    public double obtenerAmplitudEMA() {
    	// Devuelvo la amplitud en EMA
		double amp = this.obtenerAmplitud();
        this.ema = FILTRO_EMA * amp + (1.0 - FILTRO_EMA) * this.ema;
        return this.ema;
    }
	
// Constructores **************************************************************
    public Sonometro() {
		super();
    	this.audioSource = DEFAULT_AUDIO_SOURCE;
    	this.audioEncoder = DEFAULT_AUDIO_ENCODER;
    	this.outputFormat = DEFAULT_OUTPUT_FORMAT;
    	this.outputFile = DEFAULT_OUTPUT_FILE;

    	// Inicializo la amplitud a devolver
        this.ema = 0.0;
	}

    public Sonometro(int outputFormat, int audioEncoder,
			String outputFile) {
		super();
		this.outputFormat = outputFormat;
		this.audioEncoder = audioEncoder;
		this.outputFile = outputFile;

		// Inicializo la amplitud a devolver
        this.ema = 0.0;
	}

}
