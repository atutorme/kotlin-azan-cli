# kotlin-azan-cli
## JVM based CLI that plays Azan using the kotlin-azan library

Really simple example of how to use the `kotlin-azan` library.

To create an executable jar, run the `shadowJar` target.
`gradle clean shadowJar`

The output jar will appear in `build/libs`, at least using IDEA.

To run:
`java -jar azan-cli-0.1.3-all.jar`

## Credit
This player uses the awesome TinySound library to play audio. This library is a real gem for playing sound on the JVM.  
https://github.com/finnkuusisto/TinySound