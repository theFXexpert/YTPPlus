package com.ytp.ytpplus;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;

    
public class EffectsFactory {
    
    private final Utilities toolBox;
    
    public EffectsFactory(Utilities utilities) {
        this.toolBox = utilities;
    }
    
    public String pickSound() {
        FilenameFilter fileFilter = (File dir, String name) -> name.toLowerCase().endsWith(".mp3");
        File[] files = new File(toolBox.getSOUNDS()).listFiles(fileFilter);
        File file = files[toolBox.randomInt(0,files.length - 1)];
        return file.getPath();
    }
    
    public String pickSource() {
        FilenameFilter fileFilter = (File dir, String name) -> name.toLowerCase().endsWith(".mp4");
        File[] files = new File(toolBox.getSOURCES()).listFiles(fileFilter);
        File file = files[toolBox.randomInt(0,files.length - 1)];
        return file.getPath();
    }
    
    public String pickMusic() {
        FilenameFilter fileFilter = (File dir, String name) -> name.toLowerCase().endsWith(".mp3");
        File[] files = new File(toolBox.getMUSIC()).listFiles(fileFilter);
        File file = files[toolBox.randomInt(0,files.length - 1)];
        return file.getPath();
    }
    
    /* EFFECTS */
    public void effect_RandomSound(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists())
            in.renameTo(temp);
        try {
            toolBox.execFFmpeg("-i", temp.getPath(),
                "-i", pickSound(),
                "-filter_complex", "[0:a]channelsplit=channel_layout=stereo[a1][a2];"
                        + "[1:a]volume=1,apad,channelsplit=channel_layout=stereo[a3][a4];"
                        + "[a1][a2][a3][a4]amerge=inputs=4,pan=stereo|c0<c0+c2|c1<c1+c3[out]", //[1:a]volume=1,apad[A];[0:a][A]amerge[out]
                "-ac", "2",
                "-ar", "44100",
                //"-vf", "scale=640x480,setsar=1:1,fps=fps=30",
                "-map", "0:v",
                "-map", "[out]",
                "-shortest",
                "-v", "warning",
                "-y", video);
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT RANDOMSOUND: " + e.toString());
        } finally {
            temp.delete();
        }
    }   
    public void effect_RandomSoundMute(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists())
            in.renameTo(temp);
        try {
            String randomSound = pickSound();
            String soundLength = toolBox.getLength(randomSound);
            toolBox.execFFmpeg("-i", temp.getPath(),
               "-i", randomSound,
               "-to", soundLength,
               "-ar", "44100",
               "-filter_complex", "[0:a]volume=0,channelsplit=channel_layout=stereo[a1][a2];"
                        + "[1:a]volume=1,apad,channelsplit=channel_layout=stereo[a3][a4];"
                        + "[a1][a2][a3][a4]amerge=inputs=4,pan=stereo|c0<c0+c2|c1<c1+c3[out]", //[1:a]volume=1,apad[A];[0:a][A]amerge[out]
               "-ac", "2",
               "-map", "0:v",
               "-map", "[out]",
               "-shortest",
               "-v", "warning",
               "-y", video);
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT RANDOMSOUNDMUTE: " + e.toString());
        } finally {
            temp.delete();
        }
    }  
    public void effect_Reverse(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists()) {
            in.renameTo(temp);
        }
        try {
            toolBox.execFFmpeg("-i", temp.getPath(),
                "-vf", "reverse",
                "-af", "areverse",
                "-v", "warning",
                "-nostdin",
                "-y", video);
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT REVERSE: " + e.toString());
        } finally {
            temp.delete();
        }
    } 
    
    public void effect_SpeedUp(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists())
            in.renameTo(temp);
        try {
            toolBox.execFFmpeg("-i", temp.getPath(),
                "-filter:v", "setpts=0.5*PTS",
                "-filter:a", "atempo=2.0",
                "-shortest",
                "-v", "warning",
                "-nostdin",
                "-y", video);
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT SPEEDUP: " + e.toString());
        } finally {
            temp.delete();
        }
    }   
    
    public void effect_SlowDown(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists())
            in.renameTo(temp);
        try {
            toolBox.execFFmpeg("-i", temp.getPath(),
                "-filter:v", "setpts=2*PTS",
                "-filter:a", "atempo=0.5",
                "-shortest",
                "-v", "warning",
                "-nostdin",
                "-y", video);
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT SLOWDOWN: " + e.toString());
        } finally {
            temp.delete();
        }
    }   
    
    public void effect_Chorus(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists())
            in.renameTo(temp);
        try {
            toolBox.execFFmpeg("-i", temp.getPath(),
               "-af", "chorus=0.5:0.9:50|60|40:0.4|0.32|0.3:0.25|0.4|0.3:2|2.3|1.3",
               "-c:v", "copy",
               "-v", "warning",
               "-nostdin",
               "-y", video);
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT CHORUS: " + e.toString());
        } finally {
            temp.delete();
        }
    }   

    public void effect_Vibrato(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists())
            in.renameTo(temp);
        try {
            
            double f = toolBox.randomDouble(0.5, 10.0);
            double d = toolBox.randomDouble(0.4, 1.0);
            
            toolBox.execFFmpeg("-i", temp.getPath(),
               "-af", "vibrato=f=" + f + ":d=" + d + ",aformat=s16p",
               "-c:v", "copy",
               "-v", "warning",
               "-nostdin",
               "-y", video);
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT VIBRATO: " + e.toString());
        } finally {
            temp.delete();
        }
    } 
    
    public void effect_LowPitch(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists())
            in.renameTo(temp);
        try {
            toolBox.execFFmpeg("-i", temp.getPath(),
               "-filter:v", "setpts=2*PTS",
               "-af", "asetrate=44100*0.5,aresample=44100",
               "-shortest",
               "-v", "warning",
               "-nostdin",
               "-y", video);
            
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT LOWPITCH: " + e.toString());
        } finally {
            temp.delete();
        }
    }

    public void effect_HighPitch(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists())
            in.renameTo(temp);
        try {
            toolBox.execFFmpeg("-i", temp.getPath(),
               "-filter:v", "setpts=0.5*PTS",
               "-af", "asetrate=44100*2,aresample=44100",
               "-shortest",
               "-v", "warning",
               "-nostdin",
               "-y", video);
            
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT HIGHPITCH: " + e.toString());
        } finally {
            temp.delete();
        }
    }
    
    public void effect_Dance(String video) throws Exception {
        
        File in = new File(video);

        File temp = new File(toolBox.getTempVideoName()); //og file
        File temp2 = new File(toolBox.getTempVideoName()); //1st cut
        File temp3 = new File(toolBox.getTempVideoName()); //backwards (silent
        File temp4 = new File(toolBox.getTempVideoName()); //forwards (silent
        File temp5 = new File(toolBox.getTempVideoName()); //backwards & forwards concatenated
        File temp6 = new File(toolBox.getTempVideoName()); //backwards & forwards concatenated

        // final result is backwards & forwards concatenated with music

        if (in.exists())
            in.renameTo(temp);
        if (temp2.exists())
            temp2.delete();
        if (temp3.exists())
            temp3.delete();
        if (temp4.exists())
            temp4.delete();
        if (temp5.exists())
            temp5.delete();
        if (temp6.exists())
            temp6.delete();
        try {    
            String randomSound = pickMusic();
            
            int randomTime = toolBox.randomInt(3,9);
            int randomTime2 = toolBox.randomInt(0,1);

            toolBox.execFFmpeg("-i", temp.getPath(),
                "-map", "0", // "-c:v", "copy",
                "-to", "00:00:0"+randomTime2+"." + randomTime,
                "-an", "-v", "warning",
                "-nostdin",
                "-y", temp2.getPath());
            toolBox.execFFmpeg("-i", temp2.getPath(),
                "-map", "0", // "-c:v", "copy",
                "-vf", "reverse",
                "-v", "warning",
                "-nostdin",
                "-y", temp3.getPath());
            toolBox.execFFmpeg("-i", temp3.getPath(),
                "-vf", "reverse",
                "-v", "warning",
                "-nostdin",
                "-y", temp4.getPath());
            toolBox.execFFmpeg("-i", temp3.getPath(),
                "-i", temp4.getPath(),
                "-filter_complex", "[0:v:0][1:v:0][0:v:0][1:v:0][0:v:0][1:v:0][0:v:0][1:v:0]concat=n=8:v=1[outv]",
                "-map", "[outv]",
                "-c:v", "libx264",
                "-v", "warning",
                "-nostdin",
                "-shortest", "-y", temp5.getPath());
            toolBox.execFFmpeg("-i", temp5.getPath(),
                "-map", "0",
                "-vf", "setpts=0.5*PTS",
                "-af", "atempo=2.0",
                "-v", "warning",
                "-nostdin",
                "-shortest", "-y", temp6.getPath());
            toolBox.execFFmpeg("-i", temp6.getPath(),
                "-i", randomSound,
                "-c:v", "libx264",
                "-map", "0:v:0",
                "-map", "1:a:0",
                "-v", "warning",
                "-nostdin",
                "-shortest", "-y", video);
            
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT DANCE: " + e.toString());
        } finally {
            temp.delete();
            temp2.delete();
            temp3.delete();
            temp4.delete();
            temp5.delete();
            temp6.delete();
        }
    }  
    
    public void effect_Squidward(String video) throws Exception {
        
        int pictureNum = toolBox.randomInt();
        String picturePrefix = toolBox.getTEMP() + pictureNum + "-";
        File squidwardScript = new File(picturePrefix + "concatsquidward.txt");
        File squid0 = new File(picturePrefix + "squidward0.png");
        File squid1 = new File(picturePrefix + "squidward1.png");
        File squid2 = new File(picturePrefix + "squidward2.png");
        File squid3 = new File(picturePrefix + "squidward3.png");
        File squid4 = new File(picturePrefix + "squidward4.png");
        File squid5 = new File(picturePrefix + "squidward5.png");
        File black = new File(picturePrefix + "black.png");
        
        if (squidwardScript.exists())
            squidwardScript.delete();

        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName()); //og file

        // final result is backwards & forwards concatenated with music

        if (in.exists())
            in.renameTo(temp);
        
        try {

            toolBox.execFFmpeg("-i", temp.getPath(),
                "-vf", "select=gte(n\\,1)",
                "-vframes", "1",
                "-v", "warning",
                "-y", squid0.getPath());

            for (int i = 1; i < 6; i++) {
                ArrayList<String> cmdLine = new ArrayList<>();
                cmdLine.add("convert");
                cmdLine.add(squid0.getPath());
                switch (toolBox.randomInt(0, 4)) {
                    case 0:
                        cmdLine.add("-flop");
                        break;
                    case 1:
                        cmdLine.add("-flip");
                        break;
                    case 2:
                        cmdLine.add("-implode");
                        cmdLine.add((toolBox.randomInt(0, 1) == 0 ? "-" : "") + toolBox.randomInt(1, 3));
                        break;
                    case 3:
                        cmdLine.add("-swirl");
                        cmdLine.add((toolBox.randomInt(0, 1) == 0 ? "-" : "") + toolBox.randomInt(1, 180));
                        break;
                    case 4:
                        cmdLine.add("-channel");
                        cmdLine.add("RGB");
                        cmdLine.add("-negate");
                        break;
                    //case 7:
                    //    effect = " -virtual-pixel Black +distort Cylinder2Plane " + toolBox.randomInt(1,90);
                    //    break;
                }
                cmdLine.add(picturePrefix + "squidward" + i + ".png");
                toolBox.execMagick(cmdLine.toArray(new String[cmdLine.size()]));
            }
            toolBox.execMagick("convert", "-size", "640x480", "canvas:black", black.getPath());
        } catch (Exception e) {
            throw new Exception("\nEFFECT SQUIDWARD: " + e.toString());
        }

        try (PrintWriter writer = new PrintWriter(squidwardScript.getPath(), "UTF-8")){    
            writer.write
                        ("file '" + squid0.getPath() + "'\n" +
                        "duration 0.467\n" +
                        "file '" + squid1.getPath() + "'\n" +
                        "duration 0.434\n" +
                        "file '" + squid2.getPath() + "'\n" +
                        "duration 0.4\n" +
                        "file '" + black.getPath() + "'\n" +
                        "duration 0.834\n" +
                        "file '" + squid3.getPath() + "'\n" +
                        "duration 0.467\n" +
                        "file '" + squid4.getPath() + "'\n" +
                        "duration 0.4\n" +
                        "file '" + squid5.getPath() + "'\n" +
                        "duration 0.468\n");
        } catch (Exception e) {
            throw new Exception("\nEFFECT SQUIDWARD: " + e.toString());
        }
        /* Using "\\" slashes breaks concat (and thus squidward) on
         * Windows breaks
         */
        
        try {
            toolBox.execFFmpeg(
                "-f", "concat",
                "-safe", "0",
                "-i", squidwardScript.getPath(),
                "-i", toolBox.getRESOURCES() + "squidward/music.wav",
                "-map", "0:v:0", "-map", "1:a:0",
                "-r", "30",
                "-pix_fmt", "yuv420p",
                "-c:v", "libx264",
                "-c:a", "aac",
                "-vf", "select=concatdec_select",
                "-af", "aselect=concatdec_select",
                "-shortest",
                "-v", "warning",
                "-y", video);
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch (Exception e) {
            throw new Exception("\nEFFECT SQUIDWARD: " + e.toString());
        } finally {
            temp.delete();
            for (int i = 0; i < 6; i++) {
                new File(picturePrefix + "squidward" + i + ".png").delete();
            }
            black.delete();
            squidwardScript.delete();
        }
    }  
    
    public void effect_invert(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists()) {
            in.renameTo(temp);
        }
        try {
            toolBox.execFFmpeg(
                "-i", temp.getPath(),
                "-vf", "negate",
                "-c:a", "copy",
                "-v", "warning",
                "-nostdin",
                "-y", video);
            
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            throw new Exception("\nEFFECT INVERT: " + e.toString());
        } finally {
            temp.delete();
        }
    }
    
    public void effect_rainbow(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists()) {
            in.renameTo(temp);
        }
        try {
            // Get timestamps
            String clipLength = toolBox.getLength(temp.getPath());
            TimeStamp lengthStamp = new TimeStamp(Double.parseDouble(clipLength));
            Double lengthSec = lengthStamp.getLengthSec();
        
            toolBox.execFFmpeg(
                "-i", temp.getPath(),
                "-pix_fmt", "yuv420p", // Needs to be specified here or it breaks the video player.
                "-vf", "colorchannelmixer=.393:.769:.189:0:.349:.686:.168:0:.272:.534:.131,hue=H=2*PI*t/" + lengthSec + ":s=4",
                "-c:a", "copy",
                "-v", "warning",
                "-nostdin",
                "-y", video);
            
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            throw new Exception("\nEFFECT RAINBOW: " + e.toString());
        } finally {
            temp.delete();
        }
    }
    
    // Flip the clip in a random direction.
    public void effect_flip(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists()) {
            in.renameTo(temp);
        }
        String vf = "hflip";
        int flipDir = toolBox.randomInt(0,2);
        switch ( flipDir ) {
            case 0:
                vf = "hflip";
                break;
            case 1:
                vf = "vflip";
                break;
            case 2:
                vf = "hflip,vflip";
                break;
        }
        
        try {
            toolBox.execFFmpeg(
                "-i", temp.getPath(),
                "-vf", vf,
                "-c:a", "copy",
                "-v", "warning",
                "-nostdin",
                "-y", video);
            
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            throw new Exception("\nEFFECT FLIP: " + e.toString());
        } finally {
            temp.delete();
        }
    }
    
    // Mirror the left or right side randomly.
    public void effect_mirror(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists()) {
            in.renameTo(temp);
        }
        String vf;
        if (toolBox.randomInt(0,1) == 1) {
            vf = "crop=iw/2:ih:0:0,split[left][tmp];[tmp]hflip[right];[left][right] hstack";
        } else {
            vf = "crop=iw/2:ih:iw/2:0,split[right][tmp];[tmp]hflip[left];[left][right] hstack";
        }
        
        try {
            toolBox.execFFmpeg(
                "-i", temp.getPath(),
                "-vf", vf,
                "-c:a", "copy",
                "-v", "warning",
                "-nostdin",
                "-y", video);
            
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            throw new Exception("\nEFFECT MIRROR: " + e.toString());
        } finally {
            temp.delete();
        }
    }
    
    public void effect_sus(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName()); // Forwards
        File temp2 = new File(toolBox.getTempVideoName()); // Backwards
        File concat = new File(toolBox.getTEMP()+toolBox.randomInt()+"-concatSUS.txt"); // Concat text file
        
        if (in.exists()) {
            in.renameTo(temp);
        }
        if (temp2.exists()) {
            temp2.delete();
        }
        
        try (PrintWriter writer = new PrintWriter(concat.getPath(), "UTF-8")) {
            if (temp.exists()) {
                writer.write("file '" + temp.getPath() + "'\nfile '" + temp2.getPath() + "'\n"); //writing to same folder
            }
        } catch (Exception e) {
            throw new Exception("\nEFFECT SUS: " + e.toString());
        }
        
        try {
            // Make reversed section
            toolBox.execFFmpeg(
                "-i", temp.getPath(),
                "-vf", "reverse",
                "-af", "areverse",
                "-v", "warning",
                "-nostdin",
                "-y", temp2.getPath());
            
            // Combine forwards and backwards
            toolBox.execFFmpeg(
                "-f", "concat",
                "-safe", "0",
                "-i", concat.getPath(),
                "-vf", "select=concatdec_select",
                "-af", "aselect=concatdec_select",
                "-v", "warning",
                "-nostdin",
                "-y", video);
        
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            throw new Exception("\nEFFECT SUS: " + e.toString());
        } finally {
            temp.delete();
            temp2.delete();
            concat.delete();
        }
    }
    
    // Randomly make video frames stutter.
    public void effect_loopFrames(String video) throws Exception {
        
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists()) {
            in.renameTo(temp);
        }
        
        int loops = 0; // Number of loops to make.
        int size = 0;  // Size in frames of segment to loop.
        int start = 0; // First frame of loop segment.
        
        try {
            String frameLength = toolBox.getLengthFrames(temp.getPath());
            int maxFrames = Integer.parseInt(frameLength);

            do {
                loops = toolBox.randomInt(1,maxFrames);
                size = toolBox.randomInt(2,9);
            } while ( (loops * size) > maxFrames);

            int totalLoopLength = (loops * size) + 1;
            String vf = "loop=loop=" + loops + ":size=" + size + ":start=" + start + ",trim=start_frame=0:end_frame=" + totalLoopLength + ",setpts=PTS-STARTPTS"; // settb=intb,setpts=N/30/TB
            String af = "asetrate=44100,aloop=loop=" + loops + ":size=44100*" + size + "/30:start=0,asetpts=PTS-STARTPTS"; // asettb=intb,asetpts=N/SR/TB
        
            toolBox.execFFmpeg(
                    "-i", temp.getPath(),
                    "-vf", vf,
                    "-af", af,
                    "-shortest",
                    "-v", "warning",
                    "-nostdin",
                    "-y", video
            );
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            throw new Exception("\nEFFECT LOOP FRAMES: " + e.toString());
        } finally {
            temp.delete();
        }
    }
    
    // Make audio buzzing.
    // UNUSED - it doesn't work that great
    public void effect_buzz(String video) throws Exception {
        
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists()) {
            in.renameTo(temp);
        }
        try {
            String frameLength = toolBox.getLengthFrames(temp.getPath());
            int maxFrames = Integer.parseInt(frameLength);

            int loops = maxFrames/2; // Number of visual loops to make.
            int buzzSize = toolBox.randomInt(900, 1400);
            String vf = "loop=loop=" + loops + ":size=2:start=0,settb=AVTB,setpts=N/30/TB";
            String af = "asetrate=44100,aloop=loop=" + loops + ":size=" + buzzSize + ":start=0,asettb=AVTB,asetpts=N/SR/TB";
        
            toolBox.execFFmpeg(
                    "-i", temp.getPath(),
                    "-vf", vf,
                    "-af", af,
                    "-shortest",
                    "-v", "warning",
                    "-nostdin",
                    "-y", video
            );
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            System.out.println("EFFECT BUZZ: " + e.getMessage());
        } finally {
            temp.delete();
        }
    }
    
    // Randomly shuffle video frames.
    public void effect_shuffleFrames(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists()) {
            in.renameTo(temp);
        }
        try {
            String frameLength = toolBox.getLengthFrames(temp.getPath());
            int maxFrames = Integer.parseInt(frameLength);

            String frames = "";
            for (int i = 0; i < maxFrames; i++) {
                int r = toolBox.randomInt(0,maxFrames - 1);
                frames = frames + r + " ";
            }

            String vf = "shuffleframes=" + frames.trim() + ",settb=AVTB,setpts=N/30/TB";
        
            toolBox.execFFmpeg(
                    "-i", temp.getPath(),
                    "-vf", vf,
                    "-af", "asettb=AVTB,asetpts=N/SR/TB",
                    "-shortest",
                    "-v", "warning",
                    "-nostdin",
                    "-y", video
            );
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            throw new Exception("\nEFFECT SHUFFLE FRAMES: " + e.toString());
        } finally {
            temp.delete();
        }
    }
    
    // Make a stutter loop including audio.
    public void effect_stutterLoop(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        File concat = new File(toolBox.getTEMP()+toolBox.randomInt()+"-concatSL.txt");
        if (in.exists()) {
            in.renameTo(temp);
        }
        
        try (PrintWriter writer = new PrintWriter(concat.getPath(), "UTF-8")) {
            for (int i = 0; i < toolBox.randomInt(2,4); i++) {
                if (temp.exists()) {
                    writer.write("file '" + temp.getPath() + "'\n"); //writing to same folder
                }
            }
        } catch (Exception e) {
            throw new Exception("\nEFFECT STUTTER LOOP: " + e.toString());
        }
        try {
            toolBox.execFFmpeg(
                    "-f",
                    "concat",
                    "-safe", "0",
                    "-i", concat.getPath(),
                    "-vf", "select=concatdec_select", //  Select filters need to be used in concat to keep timestamps stable
                    "-af", "aselect=concatdec_select", // even though -c copy is faster, this will largely prevent glitches/freezes in output.
                    "-v", "warning",
                    "-nostdin",
                    "-y", video
            );
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            throw new Exception("\nEFFECT STUTTER LOOP: " + e.toString());
        } finally {
            temp.delete();
            if (concat.exists()) {
                concat.delete();
            }
        }
    }
    
    // Makes the audio sightly louder and crusty
    public void effect_audioCrust(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists()) {
            in.renameTo(temp);
        }
        try {
            // Equalizer clips audio to hell when put between volume and aeval filters
            // equalizer=f=10:t=h:w=200:g=5,equalizer=f=100:t=h:w=200:g=5,equalizer=f=1000:t=h:w=400:g=-5
            String af = "volume=volume=1.3:precision=fixed,aeval=val(ch)*2*random(" + toolBox.randomInt(1,Integer.MAX_VALUE - 1) + "):c=same";
            
            toolBox.execFFmpeg(
                "-i", temp.getPath(),
                "-af", af,
                "-c:v", "copy",
                "-v", "warning",
                "-nostdin",
                "-y", video);
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            throw new Exception("\nEFFECT AUDIO CRUST: " + e.toString());
        } finally {
            temp.delete();
        }
    }
    
    // Placeholder for testing/debuging
    public void effect_exp(String video) throws Exception {
        File in = new File(video);
        File temp = new File(toolBox.getTempVideoName());
        if (in.exists()) {
            in.renameTo(temp);
        }
        // roberts=scale=" + toolBox.randomInt(2,100) + ":delta=" + toolBox.randomInt(2,100)
        // "random=frames=60:seed=" + toolBox.randomInt(1,Integer.MAX_VALUE); same as shuffe frames
        // erosion
        // elbg=nb_steps=2:seed=" + toolBox.randomInt(1,Integer.MAX_VALUE);
        // curves
        // sobel
        // swapuv
        // zoompan
        String af = "volume=volume=1.5:precision=fixed,aeval=val(ch)*2*random(" + toolBox.randomInt(1,Integer.MAX_VALUE) + "):c=same";
        try {
            toolBox.execFFmpeg(
                "-i", temp.getPath(),
                "-af", af,
                "-c:v", "copy",
                "-v", "warning",
                "-nostdin",
                "-y", video);
            
        } catch (InterruptedException e) {
            File out = new File(video);
            out.delete();
        } catch( Exception e ) {
            throw new Exception("\nEFFECT EXP: " + e.toString());
        } finally {
            temp.delete();
        }
    }
}

