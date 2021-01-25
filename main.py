import shutil
import csv
import sys

def main():

          path = "Scores/"
          fileName = ""

          try:
                    fileName = sys.argv[1]
          except IndexError:
                    fileName = "Score.csv"

          with open (path+fileName, 'r') as f:
                    reader = csv.reader(f)
                    for line in reader:
                              print(line)

if __name__ == "__main__":
          # execute only if run as a script
          main()