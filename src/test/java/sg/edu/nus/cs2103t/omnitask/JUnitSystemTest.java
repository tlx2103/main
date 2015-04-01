package sg.edu.nus.cs2103t.omnitask;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.joda.time.DateTime;
import org.junit.Test;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.model.Task.Priority;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitask.storage.IOJSONImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UI;
import sg.edu.nus.cs2103t.omnitask.ui.UIStubImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Command;

public class JUnitSystemTest {
	
	// Test Whole System And All Known Commands
	@Test
	public void TestSystem() throws IOException {
		// Initialize stub ui
		UI ui = new UIStubImpl();
		
		// Initialize other components
		Parser parser = new ParserMainImpl();
		File file = new File("SystemTestingStorage.txt");
		file.deleteOnExit();
		Data data = DataImpl.GetSingleton().init(new IOJSONImpl(file));
		
		processInput(ui, parser, data, "add Hello World", CommandType.ADD, 0, "Hello World", null, null, Priority.NONE);
		processInput(ui, parser, data, "add Hello World Tmr due tomorrow", CommandType.ADD, 0, "Hello World Tmr", null, new DateTime().withMillisOfDay(0).plusDays(1), Priority.NONE);
		processInput(ui, parser, data, "delete 1", CommandType.DELETE, 1, null, null, null, Priority.NONE);
		processInput(ui, parser, data, "edit 1 Hello World Edited", CommandType.EDIT, 1, "Hello World Edited", null, null, Priority.NONE);
	
		// TODO: Check for results in data class
	}
	
	private void processInput(UI ui, Parser parser, Data data, String input, CommandType expectedCommandType, long expectedId, String expectedTaskName, DateTime expectedStartDate, DateTime expectedEndDate, Priority expectedPriority) {
		Command command = parser.parseUserInput(input);

		assertNotNull(command);
		assertEquals(expectedCommandType, command.getCommandInput().getCommandType());
		assertEquals(expectedId, command.getCommandInput().getId());
		assertEquals(expectedTaskName, command.getCommandInput().getName());
		assertEquals(expectedStartDate, command.getCommandInput().getStartDate());
		assertEquals(expectedEndDate, command.getCommandInput().getEndDate());
		assertEquals(expectedPriority, command.getCommandInput().getPriority());
		
		boolean success = command.processCommand(data, ui);
		assertTrue(success);
	}
}
