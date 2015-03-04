package sg.edu.nus.cs2103t.omnitask.ui;

public abstract class UI {
	public abstract void showMessage(String msg);
	
	public abstract void showError(String msg);
	
	public abstract void start();
	
	public abstract void exit();
}