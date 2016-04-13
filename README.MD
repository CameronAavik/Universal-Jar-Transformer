# Universal-Jar-Transformer

## HOW TO USE
Download the UniversalJarTransformer jar from
[here](https://github.com/CameronAavik/Universal-Jar-Transformer/releases/tag/1.0.0).
However you may prefer to clone the repo, make your own transformer, and build it manually. See below for details.

To use you simply place the Universal Jar Transformer in the same directory as the jar you wish to transform'

Then run `java -jar Universal-Jar-Transformer game.jar`

If it fails to find the main class in the jar, you can pass it in manually with another parameter

For example: `java -jar Universal-Jar-Transformer game.jar path.to.main.MainClass`

It will then inject Hello World into the start of the main method and run the jar. It also runs all the other classes
through the transformer so you can modify the transformer part of this code to transform any other method.

## HOW TO BUILD

To build this you will need [ASM 5](http://forge.ow2.org/projects/asm/) and
[Apache Commons IO](http://commons.apache.org/proper/commons-io/download_io.cgi) libraries.

For ASM you need the asm-all-X.X jar which should be inside the asm-X.X-bin.zip
For Apache Commons IO, you should need commons-io-X.X.jar which should be inside commons-io-X.X-bin.zip

The easiest way to make the jar is to run this project in an IDE such as IntelliJ/Eclipse and create a build artifact
for creating the Jar. This will handle the packaging, inclusion of libraries and so forth for you.