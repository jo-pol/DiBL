#!/usr/bin/env python
# Copyright 2014 Veronika Irvine
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program. If not, see http://www.gnu.org/licenses/.

from math import *

# The following five lines check to see if tkFile Dialog can 
# be used to select a file
try:
	from Tkinter import *
	import tkFileDialog as tkf
except: tk = False
else: tk = True

# These lines are only needed if you don't put the script directly into
# the installation directory
import sys
from os import path
# Unix
sys.path.append('/usr/share/inkscape/extensions')
# OS X
sys.path.append('/Applications/Inkscape.app/Contents/Resources/extensions')
# Windows
sys.path.append('C:\Program Files\Inkscape\share\extensions')
sys.path.append('C:\Program Files (x86)\share\extensions')

# We will use the inkex module with the predefined 
# Effect base class.
import inkex, simplestyle, math, pturtle

__author__ = 'Veronika Irvine'
__credits__ = ['Ben Connors','Veronika Irvine']
__license__ = 'GPL'
__version__ = '${project.version}'
__maintainer__ = 'Veronika Irvine'
__status__ = 'Development'

class LaceGround(inkex.Effect):
	"""
	Create a ground for lace from a text file descriptor 
	using specified angle and spacing
	"""
	def __init__(self,fname):
		"""
		Constructor.
		Defines the "--centerx" option of the script.
		"""
		# Call the base class constructor.
		inkex.Effect.__init__(self)
		
		# Set the fname variable
		self.fname = fname
		
		self.OptionParser.add_option('-f', '--file', action='store', type='string', dest='file', help='File containing lace ground description')
		self.OptionParser.add_option('-a', '--angle', action='store', type='float', dest='angle', default=45.0, help='Grid Angle')
		self.OptionParser.add_option('-d', '--distance', action='store', type='float', dest='spacing', default=10.0, help='Distance between grid points in mm')
		self.OptionParser.add_option('-w', '--width', action='store', type='float', dest='width', default=100, help='Width of ground pattern')
		self.OptionParser.add_option('-l', '--height', action='store', type='float', dest='height', default=100, help='Height of ground pattern')
		self.OptionParser.add_option('-s', '--size', action='store', type='float', dest='size', default=1, help='Width of lines')
		self.OptionParser.add_option('-c', '--color', action='store', type='string', dest='color', default='#FF0000', help='Color of lines')


		self.turtle = pturtle.pTurtle((100, 100))
        
	def line(self, x1, y1, x2, y2):
		"""
        Draw a line from point at (x1, y1) to point at (x2, y2).
        Style of line is hard coded and specified by 's'.
        """
		# define the motions
		self.turtle.penup()
		self.turtle.clean()
		self.turtle.setpos((x1, y1))
		self.turtle.pendown()
		self.turtle.setpos((x2, y2))
		self.turtle.penup()

		# define the stroke style
		s = {'stroke-linejoin': 'miter', 
			'stroke-width': self.options.size,
			'stroke-opacity': '1.0', 
			'fill-opacity': '1.0',
			'stroke': self.options.color, 
			'stroke-linecap': 'butt',
			'fill': 'none'
		}
        
		# create attributes from style and path
		attribs = {'style':simplestyle.formatStyle(s), 'd':self.turtle.getPath()}

		# insert path object into current layer
		inkex.etree.SubElement(self.current_layer, inkex.addNS('path', 'svg'), attribs)
    										 			
	def loadFile(self, fname):
		data = []
		rowCount = 0
		colCount = 0
		with open(fname,'r') as f:
			first = True
			for line in f:
				if first:
					# first line of file gives row count and column count
					first = False
					line = line.rstrip('\n')
					temp = line.split('\t')
					type = temp[0]
					rowCount = int(temp[1])
					colCount = int(temp[-1])
					
				else:
					line = line.lstrip('[')
					line = line.rstrip(']\t\n')
					rowData = line.split(']\t[')
					data.append([])
					for cell in rowData:
						data[-1].append([float(num) for num in cell.split(',')])
						
		return {"type":type, "rowCount":rowCount, "colCount":colCount, "data":data}
			
	def drawCheckerGround(self, data, rowCount, colCount):
		a = self.options.spacing
		theta = radians(self.options.angle)
		deltaX = a*sin(theta) 
		deltaY = a*cos(theta)
		maxRows = ceil(self.options.height / deltaY)
		maxCols = ceil(self.options.width  / deltaX)
		
		x = 0.0
		y = 0.0
		repeatY = 0
		repeatX = 0

		while repeatY * rowCount < maxRows:
			x = 0.0
			repeatX = 0
			
			while repeatX * colCount < maxCols:
				
				for row in data:
					for coords in row:
						x1 = x + coords[0]*deltaX
						y1 = y + coords[1]*deltaY
						x2 = x + coords[2]*deltaX
						y2 = y + coords[3]*deltaY
						x3 = x + coords[4]*deltaX
						y3 = y + coords[5]*deltaY
				
						self.line(x1,y1,x2,y2)
						self.line(x1,y1,x3,y3)
					
				repeatX += 1
				x += deltaX * colCount

			repeatY += 1
			y += deltaY * rowCount

	def unsignedLong(self, signedLongString):
		longColor = long(signedLongString)
		if longColor < 0:
			longColor = longColor & 0xFFFFFFFF
		return longColor

	def getColorString(self, longColor):
		"""
		Convert numeric color value to hex string using formula A*256^0 + B*256^1 + G*256^2 + R*256^3
		From: http://www.hoboes.com/Mimsy/hacks/parsing-and-setting-colors-inkscape-extensions/
		"""
		longColor = self.unsignedLong(longColor)
		hexColor = hex(longColor)[2:-3]
		hexColor = hexColor.rjust(6, '0')
		return '#' + hexColor.upper()
	
	def effect(self):
		"""
		Effect behaviour.
		Overrides base class' method and draws something.
		"""
		# Locate and load the file containing the lace ground descriptor
		if self.fname == None:
			self.fname = self.options.file
			
		if self.fname == '': sys.exit(1)
		elif not path.isfile(self.fname): sys.exit(1)
		
		result = self.loadFile(self.fname)
		
		#Convert input from mm to pixels, assuming 90 dpi
		conversion = 90.0 / 25.4
		self.options.width *= conversion
		self.options.height *= conversion
		self.options.size *= conversion
		
		# Users expect spacing to be the vertical distance between footside pins (vertical distance between every other row) 
		# but in the script we use it as as diagonal distance between grid points
		# therefore convert spacing based on the angle chosen
		theta = radians(self.options.angle)
		self.options.spacing *= conversion/(2.0*cos(theta))
		
		# Convert color from long integer to hexidecimal string
		self.options.color = self.getColorString(self.options.color)
		
		# Draw a ground based on file description and user inputs
		# For now, assume style is Checker but could change in future
		self.drawCheckerGround(result["data"],result["rowCount"],result["colCount"])


if tk:
	# Create root window
	root = Tk()
	# Hide it
	root.withdraw()
	# Ask for a file
	fname = tkf.askopenfilename(**{'initialdir' : '~'})	
else: fname = None

# Create effect instance and apply it.
effect = LaceGround(fname)
effect.affect()
