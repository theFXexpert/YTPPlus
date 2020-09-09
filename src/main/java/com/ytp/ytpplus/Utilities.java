package com.ytp.ytpplus;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FFMPEG utilities toolbox for YTP+
 *
 * @author benb
 * @author theFXexpert
 */
public class Utilities {
    
    // Paths and directories for everything.
    private final String FFPROBE;
    private final String FFMPEG;
    private final String MAGICK;
    private final String TEMP;
    private final String SOURCES;
    private final String SOUNDS;
    private final String MUSIC;
    private final String RESOURCES;
    
    // Exception flag
    public volatile Throwable ex;
    
    public Utilities(String ffprobe, String ffmpeg, String magick, String temp, String sources, String sounds, String music, String resources) {
        this.FFPROBE = ffprobe;
        this.FFMPEG = ffmpeg;
        this.MAGICK = magick;
        this.TEMP = temp;
        this.SOURCES = sources;
        this.SOUNDS = sounds;
        this.MUSIC = music;
        this.RESOURCES = resources;
        this.ex = null;
    }

    public String getFFPROBE() {
        return FFPROBE;
    }

    public String getFFMPEG() {
        return FFMPEG;
    }

    public String getMAGICK() {
        return MAGICK;
    }

    public String getTEMP() {
        return TEMP;
    }

    public String getSOURCES() {
        return SOURCES;
    }

    public String getSOUNDS() {
        return SOUNDS;
    }

    public String getMUSIC() {
        return MUSIC;
    }

    public String getRESOURCES() {
        return RESOURCES;
    }

    /**
     * Return the length of a video (in seconds)
     *
     * @param video input video filename to work with
     * @return Video length as a string (output from ffprobe)
     * @throws java.lang.Exception
     */
    public String getVideoLength(String video) throws Exception {
        try {
            return execFFprobe( 
                    "-v", "error",
                    "-sexagesimal",
                    "-show_entries", "format=duration",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    "\"" + video + "\"");
        } catch (Exception e) {
            throw new Exception("\nUTILITY getVideoLength failed to read " + video + ": " + e.toString());
        }
    }
    
    public String getLength(String file) throws Exception {
        try {
            return execFFprobe(
                    "-i", file, 
                    "-show_entries", "format=duration",
                    "-v", "warning",
                    "-of", "csv=p=0"
            );
        } catch (Exception e) {
            throw new Exception("\nUTILITY getLength failed to read " + file + ": " + e.toString());
        }
    }
    
    // Get the number of total frames in a video.
    public String getLengthFrames(String file) throws Exception {
        try {
            return execFFprobe(
                "-i", file,
                "-count_frames",
                "-select_streams", "v:0",
                "-show_entries", "stream=nb_read_frames",
                "-ignore_editlist", "1",
                "-of", "default=nokey=1:noprint_wrappers=1",
                "-v", "warning"
            );
            /* String result = execFFprobe(
                "-i", file,
                "-select_streams", "v:0",
                "-show_entries", "stream=nb_frames",
                "-of", "default=nokey=1:noprint_wrappers=1",
                "-v", "warning"
            ); */
        } catch(Exception e) {
            throw new Exception("\nUTILITY getLengthFrames failed to read " + file + ": " + e.toString());
        }
    }
    
    /**
     * Snip a video file between the start and end time, and save it to an output file.
     *
     * @param video input video filename to work with
     * @param startTime start time (in TimeStamp format, e.g. new TimeStamp(seconds);)
     * @param endTime start time (in TimeStamp format, e.g. new TimeStamp(seconds);)
     * @param output output video filename to save the snipped clip to
     * @throws java.lang.Exception
     * 
     */
    public void snipVideo(String video, TimeStamp startTime, TimeStamp endTime, String output) throws Exception {
        try {
            execFFmpeg(
                "-ss", startTime.getTimeStamp(),
                "-i", video,
                "-to", endTime.getTimeStamp(),
                "-copyts",
                "-pix_fmt", "yuv420p",
                "-ac", "2",
                "-ar", "44100",
                "-vf", "scale=640x480,setsar=1:1,fps=fps=30",
                "-v", "warning",
                "-y", output
            );
        } catch (InterruptedException e) {
            File out = new File(output);
            out.delete();
        } catch(Exception e) {
            throw new Exception("\nUTILITY snipVideo failed snipping from " + video + " to " + output + ": " + e.toString());
        }
    }   
    
    /**
     * Copies a video and encodes it in the proper format without changes.
     *
     * @param video input video filename to work with
     * @param output output video filename to save the snipped clip to
     * @throws java.lang.Exception
     */
    public void copyVideo(String video, String output) throws Exception {
        try {
            execFFmpeg(
                "-i", video,
                "-ac", "2",
                "-ar", "44100",
                "-vf", "scale=640x480,setsar=1:1,fps=fps=30",
                "-v", "warning",
                "-y", output
            );
        } catch (InterruptedException e) {
            File out = new File(output);
            out.delete();
        } catch(Exception e) {
            throw new Exception("\nUTILITY copyVideo failed copying " + video + " to " + output + ": " + e.toString());
        }
    }   
    
    /**
     * Concatenate videos by count
     * UNUSED
     * Convert the function to use Java's built-in ProcessBuilder if you do want to use it.
     *
     * @param count number of input videos to concatenate
     * @param out output video filename
     *
    public void concatenateVideo(int count, String out ) {
        try {
            File export = new File(out);
            
            if (export.exists())
                export.delete();
            
            int realcount = 0;
            
            CommandLine cmdLine = new CommandLine(FFMPEG);
            for (int i = 0; i < count; i++) {
                File vid = new File(TEMP + "video" + i + ".mp4");
                if (vid.exists()) {
                    cmdLine.addArgument("-i", false);
                    cmdLine.addArgument(vid.getPath(), false);
                    ++realcount;
                }
            }
            String filter = "";
            for (int i=0; i < realcount; i++)
                filter += "[" + i + ":v:0][" + i + ":a:0]";

            cmdLine.addArguments(new String[] {
                "-filter_complex", filter + "concat=n=" + realcount + ":v=1:a=1[outv][outa]",
                "-map", "[outv]",
                "-map", "[outa]",
                "-y", out
            }, false);
            System.out.println(cmdLine);
            new DefaultExecutor().execute(cmdLine);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    */
    
    /**
     * Concatenate videos by count using the demuxer only instead of using filters.
     * This is useful if all the input files have the same codecs.
     *
     * @param out output video filename
     * @throws java.lang.Exception
     */
    public void concatDemuxer( String out ) throws Exception {
        try {
            File export = new File(out);
            String concatFile = TEMP + "concat.txt";

            if (export.exists()) {
                export.delete();
            }
            execFFmpeg(
                    "-f", "concat",
                    "-safe", "0",
                    "-i", concatFile,
                    "-vf", "select=concatdec_select", //  Select filters need to be used in concat to keep timestamps stable
                    "-af", "aselect=concatdec_select", // even though -c copy is faster, this will largely prevent glitches/freezes in output.
                    "-nostdin",
                    "-v", "warning",
                    "-threads", "0",
                    "-y", out

            );
        } catch(Exception e) {
            throw new Exception("\nUTILITY concatDemuxer failed: " + e.toString());
        }
    }
    
    private void exec(String what, String ...args) throws Exception {
        if(ex != null) {
            return;
        }
        
        String[] prog = {what};
        String[] cmdLine = Arrays.copyOf(prog, prog.length + args.length);
        System.arraycopy(args, 0, cmdLine, prog.length, args.length);
        
        Process proc = new ProcessBuilder(cmdLine).inheritIO().start();
        try {
            int res = -1;
            do {
                if(ex != null) {
                    Thread.currentThread().interrupt();
                }
                try {
                    res = proc.exitValue();
                    break;
                } catch(IllegalThreadStateException e) {
                    Thread.sleep(100);
                }
            } while (true);
            if( res != 0 ) {
                throw new Exception("Exited with an error: " + res);
            }
            
        } catch (InterruptedException e) {
            proc.destroy();
            Thread.sleep(250); // Allow time for the process to close.
            throw new InterruptedException();
        }
    }

    public void execFFmpeg(String ...args) throws Exception {
        exec(FFMPEG, args);
    }

    public void execMagick(String ...args) throws Exception {
        exec(MAGICK, args);
    }
    
    private String execFFprobe(String ...args) throws Exception {
        String[] probe = {FFPROBE};
        String[] cmdLine = Arrays.copyOf(probe, probe.length + args.length);
        System.arraycopy(args, 0, cmdLine, probe.length, args.length);
        
        Process proc = new ProcessBuilder(cmdLine).redirectErrorStream(true).start();
        
        // We want to read the output here instead of inheriting IO.
        try ( BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream())) ) {
            String s = stdInput.readLine(); // Only one line should be read.
            int res = proc.waitFor();
            if( res != 0 ) {
                throw new Exception("Exited with an error: " + res);
            }
            return s;
        } catch ( InterruptedException e ) {
            proc.destroy();
            return "";
        }
    }
    
    // Thread optimized RNG. Max is inclusive.
    public int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    // Max is EXCLUSIVE here!
    public double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    
    public int randomInt() {
        return randomInt(0, (1 << 30) - 1);
    }
    
    public String getTempVideoName() {
        return TEMP + randomInt() + "-temp.mp4";
    }
}

