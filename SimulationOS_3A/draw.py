#!/usr/bin/python -tt
import matplotlib.pyplot as plt
import os



def main():
	plt.axis([0, 90, 0, 1])
	script_dir = os.path.dirname(__file__)
	abs_file_path = os.path.join(script_dir, "format_file.txt")


	f = open(abs_file_path, 'r')
	lines = f.readlines()
	x_axis = 1
	for line in lines:
 		numbers = line.split(' ')
 		if len(numbers) == 1:
 			y_axis = float(numbers[0])
 			#x_axis is the quantum length, y_axis is the waitingt time
 			plt.plot(x_axis, y_axis, 'ro')
 		x_axis = x_axis + 1
 			
 			
	plt.show()

if __name__ == '__main__':
  main()