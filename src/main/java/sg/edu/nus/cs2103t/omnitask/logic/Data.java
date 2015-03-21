package sg.edu.nus.cs2103t.omnitask.logic;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public abstract class Data {
	public abstract ArrayList<Task> getTasks();

	public abstract Task addTask(CommandInput commandInput);

	public abstract boolean deleteTask(CommandInput commandInput);

	public abstract boolean editTask(CommandInput commandInput);
	
	public abstract ArrayList<Task> searchTask(CommandInput commandInput);
}
