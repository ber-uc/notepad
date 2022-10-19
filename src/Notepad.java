import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Notepad implements ActionListener{
    //Declare the attributes of the Notepad Class

    Font f2 = new Font(Font.SANS_SERIF, Font.ITALIC, 15);
    public void setF2(Font f2) {
        this.f2 = f2;
    }
    


    JFrame frame;
    JTextArea textArea;
    JLabel statusBar;

    private String fileName = "Untitled";
    String applicationName = "Notepad";

    FileOperation fileOperation;

    JColorChooser bcolorChooser = null;
    JColorChooser fColorChooser = null;
    JDialog backgroundDialog = null;
    JDialog foregroundDialog = null;
    JMenuItem cutItem, copyItem, deleteItem, gotoItem, selectAllItem;

    final String fileText = "File";
    final String editText = "Edit";
    final String formatText = "Format";
    final String viewText = "View";
    final String helpText = "Help";

    final String fileNew = "New";
    final String fileOpen = "Open...";
    final String fileSave = "Save";
    final String fileSaveAs = "Save As...";

    final String fileExit = "Exit";

    final String editUndo = "Undo";
    final String editCut = "Cut";
    final String editCopy = "Copy";
    final String editPaste ="Paste";
    final String editDelete = "Delete";

    final String editGoTo = "Go To...";
    final String editSelectAll = "Select All";
    final String editTimeDate = "Time/Date";

    final String formatWordWrap = "Word Wrap";

    final String formatForeground = "Set Text Color...";
    final String formatBackground = "Set Pad Color...";

    final String viewStatusBar = "Status Bar";

    final String helpHelpTopic = "Help Topic";
    final String helpAboutNotepad = "About Notepad";

    final String aboutText = "This notepad application was developed by Bruk K. \n have fun xdddd";
    final String helperText = "Welcome to Notepad! Use Notepad to record information on the fly. Use the menus to assist in making changes to the file! Enjoy!";

    //main
    public static void main(String[] s){
        //Create new ntoepad
        //Call constructor

        new Notepad();
    }


     /****************************/
    // Constructor               /
    // Creates and shows the GUI /
    /****************************/


    Notepad(){
        //Creates new frame, textarea, and a label for status bar

            frame = new JFrame(fileName + " - " + applicationName);
            textArea = new JTextArea(30,60);
            statusBar = new JLabel("||      Ln 1, Col 1     ", JLabel.RIGHT);

            // Add scroll pane and status bar to the frame
            frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
            frame.add(statusBar, BorderLayout.SOUTH);
            frame.add(new JLabel("  "), BorderLayout.EAST);
            frame.add(new JLabel("  "), BorderLayout.WEST);

            //Create new menu for editor
            createMenuBar(frame);

            //Set window to preferred size, location visibility
            frame.pack();
            frame.setLocation(100,50);
            frame.setVisible(true);

            //Set frame to do nothing when user clicks X to close
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            //Create new file operation to handle opening, closing, and saving files
             FileOperation fileOperation = new FileOperation(this);

            //Add caret listener to "listen" for changes to text area
            //Cursor movement;
            textArea.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e)
                {
                    int lineNumber = 0, column = 0, pos = 0;
                    try{
                        pos = textArea.getCaretPosition();
                        lineNumber = textArea.getLineOfOffset(pos);
                        column = pos - textArea.getLineStartOffset(lineNumber);

                    }catch(Exception exception){
                        exception.printStackTrace();
                    } //end try catch

                    if(textArea.getText().length() == 0){
                        lineNumber = 0;
                        column = 0;
                    }

                    //When user moves the cursor, update line and column numbers on the status bar.
                    statusBar.setText("||       Ln"+(lineNumber + 1) + ", Col " + (column + 1));
                }

               
                });

                //Add listener to listen for changes user makes in doc.

                DocumentListener myListener = new DocumentListener()
                {
                    @Override
                    public void changedUpdate(DocumentEvent e){
                        fileOperation.saved = false;
                    }

                    
                    @Override
                    public void removeUpdate(DocumentEvent e){
                        fileOperation.saved = false;
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e){
                        fileOperation.saved = false;
                    }
                };

                // Add document lisetner to the txet area in the notepad app.

                textArea.getDocument().addDocumentListener(myListener);

                //Add window listener to handle when user closes app

                 WindowListener frameClose = new WindowAdapter()
                {
                    public void windowClosing(WindowEvent we){
                        //Prompt user to save their file if they clicked the x to close the app.
                        if(fileOperation.confirmSave()){
                            System.exit(0);
                        }
                    }
                    };
                
                    //Add listener to window
                    frame.addWindowListener(frameClose);
                }

                void goTo(){
                    int lineNumber = 0;
                    try{
                        // get the current position fo the cursor
                        lineNumber = textArea.getLineOfOffset(textArea.getCaretPosition()) + 1;
                        
                        // prompt user to enter line number
                        String tempStr = JOptionPane.showInputDialog(frame, "Enter Line Number:","" + lineNumber);

                        //If user didn't enter line number, then end the method
                        if(tempStr == null)
                        {
                            return;
                        }
                        //Get the line number the suer enered and set the cursor position to line number
                        lineNumber = Integer.parseInt(tempStr);
                        textArea.setCaretPosition(textArea.getLineStartOffset(lineNumber - 1));
                    
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }


                ////////////////////////////////////////////////

                //This method performs an action absed onw hat the user chooses on the menu.

                /* (non-Javadoc)
                 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
                 */
                public void actionPerformed(ActionEvent ev)
                {
                    String cmdText = ev.getActionCommand();
                    switch (cmdText){
                        case fileNew:
                            fileOperation.newFile();
                            break;

                        case fileOpen:
                            fileOperation.openFile();
                            break;

                        case fileSave:
                            fileOperation.saveThisFile();
                            break;

                        case fileSaveAs:
                            fileOperation.saveAsFile();
                            break;
                        case fileExit:
                            if(fileOperation.confirmSave()) System.exit(0);
                            break;

                        
                        case editCut:
                            textArea.cut();
                            break;

                        case editCopy:
                            textArea.copy();
                            break;

                        case editPaste:
                            textArea.paste();
                            break;

                        case editDelete:
                            textArea.replaceSelection("");
                            break;

                        case editGoTo:
                            if(Notepad.this.textArea.getText().length() == 0)
                            return;
                            goTo();
                            break;

                        case editSelectAll: 
                            textArea.selectAll();
                            break;

                        case editTimeDate: 
                            textArea.insert(new Date().toString(), textArea.getSelectionStart());
                            break;

                        case formatWordWrap: {
                            JCheckBoxMenuItem temp = (JCheckBoxMenuItem) ev.getSource();
                            textArea.setLineWrap(temp.isSelected());
                            break;
                        }

                        case formatForeground:
                            showForegroundColorDialog();
                            break;

                        case formatBackground:
                            showBackgroundColorDialog();
                            break;

                        case viewStatusBar: {
                            JCheckBoxMenuItem temp = (JCheckBoxMenuItem) ev.getSource();
                            statusBar.setVisible(temp.isSelected());
                            break;
                        }

                        case helpAboutNotepad:
                            JOptionPane.showMessageDialog(Notepad.this.frame, aboutText, "About Notepad", JOptionPane.INFORMATION_MESSAGE );
                            break;

                        case helpHelpTopic:
                            JOptionPane.showMessageDialog(Notepad.this.frame, helperText, "Help Topic", JOptionPane.INFORMATION_MESSAGE);   

                        default:
                            statusBar.setText("This " + cmdText + " command is yet to be implemented");
                            break;

                    }
                }


                ////////////////////////////////////
                //This method allows the user to change the background of text area

                void showBackgroundColorDialog(){
                    if(bcolorChooser == null){
                        bcolorChooser = new JColorChooser();
                    }

                    if(backgroundDialog == null)
                    backgroundDialog = JColorChooser.createDialog(Notepad.this.frame, formatBackground, false, bcolorChooser, new ActionListener(){

                    public void actionPerformed(ActionEvent actionEvent){Notepad.this.textArea.setBackground(bcolorChooser.getColor());
                    }

                }, null);

                //Show dialog
                backgroundDialog.setVisible(true);
            }


            //////////////////////////////////////////
            //Method allwos user to change color of text in the notepad app.

            void showForegroundColorDialog(){
                if(fColorChooser == null)
                    fColorChooser = new JColorChooser();

                if(foregroundDialog == null){
                    foregroundDialog = JColorChooser.createDialog(Notepad.this.frame, formatForeground, false, fColorChooser, new ActionListener(){
                            public void actionPerformed(ActionEvent actionEvent){
                                Notepad.this.textArea.setForeground(fColorChooser.getColor());
                            }
                    },
                    null); 
                }
                foregroundDialog.setVisible(true);
            }


            ///////////////////////////////////////////////
            //createMenuItem methods add a new item to the drop-down menu in the notepad, adds a listener to respond if user clicks on menu

            JMenuItem createMenuItem(String s, int key, JMenu toMenu, ActionListener al){

                JMenuItem menuItem = new JMenuItem(s, key);
                menuItem.addActionListener(al);
                toMenu.add(menuItem);

                return menuItem;
            }

            JMenuItem createMenuItem(String s, int key, JMenu toMenu, int aclKey, ActionListener al){

                JMenuItem menuItem = new JMenuItem(s, key);
                menuItem.addActionListener(al);
                menuItem.setAccelerator(KeyStroke.getKeyStroke(aclKey, ActionEvent.CTRL_MASK));
                toMenu.add(menuItem);
                
                return menuItem;

            }

            ///////////////////////////////////////////////
            // createMenuItem methods add a new checkbox item to the drop-down menu and adds a listener for clicking checkbox

            JCheckBoxMenuItem createCheckBoxMenuItem(String s, int key, JMenu toMenu, ActionListener al){
                JCheckBoxMenuItem temp = new JCheckBoxMenuItem(s);
                temp.setMnemonic(key);
                temp.addActionListener(al);
                temp.setSelected(false);
                toMenu.add(temp);

                return temp;
            }

            ///////////////////////////////////////////////
            // This methods creates a new deopdown menu for the app (ex. file, edit, view menus)

            JMenu createMenu(String s, int key, JMenuBar toMenuBar){
                JMenu menu = new JMenu(s);
                menu.setMnemonic(key);
                toMenuBar.add(menu);

                return menu;
            }

            ///////////////////////////////////////////////
            // Adds a menu bar and its attributes
            void createMenuBar(JFrame f)
            {
                JMenuBar menuBar = new JMenuBar();

                JMenu fileMenu = createMenu(fileText, KeyEvent.VK_F, menuBar);
                JMenu editMenu = createMenu(editText, KeyEvent.VK_E, menuBar);
                JMenu formatMenu = createMenu(formatText, KeyEvent.VK_O, menuBar);
                JMenu viewMenu = createMenu(viewText, KeyEvent.VK_V, menuBar);
                JMenu helpMenu = createMenu(helpText, KeyEvent.VK_H, menuBar);

                createMenuItem(fileNew, KeyEvent.VK_N, fileMenu, KeyEvent.VK_N, this);
                createMenuItem(fileOpen, KeyEvent.VK_O, fileMenu, KeyEvent.VK_O, this);
                createMenuItem(fileSave, KeyEvent.VK_S, fileMenu, KeyEvent.VK_S, this);
                createMenuItem(fileSaveAs, KeyEvent.VK_A, fileMenu, KeyEvent.VK_A, this);
                fileMenu.addSeparator();
                createMenuItem(fileExit, KeyEvent.VK_X, fileMenu, this);

                JMenuItem menuItem = createMenuItem(editUndo, KeyEvent.VK_U, editMenu, KeyEvent.VK_Z, this);
                menuItem.setEnabled(false);
                editMenu.addSeparator();
                cutItem = createMenuItem(editCut, KeyEvent.VK_T, editMenu, KeyEvent.VK_X, this);
                copyItem = createMenuItem(editCopy, KeyEvent.VK_C, editMenu, KeyEvent.VK_C, this);
                createMenuItem(editPaste, KeyEvent.VK_P, editMenu, KeyEvent.VK_V,this);
                deleteItem = createMenuItem(editDelete, KeyEvent.VK_L, editMenu, this);
                deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
                editMenu.addSeparator();

                gotoItem = createMenuItem(editGoTo, KeyEvent.VK_G, editMenu, KeyEvent.VK_G, this);
                editMenu.addSeparator();

                selectAllItem = createMenuItem(editSelectAll, KeyEvent.VK_A, editMenu, KeyEvent.VK_A, this);
                createMenuItem(editTimeDate, KeyEvent.VK_D, editMenu, this).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));

                createCheckBoxMenuItem(formatWordWrap, KeyEvent.VK_T, formatMenu, this);
                formatMenu.addSeparator();
                createMenuItem(formatForeground, KeyEvent.VK_T, formatMenu, this);
                createMenuItem(formatBackground, KeyEvent.VK_P, formatMenu, this);

                createCheckBoxMenuItem(viewStatusBar, KeyEvent.VK_S, viewMenu, this).setSelected(true);

                menuItem = createMenuItem(helpHelpTopic, KeyEvent.VK_H, helpMenu, this);
                menuItem.setEnabled(true);
                helpMenu.addSeparator();
                createMenuItem(helpAboutNotepad, KeyEvent.VK_A, helpMenu, this);



                //Add a listener to rsponse when the user clicks on one of the drop down menus in bar

                MenuListener editMenuListener = new MenuListener() {

                    @Override
                    public void menuSelected(MenuEvent menuEvent) {
                        if(Notepad.this.textArea.getText().length() == 0){
                            selectAllItem.setEnabled(false);
                            gotoItem.setEnabled(false);
                        }
                        else{
                            selectAllItem.setEnabled(true);
                            gotoItem.setEnabled(true);
                        }

                        if(Notepad.this.textArea.getSelectionStart() == textArea.getSelectionEnd()){
                            cutItem.setEnabled(false);
                            copyItem.setEnabled(false);
                            deleteItem.setEnabled(false);
                        }

                        else{
                            cutItem.setEnabled(true);
                            copyItem.setEnabled(true);
                            deleteItem.setEnabled(true);
                        }
                        
                    }

                    @Override
                    public void menuDeselected(MenuEvent menuEvent) {
                        
                        
                    }

                    @Override
                    public void menuCanceled(MenuEvent menuEvent) {
                        
                        
                    }
                    
                };

                editMenu.addMenuListener(editMenuListener);

                f.setJMenuBar(menuBar);
            }


            //////////////////////////////
            //Help Dialog method



            }
        
    

