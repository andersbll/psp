#!/usr/bin/env python2.6
# -*- coding: utf-8 -*-

from bendingstats import *

def avg(l):
	return sum(l)/float(len(l))

rmsd_data = []
collisions_data = []
rmsd_convergence_data = []

protein_lengths = [l for _,l,_ in bending_stats[0][3]]
print 'protein_lengths_avg:', avg(protein_lengths),' protein_lengths_max',max(protein_lengths), '  protein_lengths_min',min(protein_lengths)


for rotamer_depth, window_size, window_repetitions, proteins in bending_stats:	
	print window_size, window_repetitions
	rmsd_avg = 0
	collisions = 0
	rmsd_convergence = [0]*len(proteins[0][2])
	stats_size = len(proteins)
	for name, length, iterations in proteins:
		rmsd_avg += min([rmsd for rmsd,_ in iterations])
		collisions += min([collisions_after for _,(_,collisions_after) in iterations])
#		for rmsd,_ in iterations:
		rmsd_convergence = [rmsd_convergence[i]+iterations[i][0] for i in range(0,len(iterations))]

	rmsd_avg /= stats_size
	rmsd_convergence = [a/stats_size for a in rmsd_convergence]

	rmsd_data.append(rmsd_avg)			
	collisions_data.append(collisions)
	rmsd_convergence_data.append(rmsd_convergence)
#	print rmsd_convergence
	

xticks = range(1,len(rmsd_data)+1)
xticks_convergence = range(1,len(rmsd_convergence_data[0])+1)
print xticks_convergence 

plots = [
{
#	"title": "16 blocks, variating \# of threads",
	"file_name": "plot_rmsd",
	"type": "graph", #= graph, bar
#	"dimension_ratio": 0.75,  #defaults to golden ratio
#	"legend_position": "upper right",
	"markers": ['-','--',':','-.','-',':','--','-.'],
	"plot_colors": ['0.0','0.0','0.0','0.0','0.8','0.8','0.8','0.8'],
	"axis_x_title": "window size, $w$",
	"axis_y_title": "RMSD (\AA)",
	"axis_x_scale": "linear", # = [linear], log, symlog
	"axis_y_scale": "linear", # = [linear], log, symlog
#	"axis_x_lim": [1,24], #only for type graph
#	"axis_y_lim": [1,300], #only for type graph
#	"axis_x_ticks": [], #only for type graph
#	"axis_y_ticks": range(0, 20001, 2500),
#	"axis_x_tick_labels": [str(n+1) for n in range(8)], #only for type bar
	"data": [
		{
			"x": xticks,
			"y": rmsd_data,
		}
	]
},
{
#	"title": "16 blocks, variating \# of threads",
	"file_name": "plot_collisions",
	"type": "graph", #= graph, bar
#	"dimension_ratio": 0.75,  #defaults to golden ratio
#	"legend_position": "upper right",
	"markers": ['-','--',':','-.','-',':','--','-.'],
	"plot_colors": ['0.0','0.0','0.0','0.0','0.8','0.8','0.8','0.8'],
	"axis_x_title": "window size, $w$",
	"axis_y_title": "\# of collisions",
	"axis_x_scale": "linear", # = [linear], log, symlog
	"axis_y_scale": "linear", # = [linear], log, symlog
#	"axis_x_lim": [1,24], #only for type graph
#	"axis_y_lim": [1,300], #only for type graph
#	"axis_x_ticks": [], #only for type graph
#	"axis_y_ticks": range(0, 20001, 2500),
#	"axis_x_tick_labels": [str(n+1) for n in range(8)], #only for type bar
	"data": [
		{
			"x": xticks,
			"y": collisions_data,
		}
	]
},
{
#	"title": "16 blocks, variating \# of threads",
	"file_name": "plot_rmsd_convergence",
	"type": "graph", #= graph, bar
#	"dimension_ratio": 0.75,  #defaults to golden ratio
	"legend_position": "upper right",
	"markers": ['-','--',':','-.','-',':','--','-.'],
	"plot_colors": ['0.0','0.0','0.0','0.0','0.8','0.8','0.8','0.8'],
	"axis_x_title": "\# of algorithm repetitions",
	"axis_y_title": "RMSD (\AA)",
	"axis_x_scale": "linear", # = [linear], log, symlog
	"axis_y_scale": "linear", # = [linear], log, symlog
#	"axis_x_lim": [1,24], #only for type graph
#	"axis_y_lim": [1,300], #only for type graph
#	"axis_x_ticks": [], #only for type graph
#	"axis_y_ticks": range(0, 20001, 2500),
#	"axis_x_tick_labels": [str(n+1) for n in range(8)], #only for type bar
	"data": [
		{
			"label": "$w=2$",
			"x": xticks_convergence,
			"y": rmsd_convergence_data[1],
		}, {
			"label": "$w=8$",
			"x": xticks_convergence,
			"y": rmsd_convergence_data[7],
		}, {
			"label": "$w=20$",
			"x": xticks_convergence,
			"y": rmsd_convergence_data[19],
		}, {
			"label": "$w=24$",
			"x": xticks_convergence,
			"y": rmsd_convergence_data[23],
		}
	]
}
]
