#!/usr/bin/env python2.6
# coding: utf-8

from bendingstats import *

#print 'bending_stats_size=',stats_size

for rotamer_depth, window_size, window_repetitions, proteins in bending_stats:
	print window_size, window_repetitions
	rmsd_avg = 0
	collisions_avg = 0
	stats_size = len(proteins)
	for name, length, iterations in proteins:
#		rmsd, (collisions_before, collisions_after)  
		rmsd_avg += min([rmsd for rmsd,_ in iterations])
		collisions_avg += min([collisions_after for _,(_,collisions_after) in iterations])
	print '  ', stats_size
	print '  ', rmsd_avg/stats_size
	print '  ', collisions_avg

#	for _,(before,after,afterafter) in iterations:
#		if after!=afterafter:
#			print before,' ', after, ' ', afterafter
##	print min([rmsd for rmsd,_ in iterations])
#print rmsd_avg/stats_size

#if __name__ == '__main__':
#	filename = sys.argv[1]	
#	f = open(filename, "r")	
#	filecontents = f.read()
#	f.close()
#	exec(filecontents)

