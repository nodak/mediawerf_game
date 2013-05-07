package net.patchingzone.ru4real.nuevo;


/*
	SMSSend.send(msg.getArg(0).toString(), msg.getArg(1).toString());



						Utils.playMusic(msg.getArg(0).toString());


						int q = Integer.parseInt(msg.getArg(0).toString());

						if (q == 1) { 
							recorder = new MediaRecorder();
							recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
							recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
							recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

							_fileName = net.sweetmonster.android.util.Utils.getCurrentTime(); 
							
							File _file = new File(_rootPath); 
							
							_file.mkdirs();

							try {
								audioFile = File.createTempFile(_fileName, ".3gp", _file);
							} catch (IOException e) {
								throw new RuntimeException("Couldn't create recording audio file",
										e);
							}

							recorder.setOutputFile(audioFile.getAbsolutePath());

							try {
								recorder.prepare();
							} catch (IllegalStateException e) {
								throw new RuntimeException(
										"IllegalStateException on MediaRecorder.prepare", e);
							} catch (IOException e) {
								throw new RuntimeException("IOException on MediaRecorder.prepare",
										e);
							}

							recorder.start();

						} else { 
							
							recorder.stop(); 
							recorder.release(); 
							
							Object[] o = new Object[1];
							o[0] = _fileName; 
							OSC osc = BaseApp.getOSC(); 
							osc.send(OSCMessageType.PIC_TAKEN, o); 
							
							
							
						}


							listener.onImageDraw(msg.getArg(0).toString());

				




						TimerTask toast = new TimerTask() {
							@Override
							public void run() {
								Looper.prepare();

								if (Integer.parseInt(msg.getArg(1).toString()) == 0) {
									Toast.makeText(getApplicationContext(),
											msg.getArg(0).toString(), Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(getApplicationContext(),
											msg.getArg(0).toString(), Toast.LENGTH_SHORT).show();
								}
								Looper.loop();
							}
						};

						Timer timer = new Timer();
						timer.schedule(toast, 0);

				

						int t = Integer.parseInt((String) msg.getArg(0).toString()); 
						Utils.vibrate(getApplicationContext(), t);


						Toast toast = Toast.makeText(getApplicationContext(),
								(CharSequence) msg.getArg(0), Toast.LENGTH_SHORT);
						toast.show();

						SoundUtils.speak(getApplicationContext(), (String) msg.getArg(0), Locale.US);


						
						Intent q = new Intent(getBaseContext(), TransparentActivity.class);
						// q.putExtra("textMsg", textMsg);
						q.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(q);

}
 */
