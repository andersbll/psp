from bendingstats import *

rmsd_data = []
collisions_data = []

for rotamer_depth, window_size, window_repetitions, proteins in bending_stats:
	print window_size, window_repetitions
	rmsd_avg = 0
	collisions = 0
	stats_size = len(proteins)
	for name, length, iterations in proteins:
#		rmsd, (collisions_before, collisions_after)  
		rmsd_avg += min([rmsd for rmsd,_ in iterations])
		collisions += min([collisions_after for _,(_,collisions_after) in iterations])
	
	rmsd_avg /= stats_size
	rmsd_data.append(rmsd_avg)			
	collisions_data.append(collisions)


xticks = range(1,len(rmsd_data)+1)
print len(xticks), len(rmsd_data)
#xticks = [n*1 for n in range(1,len(data_with_memory_transfer)+1)]
##print len(xticks), len(data)

#def avg(l):
#	return sum(l)/float(len(l))


#print [(i+1) for i,d in enumerate(data) if i>30 and i<len(data)-2 and avg(data[i-1]) > avg(data[i]) < avg(data[i+1])]


plots = [
{
#	"title": "16 blocks, variating \# of threads",
	"file_name": "rmsd",
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
#			"label": "GPU, without memory transfer",
			"x": xticks,
			"y": rmsd_data,
		}
#		,
#		{
#			"label": "GPU, with memory transfer",
#			"x": xticks,
#			"y": [avg(d)/1000. for d in data_with_memory_transfer],
#		}
#		,
#		{
#		"label": "CPU, sequential version",
#		"x": xticks,
#		"y": [2.187*1000. /11. for d in data_with_memory_transfer],
#		}
	]
},
{
#	"title": "16 blocks, variating \# of threads",
	"file_name": "collisions",
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
#		,
#		{
#			"label": "GPU, with memory transfer",
#			"x": xticks,
#			"y": [avg(d)/1000. for d in data_with_memory_transfer],
#		}
#		,
#		{
#		"label": "CPU, sequential version",
#		"x": xticks,
#		"y": [2.187*1000. /11. for d in data_with_memory_transfer],
#		}
	]
}
]
