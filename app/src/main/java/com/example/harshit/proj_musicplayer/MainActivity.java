package com.example.harshit.proj_musicplayer;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity implements OnClickListener,OnSeekBarChangeListener,android.content.DialogInterface.OnClickListener{
	ImageButton b1,b2,b3,b4,b5,b6;
	SharedPreferences spf;
	ArrayList<String> names=new ArrayList<String>();
	ArrayList<String> paths=new ArrayList<String>();
	//Intent mp;
	//ImageView iv;
	TextView tv1,tv2,tv3,tv4;
	MediaPlayer m;
	boolean flag;
	ListView lv;
	SeekBar sb;
	//int[] songs={R.raw.galliyan,R.raw.jeena,R.raw.khamoshiyan,R.raw.kuchistarah,R.raw.sunraha};
	//int[] images={R.drawable.ekvillains,R.drawable.badlapur,R.drawable.khamoshiyan,R.drawable.doorie,R.drawable.aashiqui2};
	//ToggleButton tb;
	int ts,tm,tms,f;
	int c=0,i=0,size=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		  b1=(ImageButton)findViewById(R.id.play);
			b2=(ImageButton)findViewById(R.id.pause);
			b3=(ImageButton)findViewById(R.id.ff);
			b4=(ImageButton)findViewById(R.id.fb);
			b5=(ImageButton)findViewById(R.id.next);
			b6=(ImageButton)findViewById(R.id.previous);
			//iv=(ImageView)findViewById(R.id.imageView1);
			lv=(ListView)findViewById(android.R.id.list);
			tv1 = (TextView)findViewById(R.id.status);
	        tv2 = (TextView)findViewById(R.id.duration);
	        tv3=(TextView)findViewById(R.id.td);
	        tv4=(TextView)findViewById(R.id.cd);
	        registerForContextMenu(lv);
			b2.setVisibility(View.INVISIBLE);
			//tv2.setVisibility(View.INVISIBLE);
			ActionBar abr=getActionBar();
			abr.setDisplayHomeAsUpEnabled(true);
			abr.show();
			File sd= Environment.getExternalStorageDirectory();
			String filepath=sd.getPath();
			if (filepath != null) {
		        File home = new File(filepath);
		        File[] listFiles = home.listFiles();
		        if (listFiles != null && listFiles.length > 0) {
		            for (File file : listFiles) {
		                //System.out.println(file.getAbsolutePath());
		                if (file.isDirectory()) {
		                    scanDirectory(file);
		                } else {
		                	if (file.getName().endsWith(".mp3")) {
		                	names.add(file.getName().toString());
							paths.add(file.getPath().toString());
		                }
		                }
		            }
		        }
			}
			ArrayAdapter<String> add=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,names);
			setListAdapter(add);
			//m=MediaPlayer.create(this,paths[0]);
			size=names.size();
			m=new MediaPlayer();
			try {
				m.setDataSource(paths.get(i));
				m.prepare();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			b1.setOnClickListener(this);
			b2.setOnClickListener(this);
			b3.setOnClickListener(this);
			b4.setOnClickListener(this);
			b5.setOnClickListener(this);
			b6.setOnClickListener(this);
			sb=(SeekBar)findViewById(R.id.seekBar1);
			sb.setOnSeekBarChangeListener(this);
			sb.setMax(m.getDuration());
			tms=(m.getDuration()/1000);
			tm=(tms/60);
			ts=(tms%60);
			tv2.setText("Current song: "+(i+1)+"/"+size);
			tv3.setText(tm+":"+ts);
			spf=getSharedPreferences("xyz", MODE_PRIVATE);
	        String let=spf.getString("t1","0");
	        //if(!let.equalsIgnoreCase(null)){
	        toast(let);
	        int check= Integer.valueOf(let);
	        if(check==size)
	        toast("Welcome");
	        else if(check<size)
	        toast((size-check)+" song|s added to the library");
	        
	        else if(check>size)
	        toast((check-size)+" song|s deleted from the library");
			//}
	        //else{
	        	//toast((size)+" song|s deleted from the library");
	        //}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Editor edt= spf.edit();
    	String gd= String.valueOf(size);
    	toast(gd);
    	edt.putString("t1", gd);
    	edt.commit();
		super.onDestroy();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater mn=getMenuInflater();
		mn.inflate(R.menu.main, menu);
		
		//getMenuInflater().inflate(R.menu.menu_demo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		int id=item.getItemId();
		if (id == R.id.menu_omi1) {
			toast("Music player");
		}
		else if (id == R.id.menu_omi2) {
			//getSystemService(Context.VIBRATOR_SERVICE);
			m.stop();
			finish();
		}
		
		/*else{
			m.stop();
			finish();
		}*/
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Options");
		menu.setHeaderIcon(android.R.drawable.ic_menu_more);
		MenuInflater mn=getMenuInflater();
		mn.inflate(R.menu.context_main, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id=item.getItemId();
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		if (id == R.id.menu_cmi1) {
			
			byeDialog("Song name: "+names.get(info.position)+"\n"+"Location: "+paths.get(info.position));
		}
		else if (id == R.id.menu_cmi2) {
			BluetoothAdapter bd= BluetoothAdapter.getDefaultAdapter();
			if(bd==null)
				toast("Bluetooth not supported");
			else{
				Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				//i.putExt
				startActivityForResult(i,1);
			}
			//return true;
		}
		else if (id == R.id.menu_cmi3) {
			return true;
			//finish();
		}
		
		return super.onContextItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		Intent j=new Intent();
		Bundle b=data.getExtras();
		//String s=b.getString("fs");
		//int id=Integer.valueOf(s);
		MenuItem id=(MenuItem)b.get("fs");
		AdapterContextMenuInfo info1=(AdapterContextMenuInfo)id.getMenuInfo();
		File f=new File(paths.get(info1.position));
		j.setAction(Intent.ACTION_SEND);
		j.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
		PackageManager pm=getPackageManager();
		List<ResolveInfo> li=pm.queryIntentActivities(j, 0);
		if(li.size()>0){
			String classname=null;
			String packagename=null;
			for(ResolveInfo info:li){
				packagename=info.activityInfo.packageName;
				if(packagename.equals("com.android.bluetooth")){
					classname=info.activityInfo.name;
					j.setClassName(packagename, classname);
					startActivity(j);
				}
			}
		}
	}
	@Override
	public void onClick(View v) {
		flag=true;
		Thread t=new Thread(){
			public void run(){
				while (c<=m.getDuration()){
					c++;
					sb.setProgress(m.getCurrentPosition());
					try{
					sleep(1000);
				}
					catch(Exception e){}
				}
			}
		};
		t.start();

	
		// TODO Auto-generated method stub
		if(v.getId()==R.id.play){
			b1.setVisibility(View.INVISIBLE);
			b2.setVisibility(View.VISIBLE);
			m.start();
			tv1.setText("Status  :Playing");
			//tv2.setText("Current song: "+i+"/"+size);
			
		}
		else if(v.getId()==R.id.pause){
			//if(m.isPlaying()==true){
			b2.setVisibility(View.INVISIBLE);
			b1.setVisibility(View.VISIBLE);
			
			m.pause();
			tv1.setText("Status  :Paused");
			//tv2.setText("Current song: "+i+"/"+size);
			//}
			
		}
		else if(v.getId()==R.id.ff){
			int c=(m.getCurrentPosition()+5000);
			if(c<=m.getDuration()){
			m.seekTo(c);
			tms=(c/1000);
			tm=(tms/60);
			ts=(tms%60);
			tv4.setText(tm+":"+ts);
			//tv2.setText("Current Position: "+ tm+":"+ts);
			}
			else
				toast("Reached the End!");
		}
		else if(v.getId()==R.id.next){
			if(i!=size-1){
				i++;
				c=0;
				//iv.setImageResource(images[i]);
				if(m.isPlaying()==false){
					b1.setVisibility(View.INVISIBLE);
					b2.setVisibility(View.VISIBLE);
					tv1.setText("Status  :Playing");
				}
				m.stop();
				m=new MediaPlayer();
				try {
					m.setDataSource(paths.get(i));
					m.prepare();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				tms=(m.getDuration()/1000);
				tm=(tms/60);
				ts=(tms%60);
				tv3.setText(tm+":"+ts);
				sb.setMax(m.getDuration());
				m.start();
				tv2.setText("Current song: "+(i+1)+"/"+size);
			}
			else{
					//i=0;
					toast("It is the Last Song!!");
		}
		}
		else if(v.getId()==R.id.previous){
			if(i!=0){
				i--;
				c=0;
				//iv.setImageResource(images[i]);
				if(m.isPlaying()==false){
					b1.setVisibility(View.INVISIBLE);
					b2.setVisibility(View.VISIBLE);
					tv1.setText("Status  :Playing");
				}
				m.stop();
				m=new MediaPlayer();
				try {
					m.setDataSource(paths.get(i));
					m.prepare();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				tms=(m.getDuration()/1000);
				tm=(tms/60);
				ts=(tms%60);
				tv3.setText(tm+":"+ts);
				sb.setMax(m.getDuration());
				m.start();
				tv2.setText("Current song: "+(i+1)+"/"+size);
			}
			else{
				toast("It is the First Song!!");
		}
		}
		else if(v.getId()==R.id.fb){
			int c= (m.getCurrentPosition()-5000);
			if(c>0){
			m.seekTo(c);
			tms=(c/1000);
			tm=(tms/60);
			ts=(tms%60);
			tv4.setText(tm+":"+ts);
			//tv2.setText("Current Position:"+ tm+":"+ts);
			
			}
			else{
				
				toast("Beyond the Start!");
		}
		}

	}
	public void toast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
								  boolean fromUser) {
		c=progress;
		//m.seekTo(c);
		tms=(c/1000);
		tm=(tms/60);
		ts=(tms%60);
		tv4.setText(tm+":"+ts);
			
		if(fromUser==true){
			c=progress;
			m.seekTo(c);
			tms=(c/1000);
			tm=(tms/60);
			ts=(tms%60);
			tv4.setText(tm+":"+ts);
			//tv2.setText("Current Position:"+ tm+":"+ts);
			
		}
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
		
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(flag==false){
			
		}
		i=position;
		c=0;
		//iv.setImageResource(images[i]);
		if(m.isPlaying()==false){
			b1.setVisibility(View.INVISIBLE);
			b2.setVisibility(View.VISIBLE);
			tv1.setText("Status  :Playing");
		}
		m.stop();
		m=new MediaPlayer();
		try {
			m.setDataSource(paths.get(i));
			m.prepare();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		if(flag==false){
			Thread t=new Thread(){
				public void run(){
					while (c<=m.getDuration()){
						c++;
						sb.setProgress(m.getCurrentPosition());
						try{
						sleep(1000);
					}
						catch(Exception e){}
					}
				}
			};
		t.start();
		}
		
		tms=(m.getDuration()/1000);
		tm=(tms/60);
		ts=(tms%60);
		tv3.setText(tm+":"+ts);
		sb.setMax(m.getDuration());
		m.start();
		tv2.setText("Current song: "+(i+1)+"/"+size);
	}

	public void scanDirectory(File directory) {
	    if (directory != null) {
	        File[] listFiles = directory.listFiles();
	        if (listFiles != null && listFiles.length > 0) {
	            for (File file : listFiles) {
	                if (file.isDirectory()) {
	                    scanDirectory(file);
	                } else {
	                	if (file.getName().endsWith(".mp3")) {
	                		names.add(file.getName().toString());
							paths.add(file.getPath().toString());
	                	}
	                }
	            }}}
	            
}
	public void byeDialog(String msg){
		AlertDialog.Builder b=new AlertDialog.Builder(this);
		b.setTitle("Details");
		b.setIcon(android.R.drawable.ic_dialog_info);
		b.setMessage(msg);
		b.setPositiveButton("OK",null);
		//b.setNegativeButton("NO", null);
		AlertDialog a=b.create();
		a.show();
		
	}
			}
