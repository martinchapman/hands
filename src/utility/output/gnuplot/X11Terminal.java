/* Copyright (c) 2007-2014 by panayotis.com
 * (Extended by Martin Chapman, 2015).
 *
 * JavaPlot is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 2.
 *
 * JavaPlot is free in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CrossMobile; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Created on October 24, 2007, 7:47 PM
 */

package Utility.output.gnuplot;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import javax.swing.JPanel;

import com.panayotis.gnuplot.terminal.TextFileTerminal;

/**
 * This Terminal uses Tikz to output graphics in a LaTeX
 * parsable format. Requires GNUPlot to have the appropriate
 * packages to set a Tikz terminal
 *   
 * @author Martin Chapman
 */
public class X11Terminal extends TextFileTerminal {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new instance of TikzTerminal
     */
    public X11Terminal() {
        this("");
    }

    /**
     * Creates a new instance of TikzTerminal and store output to a specific file
     *
     * @param filename
     */
    public X11Terminal(String filename) {
        super("tikz", filename);
    }

}
