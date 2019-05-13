package com.example.harshit.proj_musicplayer;

public class JumbleSupport {
	String song,path;
	public JumbleSupport(String s, String m){
		this.song=s;
		this.path=m;
	}
	public String getName(){
		return song;
	}
	public String getPath(){
		return path;
	}
}
