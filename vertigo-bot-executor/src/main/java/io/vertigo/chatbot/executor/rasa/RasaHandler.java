package io.vertigo.chatbot.executor.rasa;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertigo.core.component.Activeable;
import io.vertigo.core.component.Component;
import io.vertigo.lang.VSystemException;
import io.vertigo.lang.VUserException;

public class RasaHandler implements Component, Activeable {

	private static final Logger LOGGER = LogManager.getLogger("rasa");

	private Process rasaProcess;
	private Process rasaTrainingProcess;
	
	private String pythonPath = "D:\\DT\\chatbot\\factory\\python";
	private String botPath = "D:\\DT\\chatbot\\factory\\bot";

	private StringBuilder trainingLog;

	@Override
	public void start() {
		LOGGER.info("Lancement de Rasa");
		
		rasaProcess = execRasa("run", "--enable-api");
	}

	@Override
	public void stop() {
		// stop child process
		
		if (rasaProcess.isAlive()) {
			rasaProcess.destroyForcibly();
		}
		
		if (rasaTrainingProcess.isAlive()) {
			rasaTrainingProcess.destroyForcibly();
		}
	}
	

	public void trainModel() {
		if (isTraining()) {
			throw new VUserException("Entrainement déjà en cours");
		}
		
		trainingLog = new StringBuilder();
		
		rasaTrainingProcess = execRasa("train", trainingLog::append, () -> LOGGER.info("Entrainement terminé !"));
	}
	
	public boolean isTraining() {
		return rasaTrainingProcess != null && rasaTrainingProcess.isAlive();
	}
	
	public String getTrainingLog() {
		if (trainingLog == null) {
			return "Lancez un entrainement...";
		}
		return trainingLog.toString();
	}


	private Process execRasa(String command, String... additionalArgs) {
		return execRasa(command, null, null, additionalArgs);
	}
	
	private Process execRasa(String command, Consumer<String> logConsumer, Runnable endCallback, String... additionalArgs) {
			List<String> rasaCommand = new ArrayList<>();
			
			rasaCommand.add(pythonPath + "/Scripts/rasa");
			rasaCommand.add(command);
			Collections.addAll(rasaCommand, additionalArgs);
			
	//		rasaCommand.add("--cors");
	//		rasaCommand.add("*");
			
			if (LOGGER.isDebugEnabled()) {
				rasaCommand.add("-vv"); // DEBUG
			} else if (LOGGER.isInfoEnabled()) {
				rasaCommand.add("-v"); // INFO
			} else {
	//			rasaCommand.add("--quiet"); // WARNING
			}
			
			
			ProcessBuilder builder = new ProcessBuilder(rasaCommand)
					.directory(new File(botPath))
					.redirectErrorStream(true); // WTF, tout arrive dans la sortie d'erreur... Autant tout merger du coup
	
			Process process;
			try {
				process = builder.start();
			} catch (IOException e) {
				throw new VSystemException(e, "Erreur lors du lancement du processus de Rasa");
			}
	
			logInputStream(Level.INFO, process.getInputStream(), logConsumer, endCallback);
			// logInputStream(Level.WARN, rasaProcess.getErrorStream());
			
			return process;
		}


	private static void logInputStream(Level level, InputStream is, Consumer<String> logConsumer, Runnable endCallback) {
		Thread logWatcher = new Thread(() -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					LOGGER.log(level, line);
					if (logConsumer != null) {
						logConsumer.accept(line + "\r\n");						
					}
				}
			} catch (IOException e) {
				LOGGER.error("Error reading rasa output", e);
			}
			
			if (endCallback != null) {
				endCallback.run();
			}
	
		});
	
		logWatcher.start();
	}
}
