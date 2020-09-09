package com.ytp.ytpplus;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLController {
// <editor-fold defaultstate="collapsed" desc="heres a fold to make it neater">
    @FXML
    private MediaView mediaviewVideoPlayer;

    @FXML
    private CheckBox cbEffect1;

    @FXML
    private CheckBox cbEffect2;

    @FXML
    private CheckBox cbEffect3;

    @FXML
    private CheckBox cbEffect4;

    @FXML
    private CheckBox cbEffect5;

    @FXML
    private CheckBox cbEffect6;

    @FXML
    private CheckBox cbEffect7;

    @FXML
    private CheckBox cbEffect8;

    @FXML
    private CheckBox cbEffect9;

    @FXML
    private CheckBox cbEffect10;

    @FXML
    private CheckBox cbEffect11;
    
    @FXML
    private CheckBox cbEffect12;
    
    @FXML
    private CheckBox cbEffect13;
    
    @FXML
    private CheckBox cbEffect14;
    
    @FXML
    private CheckBox cbEffect15;
    
    @FXML
    private CheckBox cbEffect16;
    
    @FXML
    private CheckBox cbEffect17;
    
    @FXML
    private CheckBox cbEffect18;
    
    @FXML
    private CheckBox cbEffect19;
    
    @FXML
    private CheckBox cbEffect20;
    
    // @FXML
    // private CheckBox cbEffect21;
    
    @FXML
    private Button btnSelectAll;
    
    @FXML
    private Button btnSelectNone;

    @FXML
    private Button btnCreate;

    @FXML
    private TextField tfClipCount;

    @FXML
    private TextField tfMaxStream;

    @FXML
    private TextField tfMinStream;

    @FXML
    private ProgressBar barProgress;

    @FXML
    private Button btnPlayVideo;

    @FXML
    private Button btnPauseVideo;

    @FXML
    private Button restartVideo;

    @FXML
    private TextField tfFFMPEG;

    @FXML
    private TextField tfFFPROBE;

    @FXML
    private TextField tfMAGICK;

    @FXML
    private TextField tfTEMP;

    @FXML
    private TextField tfSOUNDS;

    @FXML
    private TextField tfMUSIC;

    @FXML
    private TextField tfRESOURCES;

    @FXML
    private Button btnBrowseFFMPEG;

    @FXML
    private Button btnBrowseFFPROBE;

    @FXML
    private Button btnBrowseMAGICK;

    @FXML
    private Button btnBrowseTEMP;

    @FXML
    private Button btnBrowseSOUNDS;

    @FXML
    private Button btnBrowseMUSIC;

    @FXML
    private Button btnBrowseRESOURCES;

    @FXML
    private Button btnHelpMeConfig;

    @FXML
    private ListView<String> listviewSourcesList;

    @FXML
    private TextField tfSOURCES;

    @FXML
    private Button btnBrowseSOURCES;

    @FXML
    private CheckBox cbUseTransitions;

    @FXML
    private Button btnSaveAs;
    
    @FXML
    private Button btnReset;

    @FXML
    private Button btnAddSource;

    @FXML
    private Button btnRemoveSource;
    
    @FXML
    private Button btnShowCredits;
    
    @FXML
    private Slider sldTransitionProb;
    
    @FXML
    private Label labTransitionProb;
    
    @FXML
    private Slider sldEffectProb;
    
    @FXML
    private Label labEffectProb;
    
    @FXML
    private CheckBox cbAllowEffectStacking;
    
    @FXML
    private Slider sldEffectStack;
    
    @FXML
    private Label labEffectStack;
    
// </editor-fold>
    
    private final ObservableList<String> sourceList = FXCollections.observableArrayList();
    
    private final String TEMP = "temp/";
    private File lastBrowsed;
    
    public TextField getTfFFMPEG() {
        return tfFFMPEG;
    }

    public TextField getTfFFPROBE() {
        return tfFFPROBE;
    }

    public TextField getTfMAGICK() {
        return tfMAGICK;
    }
    
    @FXML
    void addSource(ActionEvent event) {
        
        FileChooser.ExtensionFilter fileFilter = new FileChooser.ExtensionFilter("Video files (*.mp4)", "*.mp4");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(fileFilter);
        fileChooser.setTitle("Choose Source");
        fileChooser.setInitialDirectory(lastBrowsed);
        List<File> selected = fileChooser.showOpenMultipleDialog(null);
        if (selected==null) return;
        selected.stream().forEach((file) -> {
            sourceList.add(file.getAbsolutePath().replace('\\', '/'));
        });
        listviewSourcesList.setItems(sourceList);
        lastBrowsed = selected.get(0).getParentFile();
    }
    
    @FXML
    void removeSource(ActionEvent event) {
        sourceList.remove(listviewSourcesList.getSelectionModel().getSelectedItems().get(0));
    }
    
    @FXML
    void updateTransitionLabel() {
        labTransitionProb.textProperty().setValue(String.valueOf((int)sldTransitionProb.getValue()));
    }
    
    @FXML
    void updateEffectLabel() {
        labEffectProb.textProperty().setValue(String.valueOf((int)sldEffectProb.getValue()));
    }
    
    @FXML
    void updateEffectStackLabel() {
        labEffectStack.textProperty().setValue(String.valueOf((int)sldEffectStack.getValue()));
    }
    
    @FXML
    void toggleEffectStackSlider(ActionEvent event) {
        if(sldEffectStack.isDisabled()) {
            sldEffectStack.setDisable(false);
        } else {
            sldEffectStack.setDisable(true);
        }
    }
    
    @FXML
    void selectAll(ActionEvent event) {
        selectAll();
    }
    
    private void selectAll() {
        cbEffect1.setSelected(true);
        cbEffect2.setSelected(true);
        cbEffect3.setSelected(true);
        cbEffect4.setSelected(true);
        cbEffect5.setSelected(true);
        cbEffect6.setSelected(true);
        cbEffect7.setSelected(true);
        cbEffect8.setSelected(true);
        cbEffect9.setSelected(true);
        cbEffect10.setSelected(true);
        cbEffect11.setSelected(true);
        cbEffect12.setSelected(true);
        cbEffect13.setSelected(true);
        cbEffect14.setSelected(true);
        cbEffect15.setSelected(true);
        cbEffect16.setSelected(true);
        cbEffect17.setSelected(true);
        cbEffect18.setSelected(true);
        cbEffect19.setSelected(true);
        cbEffect20.setSelected(true);
        // cbEffect21.setSelected(true);
    }
    
    @FXML
    void selectNone(ActionEvent event) {
        cbEffect1.setSelected(false);
        cbEffect2.setSelected(false);
        cbEffect3.setSelected(false);
        cbEffect4.setSelected(false);
        cbEffect5.setSelected(false);
        cbEffect6.setSelected(false);
        cbEffect7.setSelected(false);
        cbEffect8.setSelected(false);
        cbEffect9.setSelected(false);
        cbEffect10.setSelected(false);
        cbEffect11.setSelected(false);
        cbEffect12.setSelected(false);
        cbEffect13.setSelected(false);
        cbEffect14.setSelected(false);
        cbEffect15.setSelected(false);
        cbEffect16.setSelected(false);
        cbEffect17.setSelected(false);
        cbEffect18.setSelected(false);
        cbEffect19.setSelected(false);
        cbEffect20.setSelected(false);
        // cbEffect21.setSelected(false);
    }
    
    @FXML
    void resetValues(ActionEvent event) {
        selectAll();
        sldEffectProb.setValue(50);
        
        cbUseTransitions.setSelected(true);
        sldTransitionProb.setValue(15);
        
        cbAllowEffectStacking.setSelected(false);
        sldEffectStack.setValue(2);
        sldEffectStack.setDisable(true);
        
        tfMinStream.setText("0.2");
        tfMaxStream.setText("0.4");
        tfClipCount.setText("20");
    }

    @FXML
    void goNow(ActionEvent event) {
        // Check for valid inputs.
        if (sourceList.isEmpty()) {
            errorAlert("You need some sources.");
            return;
        }
        
        final double min;
        final double max;
        final int maxClips;
        
        try {
            min = Double.parseDouble(tfMinStream.getText());
            max = Double.parseDouble(tfMaxStream.getText());
            maxClips = Integer.parseInt(tfClipCount.getText());
            
            if (min <= 0 || max <= 0 || maxClips <= 0) {
                errorAlert("Stream settings must be positive numbers.");
                return;
            } else if(max <= min) {
                errorAlert("Min Stream Duration must be less than Max Stream Duration.");
                return;
            }
        } catch (NumberFormatException e) {
            errorAlert("Stream settings must be positive numbers.");
            return;
        }
        
        Thread vidThread = new Thread() {
            @Override
            public void run() {
                
                long timeStarted = System.currentTimeMillis();
                disableControls();
                System.out.println("\n\n\n\nRunning...");
                
                // Create temp directory for this job instance.
                String jobDir = tfTEMP.getText() + "job_" + System.currentTimeMillis() + "/";
                new File(jobDir).mkdir();
                
                // Create effect array.
                boolean[] effects = new boolean[20];
                
                effects[0] = cbEffect1.isSelected();
                effects[1] = cbEffect2.isSelected();
                effects[2] = cbEffect3.isSelected();
                effects[3] = cbEffect4.isSelected();
                effects[4] = cbEffect5.isSelected();
                effects[5] = cbEffect6.isSelected();
                effects[6] = cbEffect7.isSelected();
                effects[7] = cbEffect8.isSelected();
                effects[8] = cbEffect9.isSelected();
                effects[9] = cbEffect10.isSelected();
                effects[10] = cbEffect11.isSelected();
                effects[11] = cbEffect12.isSelected();
                effects[12] = cbEffect13.isSelected();
                effects[13] = cbEffect14.isSelected();
                effects[14] = cbEffect15.isSelected();
                effects[15] = cbEffect16.isSelected();
                effects[16] = cbEffect17.isSelected();
                effects[17] = cbEffect18.isSelected();
                effects[18] = cbEffect19.isSelected();
                effects[19] = cbEffect20.isSelected();
                // effects[20] = cbEffect21.isSelected();
                
                // Initialize directories.
                Utilities util = new Utilities(
                        tfFFPROBE.getText(),
                        tfFFMPEG.getText(),
                        tfMAGICK.getText(),
                        jobDir,
                        tfSOURCES.getText(),
                        tfSOUNDS.getText(),
                        tfMUSIC.getText(),
                        tfRESOURCES.getText()
                );
                
                // Initialize generator.
                YTPGenerator generator = new YTPGenerator(
                        util, 
                        TEMP + "tempoutput.mp4", 
                        min, 
                        max, 
                        maxClips, 
                        cbUseTransitions.isSelected(),
                        (int)sldTransitionProb.getValue(), 
                        (int)sldEffectProb.getValue(), 
                        cbAllowEffectStacking.isSelected(), 
                        (int)sldEffectStack.getValue(), 
                        effects
                );
                
                // Set source list in generator
                sourceList.stream().forEach((source) -> {
                    generator.addSource(source);
                });
                
                generator.go();
                
                while (!generator.done) {
                    if (generator.ex == null) {
                        barProgress.setProgress(generator.doneCount);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // Keep going
                        }
                    } else {
                        break; // If an exception occurs, break out of loop and handle it in the next block.
                    }
                
                }
                // Check for error..
                if (generator.ex == null) {
                    
                    // Everything is fine. Display the finished video.
                    barProgress.setProgress(1);
                    File media = new File(tfTEMP.getText() + "tempoutput.mp4");
                    
                    // If a MediaPlayer exists already, dispose it.
                    try {
                        MediaPlayer oldMediaPlayer = mediaviewVideoPlayer.getMediaPlayer();
                        oldMediaPlayer.stop();
                        oldMediaPlayer.dispose();
                    } catch (NullPointerException e) {
                        // MediaPlayer may not have been set yet particularly on the first generation run after restarting.
                        // In which case, do nothing.
                    }

                    // Create a new MediaPlayer.
                    try {
                        Media m = new Media(media.toURI().toString());
                        MediaPlayer mp = new MediaPlayer(m);
                        mediaviewVideoPlayer.setMediaPlayer(mp);
                    } catch (Exception e) {
                        System.out.println("FXML.goNow(): " + e.toString());
                    } finally {
                        enableControls();
                        barProgress.setProgress(0);
                    }

                    long elapsedTime = System.currentTimeMillis() - timeStarted;
                    System.out.println("\nDone! Elapsed time was " + elapsedTime/1000 + " seconds\n");
                    
                } else {
                    Platform.runLater(() -> {
                            errorAlert("An error has occured while generating:\n\n" + generator.ex.getMessage());
                        });
                    System.out.println("FXML.goNow(): " + generator.ex.toString());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    } finally {
                        generator.cleanUp();
                        enableControls();
                        barProgress.setProgress(0);
                    }
                }
            }
        };
        vidThread.start();
    }

    @FXML
    void helpMeConfig(ActionEvent event) {
        alert("These boxes provide the paths to the executables "
                + "which will be run throughout the batch process.\n\n"
                + "If you're on linux or OSX, \"magick\" should be empty.\n\n"
                + "The only reason it is here is because on Windows, most "
                + "ImageMagick tools are called from commandline by using "
                + "\"magick convert...\", while on other operating systems, "
                + "magick commands can be called simply by saying \"convert...\".\n\n"
                + "If you have separate installations of these tools, feel "
                + "free to change what's in these boxes to suit your fancy.\n\n"
                + "If you have no idea what any of this means, leave it be."
                );

    }
    
    @FXML
    void helpMeEffect(ActionEvent event) {
        alert("By default, there is a 1/2 chance of an effect happening. Use the slider to increase or decrease the chance an effect happens.\n\n"
                + "Once it is determined an effect will be used on a clip, each checked effect has an equal chance of appearing. "
                + "Turning an effect off will not decrease the chance of an effect overall happening.\n\n"
                + "When Effect Stacking is enabled and an effect is added to clip, there is a chance another effect will be applied on top of the first one. "
                + "This chance is deterined by rolling a random number between 1 and the stack level indicated by the slider inclusive and determines how many effects can be stacked onto a single clip. "
                + "Effects in a single stack will always be different from eachother. "
                + "Effect Stacking is slower; processing time increases the higher the stack level. "
                + "Try decreasing the effect probability when enabled to improve processing time.");

    }
    @FXML
    void helpMeTransition(ActionEvent event) {
        alert("By default there's a 1/15 chance of a \"transition\" clip being used in place of your sources. "
                + "So for every 15 clips you tell YTP+ to generate, one of them will be a transition clip from the sources folder.\n\n"
                + "You may change the probability transition clips appear using the slider below.\n"
                + "Moving the slider to the right will make transition clips more rare.\n\n"
                + "For added fun, try adding your own small clips to the transition clips folder.");
    }
    
    @FXML
    void openBrowser(ActionEvent event) {
        switch (((Control)event.getSource()).getId()) {
            case "btnBrowseFFMPEG":
                actuallyOpenBrowser(tfFFMPEG);
                break;
            case "btnBrowseFFPROBE":
                actuallyOpenBrowser(tfFFPROBE);
                break;
            case "btnBrowseMAGICK":
                actuallyOpenBrowser(tfMAGICK);
                break;
            case "btnBrowseTEMP":
                actuallyOpenDirBrowser(tfTEMP);
                break;
            case "btnBrowseSOUNDS":
                actuallyOpenDirBrowser(tfSOUNDS);
                break;
            case "btnBrowseMUSIC":
                actuallyOpenDirBrowser(tfMUSIC);
                break;
            case "btnBrowseRESOURCES":
                actuallyOpenDirBrowser(tfRESOURCES);
                break;
            case "btnBrowseSOURCES":
                actuallyOpenDirBrowser(tfSOURCES);
                break;
        }
    }
    
    void actuallyOpenBrowser(TextField toChange) {
        FileChooser.ExtensionFilter fileFilter = new FileChooser.ExtensionFilter("Executable files (*.exe)", "*.exe");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(fileFilter);
        fileChooser.setTitle("Choose File");
        fileChooser.setInitialDirectory(lastBrowsed);
        File selected = fileChooser.showOpenDialog(null);
        if (selected==null) return;
        toChange.setText(selected.getAbsolutePath().replace('\\', '/'));
        lastBrowsed = selected.getParentFile();
        writeConfig();
    }
    
    void actuallyOpenDirBrowser(TextField toChange) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.setInitialDirectory(lastBrowsed);
        File selected = fileChooser.showDialog(null);
        if (selected==null) return;
        toChange.setText(selected.getAbsolutePath().replace('\\', '/') + "/");
        lastBrowsed = selected.getParentFile();
    }
    
    private void writeConfig() {
        File config = new File("config");
        if (config.exists()) {
            config.delete();
        }
        try (PrintWriter writer = new PrintWriter(config.getPath(), "UTF-8")) {
            writer.write(tfFFMPEG.getText() + ";" + tfFFPROBE.getText() + ";" + tfMAGICK.getText() + "\n");
        } catch (Exception e) {
            errorAlert(e.getMessage());
        }
    }
    
    @FXML
    void pauseTheVideo(ActionEvent event) {
        try {
        mediaviewVideoPlayer.getMediaPlayer().pause();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } // if there's no video then don't bother doing anything
    }

    @FXML
    void playTheVideo(ActionEvent event) {
        try {
        mediaviewVideoPlayer.getMediaPlayer().play();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } // if there's no video then don't bother doing anything
    }

    @FXML
    void restartTheVideo(ActionEvent event) {
        try {
            mediaviewVideoPlayer.getMediaPlayer().stop();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } // if there's no video then don't bother doing anything
    }

    @FXML
    void saveAsVideo(ActionEvent event) {
        if (!new File(tfTEMP.getText() + "tempoutput.mp4").exists()) return;
        FileChooser.ExtensionFilter fileFilter = new FileChooser.ExtensionFilter("Video files (*.mp4)", "*.mp4");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(fileFilter);
        fileChooser.setTitle("Choose Source");
        fileChooser.setInitialDirectory(lastBrowsed);
        File selected = fileChooser.showSaveDialog(null);
        if (selected==null) return;
        Path temp = Paths.get(tfTEMP.getText() + "tempoutput.mp4");
        try {
            Files.copy(temp, selected.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            errorAlert("Had a problem copying the file:" + e.getMessage());
        }
    }
    
    @FXML
    void showCredits(ActionEvent event) {
        alert("Originally made by Ben B.\n\n"
                + "Updated by theFXexpert\n\n" // Not an expert at JavaFX at all lol
                + "Github user randompooper contributed many of the bugfixes and optimizations that were incorperated in this version.\n"
                + "");
    }
    
    void alert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        Scene scene = alert.getDialogPane().getScene();
        scene.getStylesheets().add("/styles/Styles.css");
        
        Stage stage = (Stage) scene.getWindow();
        stage.getIcons().add(new Image("file:resources/TheKingSq.png"));
        alert.getDialogPane().setGraphic(new ImageView("file:resources/TheKingSq48.png"));
        
        String[] titles = {"Dinner", "Mmmmm!", "I'm the invisible man...", "Luigi, look!", "You want it?", "WTF Booooooooooom",
            "The Enclosed Instruction Book", "Dear pesky plumbers...", "Send Link", "My ship sails in the morning", "Mah boy", "It is written...",
            "Squalada! We are off!", "It's a stone Luigi"};
        alert.setTitle(titles[randomInt(0,titles.length-1)]);
        
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.show();
    }
    
    public void errorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        Scene scene = alert.getDialogPane().getScene();
        scene.getStylesheets().add("/styles/Styles.css");
        
        Stage stage = (Stage) scene.getWindow();
        stage.getIcons().add(new Image("file:resources/TheKingSq.png"));
        alert.getDialogPane().setGraphic(new ImageView("file:resources/TheKingForSq48.png"));
        String[] titles = {"SNOOPINGAS", "YOU DUMBOTS", "WTF Booooooooooom"};
        alert.setTitle(titles[randomInt(0,titles.length-1)]);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.show();
    }
    
    public static int randomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
    
    private void disableControls() {
        btnCreate.setDisable(true);
        btnReset.setDisable(true);
        
        btnSelectAll.setDisable(true);
        btnSelectNone.setDisable(true);
        
        cbEffect1.setDisable(true);
        cbEffect2.setDisable(true);
        cbEffect3.setDisable(true);
        cbEffect4.setDisable(true);
        cbEffect5.setDisable(true);
        cbEffect6.setDisable(true);
        cbEffect7.setDisable(true);
        cbEffect8.setDisable(true);
        cbEffect9.setDisable(true);
        cbEffect10.setDisable(true);
        cbEffect11.setDisable(true);
        cbEffect12.setDisable(true);
        cbEffect13.setDisable(true);
        cbEffect14.setDisable(true);
        cbEffect15.setDisable(true);
        cbEffect16.setDisable(true);
        cbEffect17.setDisable(true);
        cbEffect18.setDisable(true);
        cbEffect19.setDisable(true);
        cbEffect20.setDisable(true);
        // cbEffect21.setDisable(true);
        
        cbUseTransitions.setDisable(true);
        sldTransitionProb.setDisable(true);
        sldEffectProb.setDisable(true);
        
        cbAllowEffectStacking.setDisable(true);
        sldEffectStack.setDisable(true);
        
        tfClipCount.setDisable(true);
        tfMaxStream.setDisable(true);
        tfMinStream.setDisable(true);
        
        btnAddSource.setDisable(true);
        btnRemoveSource.setDisable(true);
    }
    
    private void enableControls() {
        btnCreate.setDisable(false);
        btnReset.setDisable(false);
        
        btnSelectAll.setDisable(false);
        btnSelectNone.setDisable(false);
        
        cbEffect1.setDisable(false);
        cbEffect2.setDisable(false);
        cbEffect3.setDisable(false);
        cbEffect4.setDisable(false);
        cbEffect5.setDisable(false);
        cbEffect6.setDisable(false);
        cbEffect7.setDisable(false);
        cbEffect8.setDisable(false);
        cbEffect9.setDisable(false);
        cbEffect10.setDisable(false);
        cbEffect11.setDisable(false);
        cbEffect12.setDisable(false);
        cbEffect13.setDisable(false);
        cbEffect14.setDisable(false);
        cbEffect15.setDisable(false);
        cbEffect16.setDisable(false);
        cbEffect17.setDisable(false);
        cbEffect18.setDisable(false);
        cbEffect19.setDisable(false);
        cbEffect20.setDisable(false);
        // cbEffect21.setDisable(false);
        
        cbUseTransitions.setDisable(false);
        sldTransitionProb.setDisable(false);
        sldEffectProb.setDisable(false);
        
        cbAllowEffectStacking.setDisable(false);
        if (cbAllowEffectStacking.isSelected()) { 
            sldEffectStack.setDisable(false);
        }
        
        tfClipCount.setDisable(false);
        tfMaxStream.setDisable(false);
        tfMinStream.setDisable(false);
        
        btnAddSource.setDisable(false);
        btnRemoveSource.setDisable(false);
    }
}
