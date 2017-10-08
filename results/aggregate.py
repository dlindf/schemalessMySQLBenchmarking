import csv
import math
import json
import os
import sys


def mean(values):
	s = 0.0
	for value in values:
		s += float(value)
	return s/len(values)

#sqrt( Sum( (M-Xi)^2 )/N )

def stdDeviation(values):
	m = mean(values)
	s = 0.0
	for value in values:
		s += (m - float(value))**2
	return math.sqrt(s/len(values))


# accumulator structure
result = {}


# iterate through files in directory
for filename in os.listdir("."):
	# for every csv file in dir
	if filename.endswith(".csv") and filename != "output.csv":
		tableValues = {}
		# open csv
		with open(filename, 'r') as csvFile:
			# read csv into data structure
			reader = csv.reader(csvFile)
			table = []
			for element in reader:
				table.append(element)

			tableTop = []
			for i, val in enumerate(table[0]):
				tableTop.append(val + table[1][i])

			table.pop(0)
			table.pop(0)
			table.insert(0, tableTop)

			meanDev = {}
			for i, name in enumerate(table[0]):
				columnList = []
				for row in table[1:]:
					columnList.append(row[i])
				tableValues[name] = {"mean":mean(columnList), "deviation":stdDeviation(columnList)}

		result[filename.split('.')[0]] = tableValues


csvTable = []
tableTop = ["testName"]
allValueNames = []
for key, res in result.iteritems():
	for key in res:
		allValueNames.append(key)
allValueNames = list(set(allValueNames))
for name in allValueNames:
	tableTop.append("M"+name)
	tableTop.append("D"+name)

csvTable.append(tableTop)

for i, res in result.iteritems():
	l = [None]*len(tableTop)
	l[0]=i
	for j, v in res.iteritems():
		l[tableTop.index("M"+j)] = v["mean"]
		l[tableTop.index("D"+j)] = v["deviation"]
	csvTable.append(l)

with open("output.csv", "w") as f:
	writer = csv.writer(f)
	writer.writerows(csvTable)


			# calculate mean and variance
			# store mean and variance in accumulator

# create new csv file and store data from accumulator






