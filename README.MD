# Universal-Jar-Transformer

## HOW TO USE
To use you simply place the Universal Jar Transformer in the same directory as the jar you wish to transform
Then run `java -jar UniversalJarTransformer "path/to/jar.jar" "name.of.main.Class"

It will then inject Hello World into the start of the main method and run the jar. It also runs all the other classes
through the transformer so you can modify the transformer part of the code to transform any other method.

## HOW TO BUILD

To build this you will need [ASM](http://forge.ow2.org/projects/asm/) and [Apache Commons IO](http://commons.apache.org/proper/commons-io/download_io.cgi) libraries.
