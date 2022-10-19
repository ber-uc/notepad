import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;





public class FileOperation {

    Notepad notepad;

    boolean saved;
    boolean newFileFlag;
    String fileName;
    String applicationTitle = "Notepad - bruk";

    File file;
    JFileChooser fileChooser;

    


    /////////////////////////////////////////
    //Constructor XDD

    FileOperation(Notepad notepad){
        this.notepad = notepad;

        saved = true;
        newFileFlag = true;
        fileName = "Untitled";
        file = new File(fileName);
        this.notepad.frame.setTitle(fileName + " - " + applicationTitle);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
    }
    
    
    // SaveFile method writes the contents of the notepad textarea to a file on hard drive.

    boolean saveFile(File file){
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(file);
            fileWriter.write(notepad.textArea.getText());
        } catch(IOException ioe){
            updateStatus(file, false);
            return false;
        }
        finally{
            try{
                fileWriter.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        updateStatus(file,true);
        return true;

    }

    ///////////////////////////
    //Called when user saves file that is open

    boolean saveThisFile()
    {
        if(!newFileFlag)
            return saveFile(file);
        
        return saveAsFile();
    }

    /////////////////////////////////
    //Called when the user clicks on save as.

    boolean saveAsFile(){
        File file1;
        fileChooser.setDialogTitle("Save As...");
        fileChooser.setApproveButtonText("Save Now");
        fileChooser.setApproveButtonMnemonic(KeyEvent.VK_S);
        fileChooser.setApproveButtonToolTipText("Click me to save!");

        //Loop infinitely to show dialog until user saves file

        do{
            if(fileChooser.showSaveDialog(this.notepad.frame)!= JFileChooser.APPROVE_OPTION){
                return false;
            }

            file1 = fileChooser.getSelectedFile();
            if(!file1.exists()){
                break;
            }

            if(JOptionPane.showConfirmDialog(this.notepad.frame, "<html>" + file1.getPath() + " already exists. <br>Do you want to replace it? <html>", "Save As", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            break; 
            }
        }while(true);

        return saveFile(file1);
    }

    ////////////////////////////////
    //Reads in a file and displays it in the text area of the notepad.
    boolean readFile(File file){
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        
        try{
            fileInputStream = new FileInputStream(file);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String currentLine = " ";

            while(currentLine != null){
                currentLine = bufferedReader.readLine();
                if(currentLine == null)
                    break;
                this.notepad.textArea.append(currentLine + "\n");

            }
        }catch(IOException ioe){
            updateStatus(file, false);
            return false;
        }
        finally{
            try{
                bufferedReader.close();
                fileInputStream.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        updateStatus(file, true);
        this.notepad.textArea.setCaretPosition(0);
        return true;
    }


    ////////////////////////////////////
    // Called when the user clicks on open file in the menu

    void openFile(){
        if(!confirmSave()){
            return;
        }

        fileChooser.setDialogTitle("Open File...");
        fileChooser.setApproveButtonText("Open this");
        fileChooser.setApproveButtonMnemonic(KeyEvent.VK_O);
        fileChooser.setApproveButtonToolTipText("Click me to open the selected file.");

        File file1;

        do{
            if(fileChooser.showOpenDialog(this.notepad.frame) != JFileChooser.APPROVE_OPTION){
                return;
            }
            
            file1 = fileChooser.getSelectedFile();

            if(file1.exists()){
                break;
            }

            JOptionPane.showMessageDialog(this.notepad.frame, "<html>" + file1.getName() + "<br>file not found. <br>" + "Please verify the correct file name was given.<html>", "Open", JOptionPane.INFORMATION_MESSAGE);

        
        }while(true);

        this.notepad.textArea.setText("");

        if(!readFile(file1))
        {
            fileName = "Untitled";
            saved = true;
            this.notepad.frame.setTitle(fileName + " - " + applicationTitle);
        }

        if(!file1.canWrite())
            newFileFlag = true;
    }

    ////////////////////////////////
    //Updates the file status while the file is saved
    void updateStatus(File file, boolean saved){
        if(saved){
            this.saved = true;
            fileName = file.getName();
            if(!file.canWrite()){
                fileName += "(Read Only)";
                newFileFlag = true;
            }
            this.file = file;
            notepad.frame.setTitle(fileName + " - " + applicationTitle);
            notepad.statusBar.setText("File : " + file.getPath() + " saved/opened successfully.");
            newFileFlag = false;
        }
        else{
            notepad.statusBar.setText("Failed to save/open : " + file.getPath());
        }
    }

    ///////////////////////////////
    // Shows option pane dialog to prompt usre to confirm that they want to save a file

    boolean confirmSave(){
        String strMsg = "<html>The text in the " + fileName + "file has been changed.<br>" + "Do you want to save the changes?<html>";

        if(!saved){
            int x = JOptionPane.showConfirmDialog(this.notepad.frame, strMsg, applicationTitle, JOptionPane.YES_NO_CANCEL_OPTION);

            if(x == JOptionPane.YES_OPTION && !saveAsFile())
            return false;
        }

        return true;
    }

    //////////////////////////////
    // Creates a new file

    void newFile(){
        if(!confirmSave()){
            return;
        }

        this.notepad.textArea.setText("");

        fileName = "Untitled";
        file = new File(fileName);
        saved = true;
        newFileFlag = true;
        this.notepad.frame.setTitle(fileName + " - " + applicationTitle);
    }

    
}
