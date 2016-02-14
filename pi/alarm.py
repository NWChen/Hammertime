import os
import pygame
import serial

class Alarm:
	
	def __init__(self):
		self.alarm_file = "alarm.mp3"
		try:
			self.ser = serial.Serial('/dev/ttyACM0', 9600)	
		except (RuntimeError, TypeError, NameError):
			self.ser = serial.Serial('/dev/ttyACM1', 9600)
		self.is_playing = False
		pygame.init()
		pygame.mixer.init()
		pygame.mixer.music.load("alarm.mp3")
				
	def play_sound(self):
		if not self.is_playing:
			pygame.mixer.music.play(0, 0.0)
			#os.system("omxplayer -o local " + self.alarm_file)
			self.is_playing = True

	def stop_sound(self):
		if self.is_playing:
			pygame.mixer.music.stop()
			self.is_playing = False

	def hammer(self):
		try:
			self.ser.write('1')
		except (RuntimeError, TypeError, NameError):
			pass
	
	def stop_hammer(self):
		try:
			self.ser.write('0')
		except (RuntimeError, TypeError, NameError):
			pass

	def hammertime(self):
		try:
			print "STOP. HAMMERTIME."
			self.hammer()
			#self.play_sound()
			#self.hammer()

		except (RuntimeError, TypeError, NameError):
			print "HAMMERTIME FAILED."

