/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2026 Elior "Mallowigi" Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */


import java.awt.*

class GroovyColors {
    // Constructors
    def black = new Color(0, 0, 0)
    def white = new Color(255, 255, 255)
    def red = new Color(255, 0, 0)
    def green = new Color(0, 255, 0)
    def blue = new Color(0, 0, 255)

    // Methods/Properties
    def staticRed = Color.RED
    def staticBlue = Color.BLUE
    def staticGreen = Color.GREEN
    def staticBlack = Color.BLACK
    def staticWhite = Color.WHITE

    // Hex Colors (if enabled)
    def hex1 = "#ff00ff"
    def hex2 = "#00ffff"
    def hex3 = "#ffff00"

    // RGB/RGBA
    def rgb = "rgb(100, 200, 50)"
    def rgba = "rgba(100, 200, 50, 0.5)"

    def test() {
        def localColor = new Color(123, 123, 123)
        println localColor
    }
}