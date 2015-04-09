#!/usr/bin/env python
# Copyright 2015 Jo Pol
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

from __future__ import division
from math import *

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

__author__ = 'Jo Pol'
__credits__ = ['Veronika Irvine']
__license__ = 'GPLv3'
__version__ = '${project.version}'
__maintainer__ = 'Jo Pol'
__status__ = 'Development'

class PolarGrid(inkex.Effect):
	"""
	Creates a dotted polar grid where distance between the circles 
	increase with the distance between the dots on the circles
	"""
	def __init__(self):
		"""
		Constructor.
		"""
		# Call the base class constructor.
		inkex.Effect.__init__(self)
		self.OptionParser.add_option('-a', '--angle', action='store', type='float', dest='angleOnFootside', default=45, help='grid angle (degrees)')
		self.OptionParser.add_option('-d', '--dots', action='store', type='int', dest='dotsPerCircle', default=180, help='number of dots on a circle')
		self.OptionParser.add_option('-o', '--outerRadius', action='store', type='float', dest='outerRadius', default=160, help='outer radius (mm)')
		self.OptionParser.add_option('-i', '--innerRadius', action='store', type='float', dest='innerRadius', default=100, help='minimum inner radius (mm)')
		self.OptionParser.add_option('-f', '--fill', action='store', type='string', dest='dotFill', default='#FF9999', help='dot color')
		self.OptionParser.add_option('-A', '--alignment', action='store', type='string', dest='alignment', default='outside', help='exact radius on [inside|outside]')
		self.OptionParser.add_option('-s', '--size', action='store', type='float', dest='dotSize', default=0.5, help='dot diameter (mm)')
		self.OptionParser.add_option('-v', '--variant', action='store', type='string', dest='variant', default='', help='omit rows to get [|rectangle|hexagon1]')

	def dot(self, x, y, group):
		"""
		Draw a circle of radius 'options.dotSize' and origin at (x, y)
		"""
		s = simplestyle.formatStyle({'fill': self.dotFill})
		attribs = {'style':s, 'cx':str(x), 'cy':str(y), 'r':str(self.options.dotSize)}
		
		# insert path object into the group
		inkex.etree.SubElement(group, inkex.addNS('circle', 'svg'), attribs)

	def group(self, radius, distance):
		"""
		Create a labeled group for the dots on a circle of the grid
		"""
		f = "{0} mm per dot, radius: {1} mm"
		scale = 25.4/90
		s = f.format(distance*scale, radius*scale)
		attribs = {inkex.addNS('label', 'inkscape'):s}
		
		# insert group object into current layer and remember it
		return inkex.etree.SubElement(self.current_layer, inkex.addNS('g', 'svg'), attribs)

	def dots(self, radius, circleNr, group):
		"""
		Draw dots on a grid circle
		"""
		offset = (circleNr % 2) * 0.5
		for dotNr in range (0, self.options.dotsPerCircle):
			a = (dotNr + offset) * 2.0 * self.alpha
			x = radius * cos(a)
			y = radius * sin(a)
			self.dot(x, y, group)

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

	def iterate(self, radius, circleNr):
		"""
		Create a group with a ring of dots.
		The distance to the next ring is determined by the grid angle and number of dots.
		"""
		distance = radius*self.radiusFactor
		group = self.group(radius, distance)
		self.dots(radius, circleNr, group)
		self.generatedCircles.append(group)
		return distance

	def generate(self):
		"""
		Generate rings with dots, either inside out or outside in
		"""
		circleNr = 0
		if self.options.alignment == 'outside':
			radius = self.options.outerRadius
			while radius > self.options.innerRadius:
				radius -= self.iterate(radius, circleNr)
				circleNr += 1
		else:
			radius = self.options.innerRadius
			while radius < self.options.outerRadius:
				radius += self.iterate(radius, circleNr)
				circleNr += 1

	def removeGroups(self, start, increment):
		"""
		Remove complete rings with dots
		"""
		for i in range(start, len(self.generatedCircles), increment):
			self.current_layer.remove(self.generatedCircles[i])

	def removeDots(self, i, offset, step):
		"""
		Remove dots from one circle
		"""
		group = self.generatedCircles[i]
		dots = list(group)
		start = self.options.dotsPerCircle - 1 - offset
		for j in range(start, -1, 0-step):
			group.remove(dots[j])

	def effect(self):
		"""
		Effect behaviour.
		Overrides base class' method and draws something.
		"""
		
		# Convert input from mm to pixels, assuming 90 dpi
		conversion = 90.0 / 25.4
		self.options.outerRadius *= conversion
		self.options.innerRadius *= conversion
		self.options.dotSize *= conversion / 2.0 # also convert from diameter to radius
		
		self.alpha = radians(180.0/self.options.dotsPerCircle)
		angle = radians(self.options.angleOnFootside)
		self.radiusFactor = cos(angle - self.alpha/2.0) / cos(angle + self.alpha/2.0)
		self.radiusFactor -= 1.0
		
		self.dotFill = self.getColorString(self.options.dotFill)
		self.generatedCircles = []
		self.generate()

		if self.options.variant == 'rectangle':
			self.removeGroups(1, 2)
		elif self.options.variant == 'hexagon1':
			self.removeGroups(2, 3)
		elif self.options.variant == 'hexagon2':
			for i in range(1, len(self.generatedCircles), 1):
				self.removeDots(i, (i%2)*2, 3)
		elif self.options.variant == 'hexagon3':
			for i in range(1, len(self.generatedCircles), 2):
				self.removeDots(i, (i//2)%2, 2)
		elif self.options.variant == 'hexagon4':
			self.removeGroups(2, 4)
		elif self.options.variant == 'hexagon5':
			for i in range(1, len(self.generatedCircles), 2):
				self.removeDots(i, 0, 2)

# Create effect instance and apply it.
if __name__ == '__main__':
	PolarGrid().affect()