package com.ytp.ytpplus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class YTPGenerator {

    private final Utilities toolBox;
    private final EffectsFactory effectsFactory;
    private final ArrayList<String> sourceList;
    
    private final String OUTPUT_FILE; // The video file that will be produced in the end.
    private final double MIN_STREAM_DURATION; // default: 0.2s
    private final double MAX_STREAM_DURATION; // default: 0.4s
    private final int MAX_CLIPS; // default: 20 clips
    
    private final boolean insertTransitionClips;
    private final int transitionProbability;
    private final int effectProbability;
    
    private final boolean allowEffectStacking;
    private final int maxEffectStackLevel;
    
    private final boolean[] effects;
    
    // These are volitile so all threads can read error status and update the progress bar.
    public volatile boolean done;
    public volatile double doneCount;
    public volatile Throwable ex;
    
    public YTPGenerator(Utilities util, String output, double min, double max, int maxclips, boolean transClips, int transProb, int effectProb, boolean stack, int maxStack, boolean[] effects) {
        this.toolBox = util;
        this.effectsFactory = new EffectsFactory(util);
        this.sourceList = new ArrayList<>();
        
        this.OUTPUT_FILE = output;
        this.MIN_STREAM_DURATION = min;
        this.MAX_STREAM_DURATION = max;
        this.MAX_CLIPS = maxclips;
        
        this.insertTransitionClips = transClips;
        this.transitionProbability = transProb;
        this.effectProbability = effectProb;
        this.allowEffectStacking = stack;
        this.maxEffectStackLevel = maxStack;
        this.effects = effects;
        
        this.done = false;
        this.doneCount = 0;
        this.ex = null;
    }
    
    public void addSource(String sourceName) {
        sourceList.add(sourceName);
    }

    public void go() {
        
        Thread.UncaughtExceptionHandler handler = (Thread t, Throwable e) -> {
            if (ex == null) {
                ex = e;
                toolBox.ex = e;
            }
        };
        
        Thread vidThread;
        vidThread = new Thread() {
            @Override
            public void run() {
                
                // Don't run if there aren't any sources.
                if (sourceList.isEmpty()) {
                    System.out.println("No sources added...");
                    return;
                }
                
                File out = new File(OUTPUT_FILE);
                if (out.exists()) {
                    out.delete();
                }
                
                // Identify number of selected effects
                final int numEffects = numberEffectsSelected();
                final String jobDir = toolBox.getTEMP();
                
                // Create clips in parallel.
                IntStream.range(0, MAX_CLIPS).parallel().forEach(i -> {
                    // Don't start processing more clips if an exception happened! 
                    if(ex != null) {
                        return;
                    }
                    try {
                    String clipToWorkWith = jobDir+"video" + i + ".mp4";

                    // Determine whether to use a transition clip or select from source list.
                    // 1/15 is default odds
                    if (toolBox.randomInt(1, transitionProbability) == transitionProbability && insertTransitionClips == true) {
                        toolBox.copyVideo(effectsFactory.pickSource(), clipToWorkWith);
                    } else {
                        // Select source
                        String sourceToPick = sourceList.get(toolBox.randomInt(0, sourceList.size() - 1));

                        // Get timestamps
                        String sourceLength = toolBox.getLength(sourceToPick);
                        TimeStamp lengthStamp = new TimeStamp(Double.parseDouble(sourceLength));

                        // Determine clip length
                        double start = randomDouble(0.0, lengthStamp.getLengthSec() - MAX_STREAM_DURATION);
                        TimeStamp startOfClip = new TimeStamp(start);

                        double end = startOfClip.getLengthSec() + randomDouble(MIN_STREAM_DURATION, MAX_STREAM_DURATION);
                        TimeStamp endOfClip = new TimeStamp(end);

                        // Get the clip
                        toolBox.snipVideo(sourceToPick, startOfClip, endOfClip, clipToWorkWith);
                    }
                    doneCount += 0.5 / (MAX_CLIPS + 1);

                    // Don't process more clips if an exception happened! 
                    if(ex != null) {
                        return;
                    }

                    // Decide if an effect will occur.
                    if (toolBox.randomInt(0, 99) < effectProbability && (numEffects > 0)) {
                        
                        // If effect stacking is on, roll stack level for this clip.
                        int stackLevel = 1;
                        int thisMaxStack = maxEffectStackLevel;
                        if (allowEffectStacking) {
                            // If number of selected effects is less than max stack level,
                            // then limit max stack to avoid infinite looping during effect selection
                            if (numEffects < thisMaxStack) {
                                thisMaxStack = numEffects;
                            }
                            stackLevel = toolBox.randomInt(1, thisMaxStack);
                        }
                        
                        // Array to track visited valid effects for stacking.
                        // This will keep stacking interesting and ensure effects aren't duplicated nor negated when stacked.
                        boolean[] visited = new boolean[20];
                        Arrays.fill(visited, false);
                        
                        // Effect stacking loop. Runs only once by default.
                        for(int j = 0; j < stackLevel; j++) {
                            // Add a random effect to the video
                            boolean invalidEffect = true;
                            do {
                                int effect = toolBox.randomInt(0, 19);
                                switch (effect) {
                                    case 0: // random sound
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_RandomSound(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 1: // random sound mute
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_RandomSoundMute(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 2:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_Reverse(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 3:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_SpeedUp(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 4:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_SlowDown(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 5:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_Chorus(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 6:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_Vibrato(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 7:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_HighPitch(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 8:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_LowPitch(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 9:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_Dance(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 10:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_Squidward(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 11:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_invert(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 12:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_rainbow(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 13:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_flip(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 14:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_mirror(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 15:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_sus(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 16:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_stutterLoop(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 17:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_loopFrames(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 18:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_shuffleFrames(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    case 19:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_audioCrust(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break;
                                    /* case 20:
                                        if (effects[effect] == true && visited[effect] == false) {
                                            invalidEffect = false;
                                            effectsFactory.effect_exp(clipToWorkWith);
                                            visited[effect] = true;
                                        }
                                        break; */
                                    default:
                                        break;
                                }
                            } while (invalidEffect); // Selected an effect that was disabled or stacked already. Select again.
                        }
                    }
                    } catch(Exception e) {
                        System.out.println("YTPGEN CLIP " + i + " ERROR: Could not be created");
                        throw new RuntimeException(e);
                    }
                    doneCount += 0.5 / (MAX_CLIPS + 1);
                    System.out.println("YTPGEN CLIP " + i + " DONE: " + Math.round(doneCount*100) + "% Complete");
                });
                
                // Create concatenation text file.
                try (PrintWriter writer = new PrintWriter(jobDir + "concat.txt", "UTF-8")) {
                    for (int i = 0; i < MAX_CLIPS; i++) {
                        if (new File(jobDir + "video" + i + ".mp4").exists()) {
                            writer.write("file 'video" + i + ".mp4'\n");
                        }
                    }
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    cleanUp();
                    done = true;
                    throw new RuntimeException(e);
                }
                // Combine all the clips
                try {
                    toolBox.concatDemuxer(OUTPUT_FILE); // toolBox.concatenateVideo(MAX_CLIPS, OUTPUT_FILE);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally { // Cleanup everything and make sure the fxml controller breaks out of loop if concat fails
                    cleanUp();
                    doneCount += 1.0 / (MAX_CLIPS + 1);
                    System.out.println("YTPGEN CONCAT DONE: " + Math.round(doneCount*100) + "% Complete");
                    done = true;
                }   
            }
        };
        vidThread.setUncaughtExceptionHandler(handler);
        vidThread.start();
    }
    
    // Delete everything in the current job directory and then delete the directory itself.
    public void cleanUp() {
        File jobDirFolder = new File(toolBox.getTEMP());
        for (File f: jobDirFolder.listFiles()) {
            if ( !f.isDirectory() ) {
                f.delete();
            }
        }
        rmDir(jobDirFolder);
    }
    
    // Delete a specified directory.
    private void rmDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    rmDir(f);
                }
            }
        }
        file.delete();
    }
    
    // Returns the nummber of effects selected
    private int numberEffectsSelected() {
        int count = 0;
        for(boolean b: effects) {
            if(b) {
                count++;
            }
        }
        return count;
    }
    
    private double randomDouble(double min, double max) {
        double finalVal = -1;
        while (finalVal<0) {
            double x = (ThreadLocalRandom.current().nextDouble() * ((max - min) + 0)) + min;
            finalVal=Math.round(x * 100.0) / 100.0; 
        }
        return finalVal;
    }
}