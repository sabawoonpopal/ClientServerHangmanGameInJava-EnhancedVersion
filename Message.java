import java.io.*;
 
/**
 * Write a description of class Message here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Message implements Serializable
{
    // instance variables - replace the example below with your own
    private String name;
    private char messageContent;
    private char[] splittedWord;
    private String stringMessageContent;
    
    /**
     * Constructor for objects of class Message
     */
    public Message(String inputtedName, char inputtedResponse)
    {
        // initialise instance variables
        name = inputtedName;
        messageContent = inputtedResponse;
    }
    
    public Message(String inputtedName, String inputtedResponse)
    {
        // initialise instance variables
        name = inputtedName;
        stringMessageContent = inputtedResponse;
    }
    
    public Message(String inputtedName, String inputtedResponse, char[] theSplittedWord)
    {
        name = inputtedName;
        stringMessageContent = inputtedResponse;
        splittedWord = theSplittedWord;
    }
    
    public Message(String inputtedName, char inputtedResponse, char[] theSplittedWord)
    {
        name = inputtedName;
        messageContent = inputtedResponse;
        splittedWord = theSplittedWord;
    }
    
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
    
    public char getCharContent()
    {
        return messageContent;
    }
    
    public String getMessageContent()
    {
        return stringMessageContent;
    }
    
    public void setMessage(char inputtedMessage)
    {
        messageContent = inputtedMessage;
    }
    
    public char[] getSplittedWord()
    {
        return splittedWord;
    }
    
    public void setSplittedWord(char[] newSplittedWord)
    {
        splittedWord = newSplittedWord;
    }
}
