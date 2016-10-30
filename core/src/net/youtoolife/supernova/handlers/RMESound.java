package net.youtoolife.supernova.handlers;

import static jouvieje.bass.Bass.BASS_ChannelPause;
import static jouvieje.bass.Bass.BASS_ChannelPlay;
import static jouvieje.bass.Bass.BASS_ChannelStop;
import static jouvieje.bass.Bass.BASS_GetVersion;
import static jouvieje.bass.Bass.BASS_Init;
import static jouvieje.bass.Bass.BASS_MusicLoad;
import static jouvieje.bass.Bass.BASS_StreamCreateFile;
import static jouvieje.bass.Bass.BASS_ChannelBytes2Seconds;
import static jouvieje.bass.Bass.BASS_ChannelGetLength;
import static jouvieje.bass.defines.BASS_MUSIC.BASS_MUSIC_RAMP;
import static jouvieje.bass.defines.BASS_SAMPLE.BASS_SAMPLE_LOOP;
import static jouvieje.bass.defines.BASS_SAMPLE.BASS_SAMPLE_FLOAT;
import static jouvieje.bass.defines.BASS_POS.BASS_POS_BYTE;
import static net.youtoolife.supernova.handlers.Device.forceFrequency;
import static net.youtoolife.supernova.handlers.Device.forceNoSoundDevice;

import jouvieje.bass.BassInit;
import jouvieje.bass.exceptions.BassException;
import jouvieje.bass.structures.HMUSIC;
import jouvieje.bass.structures.HSTREAM;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class RMESound {
	
	private static Array<HMUSIC> musics;
	private static Array<HSTREAM> streams;
	private static Array<Integer> channels;
	private static Array<String> playList;
	private static Array<String> fullNames;
	
	static int chan;
	
	static String homeDir;
	
	public static void loadFiles(String dir) {
		homeDir = dir;
		//String file = System.getProperty("user.home")+"/YouTooLife/STALKER_DOC/SFX/";
		if (musics == null)
			musics = new Array<HMUSIC>();
		if (streams == null)
			streams = new Array<HSTREAM>();
		if (playList == null)
		playList = new Array<String>();
		if (fullNames == null)
			fullNames = new Array<String>();
		if (channels == null)
			channels = new Array<Integer>();
		
		getSubDir(Gdx.files.local(dir));
	}
	
	
	public static void play() {
		if (chan != 0) {
			BASS_ChannelPlay(chan, true);
		}
	}
	
	public static int playSFX(String fileName) {
		int chanSFX = 0;
		
		String fullName = "nop";
		try{
			int index = -1;
				 index = playList.indexOf(fileName, false);
				 if (index != -1)
					 fullName = fullNames.get(index);
				 else
				 System.out.println("SFX not found - "+fileName);
				 
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("SFX not found - "+fileName);
			}
		
		HMUSIC music = null; HSTREAM stream = null;
		if(  (stream = BASS_StreamCreateFile(false, fullName, 0, 0, 0)) == null
		  && (music = BASS_MusicLoad(false, fullName, 0, 0, BASS_MUSIC_RAMP | 0, 0)) == null) {
			System.out.println("Can't play file");
			return 0; // Can't load the file
		}
		
		chanSFX = (stream != null) ? stream.asInt() : ((music != null) ?  music.asInt() : 0);
		
		if (chanSFX != 0) {
			BASS_ChannelPlay(chanSFX, true);
		}
		
		return chanSFX;
	}
	
	
	public static double getTrackLen(int ch) {
	 	return BASS_ChannelBytes2Seconds(ch, BASS_ChannelGetLength(ch, BASS_POS_BYTE));
	}
	
	public static void playTrack(String fileName) {
		if (chan != 0) {
			BASS_ChannelStop(chan);
			chan = 0;
		}
		int index = -1;
		try{
			 index = playList.indexOf(fileName, false);
			 if (index != -1)
				 chan = channels.get(index);
			 else
			 System.out.println("SFX not found - "+fileName);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("SFX not found - "+fileName);
		}
		//BASS_ChannelPlay(chan, true);
	}
	
	public static void stop() {
		BASS_ChannelStop(chan);
		chan = 0;
	}
	
	public static void pause() {
		BASS_ChannelPause(chan);
	}
	
	public static HSTREAM getHSTREAM(String name) {
		HSTREAM stream = null;
		try{
			for (String s:playList)
				System.out.println(s);
		stream = streams.get(playList.indexOf(name, false));
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("SFX not found - "+name);
		}
		return stream;
	}
	
	public static HMUSIC getHMUSIC(String name) {
		HMUSIC music = null;
		try{
			for (String s:playList)
				System.out.println(s);
			music = musics.get(playList.indexOf(name, false));
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("SFX not found - "+name);
		}
		return music;
	}

	private static void getSubDir(FileHandle s) {
		FileHandle dir = s;
		FileHandle[] files = dir.list();
		for (FileHandle file: files) {	
			if(file.isDirectory())
				getSubDir(file);
			if (file.name().contains(".mp3")
					||file.name().contains(".m4a")
					||file.name().contains(".wav")
					||file.name().contains(".xm")
					||file.name().contains(".mod")
					||file.name().contains(".caf")) {
				

				if (s.path().contains("SFX")) {
					System.out.println("SFX::");
					//channels.add(loadSFX(s+"/"+file.name()));
				}
				else {
					channels.add(loadSound(s+"/"+file.name()));
				}
				
				playList.add(file.nameWithoutExtension());
				fullNames.add(s+"/"+file.name());
				System.out.println(s+"/"+file.name());
				//System.out.println(s+"/"+file.nameWithoutExtension());
			}
		}
	}
	
	public static boolean checkChan(HSTREAM stream, HMUSIC music) {
		int chan2 = (stream != null) ? stream.asInt() : ((music != null) ?  music.asInt() : 0);
		if (chan == chan2)
			return true;
		else
			return false;
	}
	
	public static HSTREAM loadStream(String file) {
		HSTREAM stream = null; HMUSIC music = null;
		if(  (stream = BASS_StreamCreateFile(false, file, 0, 0, BASS_SAMPLE_LOOP)) == null
		  && (music = BASS_MusicLoad(false, file, 0, 0, BASS_MUSIC_RAMP | BASS_SAMPLE_LOOP, 0)) == null) {
			System.out.println("Can't play file");
			return null; // Can't load the file
		}
		chan = (stream != null) ? stream.asInt() : ((music != null) ?  music.asInt() : 0);
		return stream;
	}
	
	public static HMUSIC loadMusic(String file) {
		HMUSIC music = null; HSTREAM stream = null;
		if(  (stream = BASS_StreamCreateFile(false, file, 0, 0, BASS_SAMPLE_LOOP)) == null
		  && (music = BASS_MusicLoad(false, file, 0, 0, BASS_MUSIC_RAMP | BASS_SAMPLE_LOOP, 0)) == null) {
			System.out.println("Can't play file");
			return null; // Can't load the file
		}
		chan = (stream != null) ? stream.asInt() : ((music != null) ?  music.asInt() : 0);
		return music;
	}
	
	public static int loadSFX(String file) {
		HMUSIC music = null; HSTREAM stream = null;
		if(  (stream = BASS_StreamCreateFile(false, file, 0, 0, 0)) == null
		  && (music = BASS_MusicLoad(false, file, 0, 0, BASS_MUSIC_RAMP | 0, 0)) == null) {
			System.out.println("Can't play file");
			return 0; // Can't load the file
		}
		chan = (stream != null) ? stream.asInt() : ((music != null) ?  music.asInt() : 0);
		System.out.println(chan);
		return chan;
	}
	
	public static int loadSound(String file) {
		HMUSIC music = null; HSTREAM stream = null;
		if(  (stream = BASS_StreamCreateFile(false, file, 0, 0, BASS_SAMPLE_LOOP)) == null
		  && (music = BASS_MusicLoad(false, file, 0, 0, BASS_MUSIC_RAMP | BASS_SAMPLE_LOOP, 0)) == null) {
			System.out.println("Can't play file");
			return 0; // Can't load the file
		}
		chan = (stream != null) ? stream.asInt() : ((music != null) ?  music.asInt() : 0);
		System.out.println(chan);
		return chan;
	}
	

	
	public static void initDevice() {
		if(((BASS_GetVersion() & 0xFFFF0000) >> 16) != BassInit.BASSVERSION()) {
			//printfExit("An incorrect version of BASS.DLL was loaded");
			System.out.print("An incorrect version of BASS.DLL was loaded\n");
			return;
		}
		
		// Initialize default output device
		if(!BASS_Init(forceNoSoundDevice(-1), forceFrequency(44100), 0, null, null)) {
			System.out.print("Can't initialize device\n");
		}
	}
	
	public static void initNativeBass() {
		try {
			BassInit.DEBUG = true;
			BassInit.loadLibraries();
		} catch(BassException e) {
			System.out.println("NativeBass error!"+e.getMessage());
			return;
		}
		if (!BASS_Init(0, 0, 0, null, null)) {
			System.exit(-1);
			}

		//
		// Checking NativeBass version
		//
		if(BassInit.NATIVEBASS_LIBRARY_VERSION() != BassInit.NATIVEBASS_JAR_VERSION()) {
			System.out.println("Error!  NativeBass library version (%08x) is different to jar version (%08x)\n"+
					BassInit.NATIVEBASS_LIBRARY_VERSION() +BassInit.NATIVEBASS_JAR_VERSION());
			return;
		}
		initDevice();
		/*==================================================*/
	}

}
