#!/usr/bin/python -tt
import matplotlib.pyplot as plt
import os



def main():
	x_coordinates = []
	y_coordinates = []
	plt.axis([0, 28800, 0, 15])
	script_dir = os.path.dirname(__file__)
	abs_file_path = os.path.join(script_dir, "output_format.txt")
	f = open(abs_file_path, 'r')
	lines = f.readlines()
	for line in lines:
 		numbers = line.split(' ')
 		if len(numbers) == 3 and int(numbers[0]) % 5 == 0:
 			plt.plot(int(numbers[2]), int(numbers[1]), 'ro')
 		elif len(numbers) == 3 and int(numbers[0]) % 5 == 1:
 			plt.plot(int(numbers[2]), int(numbers[1]), 'bo')
 		elif len(numbers) == 3 and int(numbers[0]) % 5 == 2:
 			plt.plot(int(numbers[2]), int(numbers[1]), 'go')
 		elif len(numbers) == 3 and int(numbers[0]) % 5 == 3:
 			plt.plot(int(numbers[2]), int(numbers[1]), 'yo')
 		elif len(numbers) == 3 and int(numbers[0]) % 5 == 4:
 			plt.plot(int(numbers[2]), int(numbers[1]), 'r^')

	plt.show()

if __name__ == '__main__':
  main()