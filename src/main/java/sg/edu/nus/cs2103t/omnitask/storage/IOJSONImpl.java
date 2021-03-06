package sg.edu.nus.cs2103t.omnitask.storage;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.DateTimeConverter;
import sg.edu.nus.cs2103t.omnitask.model.Task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class IOJSONImpl extends IO {
	public File storageFile;
	
	public Gson gson;
	
	public IOJSONImpl(File storageFile) throws IOException {
		this.storageFile = storageFile;
		this.gson = new GsonBuilder()
					.registerTypeAdapter(new TypeToken<DateTime>(){}.getType(), new DateTimeConverter())
					.create();
		
		IO.CheckIfFileExistAndCreateIfDoesNot(storageFile);
	}
	
	@Override
	public ArrayList<Task> readFromFile() throws IOException {
		String lines = "";
		
		InputStream in = Files.newInputStream(storageFile.toPath());
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	        lines += line + "\n";
	    }
	    in.close();
	    
	    // return empty arraylist if file has zero items
	    ArrayList<Task> tasks = new ArrayList<Task>();
	    
		// convert json to ArrayList
	    try {
	    	ArrayList<Task> tasksFromFile = gson.fromJson(lines, new TypeToken<ArrayList<Task>>(){}.getType());
	    	if (tasksFromFile != null) {
	    		tasks = tasksFromFile;
	    	}
	    } catch (Exception e) {
	    	// TODO: This is unacceptable, warn user first!
	    	// File is most likely corrupted, start over
	    	saveToFile(new ArrayList<Task>());
	    }
	    
	    return tasks;
	}
	
	@Override
	public void saveToFile(ArrayList<Task> tasks) throws IOException {
		String json = gson.toJson(tasks);
		
		OutputStream out = new BufferedOutputStream(Files.newOutputStream(storageFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
		out.write((json).getBytes());
		out.flush();
		out.close();
	}

	@Override
	public void undoFile() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redoFile() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String readFromHelpFile(String helpType) throws IOException{
		String commandDescription = "";
		File helpFileData = new File("omnitext help file.txt");
		InputStream in = Files.newInputStream(helpFileData.toPath());
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	        if(line.equals("<"+helpType+">")){
	        	while((line = reader.readLine())!=null && !line.equals("</"+helpType+">")){
	        		commandDescription += line;
	        	}
	        	break;
	        }
	    }
	    in.close();
		return commandDescription;
	}
}