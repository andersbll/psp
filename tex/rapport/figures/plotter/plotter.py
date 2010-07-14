#!/usr/bin/env python2.6
# -*- coding: utf-8 -*-

# plotting documentation can be found at
# http://matplotlib.sourceforge.net/api/
#

try:
	from matplotlib import rc
	#rc('font',**{'family':'sans-serif','sans-serif':['Helvetica']})
	rc('font',**{'family':'serif','serif':['Palatino']})
	rc('text', usetex=True)
except ImportError:
	print 'latex fonts not available, using defaults matplotlib fonts :('
	pass


import scipy
import pylab
import sys
import os
import math


linestyles = ['-',':','--','-.', '-']
linecolors = ['0.0','0.25','0.5','0.75', '0.8']

# Symbols
symbols = ['-','--','-.',':','.',',','o','^','v','<','>','s','+','x','D','d','1','2','3','4','h','H','p']
# Symbols + line
lps = [k+'-' for k in [',','.','o','^','v','<','>','s','+','x','D','d','1','2','3','4','h','H','p']]
# Colors
colors= ['b','g','r','c','m','y','k','w']





def plot(plotdata, figurenum=None):

	fig_width_pt = 246.0  # Get this from LaTeX using \showthe\columnwidth
	inches_per_pt = 1.0/72.0                # Convert pt to inch
	ratio = (math.sqrt(5)-1.0)/2.         # golden_mean = (math.sqrt(5)-1.0)/2.0
	if plotdata.has_key('plot_dimension_ratio'):
		ratio = plotdata['plot_dimension_ratio']
	fig_width = fig_width_pt*inches_per_pt  # width in inches
	fig_height = fig_width*ratio      # height in inches
	fig_size =  [fig_width,fig_height]
	params = {'backend': 'pdf',
	          'axes.labelsize': 8,
	          'text.fontsize': 8,
	          'xtick.labelsize': 7,
	          'ytick.labelsize': 7,
	          'font.size': 8,
	          'legend.fontsize': 7,
	          'legend.pad': 0.1,     # empty space around the legend box
	          'text.usetex': True,
	          'figure.figsize': fig_size}
	pylab.rcParams.update(params)


	fig = pylab.figure(figurenum)

	# adjust margin of subplots
	fig.subplots_adjust(bottom=.15, left=.15, right=.93, top=.95) #wspace, hspace

	pylab.clf()
	pylab.hold(True)

	pylab.grid(False)
	if plotdata.has_key('title'):
		pylab.title(plotdata['title'])

	ax = pylab.gca()
#	fig.delaxes(ax)
#	ax = fig.add_axes([.1, .1, .9, .9])
	
	if plotdata.has_key('axis_x_scale') and plotdata['type'] != 'bar':
		ax.set_xscale(plotdata['axis_x_scale'])
	if plotdata.has_key('axis_y_scale'):# and plotdata['type'] != 'bar':
		ax.set_yscale(plotdata['axis_y_scale'])

	if plotdata['type'] == 'graph':
		i=0
		for d in plotdata['data']:
			mark = 'None'
			if plotdata.has_key('markers'):
				pylab.rcParams['lines.linestyle'] = plotdata['markers'][i]
			else:
				pylab.rcParams['lines.linestyle'] = linestyles[i]
			pylab.plot(d['x'], d['y'], color=linecolors[i], marker=mark)
			i = (i+1)%len(linestyles)

	if plotdata['type'] == 'bar':
		x_count = len(plotdata['data'][0]['y'])
		entry_count = len(plotdata['data'])
		space = 2.0
		bar_width = 1.0/(entry_count+space)
		space_width = bar_width*space
		entry_width = entry_count*bar_width+space_width
		entry_numbers = range(x_count)
		offset = entry_width/2.0 + space_width/2.0
		
		if plotdata.has_key('bar_colors'):
				linecolors_bars = plotdata['bar_colors']
		else:
			linecolors_bars = [x*1.0/(entry_count-1) for x in range(entry_count-1)] + [1.0]
			if(len(linecolors_bars) == 1):
				linecolors_bars = [.8]
#		print "x_count", x_count
#		print "entry_count", entry_count
#		print "bar_width", bar_width
#		print "entry_width", entry_width
		i=0
		bars = []
		for d in plotdata['data']:
			xlocations = map(lambda x: offset + x*entry_width + i*bar_width, entry_numbers)
#			print linecolors_bars
#			print bar_width, xlocations
			bar = pylab.bar(xlocations, d['y'], 
					width=bar_width, 
					color=str(linecolors_bars[i]),
					label=d['label'],
					)
			bars.append(bar)
			i = (i+1)%len(linestyles)
		labellocations = range(1,x_count+1)
		pylab.xticks(labellocations, plotdata['axis_x_tick_labels'])
		ax.xaxis.set_ticks_position('none')
		if plotdata.has_key('legend_position'):
			labels = [d['label'] for d in plotdata['data']] 
			bars_ = [b[0] for b in bars] 
			lgd = ax.legend(bars_,labels, plotdata['legend_position'])
			
	if plotdata.has_key('axis_x_lim'):
		ax.set_xlim(plotdata['axis_x_lim'])
	if plotdata.has_key('axis_y_lim'):
		ax.set_ylim(plotdata['axis_y_lim'])

	if plotdata.has_key('axis_x_ticks') and plotdata['type'] != 'bar':
		ax.xaxis.set_ticks(plotdata['axis_x_ticks'])
	if plotdata.has_key('axis_y_ticks'):
		ax.yaxis.set_ticks(plotdata['axis_y_ticks'])
	if plotdata.has_key('axis_x_title'):
		pylab.xlabel(plotdata['axis_x_title'])
	if plotdata.has_key('axis_y_title'):
		pylab.ylabel(plotdata['axis_y_title'])
	if plotdata.has_key('legend_position') and plotdata['type'] != 'bar':
		labels = [d['label'] for d in plotdata['data']] 
		# bars_ = [b[0] for b in bars] 
		lgd = ax.legend(labels, plotdata['legend_position'], handlelength=3)
		# pylab.legend(labels)
	
	
	filename = plotdata['file_name']
	pylab.savefig(filename+'.eps', transparent=True)
	os.system('epstopdf '+filename+'.eps')
	os.remove(filename+'.eps')
	
	#pylab.show() # uncomment if we want to see the output

	
if __name__ == '__main__':
	filename = sys.argv[1]	
	f = open(filename, "r")	
	filecontents = f.read()
	f.close()
	exec(filecontents)

	output_filename = filename[0:filename.rfind('.')]

	i=0
	for p in plots:
		if not p.has_key('file_name'):
			if len(plots) == 1:
				p['file_name'] = output_filename
			else:
				p['file_name'] = output_filename+str(i)
		else:
				slash_pos = filename.rfind('/')
				if slash_pos != -1:
					path = filename[0:slash_pos]+"/"
					p['file_name'] = path + p['file_name']
		plot(p, i)
		i+=1

